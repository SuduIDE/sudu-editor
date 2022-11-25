// todo: highlight current line
// todo: ctrl-left-right move by elements

package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.input.InputListener;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.math.Numbers;

import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;

public class DemoEdit extends Scene {

  boolean forceMaxFPS = false;
  int footerHeight;
  Runnable[] debugFlags = new Runnable[10];

  final WglGraphics g;
  final V4f bgColor = Color.Cvt.gray(0);
  final Caret caret = new Caret();
  int caretLine, caretCharPos, caretPos;
  Canvas renderingCanvas;
  FontDesk font;
  int lineHeight, realFontSize;

  Document document = new Document(EditorConst.DOCUMENT_LINES);
  CodeLineRenderer[] lines;

  final Toolbar toolbar = new Toolbar();
  FontDesk toolBarFont;

  // layout
  int vLineXBase = 80;
  int vLineX;
  int vLineW = 1;
  int vLineLeftDelta;

  V2i vLineSize = new V2i(1, 0);
  DemoRect footerRc = new DemoRect();
  ScrollBar vScroll = new ScrollBar();
  V2i clientRect;
  int editorVScrollPos = 0;

  boolean applyContrast = true, renderBlankLines = true;
  boolean scrollDown, scrollUp, scrollFaster, scrollEvenFaster;

  // line numbers
  LineNumbersComponent lineNumbers;
  int lineNumLeftMargin = 10;

  public DemoEdit(SceneApi api) {
    super(api);
    double devicePR = api.window.devicePixelRatio();
    Debug.consoleInfo("api.window.devicePixelRatio() = ", devicePR);
    g = api.graphics;

    vLineX = Numbers.iRnd(vLineXBase * devicePR);
    vLineLeftDelta = Numbers.iRnd(10 * devicePR);

    int lineNumbersWidth = vLineX - vLineLeftDelta - lineNumLeftMargin;
    lineNumbers = new LineNumbersComponent(g, new V2i(lineNumLeftMargin, 0),
      lineNumbersWidth,
      Colors.defaultText, Colors.editBgColor);

    api.input.addListener(new MyInputListener());
    clientRect = api.window.getClientRect();
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

    int vY = Math.max(clientRect.y, screenRect.y);
    int cacheLines = Numbers.iDivRoundUp(vY, lineHeight) + EditorConst.MIN_CACHE_LINES;
    Debug.consoleInfo("cacheLines = ", cacheLines);
    allocLines(cacheLines);

//      measureAll();

    debugFlags[0] = () -> applyContrast = !applyContrast;
    debugFlags[1] = () -> renderBlankLines = !renderBlankLines;
    debugFlags[3] = () -> Debug.consoleInfo(" debug event [" + 3 + "]");
  }

  private void initToolbar() {
    toolbar.setBgColor(Colors.toolbarBg);
    toolbar.addButton("↓↓↓", Colors.toolbarText3, this::moveDown);
    toolbar.addButton("↑↑↑", Colors.toolbarText3, this::moveUp);
    toolbar.addButton("■", Colors.toolbarText3, this::stopMove);
    toolbar.addButton("B/W", Colors.toolbarTextWhite, this::toggleBW);
    toolbar.addButton("A↑", Colors.toolbarText2, this::increaseFont);
    toolbar.addButton("A↓", Colors.toolbarText2, this::decreaseFont);
    toolbar.addButton("Segoe UI", Colors.rngToolButton(), this::setSegoeUI);
    toolbar.addButton("Verdana", Colors.rngToolButton(), this::setVerdana);
    toolbar.addButton("JetBrains Mono", Colors.rngToolButton(), this::setJetBrainsMono);
    toolbar.addButton("Consolas", Colors.rngToolButton(), this::setConsolas);

    toolbar.setFont(toolBarFont);
    V2i measure = toolbar.measure(g.mCanvas);
  }

  private void toggleBW() {
    CodeLineRenderer.bw = !CodeLineRenderer.bw;
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
    scrollUp = false;
  }

  private void moveUp() {
    scrollUp = switchScroll(scrollUp);
    scrollDown = false;
  }

  private boolean switchScroll(boolean scrollValue) {
    if (scrollValue) {
      scrollEvenFaster = !scrollEvenFaster;
      scrollFaster = !scrollFaster;
      if (scrollFaster) {
        scrollValue = false;
      }
    } else {
      scrollFaster = true;
      scrollEvenFaster = false;
      scrollValue = true;
    }
    return scrollValue;
  }

  private void stopMove() {
    scrollFaster = true;
    scrollEvenFaster = false;
    scrollUp = scrollDown = false;
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
    font = g.fontDesk(name, size);
    realFontSize = font.lineHeight();
    lineHeight = Numbers.iDivRound(
        realFontSize * EditorConst.LINE_HEIGHT_NUMERATOR, EditorConst.LINE_HEIGHT_DENOMINATOR);
    footerHeight = lineHeight;
    caret.setHeight(font.caretHeight(lineHeight));
    renderingCanvas = Disposable.assign(
        renderingCanvas, g.createCanvas(EditorConst.TEXTURE_WIDTH, lineHeight));
    renderingCanvas.setFont(font);

    int baseLineBase = lineHeight - font.iDescent;
    int baseline = baseLineBase - (lineHeight - realFontSize) / 2;

    Debug.consoleInfo("Set editor font to: "+ name + " " + size
        + ", real font size = " + realFontSize
        + ", lineHeight = " + lineHeight
        + ", caretHeight = " + caret.height()
        + ", ");
    Debug.consoleInfo("\t(lineHeight - font.descent) = " + baseLineBase
        + ", baseline = " + baseline);
  }

  private void changeFont(String name, int size) {
    invalidateFont();
    setFont(name, size);
    afterFontChanged();
    initLineNumbers();
    api.window.repaint();
  }

  private void afterFontChanged() {
    // footer depends on font size and needs re-layout
    layoutFooter();
    CodeLine codeLine = document.line(caretLine);
    codeLine.measure(g.mCanvas);
    caretPos = codeLine.computePixelLocation(caretCharPos, g.mCanvas);
    adjustEditorVScrollToCaret();
  }

  private void invalidateFont() {
    Debug.consoleInfo("invalidateFont");

    for (CodeLineRenderer line : lines) {
      line.dispose();
    }
    lineNumbers.dispose();
    document.invalidateFont();
  }

  private void allocLines(int N) {
    Debug.consoleInfo("allocLines: N = ", N);
    lines = new CodeLineRenderer[N];
    for (int i = 0; i < lines.length; i++) {
      lines[i] = new CodeLineRenderer();
    }
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

  int maxEditorVScrollPos() {
    return Math.max(editorFullHeight() - editorHeight(), 0);
  }

  int editorHeight() {
    return clientRect.y - footerHeight;
  }

  private void layout() {
    layoutFooter();
  }

  private void layoutFooter() {
    int editorHeight = clientRect.y - footerHeight;
    footerRc.set(0, editorHeight, clientRect.x, footerHeight);
    footerRc.color.set(Colors.editFooterFill.v4f);
    vLineSize.y = editorHeight;
  }

  public boolean update(double timestamp) {
    int scrollSpeed = 1 + (scrollFaster ? 5 : 0) + (scrollEvenFaster ? 15 : 0);
    int vScrollPos = editorVScrollPos;
    editorVScrollPos = clampScrollPos(editorVScrollPos
        + (scrollDown ? scrollSpeed : 0) + (scrollUp ? -scrollSpeed : 0));

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

  // debug
  private int debugFL, debugLL;

  public void paint() {
    int editorBottom = clientRect.y - footerHeight;
    int editorRight = clientRect.x;

    vScroll.layoutVertical(editorVScrollPos, editorRight, editorHeight(), editorFullHeight(), vScrollBarWidth());

    g.enableBlend(false);
    g.clear(bgColor);

    drawVerticalLine();

    int caretVerticalOffset = (lineHeight - caret.height()) / 2;
    caret.setPosition(vLineX + caretPos - caret.width() / 2,
        caretVerticalOffset + caretLine * lineHeight - editorVScrollPos);

    if (lines != null) {
      int docLen = document.length();

      int firstLine = getFirstLine();
      int lastLine = Math.min(
          (editorVScrollPos + editorHeight() - 1) / lineHeight, docLen - 1);

      if (firstLine != debugFL || lastLine != debugLL) {
        debugFL = firstLine;
        debugLL = lastLine;
//        JsHelper.consoleInfo("firstLine = " + firstLine + ", lastLine = " + lastLine);
      }

      for (int i = firstLine; i <= lastLine && i < docLen; i++) {
        CodeLine nextLine = document.line(i);
        CodeLineRenderer line = lineRenderer(i);
        if (line.needsUpdate(nextLine)) {
          g.mCanvas.setFont(font);
          line.updateTexture(nextLine, renderingCanvas, font, g, lineHeight);
        }
      }

      for (int i = firstLine; i <= lastLine && i < docLen; i++) {
        int lineIndex = i % lines.length;
        int yPosition = lineHeight * i - editorVScrollPos;
        lines[lineIndex].draw(yPosition, vLineX, g, tRegion, size,
            applyContrast ? EditorConst.CONTRAST : 0);
      }

      caret.paint(g);

      // draw bottom 5 invisible lines
      if (renderBlankLines) {
        int nextLine = Math.min(lastLine + 1, docLen);
        int yPosition = lineHeight * nextLine - editorVScrollPos;
        drawDocumentBottom(editorBottom, editorRight, yPosition);
      }

      drawScrollBar();

      lineNumbers.update(firstLine);
      lineNumbers.draw(editorVScrollPos);
      if (firstLine <= caretLine && caretLine <= lastLine){
        lineNumbers.drawCaretLine(editorVScrollPos, caretLine);
      }
    }

    drawFooter();
    drawToolBar();
//    g.checkError("paint complete");
    if (0>1) {
      String s = "fullMeasure:" + CodeLine.cacheMiss + ", cacheHits: " + CodeLine.cacheHits;
      Debug.consoleInfo(s);
      CodeLine.cacheMiss = CodeLine.cacheHits = 0;
    }
  }

  private int getFirstLine() {
    return Math.min(editorVScrollPos / lineHeight, document.length() - 1);
  }

  private void initLineNumbers(){
    lineNumbers.setFont(font, lineHeight);
    lineNumbers.setEditorBottom(editorHeight());
    lineNumbers.initTextures(getFirstLine());
  }

  private CodeLineRenderer lineRenderer(int i) {
    return lines[i % lines.length];
  }

  boolean handleEnter() {
    document.line(caretLine).invalidateCache();
    document.newLineOp(caretLine, caretCharPos);
    return setCaretLinePos(caretLine + 1, 0);
  }

  boolean handleDelete() {
    document.deleteChar(caretLine, caretCharPos);
    return true;
  }

  boolean handleBackspace() {
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
    return setCaretLinePos(cLine, cPos);
  }

  boolean handleInsert(String s) {
    document.insertAt(caretLine, caretCharPos, s);
    return setCaretPos(caretCharPos + s.length());
  }

  private void drawToolBar() {
    g.enableBlend(true);
    V2i size = toolbar.size();
    int pos = (vScroll.visible() ? vScroll.bgPos.x : clientRect.x) - 2 - size.x;
    toolbar.setPos(pos, 0);
    toolbar.render(g);
  }

  int vScrollBarWidth() {
    return (int) font.WWidth;
  }

  private void drawDocumentBottom(int editorBottom, int editorRight, int yPosition) {
    if (yPosition < editorBottom) {
      size.y = editorBottom - yPosition;
      size.x = editorRight - vLineX;

      g.drawRect(vLineX, yPosition, size, Colors.editBgColor.v4f);
    }
  }

  private void drawScrollBar() {
    // draw v-scroll bar
    if (vScroll.visible()) {
      g.enableBlend(true);
      vScroll.draw(g);
    }
  }

  private void drawFooter() {
    g.enableBlend(false);
    Color.Cvt.fromRGBA(0, 0, 0, 128, footerRc.color);
    footerRc.draw(g, 0, 0);
  }

  private void drawVerticalLine() {
    vLineSize.x = vLineW;
    g.drawRect(vLineX - vLineLeftDelta, 0, vLineSize, Colors.editNumbersVLine.v4f);
    vLineSize.x = vLineLeftDelta - vLineW;
    g.drawRect(vLineX - vLineLeftDelta + vLineW, 0, vLineSize, Colors.editBgColor.v4f);
  }

  public void onResize(V2i size) {
    clientRect = size;
    layout();

    lineNumbers.resize(size, editorHeight());
    int firstLine = getFirstLine();
    lineNumbers.initTextures(firstLine);
  }

  int clampScrollPos(int pos) {
    return Math.min(Math.max(0, pos), maxEditorVScrollPos());
  }

  class MyInputListener implements InputListener {
    Consumer<V2i> dragLock;

    @Override
    public boolean onMouseWheel(MouseEvent event, double dX, double dY) {
//      JsHelper.consoleInfo("dY = ", dY);
      int change = (Math.abs((int)dY) + 4) / 2;
      int change1 = dY < 0 ? -1 : 1;
      editorVScrollPos = clampScrollPos(editorVScrollPos + change * change1);
      return true;
    }

    Consumer<IntUnaryOperator> vScrollHandler =
        move -> editorVScrollPos = move.applyAsInt(maxEditorVScrollPos());

    @Override
    public boolean onMousePress(MouseEvent event, int button, boolean press, int clickCount) {
//      String a = press ? "click b=" : "unClick b=";
//      System.out.println(a + button + ", count=" + clickCount);

      if (!press && dragLock != null) {
        dragLock = null;
        return true;
      }

      if (button == MOUSE_BUTTON_LEFT && clickCount == 1 && press) {
        if (toolbar.onMouseClick(event.position, press)) {
          return true;
        }

        dragLock = vScroll.onMouseClick(event.position, vScrollHandler);
        if (dragLock != null) return true;

        if (footerRc.isInside(event.position)) {
          dragLock = org.sudu.experiments.Const.emptyDragLock;
          return true;
        }

        dragLock = DemoEdit.this::onClickText;
        dragLock.accept(event.position);
      }
      return true;
    }

    @Override
    public boolean onMouseMove(MouseEvent event) {
      if (dragLock != null) {
        dragLock.accept(event.position);
        return true;
      }
      if (toolbar.onMouseMove(event.position)) return true;
      if (vScroll.onMouseMove(event.position)) return true;
      return false;
    }

    @Override
    public boolean onKey(KeyEvent event) {
      // do not consume browser keyboard to allow page reload and debug
      if (KeyEvent.isCopyPasteRelatedKey(event) || KeyEvent.isBrowserKey(event)) {
        return false;
      }
      // do not process release events
      if (!event.isPressed) return false;

      if (handleDebug(event)) return true;
      if (handleNavigation(event)) return true;
      if (handleEditingKeys(event)) return true;

      if (1 > 0) Debug.consoleInfo(event.desc());
      if (event.ctrl || event.alt) return false;
      if (event.keyCode == KeyCode.ESC) return false;
      return handleInsert(event.key);
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

    private boolean handleNavigation(KeyEvent event) {
      return switch (event.keyCode) {
        case KeyCode.ARROW_UP -> arrowUpDown(-1, event.ctrl, event.alt);
        case KeyCode.ARROW_DOWN -> arrowUpDown(1, event.ctrl, event.alt);
        case KeyCode.PAGE_UP ->
            event.ctrl ? setCaretLine(Numbers.iDivRoundUp(editorVScrollPos, lineHeight))
                : arrowUpDown(2 - Numbers.iDivRound(editorHeight(), lineHeight), false, event.alt);
        case KeyCode.PAGE_DOWN ->
            event.ctrl ? setCaretLine((editorVScrollPos + editorHeight()) / lineHeight - 1)
                : arrowUpDown(Numbers.iDivRound(editorHeight(), lineHeight) - 2, false, event.alt);
        case KeyCode.ARROW_LEFT -> moveCaretLeftRight(-1);
        case KeyCode.ARROW_RIGHT -> moveCaretLeftRight(1);
        case KeyCode.HOME -> setCaretPos(0);
        case KeyCode.END -> setCaretPos(caretCodeLine().totalStrLength);
        default -> false;
      };
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

    @Override
    public boolean onCopy(Consumer<String> setText, boolean isCut) {
      Debug.consoleInfo("onCopy");
      setText.accept("This is onCopy text handler, operation = " + (isCut ? "cut" : "copy"));
      return true;
    }

    public Consumer<String> onPastePlainText() {
      return DemoEdit.this::handleInsert;
    }
  }
  private boolean arrowUpDown(int amount, boolean ctrl, boolean alt) {
    if (ctrl && alt) return true;
    if (ctrl) {  //  editorVScrollPos moves, caretLine does not change
      editorVScrollPos = clampScrollPos(editorVScrollPos + amount * lineHeight * 12 / 10);
    } else if (alt) {
      // todo: smart move to prev/next method start
    } else {
      setCaretLine(caretLine + amount);
      adjustEditorVScrollToCaret();
    }
    return true;
  }

  private boolean moveCaretLeftRight(int shift) {
    int newPos = caretCharPos + shift;
    if (newPos > caretCodeLine().totalStrLength) { // goto next line
      if ((caretLine + 1) < document.length()) {
        caretCharPos = 0;
        setCaretLine(caretLine + 1);
      }
    } else if (newPos < 0) {  // goto prev line
      if (caretLine > 0) {
        caretCharPos = document.line(caretLine - 1).totalStrLength;
        setCaretLine(caretLine - 1);
      }
    } else {
      setCaretPos(newPos);
    }
    return true;
  }

  private boolean setCaretLinePos(int line, int pos) {
    caretLine = Numbers.clamp(0, line, document.length() - 1);
    return setCaretPos(pos);
  }

  private boolean setCaretLine(int value) {
    caretLine = Numbers.clamp(0, value, document.length() - 1);
    return setCaretPos(caretCharPos);
  }

  private boolean setCaretPos(int charPos) {
    caretCharPos = Numbers.clamp(0, charPos, caretCodeLine().totalStrLength);
    g.mCanvas.setFont(font);
    caretPos = caretCodeLine().computePixelLocation(caretCharPos, g.mCanvas);
    caret.startDelay(api.window.timeNow());
    return true;
  }

  private void adjustEditorVScrollToCaret() {
    int editVisibleYMin = editorVScrollPos;
    int editVisibleYMax = editorVScrollPos + editorHeight();
    int caretVisibleY0 = caretLine * lineHeight;
    int caretVisibleY1 = caretLine * lineHeight + lineHeight;

    if (caretVisibleY0 < editVisibleYMin + lineHeight) {
      editorVScrollPos = clampScrollPos(caretVisibleY0 - lineHeight);
    } else if (caretVisibleY1 > editVisibleYMax - lineHeight) {
      editorVScrollPos = clampScrollPos(caretVisibleY1 - editorHeight() + lineHeight);
    }
  }

  void onClickText(V2i position) {
    caretLine = Numbers.clamp(0,
        (position.y + editorVScrollPos) / lineHeight, document.length() - 1);

    CodeLine line = caretCodeLine();
    g.mCanvas.setFont(font);
    int documentXPosition = Math.max(0, position.x - vLineX);
    caretCharPos = line.computeCaretLocation(documentXPosition, g.mCanvas);
    caretPos = line.computePixelLocation(caretCharPos, g.mCanvas);
    if (1>0) Debug.consoleInfo(
        "onClickText: caretCharPos = " + caretCharPos + ", caretPos = " + caretPos);
    caret.startDelay(api.window.timeNow());
  }

  CodeLine caretCodeLine() {
    return document.line(caretLine);
  }
}
