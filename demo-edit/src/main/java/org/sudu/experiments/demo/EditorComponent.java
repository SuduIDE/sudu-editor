// todo: highlight current line
// todo: ctrl-left-right move by elements

package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.*;

import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;

import static org.sudu.experiments.input.InputListener.MOUSE_BUTTON_LEFT;

public class EditorComponent implements Disposable {

  boolean forceMaxFPS = false;
  int footerHeight;
  Runnable[] debugFlags = new Runnable[10];

  final SceneApi api;
  final WglGraphics g;
  final SetCursor setCursor;

  final Caret caret = new Caret();
  int caretLine, caretCharPos, caretPos;
  Canvas renderingCanvas;
  FontDesk font;
  FontDesk[] fonts = new FontDesk[4];
  EditorColorScheme colors = new EditorColorScheme();

  int lineHeight;

  Document document;

  // render cache
  CodeLineRenderer[] lines = new CodeLineRenderer[0];
  int firstLineRendered, lastLineRendered;

  final Toolbar toolbar = new Toolbar();
  FontDesk toolBarFont;

  // layout
  int vLineXBase = 100;
  int vLineX;
  int vLineW = 1;
  int vLineLeftDelta;

  V2i vLineSize = new V2i(1, 0);
  DemoRect footerRc = new DemoRect();
  ScrollBar vScroll = new ScrollBar();
  ScrollBar hScroll = new ScrollBar();
  Selection selection = new Selection();
  //V2i clientRect;
  int editorVScrollPos = 0;
  int editorHScrollPos = 0;

  int fullWidth = 0;
  double devicePR;

  boolean applyContrast, renderBlankLines = true;
  int scrollDown, scrollUp;
  boolean drawTails = true;
  int xOffset = 3;

  // line numbers
  LineNumbersComponent lineNumbers;
  //int lineNumLeftMargin = 10;

  V2i compPos;
  V2i compSize;

  public EditorComponent(
      SceneApi api,
      Document document,
      V2i compPos,
      V2i compSize
  ) {
    this.api = api;
    this.document = document;
    this.compPos = compPos;
    this.compSize = compSize;

    setCursor = SetCursor.wrap(api.window);

    devicePR = api.window.devicePixelRatio();
    Debug.consoleInfo("api.window.devicePixelRatio() = ", devicePR);
    g = api.graphics;

    vLineX = Numbers.iRnd(vLineXBase * devicePR);
    vLineLeftDelta = Numbers.iRnd(10 * devicePR);

    int lineNumbersWidth = vLineX - vLineLeftDelta;
    lineNumbers = new LineNumbersComponent(g, compPos, lineNumbersWidth);
    lineNumbers.setDevicePR(devicePR);

    //api.input.addListener(new MyInputListener());
    //clientRect = api.window.getClientRect();
    if (1<0) DebugHelper.dumpFontsSize(g);
    int editorFontSize = Numbers.iRnd(EditorConst.DEFAULT_FONT_SIZE * devicePR);
    caret.setWidth(Numbers.iRnd(caret.width() * devicePR));
    setFont(EditorConst.FONT, editorFontSize);
    initLineNumbers();

    int toolbarFontSize = Numbers.iRnd(EditorConst.TOOLBAR_FONT_SIZE * devicePR);
    toolBarFont = g.fontDesk(EditorConst.TOOLBAR_FONT_NAME, toolbarFontSize);
    layout();
    initToolbar();

    V2i screenRect = api.window.getScreenRect();
    Debug.consoleInfo("screenRect = " + screenRect);

    debugFlags[0] = this::toggleContrast;
    debugFlags[1] = this::toggleBlankLines;
    debugFlags[2] = this::toggleTails;
    debugFlags[3] = this::toggleXOffset;

    // d2d is very bold, contrast makes font heavier
    applyContrast = api.window.getHost() != Host.Direct2D;
  }

  private void toggleBlankLines() {
    renderBlankLines = !renderBlankLines;
    Debug.consoleInfo("renderBlankLines = " + renderBlankLines);
  }

  private void initToolbar() {
    toolbar.setBgColor(Colors.toolbarBg);
    toolbar.addButton("↓", Colors.toolbarText3, this::moveDown);
    toolbar.addButton("■", Colors.toolbarText3, this::stopMove);
//    toolbar.addButton("↑↑↑", Colors.toolbarText3, this::moveUp);
//
//    toolbar.addButton("C", Colors.toolbarText2, this::toggleContrast);
//    toolbar.addButton("XO", Colors.toolbarText2, this::toggleXOffset);
//    toolbar.addButton("DT", Colors.toolbarText2, this::toggleTails);
//    toolbar.addButton("TE", Colors.toolbarText2, this::toggleTopEdit);
//    toolbar.addButton("TB", Colors.toolbarText2, this::toggleTopBar);
    toolbar.addButton("A↑", Colors.toolbarText3, this::increaseFont);
    toolbar.addButton("A↓", Colors.toolbarText3, this::decreaseFont);
    toolbar.addButton("Segoe UI", Colors.rngToolButton(), this::setSegoeUI);
    toolbar.addButton("Verdana", Colors.rngToolButton(), this::setVerdana);
    toolbar.addButton("JetBrains Mono", Colors.rngToolButton(), this::setJetBrainsMono);
    toolbar.addButton("Consolas", Colors.rngToolButton(), this::setConsolas);

    toolbar.setFont(toolBarFont);
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

  private void toggleTopEdit() {
    CodeLineRenderer.bw = false;
    CodeLineRenderer.useTop = !CodeLineRenderer.useTop;
    Debug.consoleInfo("CodeLineRenderer.useTop = " + CodeLineRenderer.useTop);
    invalidateFont();
  }
  private void toggleTopBar() {
    Toolbar.useTopMode = !Toolbar.useTopMode;
    Debug.consoleInfo("Toolbar.useTopMode = " + Toolbar.useTopMode);
    toolbar.invalidateTexture();
  }

  private void increaseFont() {
    changeFont(font.name, font.iSize + 1);
  }

  private void decreaseFont() {
    if (font.iSize <= EditorConst.MIN_FONT_SIZE) return;
    changeFont(font.name, font.iSize - 1);
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

  private void setSegoeUI() {
    changeFont(Fonts.SegoeUI, font.iSize);
  }

  private void setVerdana() {
    changeFont(Fonts.Verdana, font.iSize);
  }

  private void setJetBrainsMono() {
    changeFont(Fonts.JetBrainsMono, font.iSize);
  }

  private void setConsolas() {
    changeFont(Fonts.Consolas, font.iSize);
  }

  private void setFont(String name, int size) {
    setFonts(name, size);
    font = fonts[CodeElement.fontIndex(false, false)];

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
  }

  private void changeFont(String name, int size) {
    lineNumbers.dispose();
    invalidateFont();
    setFont(name, size);
    afterFontChanged();
    initLineNumbers();
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
    toolbar.dispose();
  }

  int editorFullHeight() {
    return (document.length() + EditorConst.BLANK_LINES) * lineHeight;
  }

  int maxEditorVScrollPos() {
    return Math.max(editorFullHeight() - editorHeight(), 0);
  }

  int maxEditorHScrollPos() {
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
    int vScrollPos = editorVScrollPos;
    editorVScrollPos = clampScrollPos(editorVScrollPos + scrollDown  -  scrollUp,
        maxEditorVScrollPos());

    boolean scrollMoving = vScrollPos != editorVScrollPos;

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
    editorVScrollPos = Math.min(editorVScrollPos, maxEditorVScrollPos());
    editorHScrollPos = Math.min(editorHScrollPos, maxEditorHScrollPos());

    int caretVerticalOffset = (lineHeight - caret.height()) / 2;
    int caretX = caretPos - caret.width() / 2 - editorHScrollPos;
    caret.setPosition(vLineX + caretX, caretVerticalOffset + caretLine * lineHeight - editorVScrollPos);

    int docLen = document.length();

    int firstLine = getFirstLine();
    int lastLine = getLastLine();

    this.firstLineRendered = firstLine;
    this.lastLineRendered = lastLine;

    for (int i = firstLine; i <= lastLine && i < docLen; i++) {
      CodeLine nextLine = document.line(i);
      CodeLineRenderer line = lineRenderer(i);
      line.updateTexture(nextLine, renderingCanvas, fonts, g, lineHeight, editorWidth(), editorHScrollPos);
      CodeLine lineContent = line.line;

      fullWidth = Math.max(fullWidth, nextLine.lineMeasure() + (int) (EditorConst.RIGHT_PADDING * devicePR));
      int yPosition = lineHeight * i - editorVScrollPos;

      line.draw(
          compPos.y + yPosition, compPos.x +  vLineX, g, tRegion, size,
          applyContrast ? EditorConst.CONTRAST : 0,
          editorWidth(), lineHeight, editorHScrollPos,
          colors, getSelLineSegment(i, lineContent));
    }

    for (int i = firstLine; i <= lastLine && i < docLen && drawTails; i++) {
      CodeLineRenderer line = lineRenderer(i);
      int yPosition = lineHeight * i - editorVScrollPos;
      boolean isTailSelected = selection.isTailSelected(i);
      Color tailColor = isTailSelected? colors.selectionBgColor : colors.codeLineTailColor;
      line.drawTail(g, compPos.x + vLineX,compPos.y + yPosition, lineHeight,
          size, editorHScrollPos, editorWidth(), tailColor);
    }

    if (caretX >= -caret.width() / 2 && caret.needsPaint(compSize)) caret.paint(g, compPos);

    // draw bottom 5 invisible lines
    if (renderBlankLines) {
      int nextLine = Math.min(lastLine + 1, docLen);
      int yPosition = lineHeight * nextLine - editorVScrollPos;
      drawDocumentBottom(editorBottom, editorRight, yPosition);
    }

    drawScrollBar();
    drawLineNumbers(editorBottom, firstLine, lastLine);

    drawFooter();
    drawToolBar();

    vScroll.layoutVertical(editorVScrollPos, editorRight, editorHeight(), editorFullHeight(), vScrollBarWidth());
    hScroll.layoutHorizontal(editorHScrollPos, editorBottom, editorRight - vLineX, fullWidth, vLineX, vScrollBarWidth());

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
    int textHeight = Math.min(editorBottom, document.length() * lineHeight - editorVScrollPos);

    lineNumbers.draw(
        editorBottom, textHeight, editorVScrollPos,
        firstLine, lastLine, caretLine,
        colors.lineNumbersColors
    );
  }

  private int getFirstLine() {
    return Math.min(editorVScrollPos / lineHeight, document.length() - 1);
  }

  private int getLastLine() {
    return Math.min((editorVScrollPos + editorHeight() - 1) / lineHeight, document.length() - 1);
  }

  private void initLineNumbers() {
    lineNumbers.setFont(fonts[0], lineHeight);
    lineNumbers.initTextures(getFirstLine(), editorHeight());
  }

  private CodeLineRenderer lineRenderer(int i) {
    return lines[i % lines.length];
  }

  boolean handleEnter() {
    if (selection.isAreaSelected()) deleteSelectedArea();
    document.line(caretLine).invalidateCache();
    document.newLineOp(caretLine, caretCharPos);
    return setCaretLinePos(caretLine + 1, 0, false);
  }

  boolean handleDelete() {
    if (selection.isAreaSelected()) deleteSelectedArea();
    else document.deleteChar(caretLine, caretCharPos);
    adjustEditorScrollToCaret();
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
      return setCaretLinePos(cLine, cPos, false);
    }
  }

  boolean handleInsert(String s) {
    if (selection.isAreaSelected()) deleteSelectedArea();
    String[] lines = s.split("\n", -1);

    document.insertLines(caretLine, caretCharPos, lines);

    int newCaretLine = caretLine + lines.length - 1;
    int newCaretPos;
    if (newCaretLine == caretLine) newCaretPos = caretCharPos + lines[0].length();
    else newCaretPos = lines[lines.length - 1].length();

    setCaretLinePos(newCaretLine, newCaretPos, false);
    setSelectionToCaret();
    return true;
  }

  private void deleteSelectedArea() {
    var leftPos = selection.getLeftPos();
    document.deleteSelected(selection);
    setCaretLinePos(leftPos.line, leftPos.charInd, false);
    setSelectionToCaret();
  }

  private void setSelectionToCaret() {
    selection.isSelectionStarted = false;
    selection.startPos.set(caretLine, caretCharPos);
    selection.endPos.set(caretLine, caretCharPos);
  }

  private void drawToolBar() {
    g.enableBlend(true);
    V2i size = toolbar.size(g, devicePR);

    int editorHeight = editorHeight();
    boolean above = false; // caretLine * lineHeight - editorVScrollPos < editorHeight / 2;
    int posX = (vScroll.visible() ? vScroll.bgPos.x : compSize.x) - 2 - size.x;
    int posY = above ? editorHeight - size.y - 1 : 1;
    toolbar.setPos(posX, posY);
    toolbar.render(g, compPos);
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

  public void onResize(V2i pos, V2i size) {
    compPos = pos;
    compSize = size;
    layout();
    devicePR = api.window.devicePixelRatio();
    lineNumbers.setPos(pos);
    lineNumbers.setDevicePR(devicePR);
  }

  int clampScrollPos(int pos, int maxScrollPos) {
    return Math.min(Math.max(0, pos), maxScrollPos);
  }

  private void onFileLoad(String content) {
    Debug.consoleInfo("readAsText complete, l = " + content.length());
  }

  private void openFile(FileHandle f) {
    int fileSize = f.getSize();
    Debug.consoleInfo("openFile: name = " + f.getName() + ", size =  " + fileSize);
    f.readAsText(this::onFileLoad, System.err::println);
  }

  boolean arrowUpDown(int amount, boolean ctrl, boolean alt, boolean shiftPressed) {
    if (shiftSelection(shiftPressed)) return true;
    if (ctrl && alt) return true;
    if (ctrl) {  //  editorVScrollPos moves, caretLine does not change
      editorVScrollPos = clampScrollPos(editorVScrollPos + amount * lineHeight * 12 / 10, maxEditorVScrollPos());
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
    caret.startDelay(api.window.timeNow());
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
    int editVisibleYMin = editorVScrollPos;
    int editVisibleYMax = editorVScrollPos + editorHeight();
    int caretVisibleY0 = caretLine * lineHeight;
    int caretVisibleY1 = caretLine * lineHeight + lineHeight;

    if (caretVisibleY0 < editVisibleYMin + lineHeight) {
      editorVScrollPos = clampScrollPos(caretVisibleY0 - lineHeight, maxEditorVScrollPos());
    } else if (caretVisibleY1 > editVisibleYMax - lineHeight) {
      editorVScrollPos = clampScrollPos(caretVisibleY1 - editorHeight() + lineHeight, maxEditorVScrollPos());
    }
  }

  private void adjustEditorHScrollToCaret() {
    int xOffset = (int) devicePR * EditorConst.CARET_X_OFFSET;

    int editVisibleXMin = editorHScrollPos;
    int editVisibleXMax = editorHScrollPos + editorWidth();
    int caretVisibleX0 = caretPos;
    int caretVisibleX1 = caretPos + xOffset;

    if (caretVisibleX0 < editVisibleXMin + xOffset) {
      editorHScrollPos = clampScrollPos(caretVisibleX0 - xOffset, maxEditorHScrollPos());
    } else if (caretVisibleX1 > editVisibleXMax - xOffset) {
      editorHScrollPos = clampScrollPos(caretVisibleX1 - editorWidth() + xOffset, maxEditorHScrollPos());
    }
  }

  private void computeCaret(V2i position) {
    caretLine = Numbers.clamp(0,
        (position.y + editorVScrollPos) / lineHeight, document.length() - 1);

    CodeLine line = caretCodeLine();
    int documentXPosition = Math.max(0, position.x - vLineX + editorHScrollPos);
    caretCharPos = line.computeCaretLocation(documentXPosition, g.mCanvas, fonts);
    caretPos = line.computePixelLocation(caretCharPos, g.mCanvas, fonts);
    if (1<0) Debug.consoleInfo(
        "onClickText: caretCharPos = " + caretCharPos + ", caretPos = " + caretPos);
    caret.startDelay(api.window.timeNow());
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
      move -> editorVScrollPos = move.applyAsInt(maxEditorVScrollPos());

  Consumer<IntUnaryOperator> hScrollHandler =
      move -> editorHScrollPos = move.applyAsInt(maxEditorHScrollPos());


  public boolean onMouseWheel(MouseEvent event, double dX, double dY) {
    // chrome sends 150px, firefox send "6 lines"
    int changeY = Numbers.iRnd(lineHeight * 4 * dY / 150);
    int changeX = Numbers.iRnd(dX);
    editorVScrollPos = clampScrollPos(editorVScrollPos + changeY, maxEditorVScrollPos());
    editorHScrollPos = clampScrollPos(editorHScrollPos + changeX, maxEditorHScrollPos());
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
      if (toolbar.onMouseClick(eventPosition, press)) {
        return true;
      }

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

  public boolean onMouseMove(MouseEvent event) {
    eventPosition.set(event.position.x - compPos.x, event.position.y - compPos.y);

    if (dragLock != null) {
      dragLock.accept(eventPosition);
      return true;
    }

    if (toolbar.onMouseMove(eventPosition, setCursor)) return true;
    if (vScroll.onMouseMove(eventPosition, setCursor)) return true;
    if (hScroll.onMouseMove(eventPosition, setCursor)) return true;
    if (lineNumbers.onMouseMove(eventPosition, setCursor, editorHeight())) return true;
    if (onMouseMove(eventPosition)) return true;
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
        api.window.addChild("child", DemoEdit::new);
      }

    if (event.ctrl && event.keyCode == KeyCode.O) {
      if (event.shift) {
        api.window.showDirectoryPicker(
            s -> Debug.consoleInfo("showDirectoryPicker -> " + s));
      } else {
        api.window.showOpenFilePicker(EditorComponent.this::openFile);
      }
      return true;
    }

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
      }
    }

    setText.accept(result);
    return true;
  }

  private boolean onMouseMove(V2i position) {
    return Rect.isInside(position, new V2i(vLineX, 0), new V2i(editorWidth(), editorHeight())) && setCursor.set(Cursor.text);
  }

  private boolean handleEditingKeys(KeyEvent event) {
    return switch (event.keyCode) {
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
      case KeyCode.PAGE_UP ->
          event.ctrl ? setCaretLine(Numbers.iDivRoundUp(editorVScrollPos, lineHeight), event.shift)
              : arrowUpDown(2 - Numbers.iDivRound(editorHeight(), lineHeight), false, event.alt, event.shift);
      case KeyCode.PAGE_DOWN ->
          event.ctrl ? setCaretLine((editorVScrollPos + editorHeight()) / lineHeight - 1, event.shift)
              : arrowUpDown(Numbers.iDivRound(editorHeight(), lineHeight) - 2, false, event.alt, event.shift);
      case KeyCode.ARROW_LEFT -> moveCaretLeftRight(-1, event.ctrl, event.shift);
      case KeyCode.ARROW_RIGHT -> moveCaretLeftRight(1, event.ctrl, event.shift);
      case KeyCode.HOME -> shiftSelection(event.shift) || setCaretPos(0, event.shift);
      case KeyCode.END -> shiftSelection(event.shift) || setCaretPos(caretCodeLine().totalStrLength, event.shift);
      default -> false;
    };
    if (result && event.shift) selection.endPos.set(caretLine, caretCharPos);
    return result;
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

  private boolean selectAll() {
    int line = document.length() - 1;
    int charInd = document.strLength(line);
    selection.startPos.set(0, 0);
    selection.endPos.set(document.length() - 1, charInd);
    return true;
  }

}
