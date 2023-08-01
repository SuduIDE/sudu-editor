// todo: highlight current line
// todo: ctrl-left-right move by elements

package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.demo.ui.Focusable;
import org.sudu.experiments.demo.ui.UiContext;
import org.sudu.experiments.demo.worker.parser.*;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.*;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.worker.ArrayView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;

import static org.sudu.experiments.input.MouseListener.MOUSE_BUTTON_LEFT;

public class EditorComponent implements Focusable {
  final UiContext context;
  final WglGraphics g;
  final EditorUi ui;

  boolean forceMaxFPS = false;
  Runnable[] debugFlags = new Runnable[10];

  final Caret caret = new Caret();
  int caretLine, caretCharPos, caretPos;
  boolean hasFocus;

  int fontVirtualSize = EditorConst.DEFAULT_FONT_SIZE;
  String fontFamilyName = EditorConst.FONT;
  FontDesk font;
  FontDesk[] fonts = new FontDesk[4];
  int lineHeight;

  Model model = new Model();
  EditorRegistrations registrations = new EditorRegistrations();
  Selection selection = new Selection();

  EditorColorScheme colors;

  Canvas renderingCanvas;

  // render cache
  CodeLineRenderer[] lines = new CodeLineRenderer[0];
  int firstLineRendered, lastLineRendered;

  // layout
  int vLineXBase = 100;
  int vLineX;
  int vLineW = 1;
  int vLineLeftDelta;

  V2i vLineSize = new V2i(1, 0);

  ScrollBar vScroll = new ScrollBar();
  ScrollBar hScroll = new ScrollBar();
  int vScrollPos = 0;
  int hScrollPos = 0;

  int fullWidth = 0;

  boolean applyContrast, renderBlankLines = true;
  int scrollDown, scrollUp;
  boolean drawTails = true;
  int xOffset = 3;

  // line numbers
  LineNumbersComponent lineNumbers = new LineNumbersComponent();
  //int lineNumLeftMargin = 10;

  final V2i compPos = new V2i();
  final V2i compSize = new V2i();

  boolean fileStructureParsed, firstLinesParsed;
  String tabIndent = "  ";

  public boolean readonly = false;

  private CodeElement definition = null;
  private final List<CodeElement> usages = new ArrayList<>();
  private ExternalHighlights externalHighlights;

  Consumer<String> onError = System.err::println;

  public EditorComponent(UiContext context, EditorUi ui) {
    this.context = context;
    this.g = context.graphics;
    this.ui = ui;

    debugFlags[0] = this::toggleContrast;
    debugFlags[1] = this::toggleBlankLines;
    debugFlags[2] = this::toggleTails;
    debugFlags[3] = this::toggleXOffset;

    // d2d is very bold, contrast makes font heavier
    applyContrast = context.window.getHost() != Host.Direct2D;
  }

  void setPos(V2i pos, V2i size, float dpr) {
    compPos.set(pos);
    compSize.set(size);

    vLineX = Numbers.iRnd(vLineXBase * dpr);
    vLineLeftDelta = Numbers.iRnd(10 * dpr);

    int lineNumbersWidth = vLineX - vLineLeftDelta;
    lineNumbers.setPos(compPos, lineNumbersWidth, compSize.y, dpr);

    if (1<0) DebugHelper.dumpFontsSize(g);
    caret.setWidth(Numbers.iRnd(Caret.defaultWidth * dpr));

    // Should be called if dpr changed
    doChangeFont(fontFamilyName, fontVirtualSize);

    updateLineNumbersFont();
  }

  private void toggleBlankLines() {
    renderBlankLines = !renderBlankLines;
    Debug.consoleInfo("renderBlankLines = " + renderBlankLines);
  }

  public void onFocusGain() {
    hasFocus = true;
    startBlinking();
  }

  public void onFocusLost() {
    hasFocus = false;
  }

  private void startBlinking() {
    caret.startDelay(window().timeNow());
  }

  private void undoLastDiff() {
    if (selection.isAreaSelected()) setSelectionToCaret();
    var caretDiff = model.document.undoLastDiff();
    if (caretDiff == null) return;
    setCaretLinePos(caretDiff.x, caretDiff.y, false);
    updateDocumentDiffTimeStamp();
  }

  void setTheme(EditorColorScheme theme) {
    colors = theme;
    caret.setColor(theme.cursorColor);
  }

  void toggleXOffset() {
    xOffset = (xOffset + 3) % 6;
    for (var line: lines) {
      line.setXOffset(xOffset);
      if (line.line != null) line.line.contentDirty = true;
    }
  }

  void toggleTails() {
    drawTails ^= true;
    Debug.consoleInfo("drawTails = " + drawTails);
  }

  void toggleContrast() {
    applyContrast = !applyContrast;
    Debug.consoleInfo("applyContrast = " + applyContrast);
  }

  private void toggleTopTextRenderMode() {
    CodeLineRenderer.bw = false;
    CodeLineRenderer.useTop = !CodeLineRenderer.useTop;
    Debug.consoleInfo("CodeLineRenderer.useTop = " + CodeLineRenderer.useTop);
    invalidateFont();
  }

  public void increaseFont() {
    changeFont(font.name, fontVirtualSize + 1);
  }

  public void decreaseFont() {
    if (fontVirtualSize <= EditorConst.MIN_FONT_SIZE) return;
    changeFont(font.name, fontVirtualSize - 1);
  }

  void moveDown() {
    scrollDown = switchScroll(scrollDown);
    scrollUp = 0;
  }

  void moveUp() {
    scrollUp = switchScroll(scrollUp);
    scrollDown = 0;
  }

  private int switchScroll(int scrollValue) {
    return (scrollValue + 4) % 20;
  }

  void stopMove() {
    scrollUp = scrollDown = 0;
  }

  public int getFontVirtualSize() {
    return fontVirtualSize;
  }

  public String getFontFamily() {
    return fontFamilyName;
  }

  private void setFont(String name, int pixelSize) {
    setFonts(name, pixelSize);

    int fontLineHeight = font.lineHeight();
    lineHeight = Numbers.iRnd(fontLineHeight * EditorConst.LINE_HEIGHT);
    caret.setHeight(font.caretHeight(lineHeight));
    renderingCanvas = Disposable.assign(
        renderingCanvas, g.createCanvas(EditorConst.TEXTURE_WIDTH, lineHeight));

    // TODO: Remove min value when texture allocator appears
    ui.usagesMenu.setFont(g.fontDesk(
        name,
        Math.min(EditorConst.MAX_FONT_SIZE_USAGES_WINDOW, font.size),
        font.weight,
        font.style)
    );

    Debug.consoleInfo("Set editor font to: " + name + " " + pixelSize
        + ", ascent+descent = " + fontLineHeight
        + ", lineHeight = " + lineHeight
        + ", caretHeight = " + caret.height());

    if (CodeLineRenderer.useTop) {
      Debug.consoleInfo("topBase(font, lineHeight) = " + CodeLineRenderer.topBase(font, lineHeight));
    }
  }

  private void setFonts(String name, int size) {
    fonts[CodeElement.fontIndex(false, false)] =
        g.fontDesk(name, size, FontDesk.WEIGHT_REGULAR, FontDesk.STYLE_NORMAL);
    fonts[CodeElement.fontIndex(false, true)] =
        g.fontDesk(name, size, FontDesk.WEIGHT_REGULAR, FontDesk.STYLE_ITALIC);
    fonts[CodeElement.fontIndex(true, false)] =
        g.fontDesk(name, size, FontDesk.WEIGHT_BOLD, FontDesk.STYLE_NORMAL);
    fonts[CodeElement.fontIndex(true, true)] =
        g.fontDesk(name, size, FontDesk.WEIGHT_BOLD, FontDesk.STYLE_ITALIC);
    font = fonts[CodeElement.fontIndex(false, false)];
  }


  public void changeFont(String name) {
    changeFont(name, getFontVirtualSize());
  }

  public void changeFont(String name, int virtualSize) {
    if (context.dpr != 0) {
      doChangeFont(name, virtualSize);
      window().repaint();
    }
    fontVirtualSize = virtualSize;
    fontFamilyName = name;
  }

  private void doChangeFont(String name, int virtualSize) {
    int newPixelFontSize = Numbers.iRnd(virtualSize * context.dpr);
    int oldPixelFontSize = font == null ? 0 : font.iSize;
    if (newPixelFontSize != oldPixelFontSize || !Objects.equals(name, fontFamilyName)) {
      lineNumbers.dispose();
      invalidateFont();
      setFont(name, newPixelFontSize);
      afterFontChanged();
      updateLineNumbersFont();
    }
  }

  private void afterFontChanged() {
    caretPos = caretCodeLine().computePixelLocation(caretCharPos, g.mCanvas, fonts);
    adjustEditorScrollToCaret();
  }

  private void invalidateFont() {
//    Debug.consoleInfo("invalidateFont");

    for (CodeLineRenderer line : lines) {
      line.dispose();
    }
    model.document.invalidateFont();
  }

  public void dispose() {
    for (CodeLineRenderer line : lines) {
      line.dispose();
    }
    renderingCanvas = Disposable.assign(renderingCanvas, null);
    lineNumbers.dispose();
  }

  int editorVirtualHeight() {
    return (model.document.length() + EditorConst.BLANK_LINES) * lineHeight;
  }

  int maxVScrollPos() {
    return Math.max(editorVirtualHeight() - compSize.y, 0);
  }

  int maxHScrollPos() {
    return Math.max(fullWidth - editorWidth(), 0);
  }

  int editorWidth() {
    return compSize.x - vLineX;
  }

  int editorHeight() {
    return compSize.y;
  }

  private int iterativeVersion;

  public boolean update(double timestamp) {
    if (model.document.needReparse(timestamp) && iterativeVersion != model.document.currentVersion) {
      iterativeVersion = model.document.currentVersion;
      iterativeParsing();
    }

    int oldVScrollPos = vScrollPos;
    vScrollPos = clampScrollPos(vScrollPos + scrollDown  -  scrollUp,
        maxVScrollPos());
    boolean scrollMoving = oldVScrollPos != vScrollPos;

//    boolean replaceCurrentLine = debugFlags[2];
//    if (replaceCurrentLine) {
//      document[caretLine] = TestText.generateLine();
//    }

    // repaint only if caret blinking
    // or animation in progress
    return caret.update(timestamp) || scrollMoving
        // || replaceCurrentLine
        || forceMaxFPS;
  }

  // temp vars
  private final V4f tRegion = new V4f();
  private final V2i size = new V2i();


  public void paint() {

    int cacheLines = Numbers.iDivRoundUp(compSize.y, lineHeight) + EditorConst.MIN_CACHE_LINES;
    if (lines.length < cacheLines) {
      lines = CodeLineRenderer.reallocRenderLines(cacheLines, lines, firstLineRendered, lastLineRendered, model.document);
    }

    g.enableBlend(false);

    drawVerticalLine();
    vScrollPos = Math.min(vScrollPos, maxVScrollPos());
    hScrollPos = Math.min(hScrollPos, maxHScrollPos());

    int caretVerticalOffset = (lineHeight - caret.height()) / 2;
    int caretX = caretPos - caret.width() / 2 - hScrollPos;
    caret.setPosition(vLineX + caretX, caretVerticalOffset + caretLine * lineHeight - vScrollPos);

    int docLen = model.document.length();

    int firstLine = getFirstLine();
    int lastLine = getLastLine();

    firstLineRendered = firstLine;
    lastLineRendered = lastLine;

    for (int i = firstLine; i <= lastLine && i < docLen; i++) {
      CodeLine nextLine = model.document.line(i);
      CodeLineRenderer line = lineRenderer(i);
      line.updateTexture(nextLine, renderingCanvas, fonts, g, lineHeight, editorWidth(), hScrollPos);
      CodeLine lineContent = line.line;

      fullWidth = Math.max(fullWidth, nextLine.lineMeasure() + (int) (EditorConst.RIGHT_PADDING * context.dpr));
      int yPosition = lineHeight * i - vScrollPos;

      line.draw(
          compPos.y + yPosition, compPos.x +  vLineX, g, tRegion, size,
          applyContrast ? EditorConst.CONTRAST : 0,
          editorWidth(), lineHeight, hScrollPos,
          colors, getSelLineSegment(i, lineContent),
          definition, usages);
    }

    for (int i = firstLine; i <= lastLine && i < docLen && drawTails; i++) {
      CodeLineRenderer line = lineRenderer(i);
      int yPosition = lineHeight * i - vScrollPos;
      boolean isTailSelected = selection.isTailSelected(i);
      Color tailColor = isTailSelected? colors.selectionBgColor : colors.codeLineTailColor;
      line.drawTail(g, compPos.x + vLineX,compPos.y + yPosition, lineHeight,
          size, hScrollPos, editorWidth(), tailColor);
    }

    if (hasFocus && caretX >= -caret.width() / 2 && caret.needsPaint(compSize)) {
      caret.paint(g, compPos);
    }

    // draw bottom 5 invisible lines
    if (renderBlankLines) {
      int nextLine = Math.min(lastLine + 1, docLen);
      int yPosition = lineHeight * nextLine - vScrollPos;
      drawDocumentBottom(yPosition);
    }

    drawLineNumbers(firstLine, lastLine);

    layoutScrollbar();
    drawScrollBar();

//    g.checkError("paint complete");
    if (0>1) {
      String s = "fullMeasure:" + CodeLine.cacheMiss + ", cacheHits: " + CodeLine.cacheHits;
      Debug.consoleInfo(s);
      CodeLine.cacheMiss = CodeLine.cacheHits = 0;
    }
  }

  private V2i getSelLineSegment(int lineInd, CodeLine line) {
    V2i selLine = selection.getLine(lineInd);
    if (selLine != null) {
      if (selLine.y == -1) selLine.y = line.totalStrLength;
      selLine.x = line.computePixelLocation(selLine.x, g.mCanvas, fonts);
      selLine.y = line.computePixelLocation(selLine.y, g.mCanvas, fonts);
    }
    return selLine;
  }

  private void drawLineNumbers(int firstLine, int lastLine) {
    int editorBottom = compSize.y;
    int textHeight = Math.min(editorBottom, model.document.length() * lineHeight - vScrollPos);

    lineNumbers.draw(editorBottom, textHeight, vScrollPos, firstLine, lastLine, caretLine, g,
        colors.lineNumbersColors
    );
  }

  private int getFirstLine() {
    return Math.min(vScrollPos / lineHeight, model.document.length() - 1);
  }

  private int getLastLine() {
    return Math.min((vScrollPos + editorHeight() - 1) / lineHeight, model.document.length() - 1);
  }

  private void updateLineNumbersFont() {
    lineNumbers.setFont(fonts[0], lineHeight, g);
    lineNumbers.initTextures(g, getFirstLine(), editorHeight());
  }

  private CodeLineRenderer lineRenderer(int i) {
    return lines[i % lines.length];
  }

  boolean handleTab() {
    handleInsert(tabIndent);
    return true;
  }

  boolean handleEnter() {
    if (selection.isAreaSelected()) deleteSelectedArea();
    model.document.line(caretLine).invalidateCache();
    model.document.newLineOp(caretLine, caretCharPos);
    updateDocumentDiffTimeStamp();
    return setCaretLinePos(caretLine + 1, 0, false);
  }

  boolean handleDelete() {
    if (selection.isAreaSelected()) deleteSelectedArea();
    else model.document.deleteChar(caretLine, caretCharPos);
    adjustEditorScrollToCaret();
    updateDocumentDiffTimeStamp();
    return true;
  }

  boolean handleBackspace() {
    if (selection.isAreaSelected()) {
      deleteSelectedArea();
      return true;
    } else {
      if (caretCharPos == 0 && caretLine == 0) return true;

      int cLine, cPos;
      if (caretCharPos == 0) {
        cLine = caretLine - 1;
        cPos = model.document.strLength(cLine);
        model.document.concatLines(cLine);
      } else {
        cLine = caretLine;
        cPos = caretCharPos - 1;
        model.document.deleteChar(cLine, cPos);
      }
      updateDocumentDiffTimeStamp();
      return setCaretLinePos(cLine, cPos, false);
    }
  }

  boolean handleInsert(String s) {
    if (readonly) return false;
    if (selection.isAreaSelected()) deleteSelectedArea();
    String[] lines = s.replace("\r", "").split("\n", -1);

    model.document.insertLines(caretLine, caretCharPos, lines);

    int newCaretLine = caretLine + lines.length - 1;
    int newCaretPos;
    if (newCaretLine == caretLine) newCaretPos = caretCharPos + lines[0].length();
    else newCaretPos = lines[lines.length - 1].length();

    setCaretLinePos(newCaretLine, newCaretPos, false);
    setSelectionToCaret();
    updateDocumentDiffTimeStamp();
    return true;
  }

  private void deleteSelectedArea() {
    var leftPos = selection.getLeftPos();
    model.document.deleteSelected(selection);
    setCaretLinePos(leftPos.line, leftPos.charInd, false);
    setSelectionToCaret();
    updateDocumentDiffTimeStamp();
  }

  private void setSelectionToCaret() {
    selection.isSelectionStarted = false;
    selection.startPos.set(caretLine, caretCharPos);
    selection.endPos.set(caretLine, caretCharPos);
  }

  int scrollBarWidth() {
    return (int) font.WWidth;
  }

  private void drawDocumentBottom(int yPosition) {
    if (yPosition < compSize.y) {
      size.y = compSize.y - yPosition;
      size.x = editorWidth();

      g.drawRect(compPos.x + vLineX, compPos.y + yPosition, size, colors.editBgColor);
    }
  }

  private void layoutScrollbar() {
    vScroll.layoutVertical(vScrollPos,
        compPos.y,
        editorHeight(), editorVirtualHeight(),
        compPos.x + compSize.x, scrollBarWidth());
    hScroll.layoutHorizontal(hScrollPos,
        compPos.x + vLineX,
        editorWidth(), fullWidth,
        compPos.y + editorHeight(), scrollBarWidth());
  }

  private void drawScrollBar() {
    boolean vv = vScroll.visible();
    boolean hv = hScroll.visible();
    if (vv || hv) {
      g.enableBlend(true);
      if (vv) vScroll.drawBg(g);
      if (hv) hScroll.drawBg(g);
      if (vv) vScroll.drawButton(g);
      if (hv) hScroll.drawButton(g);
    }
  }

  private void drawVerticalLine() {
    vLineSize.y = compSize.y;
    vLineSize.x = vLineW;
    g.drawRect(compPos.x + vLineX - vLineLeftDelta, compPos.y, vLineSize, colors.editNumbersVLine);
    vLineSize.x = vLineLeftDelta - vLineW;
    g.drawRect(compPos.x + vLineX - vLineLeftDelta + vLineW, compPos.y, vLineSize, colors.editBgColor);
  }

  int clampScrollPos(int pos, int maxScrollPos) {
    return Math.min(Math.max(0, pos), maxScrollPos);
  }

  private long parsingTimeStart;

  private void onFileParsed(Object[] result) {
//    Debug.consoleInfo("onFileParsed");
    fileStructureParsed = true;
    firstLinesParsed = true;

    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();
    int type = ((ArrayView) result[2]).ints()[0];

    model.document = ParserUtils.makeDocument(ints, chars);

    changeModelLanguage(Languages.getLanguage(type));

    window().setCursor(Cursor.arrow);
    window().repaint();
    Debug.consoleInfo("Full file parsed in " + (System.currentTimeMillis() - parsingTimeStart) + "ms");
  }

  private void onFileStructureParsed(Object[] result) {
    int type = ((ArrayView) result[2]).ints()[0];
    if (type != FileParser.JAVA_FILE) {
      onFileParsed(result);
      return;
    }
    fileStructureParsed = true;

    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();

    model.document = ParserUtils.updateDocument(model.document, ints, chars, firstLinesParsed);
    changeModelLanguage(Languages.getLanguage(type));

    window().setCursor(Cursor.arrow);
    window().repaint();
    Debug.consoleInfo("File structure parsed in " + (System.currentTimeMillis() - parsingTimeStart) + "ms");

    parseViewport();
    parseFullFile();
  }

  private void changeModelLanguage(String languageFromParser) {
    String language = model.language();
    if (!Objects.equals(language, languageFromParser)) {
      Debug.consoleInfo("change model language: from = " + language + " to = " + languageFromParser);
      model.setLanguage(languageFromParser);
    }
  }

  private void onVpParsed(Object[] result) {
//    Debug.consoleInfo("onVpParsed");
    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();

    ParserUtils.updateDocument(model.document, ints, chars);

    window().setCursor(Cursor.arrow);
    window().repaint();
    Debug.consoleInfo("Viewport parsed in " + (System.currentTimeMillis() - parsingTimeStart) + "ms");
  }

  private Window window() { return context.window; }

  private void onFileLoad(byte[] content) {
    Debug.consoleInfo("readAsBytes complete, l = " + content.length);
    parsingTimeStart = System.currentTimeMillis();
    window().sendToWorker(this::onFileParsed, JavaParser.PARSE, content);
  }

  private void onFirstLinesParsed(Object[] result) {
    if (fileStructureParsed) return;
    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();
    this.model.document = ParserUtils.makeDocument(ints, chars);
    firstLinesParsed = true;
    Debug.consoleInfo("First lines parsed in " + (System.currentTimeMillis() - parsingTimeStart) + "ms");
  }

  void openFile(FileHandle f) {
    Debug.consoleInfo("opening file " + f.getName());
    window().setTitle(f.getName());
    setCaretLinePos(0, 0, false);
    // f.readAsBytes(this::onFileLoad, System.err::println);
    parsingTimeStart = System.currentTimeMillis();
    fileStructureParsed = false;
    firstLinesParsed = false;
    model.document = new Document();
    setCaretLinePos(0, 0, false);
    sendFileToWorkers(f);
  }

  private void sendFileToWorkers(FileHandle f) {
    String ext = f.getExtension();
    int bigFileSize = FileParser.isJavaExtension(ext) ?
        EditorConst.FILE_SIZE_10_KB : EditorConst.FILE_SIZE_5_KB;

    f.getSize(size -> {
      if (size <= bigFileSize) {
        window().sendToWorker(this::onFileParsed, FileParser.asyncParseFullFile, f);
      } else {
        window().sendToWorker(this::onFirstLinesParsed, FileParser.asyncParseFirstLines,
                f, new int[]{EditorConst.FIRST_LINES});
        window().sendToWorker(this::onFileStructureParsed, FileParser.asyncParseFile, f);
      }
    });
  }

  boolean arrowUpDown(int amount, boolean ctrl, boolean alt, boolean shiftPressed) {
    if (shiftSelection(shiftPressed)) return true;
    if (ctrl && alt) return true;
    if (ctrl) {  //  editorVScrollPos moves, caretLine does not change
      vScrollPos = clampScrollPos(vScrollPos + amount * lineHeight * 12 / 10, maxVScrollPos());
    } else if (alt) {
      // todo: smart move to prev/next method start
    } else {
      setCaretLine(caretLine + amount, shiftPressed);
      adjustEditorVScrollToCaret();
    }
    return true;
  }

  private boolean moveCaretLeftRight(int shift, boolean ctrl, boolean shiftPressed) {
    if (shiftSelection(shiftPressed)) return true;
    var caretCodeLine = caretCodeLine();
    int newPos = ctrl
            ? shift < 0
                ? caretCodeLine.prevPos(caretCharPos)
                : caretCodeLine.nextPos(caretCharPos)
            : caretCharPos + shift;

    if (newPos > caretCodeLine.totalStrLength) { // goto next line
      if (caretLine + 1 < model.document.length()) {
        setCaretLinePos(caretLine + 1, 0, shiftPressed);
      }
    } else if (newPos < 0) {  // goto prev line
      if (caretLine > 0) {
        int pos = model.document.line(caretLine - 1).totalStrLength;
        setCaretLinePos(caretLine - 1, pos, shiftPressed);
      }
    } else {
      setCaretPos(newPos, shiftPressed);
    }
    adjustEditorHScrollToCaret();
    return true;
  }

  private boolean shiftSelection(boolean shift) {
    if (selection.isAreaSelected() && !shift) {
      setSelectionToCaret();
      adjustEditorScrollToCaret();
      return true;
    }
    if (!shift || !selection.isAreaSelected()) setSelectionToCaret();
    return false;
  }

  private boolean setCaretLinePos(int line, int pos, boolean shift) {
    caretCharPos = pos;
    return setCaretLine(line, shift);
  }

  private boolean setCaretLine(int value, boolean shift) {
    caretLine = Numbers.clamp(0, value, model.document.length() - 1);
    return setCaretPos(caretCharPos, shift);
  }

  private boolean setCaretPos(int charPos, boolean shift) {
    caretCharPos = Numbers.clamp(0, charPos, caretCodeLine().totalStrLength);
    caretPos = context.dpr == 0 ? 0
        : caretCodeLine().computePixelLocation(caretCharPos, g.mCanvas, fonts);
    startBlinking();
    adjustEditorScrollToCaret();
    if (shift) selection.isSelectionStarted = true;
    selection.select(caretLine, caretCharPos);
    selection.isSelectionStarted = false;
    return true;
  }

  private void adjustEditorScrollToCaret() {
    adjustEditorVScrollToCaret();
    adjustEditorHScrollToCaret();
  }

  private void adjustEditorVScrollToCaret() {
    int editVisibleYMin = vScrollPos;
    int editVisibleYMax = vScrollPos + editorHeight();
    int caretVisibleY0 = caretLine * lineHeight;
    int caretVisibleY1 = caretLine * lineHeight + lineHeight;

    if (caretVisibleY0 < editVisibleYMin + lineHeight) {
      vScrollPos = clampScrollPos(caretVisibleY0 - lineHeight, maxVScrollPos());
    } else if (caretVisibleY1 > editVisibleYMax - lineHeight) {
      vScrollPos = clampScrollPos(caretVisibleY1 - editorHeight() + lineHeight, maxVScrollPos());
    }
  }

  private void adjustEditorHScrollToCaret() {
    int xOffset = Numbers.iRnd(context.dpr * EditorConst.CARET_X_OFFSET);

    int editVisibleXMin = hScrollPos;
    int editVisibleXMax = hScrollPos + editorWidth();
    int caretVisibleX0 = caretPos;
    int caretVisibleX1 = caretPos + xOffset;

    if (caretVisibleX0 < editVisibleXMin + xOffset) {
      hScrollPos = clampScrollPos(caretVisibleX0 - xOffset, maxHScrollPos());
    } else if (caretVisibleX1 > editVisibleXMax - xOffset) {
      hScrollPos = clampScrollPos(caretVisibleX1 - editorWidth() + xOffset, maxHScrollPos());
    }
  }

  private void computeUsages() {
    Pos caretPos = new Pos(caretLine, caretCharPos);
    Pos elementPos = model.document.getElementStart(caretLine, caretCharPos);
    computeUsages(caretPos, elementPos);
  }

  private void computeUsages(Pos caretPos, Pos elementPos) {
    clearUsages();

    Document document = model.document;
    Pos def = document.getDefinition(elementPos);

    if (def != null) definition = document.getCodeElement(def);

    List<Pos> usageList = document.getUsagesList(def != null ? def : elementPos);
    if (usageList != null) {
      for (var usage : usageList) {
        usages.add(document.getCodeElement(usage));
      }
    }

    useDocumentHighlightProvider(caretPos.line, caretPos.pos);
  }

  private void applyHighlights() {
    clearUsages();
    externalHighlights.buildUsages(model.document, usages);
  }

  public void findUsages(V2i position, ReferenceProvider.Provider provider) {
    Pos documentPosition = computeCharPos(position);

    if (provider != null) {
      provider.provideReferences(model, documentPosition.line, documentPosition.pos, true,
          (locs) -> showUsagesViaLocations(position, locs), onError);
    }
  }

  public void findUsages(V2i position, DefDeclProvider.Provider provider) {

    Pos pos = computeCharPos(position);
    if (provider != null) {
      provider.provide(model, pos.line, pos.pos, (locs) -> gotoDefinition(position, locs), onError);
      return;
    }

    model.document.moveToElementStart(pos);
    Pos def = model.document.usageToDef.get(pos);
    if (def != null) {
      gotoUsage(def);
      return;
    }

    List<Pos> usages = model.document.defToUsages.get(pos);
    if (usages == null || usages.isEmpty()) {
      ui.displayNoUsagesPopup(position, this);
    } else {
      ui.showUsagesWindow(position, usages, this);
    }
  }

  private void showUsagesViaLocations(V2i position, Location[] locs) {
    List<Pos> pos = new ArrayList<>();
    for (Location loc : locs) {
      pos.add(new Pos(loc.range.startLineNumber, loc.range.startColumn));
    }
    if (pos.isEmpty()) {
      ui.displayNoUsagesPopup(position, this);
    } else {
      ui.showUsagesWindow(position, locs, this);
    }
  }

  public final void gotoUsage(Pos defPos) {
    setCaretLinePos(defPos.line, defPos.pos, false);
    int nextPos = caretCodeLine().nextPos(caretCharPos);
    selection.endPos.set(caretLine, nextPos);
    selection.startPos.set(caretLine, caretCharPos);
  }

  void useDocumentHighlightProvider(int line, int column) {
    var p = registrations.findDocumentHighlightProvider(model.language(), model.uriScheme());
    if (p != null) {
      Model saveModel = model;
      p.provide(model, line, column,
          highlights -> setHighlights(saveModel, line, column, highlights),
          onError);
    }
  }

  void setHighlights(Model saveModel, int line, int column, DocumentHighlight[] highlights) {
    if (model != saveModel || caretLine != line || caretCharPos != column) return; // late reply
    externalHighlights = new ExternalHighlights(line, column, highlights);
    applyHighlights();
  }

  Pos computeCharPos(V2i eventPosition) {
    int localX = eventPosition.x - compPos.x;
    int localY = eventPosition.y - compPos.y;

    int line = Numbers.clamp(0, (localY + vScrollPos) / lineHeight, model.document.length() - 1);
    int documentXPosition = Math.max(0, localX - vLineX + hScrollPos);
    int charPos = model.document.line(line).computeCharPos(documentXPosition, g.mCanvas, fonts);
    return new Pos(line, charPos);
  }

  void onClickText(MouseEvent event) {
    V2i eventPosition = event.position;
    Pos pos = computeCharPos(eventPosition);
    Pos elementPos = model.document.getElementStart(pos.line, pos.pos);

    if (event.ctrl) {
      var provider = registrations.findDefinitionProvider(model.language(), model.uriScheme());
      if (provider != null) {
        provider.provide(model, pos.line, pos.pos,
            (locs) -> gotoDefinition(eventPosition, locs), onError);
      } else {
        // Default def provider
        if (gotoByLocalProvider(eventPosition, elementPos)) return;
      }
    }

    moveCaret(pos);
    computeUsages(pos, elementPos);

    if (!event.shift && !selection.isSelectionStarted) {
      selection.startPos.set(caretLine, caretCharPos);
    }
    selection.isSelectionStarted = true;
    selection.select(caretLine, caretCharPos);
    dragLock = this::dragText;
  }

  private void dragText(MouseEvent event) {
    Pos pos = computeCharPos(event.position);
    moveCaret(pos);
    selection.select(caretLine, caretCharPos);
    adjustEditorScrollToCaret();
  }

  private void moveCaret(Pos pos) {
    caretLine = pos.line;
    caretCharPos = pos.pos;
    caretPos = model.document.line(pos.line).computePixelLocation(caretCharPos, g.mCanvas, fonts);
    startBlinking();
  }

  private boolean gotoByLocalProvider(V2i position, Pos elementStart) {
    var defPos = model.document.usageToDef.get(elementStart);
    if (defPos != null) {
      gotoUsage(defPos);
      return true;
    } else {
      var usagesList = model.document.defToUsages.get(elementStart);
      if (usagesList != null && !usagesList.isEmpty()) {
        if (usagesList.size() == 1) {
          gotoUsage(usagesList.get(0));
          return true;
        } else {
          ui.showUsagesWindow(position, usagesList, this);
          return true;
        }
      }
    }
    return false;
  }

  private void gotoDefinition(V2i position, Location[] locs) {
    switch (locs.length) {
      case 0 -> ui.displayNoUsagesPopup(position,this);
      case 1 -> gotoDefinition(locs[0]);
      default -> ui.showUsagesWindow(position, locs, this);
    }
  }

  public void gotoDefinition(Location loc) {
    if (!Objects.equals(loc.uri, model.uri)) {
      EditorOpener editorOpener = registrations.findOpener();
      if (editorOpener != null) {
        window().runLater(() -> {
          editorOpener.open(loc.uri, loc.range, null);
        });
      }
    } else {
      updateSelectionViaRange(loc.range);
    }
  }

  private void updateSelectionViaRange(Range range) {
    setCaretLinePos(range.startLineNumber, range.startColumn, false);
    selection.startPos.set(range.startLineNumber, range.startColumn);
    selection.endPos.set(range.endLineNumber, range.endColumn);
  }

  void onDoubleClickText(V2i eventPosition) {
    Pos pos = computeCharPos(eventPosition);
    CodeLine line = codeLine(pos.line);
    int wordStart = line.getElementStart(caretCharPos);
    int wordEnd = line.nextPos(caretCharPos);
    CodeElement elem = line.getCodeElement(wordStart);

    // Select line without tale if double-clicking the end of the line
    if (wordEnd - 1 == line.totalStrLength) {
      selection.startPos.set(caretLine, line.getBlankStartLength());
      selection.endPos.set(caretLine, line.totalStrLength);
      return;
    }

    // Select adjacent CodeElements if one ' ', or the whole line
    if (elem != null && elem.s.isBlank()) {
      if (wordStart == caretCharPos) {
        wordStart = line.getElementStart(wordStart - 1);
        wordEnd = line.nextPos(wordStart);
      } else if (wordEnd == caretCharPos) {
        wordStart = line.getElementStart(wordEnd + 1);
        wordEnd = line.nextPos(wordStart);
      } else {
        selection.selectLine(caretLine);
        return;
      }
    }

    // Select CodeElement that holds the caret inside
    selection.startPos.set(caretLine, wordStart);
    selection.isSelectionStarted = true;
    setCaretLinePos(caretLine, wordEnd, false);
    selection.isSelectionStarted = false;
  }

  void onTripleClickText(V2i eventPosition) {
    selection.selectLine(caretLine);
  }

  CodeLine caretCodeLine() {
    return model.document.line(caretLine);
  }

  CodeLine codeLine(int n) {
    return model.document.line(n);
  }

  // InputListener methods

  Consumer<MouseEvent> dragLock;

  Consumer<IntUnaryOperator> vScrollHandler =
      move -> vScrollPos = move.applyAsInt(maxVScrollPos());

  Consumer<IntUnaryOperator> hScrollHandler =
      move -> hScrollPos = move.applyAsInt(maxHScrollPos());


  public boolean onScroll(float dX, float dY) {
    // chrome sends 150px, firefox send "6 lines"
    int changeY = Numbers.iRnd(lineHeight * 4 * dY / 150);
    int changeX = Numbers.iRnd(dX);
    vScrollPos = clampScrollPos(vScrollPos + changeY, maxVScrollPos());
    hScrollPos = clampScrollPos(hScrollPos + changeX, maxHScrollPos());
    return true;
  }

  public boolean onMouseUp(MouseEvent event, int button) {
    selection.isSelectionStarted = false;
    if (dragLock != null) dragLock = null;
    return true;
  }

  public boolean onMouseDown(MouseEvent event, int button) {
    if (button == MOUSE_BUTTON_LEFT) {
      dragLock = vScroll.onMouseClick(event.position, vScrollHandler, true);
      if (dragLock != null) return true;

      dragLock = hScroll.onMouseClick(event.position, hScrollHandler, false);
      if (dragLock != null) return true;

      onClickText(event);
    }
    return true;
  }

  public boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    if (button == MOUSE_BUTTON_LEFT && clickCount == 3) {
      onTripleClickText(event.position);
      return true;
    }
    if (button == MOUSE_BUTTON_LEFT && clickCount == 2) {
      onDoubleClickText(event.position);
      return true;
    }
    return true;
  }

  public boolean onMouseMove(MouseEvent event) {
    if (dragLock != null) {
      dragLock.accept(event);
      return true;
    }

    SetCursor setCursor = context.windowCursor;
    if (vScroll.onMouseMove(event.position, setCursor)) return true;
    if (hScroll.onMouseMove(event.position, setCursor)) return true;
    if (lineNumbers.onMouseMove(event.position, setCursor)) return true;

    if (isInsideText(event.position)) {
      if (event.ctrl) {
        Pos pos = computeCharPos(event.position);
        model.document.moveToElementStart(pos);
        if (model.document.hasDefOrUsagesForElementPos(pos)) {
          return setCursor.set(Cursor.pointer);
        }
      }
      return setCursor.set(Cursor.text);
    }
    return setCursor.setDefault();
  }

  public boolean onKeyPress(KeyEvent event) {
//    Debug.consoleInfo("EditorComponent::onKey: "+ event.desc());

    if (event.ctrl && event.keyCode == KeyCode.A) return selectAll();

    if (event.ctrl && event.keyCode == KeyCode.P) {
      parseFullFile();
      return true;
    }

    if (!readonly && event.ctrl && event.keyCode == KeyCode.Z) {
      undoLastDiff();
      return true;
    }

    if (handleDoubleKey(event)) return true;
    if (handleDebug(event)) return true;
    if (handleSpecialKeys(event)) return true;
    if (handleNavigation(event)) return true;
    if (handleEditingKeys(event)) return true;

    if (event.ctrl && event.keyCode == KeyCode.W) {
      Debug.consoleInfo("Ctrl-W is not possible ;)");
      return true;
    }

    if (event.keyCode == KeyCode.ESC) return false;
    return event.key.length() > 0 && handleInsert(event.key);
  }

  void debugPrintDocumentIntervals() {
    model.document.printIntervals();
  }

  public void parseViewport() {
    if (Languages.JAVA.equals(model.language()))
      window().sendToWorker(this::onVpParsed, JavaParser.PARSE_BYTES_JAVA_VIEWPORT,
          model.document.getChars(), getViewport(), model.document.getIntervals() );
  }

  private int[] getViewport() {
    int firstLine = getFirstLine();
    int lastLine = getLastLine();

    firstLine = Math.max(0, firstLine - EditorConst.VIEWPORT_OFFSET);
    lastLine = Math.min(model.document.length() - 1, lastLine + EditorConst.VIEWPORT_OFFSET);

    return new int[]{model.document.getLineStartInd(firstLine), model.document.getVpEnd(lastLine), firstLine};
  }

  public void parseFullFile() {
    String parseJob = parseJobName(model.language());
    if (parseJob != null) {
      parsingTimeStart = System.currentTimeMillis();
      window().sendToWorker(this::onFileParsed, parseJob, model.document.getChars());
    }
  }

  public void onFileIterativeParsed(Object[] result) {
    if (model.document.currentVersion != iterativeVersion) return;
    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();
    ParserUtils.updateDocument(model.document, ints, chars);
    model.document.onReparse();
  }

  public void iterativeParsing() {
    var node = model.document.tree.getReparseNode();
    if (node == null) return;
    if (Languages.TEXT.equals(model.language())) {
      model.document.onReparse();
    }
    int[] interval = new int[]{node.getStart(), node.getStop(), node.getType()};
    char[] chars = model.document.getChars();
    int[] type = new int[] {Languages.getType(model.language())};

    window().sendToWorker(this::onFileIterativeParsed, FileParser.asyncIterativeParsing, chars, type, interval);
  }

  public boolean onCopy(Consumer<String> setText, boolean isCut) {
    if (isCut && readonly) return false;
    var left = selection.getLeftPos();
    int line = left.line;
    String result;

    if (!selection.isAreaSelected()) {
      result = model.document.copyLine(line);
      int newLine = Math.min(model.document.length() - 1, line);

      selection.endPos.set(newLine, 0);
      if (line < model.document.length() - 1)
        selection.startPos.set(newLine + 1, 0);
      else
        selection.endPos.set(newLine, model.document.strLength(newLine));

      if (isCut) deleteSelectedArea();
      else setCaretLinePos(line, 0, false);
    } else {
      result = model.document.copy(selection, isCut);
      if (isCut) {
        setCaretLinePos(left.line, left.charInd, false);
        setSelectionToCaret();
        updateDocumentDiffTimeStamp();
      }
    }

    setText.accept(result);
    return true;
  }

  private boolean isInsideText(V2i position) {
    return Rect.isInside(position,
        compPos.x + vLineX, compPos.y,
        editorWidth(), editorHeight());
  }

  private boolean handleSpecialKeys(KeyEvent event) {
    if (KeyCode.F1 <= event.keyCode && event.keyCode <= KeyCode.F12) return true;
    return switch (event.keyCode) {
      case KeyCode.INSERT, KeyCode.Pause, KeyCode.META,
          KeyCode.CapsLock, KeyCode.NumLock, KeyCode.ScrollLock,
          KeyCode.ALT, KeyCode.SHIFT, KeyCode.CTRL -> true;
      default -> false;
    };
  }

  private boolean handleEditingKeys(KeyEvent event) {
    if (readonly) return false;
    return switch (event.keyCode) {
      case KeyCode.TAB -> handleTab();
      case KeyCode.ENTER -> handleEnter();
      case KeyCode.DELETE -> handleDelete();
      case KeyCode.BACKSPACE -> handleBackspace();
      default -> false;
    };
  }

  private boolean handleNavigation(KeyEvent event) {
    boolean result = switch (event.keyCode) {
      case KeyCode.ARROW_UP -> arrowUpDown(-1, event.ctrl, event.alt, event.shift);
      case KeyCode.ARROW_DOWN -> arrowUpDown(1, event.ctrl, event.alt, event.shift);
      case KeyCode.PAGE_UP -> pgUp(event);
      case KeyCode.PAGE_DOWN -> pgDown(event);
      case KeyCode.ARROW_LEFT -> moveCaretLeftRight(-1, event.ctrl, event.shift);
      case KeyCode.ARROW_RIGHT -> moveCaretLeftRight(1, event.ctrl, event.shift);
      case KeyCode.HOME -> shiftSelection(event.shift) || setCaretPos(0, event.shift);
      case KeyCode.END -> shiftSelection(event.shift) ||
          setCaretPos(caretCodeLine().totalStrLength, event.shift);
      default -> false;
    };
    if (result && event.shift) selection.endPos.set(caretLine, caretCharPos);
    if (result) computeUsages();
    return result;
  }

  boolean pgDown(KeyEvent event) {
    return event.ctrl
        ? setCaretLine((vScrollPos + editorHeight()) / lineHeight - 1, event.shift)
        : arrowUpDown(Numbers.iDivRound(editorHeight(), lineHeight) - 2,
        false, event.alt, event.shift);
  }

  boolean pgUp(KeyEvent event) {
    return event.ctrl
        ? setCaretLine(Numbers.iDivRoundUp(vScrollPos, lineHeight), event.shift)
        : arrowUpDown(2 - Numbers.iDivRound(editorHeight(), lineHeight),
        false, event.alt, event.shift);
  }

  private boolean handleDebug(KeyEvent event) {
    if (event.alt || event.ctrl) {
      if (event.keyCode >= KeyCode._0 && event.keyCode <= KeyCode._9) {
        int index = event.keyCode - KeyCode._0;
        Runnable r = debugFlags[index];
        if (r != null) r.run();
        return true;
      }
    }
    return false;
  }

  private boolean handleDoubleKey(KeyEvent event) {
    if (event.ctrl || event.alt) return false;
    if (event.key.equals("{")) {
      handleInsert("{}");
      setCaretPos(caretCharPos - 1, false);
      return true;
    }
    if (event.key.equals("(")) {
      handleInsert("()");
      setCaretPos(caretCharPos - 1, false);
      return true;
    }
    if (event.key.equals("[")) {
      handleInsert("[]");
      setCaretPos(caretCharPos - 1, false);
      return true;
    }
    if (event.key.equals("<")) {
      handleInsert("<>");
      setCaretPos(caretCharPos - 1, false);
      return true;
    }
    if (event.key.equals("\"")) {
      handleInsert("\"\"");
      setCaretPos(caretCharPos - 1, false);
      return true;
    }
    if (event.key.equals("'")) {
      handleInsert("''");
      setCaretPos(caretCharPos - 1, false);
      return true;
    }
    return false;
  }

  public boolean selectAll() {
    int line = model.document.length() - 1;
    int charInd = model.document.strLength(line);
    selection.startPos.set(0, 0);
    selection.endPos.set(model.document.length() - 1, charInd);
    return true;
  }

  private void updateDocumentDiffTimeStamp() {
    model.document.setLastDiffTimestamp(window().timeNow());
  }

  public void setPosition(int column, int lineNumber) {
    setCaretLinePos(lineNumber, column, false);
  }

  public int caretLine() { return caretLine; }
  public int caretCharPos() { return caretCharPos; }

  public void setSelection(
      int endColumn,
      int endLineNumber,
      int startColumn,
      int startLineNumber
  ) {
    selection.getLeftPos().set(startLineNumber,startColumn);
    selection.getRightPos().set(endLineNumber, endColumn);
  }

  public EditorRegistrations registrations() { return registrations; }

  public void setModel(Model model) {
    externalHighlights = null;
    clearUsages();
    Model oldModel = this.model;
    this.model = model;
    onContentChange();
    registrations.fireModelChange(oldModel, model);
  }

  private void clearUsages() {
    definition = null;
    usages.clear();
  }

  public Model model() { return model; }

  public void setText(String[] newLines) {
    model.document.setContent(newLines);
    onContentChange();
  }

  private void onContentChange() {
    parsingTimeStart = System.currentTimeMillis();
    String jobName = parseJobName(model.language());
    if (jobName != null) {
      window().sendToWorker(this::onFileParsed, jobName, getChars());
    }
  }

  public void setLanguage(String language) {
    model.setLanguage(language);
  }

  public void revealLineInCenter(int lineNumber) {
    if (lineNumber <= 0) return;
    int computed = lineHeight * (lineNumber - (editorHeight() / (lineHeight * 2)) - 1);
    vScrollPos = clampScrollPos(computed, maxVScrollPos());
  }

  public void revealLine(int lineNumber) {
    if (lineNumber <= 0) return;
    int lineVPos = (lineNumber - 1) * lineHeight;
    if (lineVPos >= vScrollPos) {
      if (lineVPos - vScrollPos < editorHeight()) return;
      scrollDownToLine(lineNumber);
    } else {
      scrollUpToLine(lineNumber);
    }
  }

  private void scrollDownToLine(int lineNumber) {
    if (lineNumber > model.document.length()) {
      vScrollPos = maxVScrollPos();
    } else {
      vScrollPos = clampScrollPos((lineNumber + 1) * lineHeight - editorHeight(), maxVScrollPos());
    }
  }

  private void scrollUpToLine(int lineNumber) {
    vScrollPos = clampScrollPos((lineNumber - 2) * lineHeight, maxVScrollPos());
  }

  static String parseJobName(String language) {
    return language != null ? switch (language) {
      case Languages.JAVA -> JavaParser.PARSE;
      case Languages.CPP -> CppParser.PARSE;
      case Languages.JS -> JavaScriptParser.PARSE;
      default -> null;
    } : null;
  }

  public char[] getChars() {
    return model.document.getChars();
  }
}
