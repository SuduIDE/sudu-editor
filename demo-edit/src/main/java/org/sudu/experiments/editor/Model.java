package org.sudu.experiments.editor;

import org.sudu.experiments.Debug;
import org.sudu.experiments.SplitInfo;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.worker.parser.LineParser;
import org.sudu.experiments.editor.worker.parser.ParseStatus;
import org.sudu.experiments.editor.worker.parser.ParserUtils;
import org.sudu.experiments.editor.worker.proxy.*;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.common.graph.ScopeGraph;
import org.sudu.experiments.parser.common.graph.writer.ScopeGraphWriter;
import org.sudu.experiments.text.SplitText;
import org.sudu.experiments.worker.ArrayView;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.*;

import static org.sudu.experiments.math.ArrayOp.copyOf;

public class Model {

  public final Uri uri;
  public final Document document;

  public Object platformObject;
  private String docLanguage;

  final Selection selection = new Selection();
  final NavigationStack navStack = new NavigationStack();

  EditorToModel editor;
  WorkerJobExecutor executor;

  LineDiff[] diffModel;
  int caretLine, caretCharPos, caretPos;
  CodeElement definition = null;
  final List<CodeElement> usages = new ArrayList<>();
  final Map<String, String> properties = new HashMap<>();
  final List<V2i> parsedVps = new ArrayList<>();

  int fullFileParsed = ParseStatus.NOT_PARSED;
  int fileStructureParsed = ParseStatus.NOT_PARSED;
  int firstLinesParsed = ParseStatus.NOT_PARSED;

  long parsingTimeStart, viewportParseStart, resolveTimeStart;

  boolean highlightResolveError, printResolveTime;
  int hScrollPos = 0;
  double vScrollLine = .0;

  boolean debug = false;

  public Model(String text, Uri uri) {
    this(text, null, uri);
  }

  public Model(String text, String language, Uri uri) {
    this(SplitText.splitInfo(text), language, uri);
  }

  public Model(SplitInfo text, String language, Uri uri) {
    this(text.lines, language, uri);
  }

  public Model(String[] text, Uri uri) {
    this(text, null, uri);
  }

  public Model() {
    this(new String[0], null, null);
  }

  public Model(String[] text, String language, Uri uri) {
    this.uri = uri;
    docLanguage = language;
    document = new Document(text);
  }

  String languageFromFile() {
    return uri != null
        ? Languages.languageFromFilename(uri.path)
        : Languages.TEXT;
  }

  public void setLanguage(String language) {
    docLanguage = language;
  }

  public String language() {
    return docLanguage != null ? docLanguage : languageFromFile();
  }

  public String docLanguage() {
    return docLanguage;
  }

  public String uriScheme() {
    return uri != null ? uri.scheme : null;
  }

  void onFileParsed(Object[] result) {
//    Debug.consoleInfo("onFileParsed");
    fileStructureParsed = ParseStatus.PARSED;
    firstLinesParsed = ParseStatus.PARSED;
    fullFileParsed = ParseStatus.PARSED;

    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();
    int type = ((ArrayView) result[2]).ints()[0];

    if (type == FileProxy.ACTIVITY_FILE) { //TODO hack to transfer special data, need to be refactored

      String dag1 = new String(((ArrayView) result[3]).chars());
      properties.put("mermaid", dag1);
//      String dag2 = new String(((ArrayView) result[4]).chars());
//      properties.put("mermaid2", dag2);

//      System.out.println("dag1 = " + dag1);
//      System.out.println("dag2 = " + dag2);

      ParserUtils.updateDocument(document, ints, chars);
    } else if (result.length >= 5) {
      int[] graphInts = ((ArrayView) result[3]).ints();
      char[] graphChars = ((ArrayView) result[4]).chars();
      ParserUtils.updateDocument(document, ints, chars, graphInts, graphChars, false);
      requestResolve(
          Arrays.copyOf(graphInts, graphInts.length),
          copyOf(graphChars)
      );
    } else {
      ParserUtils.updateDocument(document, ints, chars);
    }

    if (debug) {
      Debug.consoleInfo(getFileName() + "/Full file parsed in " + (System.currentTimeMillis() - parsingTimeStart) + "ms");
    }
    if (editor != null) editor.fireFullFileParsed();
  }

  void onResolved(Object[] result) {
    int[] ints = ((ArrayView) result[0]).ints();
    int version = ((ArrayView) result[1]).ints()[0];
    if (document.needReparse() || document.currentVersion != version) return;
    document.onResolve(ints, highlightResolveError);
    computeUsages();
    if (printResolveTime) {
      long resolveTime = System.currentTimeMillis() - resolveTimeStart;
      if (resolveTime >= EditorConst.BIG_RESOLVE_TIME_MS) {
        Debug.consoleInfo(getFileName() + "/Resolved in " + resolveTime + "ms");
      }
    }
  }

  void onFileStructureParsed(Object[] result) {
    if (fullFileParsed == ParseStatus.PARSED) return;
    fileStructureParsed = ParseStatus.PARSED;
    int type = ((ArrayView) result[2]).ints()[0];
    if (type != FileProxy.JAVA_FILE) {
      onFileParsed(result);
      return;
    }

    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();
    boolean saveOldLines = firstLinesParsed == ParseStatus.PARSED;
    ParserUtils.updateDocument(document, ints, chars, saveOldLines);

    if (debug) {
      Debug.consoleInfo(getFileName() + "/File structure parsed in " +
          (System.currentTimeMillis() - parsingTimeStart) + "ms");
    }
  }

  void onVpParsed(Object[] result) {
    if (fullFileParsed == ParseStatus.PARSED) return;
//    Debug.consoleInfo("onVpParsed");
    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();

    ParserUtils.updateDocumentInterval(document, ints, chars);
    if (debug) {
      Debug.consoleInfo(getFileName() + "/Viewport parsed in " +
          (System.currentTimeMillis() - viewportParseStart) + "ms");
    }
  }

  void onFirstLinesParsed(Object[] result) {
    if (fileStructureParsed == ParseStatus.PARSED || fullFileParsed == ParseStatus.PARSED) return;
    firstLinesParsed = ParseStatus.PARSED;
    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();
    ParserUtils.updateDocument(document, ints, chars);
    if (debug) {
      Debug.consoleInfo(getFileName() + "/First lines parsed in " +
          (System.currentTimeMillis() - parsingTimeStart) + "ms");
    }
  }

  void changeModelLanguage(String languageFromParser) {
    String language = language();
    if (!Objects.equals(language, languageFromParser)) {
      if (debug) {
        Debug.consoleInfo(getFileName() + "/change model language: from = "
            + language + " to = " + languageFromParser);
      }
      setLanguage(languageFromParser);
    }
  }

  void computeUsages() {
    Pos caretPos = new Pos(caretLine, caretCharPos);
    Pos elementPos = document.getElementStart(caretLine, caretCharPos);
    computeUsages(caretPos, elementPos);

    if ((definition == null || usages.isEmpty()) && caretCharPos > 0) {
      Pos prevCaretPos = new Pos(caretLine, caretCharPos - 1);
      Pos prevElementPos = document.getElementStart(caretLine, caretCharPos - 1);
      computeUsages(prevCaretPos, prevElementPos);
    }
  }

  private void computeUsages(Pos caretPos, Pos elementPos) {
    clearUsages();

    Pos def = document.getDefinition(elementPos);

    if (def == null) def = elementPos;

    List<Pos> usageList = document.getUsagesList(def);
    if (usageList != null) {
      definition = document.getCodeElement(def);
      for (var usage : usageList) {
        usages.add(document.getCodeElement(usage));
      }
    }

    if (editor != null) {
      editor.useDocumentHighlightProvider(caretPos.line, caretPos.pos);
    }
  }

  void clearUsages() {
    definition = null;
    usages.clear();
  }

  void requestResolve(int[] ints, char[] chars) {
    if (executor != null) {
      resolveTimeStart = System.currentTimeMillis();
      var lastParsedVersion = document.currentVersion;
      executor.sendToWorker(this::onResolved,
          ScopeProxy.RESOLVE_ALL, ints, chars,
          new int[]{lastParsedVersion}
      );
    } else {
      // todo: add ScopeProxy.RESOLVE_ALL to pending jobs
    }
  }

  void setEditor(EditorToModel editor, WorkerJobExecutor executor) {
    this.editor = editor;
    this.executor = executor;
    if (executor != null) {
      if (fullFileParsed == ParseStatus.NOT_PARSED) requestParseFile();
      // todo: execute eny pending jobs here
    }
  }

  void requestParseFile() {
    if (executor == null) return;
    this.parsingTimeStart = System.currentTimeMillis();

    String lang = language();
    boolean isJava = Objects.equals(lang, Languages.JAVA);
    boolean isActivity = Objects.equals(lang, Languages.ACTIVITY);
    char[] chars = document.getChars();
    int size = chars.length;
    int bigFileSize = isJava
        ? EditorConst.FILE_SIZE_10_KB
        : EditorConst.FILE_SIZE_5_KB;
    int langType = Languages.getType(lang);
    if (isActivity) {
      executor.sendToWorker(
          this::onFileParsed, WorkerJobExecutor.ACTIVITY_CHANNEL,
          FileProxy.asyncParseFullFile, chars,
          new int[]{langType});
      fullFileParsed = ParseStatus.SENT;
    } else if (size <= bigFileSize) {
      sendFull(chars, langType);
    } else if (isJava) {
      // Structure parsing is for java only
      sendFirstLines(copyOf(chars), langType);
      sendStructure(copyOf(chars), langType);
      sendFull(chars, langType);
    } else {
      sendFirstLines(copyOf(chars), langType);
      sendFull(chars, langType);
    }
  }

  private void sendFirstLines(char[] chars, int langType) {
    executor.sendToWorker(this::onFirstLinesParsed, FileProxy.asyncParseFirstLines,
        chars, new int[]{langType, EditorConst.FIRST_LINES});
    firstLinesParsed = ParseStatus.SENT;
  }

  private void sendStructure(char[] chars, int langType) {
    executor.sendToWorker(this::onFileStructureParsed, FileProxy.asyncParseFile,
        chars, new int[]{langType});
    fileStructureParsed = ParseStatus.SENT;
  }

  private void sendFull(char[] chars, int langType) {
    executor.sendToWorker(this::onFileParsed, FileProxy.asyncParseFullFile,
        chars, new int[]{langType});
    fullFileParsed = ParseStatus.SENT;
  }

  private String getFileName() {
    return uri != null ? uri.getFileName() : "";
  }

  void parseFullFile() {
    if (debug) {
      Debug.consoleInfo(getFileName() + "/Model::parseFullFile");
    }
    String parseJob = parseJobName(language());
    if (parseJob != null) {
      parsingTimeStart = System.currentTimeMillis();
      if (parseJob.equals(ActivityProxy.PARSE_FULL_FILE))
        executor.sendToWorker(this::onFileParsed,
            WorkerJobExecutor.ACTIVITY_CHANNEL, parseJob, document.getChars());
      else
        executor.sendToWorker(this::onFileParsed, parseJob, document.getChars());
    } else {
      editor.fireFullFileParsed();
    }
  }

  void iterativeParsing() {
    if (debug) {
      Debug.consoleInfo(getFileName() + "/Model::iterativeParsing");
    }

    String language = language();
    if (language == null || Languages.TEXT.equals(language)) {
      document.onReparse();
    } else if (Languages.ACTIVITY.equals(language)) {
      parseFullFile();
      document.onReparse();
    } else {
      var reparseNode = document.tree.getReparseNode();
      if (reparseNode == null) {
        resolveAll();
        document.onReparse();
        return;
      }

      int[] interval = new int[]{reparseNode.getStart(), reparseNode.getStop(), reparseNode.getType()};
      char[] chars = document.getChars();
      int[] type = new int[]{Languages.getType(language)};

      int[] graphInts;
      char[] graphChars;
      if (document.scopeGraph.root != null) {
        ScopeGraph oldGraph = document.scopeGraph;
        ScopeGraph reparseGraph = new ScopeGraph(reparseNode.scope, oldGraph.typeMap);
        ScopeGraphWriter writer = new ScopeGraphWriter(reparseGraph, reparseNode);
        writer.toInts();
        graphInts = writer.graphInts;
        graphChars = writer.graphChars;
      } else {
        graphInts = new int[]{};
        graphChars = new char[]{};
      }
      int version = document.currentVersion;
      executor.sendToWorker(this::onFileIterativeParsed, FileProxy.asyncIterativeParsing, chars, type, interval, new int[]{version}, graphInts, graphChars);
    }
  }

  void onFileIterativeParsed(Object[] result) {
    if (debug) {
      Debug.consoleInfo(getFileName() + "/Model::onFileIterativeParsed");
    }

    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();
    int version = ((ArrayView) result[2]).ints()[0];
    if (document.currentVersion != version) return;
    int[] graphInts = null;
    char[] graphChars = null;
    if (result.length >= 5) {
      graphInts = ((ArrayView) result[3]).ints();
      graphChars = ((ArrayView) result[4]).chars();
    }
    ParserUtils.updateDocumentInterval(document, ints, chars, graphInts, graphChars);
    document.defToUsages.clear();
    document.usageToDef.clear();
    document.countPrefixes();
    document.onReparse();
    resolveAll();
  }

  static String parseJobName(String language) {
    return language != null ? switch (language) {
      case Languages.JAVA -> JavaProxy.PARSE_FULL_FILE_SCOPES;
      case Languages.CPP -> CppProxy.PARSE_FULL_FILE_SCOPES;
      case Languages.JS -> JavaScriptProxy.PARSE_FULL_FILE;
      case Languages.ACTIVITY -> ActivityProxy.PARSE_FULL_FILE;
      case Languages.TEXT -> LineParser.PARSE;
      default -> null;
    } : null;
  }

  void resolveAll() {
    ScopeGraphWriter writer = new ScopeGraphWriter(document.scopeGraph, null);
    writer.toInts();
    requestResolve(writer.graphInts, writer.graphChars);
  }

  void parseViewport(int editorFirstLine, int editorLastLine) {
    if (fullFileParsed == ParseStatus.PARSED || fileStructureParsed != ParseStatus.PARSED) return;
    boolean vpParsed = isVpParsed(editorFirstLine, editorLastLine);
    if (!vpParsed) {
      if (Languages.JAVA.equals(language())) {
        int firstLine = Math.max(0, editorFirstLine - EditorConst.VIEWPORT_OFFSET);
        int lastLine = Math.min(document.length() - 1, editorLastLine + EditorConst.VIEWPORT_OFFSET);
        parsedVps.add(new V2i(firstLine, lastLine));
        int[] vpInts = new int[]{document.getLineStartInd(firstLine), document.getVpEnd(lastLine), firstLine};
        viewportParseStart = System.currentTimeMillis();
        executor.sendToWorker(this::onVpParsed, JavaProxy.PARSE_VIEWPORT,
            document.getChars(), vpInts, document.getIntervals() );
      }
    }
  }

  private boolean isVpParsed(int editorFirstLine, int editorLastLine) {
    return parsedVps.stream().anyMatch(it -> it.x <= editorFirstLine && editorLastLine <= it.y);
  }

  interface EditorToModel {
    void useDocumentHighlightProvider(int line, int column);
    void fireFullFileParsed();
  }
}
