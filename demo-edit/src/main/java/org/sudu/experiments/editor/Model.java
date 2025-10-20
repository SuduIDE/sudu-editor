package org.sudu.experiments.editor;

import org.sudu.experiments.Debug;
import org.sudu.experiments.SplitInfo;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.worker.ArgsCast;
import org.sudu.experiments.editor.worker.parser.ParseResult;
import org.sudu.experiments.editor.worker.parser.ParseStatus;
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

import static org.sudu.experiments.math.ArrayOp.copyOf;

public class Model extends Model0 {

  public final Uri uri;
  public final Document document;

  public Object platformObject;

  final NavigationStack navStack = new NavigationStack();

  Runnable onDiffMadeListener;

  LineDiff[] diffModel;

  // this properties might need to be converted
  //   to Subscribers<CaretChangeListeners>
  final List<V2i> parsedVps = new ArrayList<>();

  int fullFileLexed = ParseStatus.NOT_PARSED;
  int fileStructureParsed = ParseStatus.NOT_PARSED;
  int fullFileParsed = ParseStatus.NOT_PARSED;
  int iterativeVersion;

  long parsingTimeStart, viewportParseStart, resolveTimeStart;


  boolean debug = false;

  public Model(String text, Uri uri) {
    this(text, null, uri);
  }

  public Model(String text, String language, Uri uri) {
    this(SplitText.splitInfo(text), language, uri);
  }

  public Model(SplitInfo text, String language, Uri uri) {
    this(text.lines, language, null, uri);
  }

  public Model(String[] text, Uri uri) {
    this(text, null, null, uri);
  }

  public Model() {
    this(new String[0], null, null, null);
  }

  public Model(String[] text, String language, String encoding, Uri uri) {
    this.uri = uri;
    document = new Document(text);
    document.language = language;
    document.encoding = encoding;
    document.updateModelOnDiff = this::updateModelOnDiff;
    document.onDiffMade = this::onDiffMade;
  }

  String languageFromFile() {
    return uri != null
        ? Languages.languageFromFilename(uri.path)
        : Languages.TEXT;
  }

  public void setLanguage(String language) {
    document.language = language;
  }

  public String language() {
    return document.language != null ? document.language : languageFromFile();
  }

  public void setEncoding(String encoding) {
    document.encoding = encoding;
  }

  public String encoding() {
    return document.encoding;
  }

  public CodeLine line(int i) {
    return document.lines[i];
  }

  public String uriScheme() {
    return uri != null ? uri.scheme : null;
  }

  public V2i getCaretPos() {
    return new V2i(caretLine, caretCharPos);
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
    printParsingTime("Full file lexed");
    if (isDisableParser()) setParsed();
    else {
      if (editor != null) editor.fireFileLexed();
      sendStructure();
      sendFull();
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
      for (var usage: usageList) {
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
      executor.sendToWorker(true, this::onResolved,
          ScopeProxy.RESOLVE_ALL, ints, chars,
          new int[]{lastParsedVersion}
      );
    } else {
      // todo: add ScopeProxy.RESOLVE_ALL to pending jobs
    }
  }

  void setEditor(EditorToModel editor, WorkerJobExecutor executor) {
    super.setEditor(editor, executor);
    if (executor != null) {
      if (fullFileParsed == ParseStatus.NOT_PARSED)
        requestParseFile();
      // todo: execute eny pending jobs here
    }
  }

  @Override
  void documentInvalidateMeasure() {
    document.invalidateMeasure();
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
    char[] chars = document.getChars();
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

  private void sendStructure() {
    sendStructure(document.getChars(), Languages.getType(language()));
  }

  private void sendFull() {
    sendFull(document.getChars(), Languages.getType(language()));
  }

  private void sendLexer(char[] chars, int langType) {
    executor.sendToWorker(true, this::onFileLexed,
        FileProxy.asyncLexer,
        chars, new int[]{langType, Integer.MAX_VALUE, document.currentVersion});
    fullFileParsed = ParseStatus.SENT;
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

  @Override
  void parseFullFile() {
    if (debug) {
      Debug.consoleInfo(getFileName() + "/Model::parseFullFile");
    }
    char[] chars = document.getChars();
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

    if (fullFileParsed != ParseStatus.PARSED) return;

    String language = language();
    if (isDisableParser()) {
      char[] chars = document.getChars();
      int langType = Languages.getType(language);
      sendLexer(chars, langType);
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
    if (editor != null) editor.fireFileIterativeParsed(start, stop);
  }

  static String parseJobName(String language) {
    return language != null ? switch (language) {
      case Languages.JAVA -> JavaProxy.PARSE_FULL_FILE_SCOPES;
      case Languages.CPP -> CppProxy.PARSE_FULL_FILE_SCOPES;
      case Languages.JS -> JavaScriptProxy.PARSE_FULL_FILE;
      case Languages.TS -> TypeScriptProxy.PARSE_FULL_FILE;
      case Languages.ACTIVITY -> ActivityProxy.PARSE_FULL_FILE;
      case Languages.HTML -> HtmlProxy.PARSE_FULL_FILE;
      case Languages.JSON -> JsonProxy.PARSE_FULL_FILE;
      default -> TextProxy.PARSE_FULL_FILE;
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
        executor.sendToWorker(true, this::onVpParsed,
            JavaProxy.PARSE_VIEWPORT,
            document.getChars(), vpInts,
            document.getIntervals(),
            new int[]{Languages.getType(language()), document.currentVersion}
        );
      }
    }
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

  public void setOnDiffMadeListener(Runnable listener) {
    onDiffMadeListener = listener;
  }

  private void onDiffMade() {
    if (editor != null) editor.onDiffMade();
    if (onDiffMadeListener != null)
      onDiffMadeListener.run();
  }

  @Override
  CodeLine caretCodeLine() {
    return document.lines[caretLine];
  }

  private boolean isDisableParser() {
    return editor != null ? editor.isDisableParser() : EditorConst.DEFAULT_DISABLE_PARSER;
  }

  @Override
  boolean hasDiffModel() {
    return LineDiff.notEmpty(diffModel);
  }

  @Override
  public void update(double timestamp) {
    if (document.needReparse(timestamp) && iterativeVersion != document.currentVersion) {
      iterativeVersion = document.currentVersion;
      iterativeParsing();
    }
  }

  @Override
  LineDiff lineDiff(int i) {
    return diffModel == null || i >= diffModel.length ? null : diffModel[i];
  }

  @Override
  CodeLineMapping defaultMapping() {
    return new CodeLineMapping.Id(document);
  }

  @Override
  public Document document() {
    return document;
  }

  @Override
  Uri uri() {
    return uri;
  }

  @Override
  int length() {
    return document.lines.length;
  }

  @Override
  void invalidateFont() {
    document.invalidateFont();
  }

  // editing


  @Override
  void updateDocumentDiffTimeStamp() {
    document.setLastDiffTimestamp(editor.timeNow());
  }

  // todo: need to restore selection state after undo
  @Override
  void undoLastDiff(boolean isRedo) {
    if (selection.isAreaSelected())
      setSelectionToCaret();
    var caretDiff = document.undoLastDiff(isRedo);
    if (caretDiff == null) return;
    var caretReturn = isRedo ? caretDiff.caretPos : caretDiff.caretReturn;
    setCaretLinePos(caretReturn.x, caretReturn.y);
    updateDocumentDiffTimeStamp();
    onDiffMade();
  }

  public String onCopy(boolean isCut) {
    var left = selection.getLeftPos();
    int line = left.line;
    String result;

    if (!selection.isAreaSelected()) {
      result = document.copyLine(line);
      int newLine = Math.min(document.length() - 1, line);

      selection.endPos.set(newLine, 0);
      if (line < document.length() - 1)
        selection.startPos.set(newLine + 1, 0);
      else
        selection.endPos.set(newLine, document.strLength(newLine));

      if (isCut) deleteSelectedArea();

    } else {
      result = document.copy(selection, isCut);
      if (isCut) {
        setCaretLinePos(left.line, left.charInd);
        setSelectionToCaret();
        updateDocumentDiffTimeStamp();
      }
    }
    return result;
  }

  public void handleInsert(String s) {
    if (selection.isAreaSelected()) deleteSelectedArea();
    String[] lines = SplitText.split(s);

    document.insertLines(caretLine, caretCharPos, lines);

    int newCaretLine = caretLine + lines.length - 1;
    int newCaretPos;
    if (newCaretLine == caretLine) newCaretPos = caretCharPos + lines[0].length();
    else newCaretPos = lines[lines.length - 1].length();

    setCaretLinePos(newCaretLine, newCaretPos);
    setSelectionToCaret();
    updateDocumentDiffTimeStamp();
  }

  void deleteSelectedArea() {
    var leftPos = selection.getLeftPos();
    document.deleteSelected(selection);
    setCaretLinePos(leftPos.line, leftPos.charInd);
    setSelectionToCaret();
    updateDocumentDiffTimeStamp();
  }

  void newLine() {
    if (selection.isAreaSelected()) deleteSelectedArea();
    document.lines[caretLine].invalidateCache();
    document.newLineOp(caretLine, caretCharPos);
    updateDocumentDiffTimeStamp();
    setCaretLinePos(caretLine + 1, 0);
  }

  void handleDelete() {
    if (selection.isAreaSelected()) deleteSelectedArea();
    else document.deleteChar(caretLine, caretCharPos);
    if (editor != null) {
      editor.recomputeCaretPosY();
      editor.recomputeCaretPosX();
    }
    updateDocumentDiffTimeStamp();
  }

  void handleBackspace() {
    if (selection.isAreaSelected()) {
      deleteSelectedArea();
    } else {
      if (caretCharPos == 0 && caretLine == 0) return;

      int cLine, cPos;
      if (caretCharPos == 0) {
        cLine = caretLine - 1;
        cPos = document.strLength(cLine);
        document.concatLines(cLine);
      } else {
        cLine = caretLine;
        cPos = caretCharPos - 1;
        document.deleteChar(cLine, cPos);
      }
      updateDocumentDiffTimeStamp();
      setCaretLinePos(cLine, cPos);
    }
  }

  void handleTab(boolean shiftPressed) {
    if (shiftPressed) handleShiftTabOp();
    else handleTabOp();
  }

  private void handleTabOp() {
    if (selection.isAreaSelected()) {
      Selection.SelPos left = selection.getLeftPos();
      Selection.SelPos right = selection.getRightPos();
      int size = right.line - left.line + 1;
      int[] lines = new int[size];
      String[] changes = new String[size];
      int i = 0;
      for (int l = left.line; l <= right.line; l++) {
        lines[i] = l;
        changes[i++] = tabIndent;
      }

      tabDiffHandler(
          lines, 0, false, changes,
          new Pos(caretLine, caretCharPos),
          (l, c) -> document.insertAt(l, 0, tabIndent)
      );

      left.charInd += tabIndent.length();
      right.charInd += tabIndent.length();
      setCaretPos(caretCharPos + length());
      updateDocumentDiffTimeStamp();
    } else {
      handleInsert(tabIndent);
    }
  }

  private void handleShiftTabOp() {
    if (selection.isAreaSelected()) {
      shiftTabSelection();
    } else {
      CodeLine codeLine = caretCodeLine();
      if (codeLine.elements.length > 0) {
        String indent = calculateTabIndent(codeLine, tabIndent.length());
        if (indent == null) return;
        document.makeDiffWithCaretReturn(
            caretLine, 0, true, indent, new Pos(caretLine, caretCharPos)
        );
        codeLine.delete(0, indent.length());
        setCaretPos(caretCharPos - indent.length());
      }
    }
    updateDocumentDiffTimeStamp();
  }

  private void shiftTabSelection() {
    Selection.SelPos left = selection.getLeftPos();
    Selection.SelPos right = selection.getRightPos();
    int initSize = right.line - left.line + 1;
    int[] lines = new int[initSize];
    String[] changes = new String[initSize];
    int prevCaretPos = caretCharPos;
    int prevCaretLine = caretLine;
    int size = 0;
    for (int l = left.line; l <= right.line; l++) {
      CodeLine codeLine = document.lines[l];
      if (codeLine.elements.length > 0) {
        String indent = calculateTabIndent(codeLine, tabIndent.length());
        if (indent == null) continue;
        lines[size] = l;
        changes[size++] = indent;
      }
    }
    lines = Arrays.copyOf(lines, size);
    changes = Arrays.copyOf(changes, size);
    for (int i = 0; i < size; i++) {
      String indent = changes[i];
      int l = lines[i];
      if (l == left.line) left.charInd = Math.max(0, left.charInd - indent.length());
      if (l == right.line) {
        right.charInd = Math.max(0, right.charInd - indent.length());
        setCaretPos(caretCharPos - indent.length());
      }
    }
    tabDiffHandler(lines, 0, true, changes, new Pos(prevCaretLine, prevCaretPos),
        (l, c) -> {
          CodeLine codeLine = document.lines[l];
          codeLine.delete(0, c.length());
        }
    );
  }

  private void tabDiffHandler(
      int[] lines,
      int fromValue,
      boolean isDelValue,
      String[] changes,
      Pos caretPosition,
      BiConsumer<Integer, String> editorAction

  ) {
    if (lines.length == 0) return;
    int[] from = new int[lines.length];
    boolean[] areDeletes = new boolean[lines.length];
    Arrays.fill(from, fromValue);
    Arrays.fill(areDeletes, isDelValue);
    document.makeComplexDiff(
        lines,
        from,
        areDeletes,
        changes,
        caretPosition,
        editorAction
    );
  }

  @Override
  void selectAll() {
    int line = document.lines.length - 1;
    int charInd = document.strLength(line);
    selection.startPos.set(0, 0);
    selection.endPos.set(line, charInd);
  }

  @Override
  void saveToNavStack() {
    NavigationContext curr = navStack.getCurrentCtx();
    if (curr != null && caretLine == curr.getLine() && caretCharPos == curr.getCharPos()) {
      return;
    }
    navStack.add(
        new NavigationContext(caretLine, caretCharPos, selection)
    );
  }

  @Override
  void setCaretPos(int charPos, boolean shift) {
    super.setCaretPos(charPos, shift);
    if (shift) selection.isSelectionStarted = true;
    selection.select(caretLine, caretCharPos);
    selection.isSelectionStarted = false;
  }

  void navigateBack() {
    saveToNavStack();
    NavigationContext prev = navStack.getPrevCtx();
    if (prev == null) return;
    setCaretLinePos(prev.getLine(), prev.getCharPos());
    selection.set(prev.getSelection());
  }

  void navigateForward() {
    NavigationContext curr = navStack.getNextCtx();
    if (curr == null) return;
    setCaretLinePos(curr.getLine(), curr.getCharPos());
    selection.set(curr.getSelection());
  }

  // parsing
  @Override
  void debugPrintDocumentIntervals() {
    document.printIntervals();
  }


  // js interop

  @Override
  public Model jsExportModel() {
    return this;
  }
}
