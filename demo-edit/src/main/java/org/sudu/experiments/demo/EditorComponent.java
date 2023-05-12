// todo: highlight current line
// todo: ctrl-left-right move by elements

package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.demo.worker.*;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.*;
import org.sudu.experiments.parser.java.parser.JavaIntervalParser;
import org.sudu.experiments.worker.ArrayView;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;

import static org.sudu.experiments.input.InputListener.MOUSE_BUTTON_LEFT;

public class EditorComponent implements EditApi, Disposable {

  boolean forceMaxFPS = false;
  int footerHeight;
  Runnable[] debugFlags = new Runnable[10];

  final SceneApi api;
  final WglGraphics g;

  final Caret caret = new Caret();
  int caretLine, caretCharPos, caretPos;
  boolean hasFocus;

  int fontVirtualSize = EditorConst.DEFAULT_FONT_SIZE;
  String fontFamilyName = EditorConst.FONT;
  FontDesk font;
  FontDesk[] fonts = new FontDesk[4];
  int lineHeight;

  Document document;
  Selection selection = new Selection();

  EditorColorScheme colors = new EditorColorScheme();

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
  DemoRect footerRc = new DemoRect();

  ScrollBar vScroll = new ScrollBar();
  ScrollBar hScroll = new ScrollBar();
  int vScrollPos = 0;
  int hScrollPos = 0;

  int fullWidth = 0;
  double devicePR;

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
  int fileType = FileParser.JAVA_FILE;
  String tabIndent = "  ";

  public EditorComponent(SceneApi api) {
    this(api, new Document());
  }

  public EditorComponent(
      SceneApi api,
      Document document
  ) {
    this.api = api;
    this.document = document;
    this.g = api.graphics;

    if (api.window.hasFocus()) onFocusGain();

    debugFlags[0] = this::toggleContrast;
    debugFlags[1] = this::toggleBlankLines;
    debugFlags[2] = this::toggleTails;
    debugFlags[3] = this::toggleXOffset;

    // d2d is very bold, contrast makes font heavier
    applyContrast = api.window.getHost() != Host.Direct2D;
  }

  void setPos(V2i pos, V2i size, double dpr) {
    compPos.set(pos);
    compSize.set(size);
    devicePR = dpr;

    vLineX = Numbers.iRnd(vLineXBase * devicePR);
    vLineLeftDelta = Numbers.iRnd(10 * devicePR);

    int lineNumbersWidth = vLineX - vLineLeftDelta;
    lineNumbers.setPos(compPos, lineNumbersWidth, editorHeight(), devicePR);

    //api.input.addListener(new MyInputListener());
    //clientRect = api.window.getClientRect();
    if (1<0) DebugHelper.dumpFontsSize(g);
    caret.setWidth(Numbers.iRnd(Caret.defaultWidth * devicePR));

    // Should be called if dpr changed
    setFont(fontFamilyName, fontVirtualSize);

    updateLineNumbersFont();

    layout();
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
    caret.startDelay(api.window.timeNow());
  }

  private void undoLastDiff() {
    if (selection.isAreaSelected()) setSelectionToCaret();
    var caretDiff = document.undoLastDiff();
    if (caretDiff == null) return;
    setCaretLinePos(caretDiff.x, caretDiff.y, false);
    updateDocumentDiffTimeStamp();
  }

  private void toggleXOffset() {
    xOffset = (xOffset + 3) % 6;
    for (var line: lines) {
      line.setXOffset(xOffset);
      if (line.line != null) line.line.contentDirty = true;
    }
  }

  private void toggleTails() {
    drawTails ^= true;
    Debug.consoleInfo("drawTails = " + drawTails);
  }

  private void toggleContrast() {
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

  private void moveDown() {
    scrollDown = switchScroll(scrollDown);
    scrollUp = 0;
  }

  private void moveUp() {
    scrollUp = switchScroll(scrollUp);
    scrollDown = 0;
  }

  private int switchScroll(int scrollValue) {
    return (scrollValue + 4) % 20;
  }

  private void stopMove() {
    scrollUp = scrollDown = 0;
  }

  public int getFontVirtualSize() {
    return fontVirtualSize;
  }

  public String getFontFamily() {
    return fontFamilyName;
  }

  private void setFont(String name, int size) {
    setFonts(name, size);

    int fontLineHeight = font.lineHeight();
    lineHeight = Numbers.iRnd(fontLineHeight * EditorConst.LINE_HEIGHT);
    footerHeight = lineHeight;
    caret.setHeight(font.caretHeight(lineHeight));
    renderingCanvas = Disposable.assign(
        renderingCanvas, g.createCanvas(EditorConst.TEXTURE_WIDTH, lineHeight));

    Debug.consoleInfo("Set editor font to: " + name + " " + size
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

  public void changeFont(String name, int virtualSize) {
    if (devicePR != 0) {
      int newPixelFontSize = Numbers.iRnd(virtualSize * devicePR);
      int oldPixelFontSize = font == null ? 0 : font.iSize;
      if (newPixelFontSize != oldPixelFontSize || !Objects.equals(name, fontFamilyName)) {
        lineNumbers.dispose();
        invalidateFont();
        setFont(name, newPixelFontSize);
        afterFontChanged();
        updateLineNumbersFont();
      }
    }
    fontVirtualSize = virtualSize;
    fontFamilyName = name;
    api.window.repaint();
  }

  private void afterFontChanged() {
    // footer depends on font size and needs re-layout
    layoutFooter();
    caretPos = caretCodeLine().computePixelLocation(caretCharPos, g.mCanvas, fonts);
    adjustEditorScrollToCaret();
  }

  private void invalidateFont() {
    Debug.consoleInfo("invalidateFont");

    for (CodeLineRenderer line : lines) {
      line.dispose();
    }
    document.invalidateFont();
  }

  public void dispose() {
    for (CodeLineRenderer line : lines) {
      line.dispose();
    }
    renderingCanvas = Disposable.assign(renderingCanvas, null);
    lineNumbers.dispose();
  }

  int editorFullHeight() {
    return (document.length() + EditorConst.BLANK_LINES) * lineHeight;
  }

  int maxVScrollPos() {
    return Math.max(editorFullHeight() - editorHeight(), 0);
  }

  int maxHScrollPos() {
    return Math.max(fullWidth - editorWidth(), 0);
  }

  int editorWidth() {
    int editorRight = compSize.x;
    return editorRight - vLineX;
  }

  int editorHeight() {
    return compSize.y - footerHeight;
  }

  private void layout() {
    layoutFooter();
  }

  private void layoutFooter() {
    footerRc.set(0, editorHeight(), compSize.x, lineHeight);
    footerRc.color.set(colors.editFooterFill);
    vLineSize.y = editorHeight();
  }

  public boolean update(double timestamp) {
    if (document.needReparse(timestamp)) iterativeParsing();

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

    int editorBottom = editorHeight();
    int editorRight = compSize.x;

    int cacheLines = Numbers.iDivRoundUp(editorHeight(), lineHeight) + EditorConst.MIN_CACHE_LINES;
    if (lines.length < cacheLines) {
      lines = CodeLineRenderer.reallocRenderLines(cacheLines, lines, firstLineRendered, lastLineRendered, document);
    }

    g.enableBlend(false);

    drawVerticalLine();
    vScrollPos = Math.min(vScrollPos, maxVScrollPos());
    hScrollPos = Math.min(hScrollPos, maxHScrollPos());

    int caretVerticalOffset = (lineHeight - caret.height()) / 2;
    int caretX = caretPos - caret.width() / 2 - hScrollPos;
    caret.setPosition(vLineX + caretX, caretVerticalOffset + caretLine * lineHeight - vScrollPos);

    int docLen = document.length();

    int firstLine = getFirstLine();
    int lastLine = getLastLine();

    this.firstLineRendered = firstLine;
    this.lastLineRendered = lastLine;

    for (int i = firstLine; i <= lastLine && i < docLen; i++) {
      CodeLine nextLine = document.line(i);
      CodeLineRenderer line = lineRenderer(i);
      line.updateTexture(nextLine, renderingCanvas, fonts, g, lineHeight, editorWidth(), hScrollPos);
      CodeLine lineContent = line.line;

      fullWidth = Math.max(fullWidth, nextLine.lineMeasure() + (int) (EditorConst.RIGHT_PADDING * devicePR));
      int yPosition = lineHeight * i - vScrollPos;

      line.draw(
          compPos.y + yPosition, compPos.x +  vLineX, g, tRegion, size,
          applyContrast ? EditorConst.CONTRAST : 0,
          editorWidth(), lineHeight, hScrollPos,
          colors, getSelLineSegment(i, lineContent));
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
      drawDocumentBottom(editorBottom, editorRight, yPosition);
    }

    drawLineNumbers(editorBottom, firstLine, lastLine);

    drawFooter();

    g.enableBlend(true);
    vScroll.layoutVertical(vScrollPos, editorRight, editorHeight(), editorFullHeight(), vScrollBarWidth());
    hScroll.layoutHorizontal(hScrollPos, editorBottom, editorRight - vLineX, fullWidth, vLineX, vScrollBarWidth());
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

  private void drawLineNumbers(int editorBottom, int firstLine, int lastLine) {
    int textHeight = Math.min(editorBottom, document.length() * lineHeight - vScrollPos);

    lineNumbers.draw(editorBottom, textHeight, vScrollPos, firstLine, lastLine, caretLine, g,
        colors.lineNumbersColors
    );
  }

  private int getFirstLine() {
    return Math.min(vScrollPos / lineHeight, document.length() - 1);
  }

  private int getLastLine() {
    return Math.min((vScrollPos + editorHeight() - 1) / lineHeight, document.length() - 1);
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
    document.line(caretLine).invalidateCache();
    document.newLineOp(caretLine, caretCharPos);
    updateDocumentDiffTimeStamp();
    return setCaretLinePos(caretLine + 1, 0, false);
  }

  boolean handleDelete() {
    if (selection.isAreaSelected()) deleteSelectedArea();
    else document.deleteChar(caretLine, caretCharPos);
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
        cPos = document.strLength(cLine);
        document.concatLines(cLine);
      } else {
        cLine = caretLine;
        cPos = caretCharPos - 1;
        document.deleteChar(cLine, cPos);
      }
      updateDocumentDiffTimeStamp();
      return setCaretLinePos(cLine, cPos, false);
    }
  }

  boolean handleInsert(String s) {
    if (selection.isAreaSelected()) deleteSelectedArea();
    String[] lines = s.replace("\r", "").split("\n", -1);

    document.insertLines(caretLine, caretCharPos, lines);

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
    document.deleteSelected(selection);
    setCaretLinePos(leftPos.line, leftPos.charInd, false);
    setSelectionToCaret();
    updateDocumentDiffTimeStamp();
  }

  private void setSelectionToCaret() {
    selection.isSelectionStarted = false;
    selection.startPos.set(caretLine, caretCharPos);
    selection.endPos.set(caretLine, caretCharPos);
  }

  int vScrollBarWidth() {
    return (int) font.WWidth;
  }

  private void drawDocumentBottom(int editorBottom, int editorRight, int yPosition) {
    if (yPosition < editorBottom) {
      size.y = editorBottom - yPosition;
      size.x = editorRight - vLineX;

      g.drawRect(compPos.x + vLineX, compPos.y + yPosition, size, colors.editBgColor);
    }
  }

  private void drawScrollBar() {
    // draw v-scroll bar
    if (vScroll.visible()) {
      g.enableBlend(true);
      vScroll.draw(g, compPos);
    }
    if (hScroll.visible()) {
      g.enableBlend(true);
      hScroll.draw(g, compPos);
    }
  }

  private void drawFooter() {
    g.enableBlend(false);
    Color.Cvt.fromRGBA(0, 0, 0, 128, footerRc.color);
    footerRc.draw(g, compPos.x, compPos.y);
  }

  private void drawVerticalLine() {
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
    Debug.consoleInfo("onFileParsed");
    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();

    this.document = BaseParser.makeDocument(ints, chars);

    int newCaretLine = Numbers.clamp(0, caretLine, document.length());
    int newCaretCharInd = Numbers.clamp(0, caretCharPos, document.strLength(newCaretLine));

    setCaretLinePos(newCaretLine, newCaretCharInd, false);
    api.window.setCursor(Cursor.arrow);
    api.window.repaint();
    Debug.consoleInfo("Full file parsed in " + (System.currentTimeMillis() - parsingTimeStart) + "ms");
  }

  private void onFileStructureParsed(Object[] result) {
    fileStructureParsed = true;

    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();
    int type = ((ArrayView) result[2]).ints()[0];
    this.fileType = type;

    this.document = JavaLexerFirstLines.makeDocument(document, ints, chars, firstLinesParsed);

    int newCaretLine = Numbers.clamp(0, caretLine, document.length());
    int newCaretCharInd = Numbers.clamp(0, caretCharPos, document.strLength(newCaretLine));

    setCaretLinePos(newCaretLine, newCaretCharInd, false);
    api.window.setCursor(Cursor.arrow);
    api.window.repaint();
    Debug.consoleInfo("File structure parsed in " + (System.currentTimeMillis() - parsingTimeStart) + "ms");

    parseViewport(type);
  }

  private void onVpParsed(Object[] result) {
    Debug.consoleInfo("onVpParsed");
    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();

    BaseParser.updateDocument(document, ints, chars);

    int newCaretLine = Numbers.clamp(0, caretLine, document.length());
    int newCaretCharInd = Numbers.clamp(0, caretCharPos, document.strLength(newCaretLine));

    setCaretLinePos(newCaretLine, newCaretCharInd, false);
    api.window.setCursor(Cursor.arrow);
    api.window.repaint();
    Debug.consoleInfo("Viewport parsed in " + (System.currentTimeMillis() - parsingTimeStart) + "ms");

    parseFullFile();
  }

  private void onFileLoad(byte[] content) {
    Debug.consoleInfo("readAsBytes complete, l = " + content.length);
    parsingTimeStart = System.currentTimeMillis();
    api.window.sendToWorker(this::onFileParsed, JavaParser.PARSE_BYTES_JAVA, content);
  }

  private void onFirstLinesParsed(Object[] result) {
    if (fileStructureParsed) return;
    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();
    this.document = BaseParser.makeDocument(ints, chars);
    firstLinesParsed = true;
    Debug.consoleInfo("First lines parsed in " + (System.currentTimeMillis() - parsingTimeStart) + "ms");
  }

  void openFile(FileHandle f) {
    Debug.consoleInfo("opening file " + f.getName());
    setCaretLinePos(0, 0, false);
    // f.readAsBytes(this::onFileLoad, System.err::println);
    parsingTimeStart = System.currentTimeMillis();
    fileStructureParsed = false;
    firstLinesParsed = false;
    f.getSize(size -> {
      if (size <= EditorConst.BIG_FILE_SIZE_KB) {
        api.window.sendToWorker(this::onFileParsed, FileParser.asyncParseFullFile, f);
      } else {
        api.window.sendToWorker(this::onFirstLinesParsed, FileParser.asyncParseFirstLines, f, Arrays.copyOf(EditorConst.FIRST_LINES, 1));
        api.window.sendToWorker(this::onFileStructureParsed, FileParser.asyncParseFile, f);
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
    int newPos;
    if (ctrl) {
      if (shift < 0)
        newPos = caretCodeLine.prevPos(caretPos);
      else
        newPos = caretCodeLine.nextPos(caretPos);
    } else {
      newPos = caretCharPos + shift;
    }
    if (newPos > caretCodeLine.totalStrLength) { // goto next line
      if ((caretLine + 1) < document.length()) {
        caretCharPos = 0;
        setCaretLine(caretLine + 1, shiftPressed);
      }
    } else if (newPos < 0) {  // goto prev line
      if (caretLine > 0) {
        caretCharPos = document.line(caretLine - 1).totalStrLength;
        setCaretLine(caretLine - 1, shiftPressed);
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
    caretLine = Numbers.clamp(0, line, document.length() - 1);
    return setCaretPos(pos, shift);
  }

  private boolean setCaretLine(int value, boolean shift) {
    caretLine = Numbers.clamp(0, value, document.length() - 1);
    return setCaretPos(caretCharPos, shift);
  }

  private boolean setCaretPos(int charPos, boolean shift) {
    caretCharPos = Numbers.clamp(0, charPos, caretCodeLine().totalStrLength);
    caretPos = caretCodeLine().computePixelLocation(caretCharPos, g.mCanvas, fonts);
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
    int xOffset = (int) devicePR * EditorConst.CARET_X_OFFSET;

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

  private void computeCaret(V2i position) {
    caretLine = Numbers.clamp(0,
        (position.y + vScrollPos) / lineHeight, document.length() - 1);

    CodeLine line = caretCodeLine();
    int documentXPosition = Math.max(0, position.x - vLineX + hScrollPos);
    caretCharPos = line.computeCaretLocation(documentXPosition, g.mCanvas, fonts);
    caretPos = line.computePixelLocation(caretCharPos, g.mCanvas, fonts);
    if (1<0) Debug.consoleInfo(
        "onClickText: caretCharPos = " + caretCharPos + ", caretPos = " + caretPos);
    startBlinking();
  }

  void onClickText(V2i position, boolean shift) {
    computeCaret(position);
    adjustEditorScrollToCaret();
    if (shift) selection.isSelectionStarted = true;
    if (!selection.isSelectionStarted) {
      selection.startPos.set(caretLine, caretCharPos);
      selection.isSelectionStarted = true;
    }
    selection.select(caretLine, caretCharPos);
  }

  void onDoubleClickText(V2i position) {
    computeCaret(position);
    adjustEditorScrollToCaret();

    CodeLine line = caretCodeLine();
    int wordStart = line.wordStart(caretPos);
    int wordEnd = line.wordEnd(caretPos);

    selection.startPos.set(caretLine, wordStart);
    selection.isSelectionStarted = true;
    setCaretLinePos(caretLine, wordEnd, false);
    selection.isSelectionStarted = false;
  }

  CodeLine caretCodeLine() {
    return document.line(caretLine);
  }

  // InputListener methods

  final V2i eventPosition = new V2i();

  Consumer<V2i> dragLock;

  Consumer<IntUnaryOperator> vScrollHandler =
      move -> vScrollPos = move.applyAsInt(maxVScrollPos());

  Consumer<IntUnaryOperator> hScrollHandler =
      move -> hScrollPos = move.applyAsInt(maxHScrollPos());


  public boolean onMouseWheel(MouseEvent event, double dX, double dY) {
    // chrome sends 150px, firefox send "6 lines"
    int changeY = Numbers.iRnd(lineHeight * 4 * dY / 150);
    int changeX = Numbers.iRnd(dX);
    vScrollPos = clampScrollPos(vScrollPos + changeY, maxVScrollPos());
    hScrollPos = clampScrollPos(hScrollPos + changeX, maxHScrollPos());
    return true;
  }

  public boolean onMousePress(MouseEvent event, int button, boolean press, int clickCount) {
    eventPosition.set(event.position.x - compPos.x, event.position.y - compPos.y);

    if (!press) selection.isSelectionStarted = false;
    if (!press && dragLock != null) {
      dragLock = null;
      return true;
    }

    if (button == MOUSE_BUTTON_LEFT && clickCount == 2 && press) {
      onDoubleClickText(eventPosition);
      return true;
    }
    if (button == MOUSE_BUTTON_LEFT && clickCount == 1 && press) {
      dragLock = vScroll.onMouseClick(eventPosition, vScrollHandler, true);
      if (dragLock != null) return true;

      dragLock = hScroll.onMouseClick(eventPosition, hScrollHandler, false);
      if (dragLock != null) return true;

      if (footerRc.isInside(eventPosition)) {
        dragLock = org.sudu.experiments.Const.emptyDragLock;
        return true;
      }

      dragLock = (position) -> onClickText(position, event.shift);
      dragLock.accept(eventPosition);
    }
    return true;
  }

  public boolean onMouseMove(MouseEvent event, SetCursor setCursor) {
    eventPosition.set(event.position.x - compPos.x, event.position.y - compPos.y);

    if (dragLock != null) {
      dragLock.accept(eventPosition);
      return true;
    }

    if (vScroll.onMouseMove(eventPosition, setCursor)) return true;
    if (hScroll.onMouseMove(eventPosition, setCursor)) return true;
    if (lineNumbers.onMouseMove(eventPosition, setCursor)) return true;
    if (onMouseMove(eventPosition)) return setCursor.set(Cursor.text);
    return setCursor.setDefault();
  }

  public boolean onKey(KeyEvent event) {
    // do not consume browser keyboard to allow page reload and debug
    if (KeyEvent.isCopyPasteRelatedKey(event) || KeyEvent.isBrowserKey(event)) {
      return false;
    }
    if (event.ctrl && event.keyCode == KeyCode.A) return selectAll();
    // do not process release events
    if (!event.isPressed) return false;

    if (event.keyCode == KeyCode.F10) {
      api.window.addChild("child", DemoEdit0::new);
    }

    if (event.ctrl && event.keyCode == KeyCode.P) {
      parseFullFile();
      return true;
    }

    if (event.ctrl && event.keyCode == KeyCode.Z) {
      undoLastDiff();
      return true;
    }

    if (handleDoubleKey(event)) return true;
    if (handleDebug(event)) return true;
    if (handleNavigation(event)) return true;
    if (handleEditingKeys(event)) return true;

    if (1 < 0) Debug.consoleInfo("EditorComponent::onKey: "+ event.desc());

    if (event.ctrl && event.keyCode == KeyCode.W) {
      Debug.consoleInfo("Ctrl-W pressed ;)");
      return true;
    }

    if (event.ctrl || event.alt || event.meta) return false;
    if (event.keyCode == KeyCode.ESC) return false;
    return event.key.length() > 0 && handleInsert(event.key);
  }

  void reparse() {
    parsingTimeStart = System.currentTimeMillis();
    api.window.sendToWorker(this::onFileParsed, JavaParser.PARSE_BYTES_JAVA, document.getBytes());
  }

  public void parseViewport() {
    parseViewport(fileType);
  }

  public void debugPrintDocumentIntervals() {
    document.printIntervals();
  }

  private void parseViewport(int type) {
    if (type == FileParser.JAVA_FILE)
      api.window.sendToWorker(this::onVpParsed, JavaParser.PARSE_BYTES_JAVA_VIEWPORT, document.getBytes(), getViewport(), document.getIntervals() );
  }

  private int[] getViewport() {
    int firstLine = getFirstLine();
    int lastLine = getLastLine();

    firstLine = Math.max(0, firstLine - EditorConst.VIEWPORT_OFFSET);
    lastLine = Math.min(document.length() - 1, lastLine + EditorConst.VIEWPORT_OFFSET);

    return new int[]{document.getLineStartInd(firstLine), document.getVpEnd(lastLine), firstLine};
  }

  public void parseFullFile() {
    parsingTimeStart = System.currentTimeMillis();
    api.window.sendToWorker(this::onFileParsed, JavaParser.PARSE_BYTES_JAVA, document.getBytes());
  }

  public void iterativeParsing() {
    var node = document.tree.getReparseNode();
    if (node == null) return;
    String source = document.makeString();
    int[] interval = new int[]{node.getStart(), node.getStop(), node.getType()};
    int[] ints = new JavaIntervalParser().parseInterval(source, interval);
    char[] chars = document.makeString().toCharArray();
    BaseParser.updateDocument(document, ints, chars);
    document.onReparse();
  }

  private void showOpenFile() {
    api.window.showOpenFilePicker(EditorComponent.this::openFile);
  }

  public boolean onCopy(Consumer<String> setText, boolean isCut) {
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
      else setCaretLinePos(line, 0, false);
    } else {
      result = document.copy(selection, isCut);
      if (isCut) {
        setCaretLinePos(left.line, left.charInd, false);
        setSelectionToCaret();
        updateDocumentDiffTimeStamp();
      }
    }

    setText.accept(result);
    return true;
  }

  private boolean onMouseMove(V2i position) {
    return Rect.isInside(position,
        new V2i(vLineX, 0),
        new V2i(editorWidth(), editorHeight()));
  }

  private boolean handleEditingKeys(KeyEvent event) {
    return switch (event.keyCode) {
      case KeyCode.TAB -> handleTab();
      case KeyCode.ENTER -> handleEnter();
      case KeyCode.DELETE -> handleDelete();
      case KeyCode.BACKSPACE -> handleBackspace();
      case KeyCode.INSERT, KeyCode.ALT, KeyCode.SHIFT,
          KeyCode.CAPS_LOCK, KeyCode.CTRL -> true;
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
    int line = document.length() - 1;
    int charInd = document.strLength(line);
    selection.startPos.set(0, 0);
    selection.endPos.set(document.length() - 1, charInd);
    return true;
  }

  private void updateDocumentDiffTimeStamp() {
    document.setLastDiffTimestamp(api.window.timeNow());
  }

  public boolean hasVScroll() {
    return vScroll.visible();
  }

  public int getVScrollSize() {
    return vScroll.bgSize.x;
  }

  /* API */

  @Override
  public void setText(byte[] utf8bytes) {
    parsingTimeStart = System.currentTimeMillis();
    api.window.sendToWorker(this::onFileParsed, JavaParser.PARSE_BYTES_JAVA, utf8bytes);
  }

  @Override
  public byte[] getText() {
    return document.getBytes();
  }

  @Override
  public Disposable addListener(Listener listener) {
    return Disposable.empty();
  }
}
