package org.sudu.experiments.editor;

import org.sudu.experiments.Debug;
import org.sudu.experiments.SplitInfo;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.ui.colors.CodeLineColorScheme;
import org.sudu.experiments.editor.worker.ArgsCast;
import org.sudu.experiments.editor.worker.parser.ParseResult;
import org.sudu.experiments.editor.worker.parser.ParseStatus;
import org.sudu.experiments.editor.worker.parser.LangBuildFlags;
import org.sudu.experiments.editor.worker.parser.ParserUtils;
import org.sudu.experiments.editor.worker.proxy.*;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.common.graph.ScopeGraph;
import org.sudu.experiments.parser.common.graph.writer.ScopeGraphWriter;
import org.sudu.experiments.text.SplitText;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.sudu.experiments.math.ArrayOp.copyOf;

public class Model {

  public final Uri uri;
  public final Document document;

  public Object platformObject;
  private String docLanguage;
  String encoding;

  final Selection selection = new Selection();
  final NavigationStack navStack = new NavigationStack();

  EditorToModel editor;

  WorkerJobExecutor executor;

  LineDiff[] diffModel;

  // this properties might need to be converted
  //   to Subscribers<CaretChangeListeners>
  int caretLine, caretCharPos;
  CodeElement definition = null;
  final List<CodeElement> usages = new ArrayList<>();
  Range[] highlightRanges;
  final List<V2i> parsedVps = new ArrayList<>();
  private SemanticTokenInfo[] pendingSemanticTokens;
  private SemanticTokenInfo[] documentTokens;

  int fullFileLexed = ParseStatus.NOT_PARSED;
  int fileStructureParsed = ParseStatus.NOT_PARSED;
  int fullFileParsed = ParseStatus.NOT_PARSED;
  int iterativeFileParsed = ParseStatus.PARSED;

  long parsingTimeStart, viewportParseStart, resolveTimeStart;

  boolean highlightResolveError, printResolveTime;

  double vScrollLine = .0;

  boolean debug = false;

  private Consumer<int[]> onDiffMadeListener;
  private BiConsumer<CpxDiff, Boolean> syncEditing;
  private Supplier<UndoBuffer> getUndoBuffer;

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
    this(new Document(text), language, uri);
  }

  public Model(Document document, String language, Uri uri) {
    this.uri = uri;
    docLanguage = language;
    this.document = document;
    document.updateModelOnDiff = this::updateModelOnDiff;
    document.onEditMade = this::onDiffMade;
    document.onChangeApplied = this::onChangeApplied;
    document.syncEditing = this::syncEditing;
    document.getUndoBuffer = this::getUndoBuffer;
    document.getCaretPos = this::getCaretPos;
    document.getSelection = () -> this.selection;
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
    String lang = docLanguage != null ? docLanguage : languageFromFile();
    return Languages.resolveLanguage(lang);
  }

  public String docLanguage() {
    return docLanguage;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  public String encoding() {
    return encoding;
  }

  public String uriScheme() {
    return uri != null ? uri.scheme : null;
  }

  public V2i getCaretPos() {
    return new V2i(caretLine, caretCharPos);
  }

  public boolean fileLexed() {
    return fullFileLexed == ParseStatus.PARSED;
  }

  void onFileParsed(Object[] result) {
    var parseRes = new ParseResult(result);
    if (parseRes.version != document.currentVersion) {
      requestParseFile();
      return;
    }

    ParserUtils.updateDocument(document, parseRes);
    if (parseRes.haveGraph()) requestResolve(
        copyOf(parseRes.graphInts),
        copyOf(parseRes.graphChars)
    );
    printParsingTime("Full file parsed");
    if (fullFileLexed != ParseStatus.PARSED && editor != null) editor.fireFileLexed();
    fileStructureParsed = ParseStatus.PARSED;
    fullFileLexed = ParseStatus.PARSED;
    fullFileParsed = ParseStatus.PARSED;
  }

  private void printParsingTime(String msg) {
    if (!debug) return;
    long time = System.currentTimeMillis() - parsingTimeStart;
    Debug.consoleInfo(String.format("%s/%s in %dms", getFileName(), msg, time));
  }

  void onResolved(Object[] result) {
    int[] ints = ArgsCast.intArray(result, 0);
    int version = ArgsCast.intArray(result, 1)[0];
    if (document.needReparse() || document.currentVersion != version) return;
    document.onResolve(ints, highlightResolveError);
    computeUsages();
    if (printResolveTime) printParsingTime("Resolved");
  }

  void onFileStructureParsed(Object[] result) {
    if (fullFileParsed == ParseStatus.PARSED) return;
    fileStructureParsed = ParseStatus.PARSED;
    var parseRes = new ParseResult(result);
    if (parseRes.version != document.currentVersion) return;
    if (parseRes.language != FileProxy.JAVA_FILE) {
      onFileParsed(result);
      return;
    }

    boolean saveOldLines = fullFileLexed == ParseStatus.PARSED;
    ParserUtils.updateDocument(document, parseRes, saveOldLines);
    printParsingTime("File structure parsed");
  }

  void onVpParsed(Object[] result) {
    if (fullFileParsed == ParseStatus.PARSED) return;
    var parseRes = new ParseResult(result);
    if (parseRes.version != document.currentVersion) return;
    ParserUtils.updateDocumentInterval(document, parseRes);
    printParsingTime("Viewport parsed");
  }

  void onFileLexed(Object[] result) {
    var parseRes = new ParseResult(result);
    if (parseRes.version != document.currentVersion) return;
    ParserUtils.updateDocument(document, parseRes);
    fullFileLexed = ParseStatus.PARSED;
    printParsingTime("Full file lexed");
    if (isDisableParser()) setParsed();
    else {
      if (editor != null) editor.fireFileLexed();
      sendStructure();
      sendFull();
    }
    setSemanticTokens(pendingSemanticTokens);
  }

  void onFileLexedIterative(Object[] result) {
    var parseRes = new ParseResult(result);
    if (parseRes.version != document.currentVersion) return;
    ParserUtils.updateDocument(document, parseRes);
    fullFileLexed = iterativeFileParsed = ParseStatus.PARSED;
    printParsingTime("Full file lexed");
    if (isDisableParser()) setParsed();
    else {
      if (editor != null) editor.fireFileLexed();
      sendStructure();
      sendFull();
    }
    setSemanticTokens(pendingSemanticTokens);
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
    if (document.isMapped() && document.sourceIndex(caretLine) < 0) {
      clearUsages();
      return;
    }
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

    Pos sourceElem = document.toSourcePos(elementPos);
    if (sourceElem == null) return;

    Pos def = document.getDefinition(sourceElem);

    if (def == null) def = sourceElem;

    List<Pos> usageList = document.getUsagesList(def);
    if (usageList != null) {
      Pos defDoc = document.toDocPos(def);
      if (defDoc != null) definition = document.getCodeElement(defDoc);
      for (var usage: usageList) {
        Pos usageDoc = document.toDocPos(usage);
        if (usageDoc != null) usages.add(document.getCodeElement(usageDoc));
      }
    }

    if (editor != null) {
      editor.useDocumentHighlightProvider(caretPos.line, caretPos.charPos);
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
      executor.sendToWorker(true, this::onResolved,
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
    applyDocumentTokens();
    if (executor != null) {
      if (fullFileParsed == ParseStatus.NOT_PARSED) requestParseFile();
      // todo: execute eny pending jobs here
    } else {
      document.invalidateMeasure();
    }
  }

  public void setExecutor(WorkerJobExecutor executor) {
    this.executor = executor;
  }

  private void setParsed() {
    fileStructureParsed = ParseStatus.PARSED;
    fullFileLexed = ParseStatus.PARSED;
    fullFileParsed = ParseStatus.PARSED;
    if (editor != null) editor.fireFileLexed();
  }

  void requestParseFile() {
    if (debug) System.out.println("Model.requestParseFile");
    if (executor == null) return;
    if (isEmpty()) {
      setParsed();
      return;
    }
    this.parsingTimeStart = System.currentTimeMillis();

    String lang = language();
    int langType = Languages.getType(lang);
    char[] chars = sourceChars();
    if (isDisableParser()) {
      sendLexer(chars, langType);
      return;
    }

    boolean isActivity = Objects.equals(lang, Languages.ACTIVITY);
    boolean isJava = Objects.equals(lang, Languages.JAVA);
    boolean isJSON = Objects.equals(lang, Languages.JSON);
    boolean isHTML = Objects.equals(lang, Languages.HTML);

    int size = chars.length;
    int bigFileSize = isJava
        ? EditorConst.FILE_SIZE_10_KB
        : (isJSON || isHTML)          // Json & html grammars are fast to parse
        ? EditorConst.FILE_SIZE_50_KB
        : EditorConst.FILE_SIZE_5_KB;

    if (size <= bigFileSize || isActivity) {
      sendFull(chars, langType);
    } else {
      sendLexer(chars, langType);
    }
  }

  private char[] sourceChars() {
    return document.getSourceChars();
  }

  private void sendStructure() {
    sendStructure(sourceChars(), Languages.getType(language()));
  }

  private void sendFull() {
    sendFull(sourceChars(), Languages.getType(language()));
  }

  private void sendLexer(char[] chars, int langType) {
    executor.sendToWorker(true, this::onFileLexed,
        FileProxy.asyncLexer,
        chars, new int[]{langType, Integer.MAX_VALUE, document.currentVersion});
    pendingSemanticTokens = null;
    fullFileParsed = ParseStatus.SENT;
  }

  private void sendLexerIterative(char[] chars, int langType) {
    executor.sendToWorker(true, this::onFileLexedIterative,
            FileProxy.asyncLexer,
            chars, new int[]{langType, Integer.MAX_VALUE, document.currentVersion});
    pendingSemanticTokens = null;
    iterativeFileParsed = ParseStatus.SENT;
  }

  private void sendStructure(char[] chars, int langType) {
    if (langType != FileProxy.JAVA_FILE) return;
    executor.sendToWorker(true, this::onFileStructureParsed,
        FileProxy.asyncParseFile,
        chars, new int[]{langType, document.currentVersion});
    fileStructureParsed = ParseStatus.SENT;
  }

  private void sendFull(char[] chars, int langType) {
    executor.sendToWorker(true, this::onFileParsed,
        FileProxy.asyncParseFullFile,
        chars, new int[]{langType, document.currentVersion});
    fullFileParsed = ParseStatus.SENT;
  }

  private String getFileName() {
    return uri != null ? uri.getFileName() : "";
  }

  void parseFullFile() {
    if (debug) {
      Debug.consoleInfo(getFileName() + "/Model::parseFullFile");
    }
    char[] chars = sourceChars();
    if (isDisableParser()) {
      sendLexer(chars, Languages.getType(language()));
      return;
    }
    String parseJob = parseJobName(language());
    if (parseJob != null) {
      parsingTimeStart = System.currentTimeMillis();
      executor.sendToWorker(true, this::onFileParsed,
          parseJob, chars, new int[]{Languages.getType(language()), document.version()});
    } else if (editor != null) editor.fireFileLexed();
  }

  void iterativeParsing() {
    if (debug) {
      Debug.consoleInfo(getFileName() + "/Model::iterativeParsing");
    }
    if (document.isMapped()) return;
    iterativeFileParsed = ParseStatus.NOT_PARSED;
    if (fullFileParsed != ParseStatus.PARSED) return;

    String language = language();
    if (isDisableParser()) {
      char[] chars = sourceChars();
      int langType = Languages.getType(language);
      sendLexerIterative(chars, langType);
      return;
    }
    var reparseNode = document.tree.getReparseNode();
    if (reparseNode == null) {
      resolveAll();
      document.onReparse();
      if (editor != null) editor.fireFileLexed();
      return;
    }

    int start = reparseNode.getStart(), stop = reparseNode.getStop();
    int[] interval = new int[]{start, stop, reparseNode.getType()};
    char[] chars = sourceChars();
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
    iterativeFileParsed = ParseStatus.SENT;
    executor.sendToWorker(true,
        res -> onFileIterativeParsed(res, start, stop), FileProxy.asyncIterativeParsing,
        chars, type, interval, new int[]{Languages.getType(language()), version}, graphInts, graphChars
    );
  }

  void onFileIterativeParsed(Object[] result, int start, int stop) {
    if (debug) {
      Debug.consoleInfo(getFileName() + "/Model::onFileIterativeParsed");
    }
    var parseRes = new ParseResult(result);
    if (parseRes.version != document.currentVersion) return;
    if (!Languages.isFullReparseOnEdit(language())) {
      ParserUtils.updateDocumentInterval(document, parseRes);
      document.defToUsages.clear();
      document.usageToDef.clear();
      document.countPrefixes();
      document.onReparse();
      resolveAll();
    } else {
      ParserUtils.updateDocument(document, parseRes);
      document.onReparse();
    }
    iterativeFileParsed = ParseStatus.PARSED;
    if (editor != null) editor.fireFileIterativeParsed(start, stop);
  }

  static String parseJobName(String language) {
    if (language == null) return null;
    if (Languages.JAVA.equals(language) && LangBuildFlags.JAVA)
      return JavaProxy.PARSE_FULL_FILE_SCOPES;
    if (Languages.CPP.equals(language) && LangBuildFlags.CPP)
      return CppProxy.PARSE_FULL_FILE_SCOPES;
    if (Languages.JS.equals(language) && LangBuildFlags.JS)
      return JavaScriptProxy.PARSE_FULL_FILE;
    if (Languages.TS.equals(language) && LangBuildFlags.TS)
      return TypeScriptProxy.PARSE_FULL_FILE;
    if (Languages.ACTIVITY.equals(language) && LangBuildFlags.ACTIVITY)
      return ActivityProxy.PARSE_FULL_FILE;
    if (Languages.HTML.equals(language) && LangBuildFlags.HTML)
      return HtmlProxy.PARSE_FULL_FILE;
    if (Languages.JSON.equals(language) && LangBuildFlags.JSON)
      return JsonProxy.PARSE_FULL_FILE;
    if (Languages.PYTHON.equals(language) && LangBuildFlags.PYTHON)
      return PythonProxy.PARSE_FULL_FILE;
    if (Languages.ASCEND_C.equals(language) && LangBuildFlags.ASCEND_C)
      return AscendCProxy.PARSE_FULL_FILE;
    return TextProxy.PARSE_FULL_FILE;
  }

  void resolveAll() {
    ScopeGraphWriter writer = new ScopeGraphWriter(document.scopeGraph, null);
    writer.toInts();
    requestResolve(writer.graphInts, writer.graphChars);
  }

  void parseViewport(int editorFirstLine, int editorLastLine) {
    if (fullFileParsed == ParseStatus.PARSED || fileStructureParsed != ParseStatus.PARSED) return;
    if (!LangBuildFlags.JAVA || !Languages.JAVA.equals(language())) return;

    int firstLine = Math.max(0, editorFirstLine - EditorConst.VIEWPORT_OFFSET);
    int lastLine = Math.min(document.length() - 1, editorLastLine + EditorConst.VIEWPORT_OFFSET);
    int srcFirst = firstLine, srcLast = lastLine;
    int[] vpInts;
    if (document.isMapped()) {
      srcFirst = Document.lowerBound(document.sourceToDoc, firstLine);
      srcLast = Document.upperBound(document.sourceToDoc, lastLine) - 1;
      if (srcFirst < document.sourceToDoc.length
          && document.sourceToDoc[srcFirst] > firstLine
          && srcFirst > 0) {
        srcFirst--;
      }
      if (srcFirst > srcLast) return;
      if (isVpParsed(srcFirst, srcLast)) return;
      parsedVps.add(new V2i(srcFirst, srcLast));
      int vpStart = document.srcStart[srcFirst];
      int vpEnd = document.srcStart[srcLast]
          + document.strLength(document.sourceToDoc[srcLast]);
      vpInts = new int[]{vpStart, vpEnd, srcFirst};
    } else {
      if (isVpParsed(firstLine, lastLine)) return;
      parsedVps.add(new V2i(firstLine, lastLine));
      vpInts = new int[]{document.getLineStartInd(firstLine), document.getVpEnd(lastLine), firstLine};
    }
    viewportParseStart = System.currentTimeMillis();
    executor.sendToWorker(true, this::onVpParsed,
        JavaProxy.PARSE_VIEWPORT,
        sourceChars(), vpInts,
        document.getIntervals(),
        new int[]{Languages.getType(language()), document.currentVersion}
    );
  }

  /**
   * Finds the nearest highlighted range strictly after ({@code next}) or
   * before the caret, by range start. Returns null when none exist.
   *
   * <p>For prev, ranges that contain the caret are skipped so a caret inside
   * a match does not re-select that same match.
   */
  Range findAdjacentHighlightRange(boolean next) {
    if (highlightRanges == null) return null;
    Range best = null;
    for (Range r: highlightRanges) {
      if (r == null) continue;
      int cmp = comparePos(r.startLineNumber, r.startColumn, caretLine, caretCharPos);
      if ((next && cmp <= 0) || (!next && (cmp >= 0 || containsCaret(r)))) continue;
      if (best == null) {
        best = r;
        continue;
      }
      cmp = comparePos(r.startLineNumber, r.startColumn, best.startLineNumber, best.startColumn);
      if ((next && cmp < 0) || (!next && cmp > 0)) best = r;
    }
    return best;
  }

  /** True when caret is in {@code [start, end)} of {@code r}. */
  private boolean containsCaret(Range r) {
    return comparePos(r.startLineNumber, r.startColumn, caretLine, caretCharPos) <= 0
        && comparePos(r.endLineNumber, r.endColumn, caretLine, caretCharPos) > 0;
  }

  private static int comparePos(int line1, int col1, int line2, int col2) {
    if (line1 != line2) return Integer.compare(line1, line2);
    return Integer.compare(col1, col2);
  }

  private boolean isEmpty() {
    return document.length() == 1
        && document.lines[0].length() == 1
        && document.lines[0].get(0).length() == 0;
  }

  private boolean isVpParsed(int editorFirstLine, int editorLastLine) {
    return parsedVps.stream().anyMatch(it -> it.x <= editorFirstLine && editorLastLine <= it.y);
  }

  private void updateModelOnDiff(Diff diff, boolean isUndo) {
    if (editor != null) editor.updateModelOnDiff(diff, isUndo);
  }

  private void syncEditing(CpxDiff diffs, boolean isUndo) {
    if (syncEditing != null) syncEditing.accept(diffs, isUndo);
  }

  private UndoBuffer getUndoBuffer() {
    return getUndoBuffer != null ? getUndoBuffer.get() : null;
  }

  public void setSyncEditing(BiConsumer<CpxDiff, Boolean> syncEditing) {
    this.syncEditing = syncEditing;
  }

  public void setGetUndoBuffer(Supplier<UndoBuffer> getUndoBuffer) {
    this.getUndoBuffer = getUndoBuffer;
  }

  public void setOnDiffMadeListener(Consumer<int[]> listener) {
    onDiffMadeListener = listener;
  }

  public void setSemanticTokens(SemanticTokenInfo[] tokens) {
    if (tokens == null) return;
    if (fullFileLexed == ParseStatus.PARSED) {
      for (var token: tokens) setSemanticToken(token);
      pendingSemanticTokens = null;
    } else {
      pendingSemanticTokens = tokens;
    }
  }

  private void setSemanticToken(SemanticTokenInfo token) {
    if (token == null || token.line < 0 || token.line >= document.sourceCount()) return;
    int docLine = document.toDocLine(token.line);
    if (docLine < 0) return;
    applyTokenAt(token, docLine);
  }

  /**
   * Tokens that belong to the document as built (e.g. disassembly run segments
   * from {@code newTextModelFromLines}). Unlike semantic tokens they address
   * document lines directly, so attached lines can be colored.
   *
   * <p><b>Attached lines</b> (no {@code lineNumber}, not in {@code sourceToDoc})
   * are the intended target: the parser never sees them, so their colors live for
   * the whole lifetime of the document.
   *
   * <p><b>Numbered (source) lines are best-effort only.</b> The editor's job is
   * to colorize source lines from the language parser, so a caller-supplied
   * {@code tokenType} there is a hint, not a guarantee. {@code ParserUtils.updateDocument}
   * rebuilds every source {@code CodeLine} from parser output, and
   * {@code Document.setLine} then copies the old element colors back <b>by
   * position</b> when the element count is unchanged (it does not compare text).
   * So a caller color may survive, may be attached to a different parser element,
   * or may be dropped when the parser changes the split. Semantic tokens later
   * delivered by the language parser overwrite matching elements through
   * {@code setSemanticTokens}. Do not rely on these colors on numbered lines;
   * colors on attached lines are unaffected by parsing.
   *
   * <p>Colors are resolved through the editor color scheme, so they are applied
   * when the editor is attached (or right away if it already is).
   */
  public void setDocumentTokens(SemanticTokenInfo[] tokens) {
    documentTokens = tokens;
    applyDocumentTokens();
  }

  /**
   * Re-resolves document-token colors against the current editor color scheme.
   * Needed after a theme change, which rebuilds the scheme (and therefore the
   * palette of explicit colors). Does nothing when no document tokens exist.
   */
  public void reapplyDocumentTokens() {
    applyDocumentTokens();
  }

  private void applyDocumentTokens() {
    if (documentTokens == null || editor == null) return;
    if (editor.getColorScheme() == null) return;
    for (var token: documentTokens) {
      if (token == null || token.line < 0 || token.line >= document.length()) continue;
      applyTokenAt(token, token.line);
    }
  }

  private void applyTokenAt(SemanticTokenInfo token, int docLine) {
    var line = document.line(docLine);
    if (token.startCharPos < 0 || token.startCharPos >= line.totalStrLength) return;
    var element = line.getCodeElement(token.startCharPos);
    if (element == null || !element.s.equals(token.text)) {
      String errMsg = element == null
          ? "element at (" + token.line + ":" + token.startCharPos + ") is null"
          : "element.s(" + element.s + ") != token.text(" + token.text
          + "), token.line=" + token.line + ", token.startCharPos =  " + token.startCharPos;
      System.err.println(errMsg);
      return;
    }
    if (token.hasColor() && editor != null) {
      var colorScheme = editor.getColorScheme();
      token.tokenType = colorScheme.getSemanticIndex(token.foreground, token.background);
    }
    line.contentDirty = element.style != token.tokenStyle;
    element.color = token.tokenType;
    element.style = token.tokenStyle;
  }

  private void onDiffMade(CpxDiff cpxDiff) {
    if (editor != null) editor.onDiffMade();
    if (onDiffMadeListener != null) onDiffMadeListener.accept(cpxDiff.diffLineRanges());
  }

  private void onChangeApplied(CpxDiff cpxDiff) {
    if (editor != null) editor.onDiffMade();
  }

  CodeLine caretCodeLine() {
    return document.lines[caretLine];
  }

  private boolean isDisableParser() {
    return editor != null ? editor.isDisableParser() : EditorConst.DEFAULT_DISABLE_PARSER;
  }

  interface EditorToModel {
    void useDocumentHighlightProvider(int line, int column);

    void fireFileLexed();

    void fireFileIterativeParsed(int start, int stop);

    void updateModelOnDiff(Diff diff, boolean isUndo);

    void onDiffMade();

    boolean isDisableParser();

    CodeLineColorScheme getColorScheme();
  }

  public boolean hasDiffModel() {
    return LineDiff.notEmpty(diffModel);
  }
}
