package org.sudu.experiments.editor;

import org.sudu.experiments.Debug;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.worker.parser.LineParser;
import org.sudu.experiments.editor.worker.parser.ParserUtils;
import org.sudu.experiments.editor.worker.proxy.*;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.worker.ArrayView;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.*;

public class Model {

  public final Uri uri;
  public final Document document;

  public Object platformObject;
  private String docLanguage;
  private String languageFromFile;

  final Selection selection = new Selection();
  final NavigationStack navStack = new NavigationStack();

  EditorToModel editor;
  WorkerJobExecutor executor;

  LineDiff[] diffModel;
  int caretLine, caretCharPos, caretPos;
  CodeElement definition = null;
  final List<CodeElement> usages = new ArrayList<>();
  final Map<String, String> properties = new HashMap<>();

  boolean fullFileParsed, fileStructureParsed, firstLinesParsed;
  long parsingTimeStart, viewportParseStart, resolveTimeStart;
  boolean highlightResolveError, printResolveTime;

  public Model(String text, String language, Uri uri) {
    this(SplitText.split(text), language, uri);
  }

  public Model(String[] text, Uri uri) {
    this(text, null, uri);
  }

  public Model(String[] text, String language, Uri uri) {
    this.uri = uri;
    docLanguage = language;
    languageFromFile = uri != null ? Languages.languageFromFilename(uri.path) : null;
    document = new Document(text);
  }

  public Model() {
    this.document = new Document();
    this.docLanguage = Languages.TEXT;
    this.uri = null;
  }

  public void setLanguage(String language) {
    docLanguage = language;
  }

  public String language() {
    return docLanguage != null ? docLanguage : languageFromFile;
  }

  public String docLanguage() { return docLanguage; }

  public String uriScheme() {
    return uri != null ? uri.scheme : null;
  }

  void onFileParsed(Object[] result) {
    Debug.consoleInfo("onFileParsed");
    fileStructureParsed = true;
    firstLinesParsed = true;
    fullFileParsed = true;

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
          Arrays.copyOf(graphChars, graphChars.length)
      );
    } else {
      ParserUtils.updateDocument(document, ints, chars);
    }

    changeModelLanguage(Languages.getLanguage(type));
    Debug.consoleInfo("Full file parsed in " + (System.currentTimeMillis() - parsingTimeStart) + "ms");
    editor.fireFullFileParsed();
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
        System.out.println("Resolved in " + resolveTime + "ms");
      }
    }
  }

  void onFileStructureParsed(Object[] result) {
    if (fullFileParsed) return;
    fileStructureParsed = true;
    int type = ((ArrayView) result[2]).ints()[0];
    if (type != FileProxy.JAVA_FILE) {
      onFileParsed(result);
      return;
    }

    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();

    ParserUtils.updateDocument(document, ints, chars, firstLinesParsed);
    changeModelLanguage(Languages.getLanguage(type));

    Debug.consoleInfo("File structure parsed in " + (System.currentTimeMillis() - parsingTimeStart) + "ms");
  }

  void onVpParsed(Object[] result) {
//    Debug.consoleInfo("onVpParsed");
    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();

    ParserUtils.updateDocumentInterval(document, ints, chars);

    Debug.consoleInfo("Viewport parsed in " + (System.currentTimeMillis() - viewportParseStart) + "ms");
  }

  void onFirstLinesParsed(Object[] result) {
    if (fileStructureParsed || fullFileParsed) return;
    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();
    ParserUtils.updateDocument(document, ints, chars);
    firstLinesParsed = true;
    Debug.consoleInfo("First lines parsed in " + (System.currentTimeMillis() - parsingTimeStart) + "ms");
  }

  void changeModelLanguage(String languageFromParser) {
    String language = language();
    if (!Objects.equals(language, languageFromParser)) {
      Debug.consoleInfo("change model language: from = " + language + " to = " + languageFromParser);
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
      if (!fullFileParsed) requestParseFile();
      // todo: execute eny pending jobs here
    }
  }

  void requestParseFile() {
    this.parsingTimeStart = System.currentTimeMillis();
    String jobName = parseJobName(language());
    if (jobName != null) {
      executor.sendToWorker(this::onFileParsed, jobName, document.getChars());
    }
  }

  void parseFullFile() {
    Debug.consoleInfo("Model::parseFullFile");
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

  interface EditorToModel {
    void useDocumentHighlightProvider(int line, int column);
    void fireFullFileParsed();
  }
}
