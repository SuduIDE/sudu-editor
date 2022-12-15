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
  FontDesk[] fonts = new FontDesk[4];
  EditorColorScheme colors = new EditorColorScheme();
  
  int lineHeight;

  Document document = new Document(EditorConst.DOCUMENT_LINES);
  CodeLineRenderer[] lines;

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
  V2i clientRect;
  int editorVScrollPos = 0;
  int editorHScrollPos = 0;

  int fullWidth = 0;
  double devicePR;

  boolean applyContrast, renderBlankLines = true;
  boolean scrollDown, scrollUp, scrollFaster, scrollEvenFaster;
  boolean drawTails = true;
  int xOffset = 3;

  // line numbers
  LineNumbersComponent lineNumbers;
  int lineNumLeftMargin = 10;

  public DemoEdit(SceneApi api) {
    super(api);
    devicePR = api.window.devicePixelRatio();
    Debug.consoleInfo("api.window.devicePixelRatio() = ", devicePR);
    g = api.graphics;

    vLineX = Numbers.iRnd(vLineXBase * devicePR);
    vLineLeftDelta = Numbers.iRnd(10 * devicePR);

    int lineNumbersWidth = vLineX - vLineLeftDelta - lineNumLeftMargin;
    lineNumbers = new LineNumbersComponent(g, new V2i(lineNumLeftMargin, 0), lineNumbersWidth);
    lineNumbers.setDevicePR(devicePR);

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
    Debug.consoleInfo("cacheLines = " + cacheLines);
    allocLines(cacheLines);

//      measureAll();

    debugFlags[0] = this::toggleContrast;
    debugFlags[1] = this::toggleBlankLines;
    debugFlags[2] = this::toggleTails;

    // d2d is very bold, contrast makes font heavier
    applyContrast = api.window.getHost() != Host.Direct2D;
  }

  private void toggleBlankLines() {
    renderBlankLines = !renderBlankLines;
    Debug.consoleInfo("renderBlankLines = " + renderBlankLines);
  }

  private void initToolbar() {
    toolbar.setBgColor(Colors.toolbarBg);
//    toolbar.addButton("↓↓↓", Colors.toolbarText3, this::moveDown);
//    toolbar.addButton("↑↑↑", Colors.toolbarText3, this::moveUp);
//    toolbar.addButton("■", Colors.toolbarText3, this::stopMove);

    toolbar.addButton("C", Colors.toolbarText2, this::toggleContrast);
    toolbar.addButton("XO", Colors.toolbarText2, this::toggleXOffset);
    toolbar.addButton("DT", Colors.toolbarText2, this::toggleTails);
    toolbar.addButton("TE", Colors.toolbarText2, this::toggleTopEdit);
    toolbar.addButton("TB", Colors.toolbarText2, this::toggleTopBar);
    toolbar.addButton("A↑", Colors.toolbarText3, this::increaseFont);
    toolbar.addButton("A↓", Colors.toolbarText3, this::decreaseFont);
    toolbar.addButton("Segoe UI", Colors.rngToolButton(), this::setSegoeUI);
    toolbar.addButton("Verdana", Colors.rngToolButton(), this::setVerdana);
    toolbar.addButton("JetBrains Mono", Colors.rngToolButton(), this::setJetBrainsMono);
    toolbar.addButton("Consolas", Colors.rngToolButton(), this::setConsolas);

    toolbar.setFont(toolBarFont);
    V2i measure = toolbar.measure(g.mCanvas, devicePR);
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

  private void allocLines(int N) {
    System.out.println("allocLines:N = " + N);
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
    int editorRight = clientRect.x;
    return editorRight - vLineX;
  }

  int editorHeight() {
    return clientRect.y - footerHeight;
  }

  private void layout() {
    layoutFooter();
  }

  private void layoutFooter() {
    int editorHeight = editorHeight();
    footerRc.set(0, editorHeight, clientRect.x, footerHeight);
    footerRc.color.set(Colors.editFooterFill.v4f);
    vLineSize.y = editorHeight;
  }

  public boolean update(double timestamp) {
    int scrollSpeed = 1 + (scrollFaster ? 5 : 0) + (scrollEvenFaster ? 15 : 0);
    int vScrollPos = editorVScrollPos;
    editorVScrollPos = clampScrollPos(editorVScrollPos
        + (scrollDown ? scrollSpeed : 0) + (scrollUp ? -scrollSpeed : 0),
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

  // debug
  private int debugFL, debugLL;

  public void paint() {
    int editorBottom = editorHeight();
    int editorRight = clientRect.x;

    g.enableBlend(false);
    g.clear(bgColor);

    drawVerticalLine();
    editorVScrollPos = Math.min(editorVScrollPos, maxEditorVScrollPos());
    editorHScrollPos = Math.min(editorHScrollPos, maxEditorHScrollPos());

    int caretVerticalOffset = (lineHeight - caret.height()) / 2;
    int caretX = caretPos - caret.width() / 2 - editorHScrollPos;
    caret.setPosition(vLineX + caretX, caretVerticalOffset + caretLine * lineHeight - editorVScrollPos);

    if (lines != null) {
      int docLen = document.length();

      int firstLine = getFirstLine();
      int lastLine = getLastLine();

      if (firstLine != debugFL || lastLine != debugLL) {
        debugFL = firstLine;
        debugLL = lastLine;
//        JsHelper.consoleInfo("firstLine = " + firstLine + ", lastLine = " + lastLine);
      }

      for (int i = firstLine; i <= lastLine && i < docLen; i++) {
        CodeLine nextLine = document.line(i);
        CodeLineRenderer line = lineRenderer(i);
        line.updateTexture(nextLine, renderingCanvas, fonts, g, lineHeight, editorWidth(), editorHScrollPos);

        fullWidth = Math.max(fullWidth, nextLine.lineMeasure() + (int) (EditorConst.RIGHT_PADDING * devicePR));
      }

      for (int i = firstLine; i <= lastLine && i < docLen; i++) {
        int lineIndex = i % lines.length;
        int yPosition = lineHeight * i - editorVScrollPos;
        lines[lineIndex].draw(
            yPosition, vLineX, g, tRegion, size,
            applyContrast ? EditorConst.CONTRAST : 0, 
            editorWidth(), lineHeight, editorHScrollPos,
            colors.codeColors);
      }

      for (int i = firstLine; i <= lastLine && i < docLen && drawTails; i++) {
        CodeLineRenderer line = lineRenderer(i);
        int yPosition = lineHeight * i - editorVScrollPos;
        line.drawTail(g, vLineX, yPosition, lineHeight,
            size, editorHScrollPos, editorWidth(), colors.codeLineTailColor);
      }

      if (caretX >= -caret.width() / 2) caret.paint(g);

      // draw bottom 5 invisible lines
      if (renderBlankLines) {
        int nextLine = Math.min(lastLine + 1, docLen);
        int yPosition = lineHeight * nextLine - editorVScrollPos;
        drawDocumentBottom(editorBottom, editorRight, yPosition);
      }

      drawScrollBar();
      drawLineNumbers(editorBottom, firstLine, lastLine);
    }

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
    document.line(caretLine).invalidateCache();
    document.newLineOp(caretLine, caretCharPos);
    return setCaretLinePos(caretLine + 1, 0);
  }

  boolean handleDelete() {
    document.deleteChar(caretLine, caretCharPos);
    adjustEditorScrollToCaret();
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

    int editorHeight = editorHeight();
    boolean above = false; // caretLine * lineHeight - editorVScrollPos < editorHeight / 2;
    int posX = (vScroll.visible() ? vScroll.bgPos.x : clientRect.x) - 2 - size.x;
    int posY = above ? editorHeight - size.y - 1 : 1;
    toolbar.setPos(posX, posY);
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
    if (hScroll.visible()) {
      g.enableBlend(true);
      hScroll.draw(g);
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
    devicePR = api.window.devicePixelRatio();
    lineNumbers.setDevicePR(devicePR);
  }

  int clampScrollPos(int pos, int maxScrollPos) {
    return Math.min(Math.max(0, pos), maxScrollPos);
  }

  class MyInputListener implements InputListener {
    Consumer<V2i> dragLock;

    @Override
    public boolean onMouseWheel(MouseEvent event, double dX, double dY) {
      // chrome sends 150px, firefox send "6 lines"
      int changeY = Numbers.iRnd(lineHeight * 4 * dY / 150);
      int changeX = Numbers.iRnd(dX);
      editorVScrollPos = clampScrollPos(editorVScrollPos + changeY, maxEditorVScrollPos());
      editorHScrollPos = clampScrollPos(editorHScrollPos + changeX, maxEditorHScrollPos());
      return true;
    }

    Consumer<IntUnaryOperator> vScrollHandler =
        move -> editorVScrollPos = move.applyAsInt(maxEditorVScrollPos());

    Consumer<IntUnaryOperator> hScrollHandler =
      move -> editorHScrollPos = move.applyAsInt(maxEditorHScrollPos());

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

        dragLock = vScroll.onMouseClick(event.position, vScrollHandler, true);
        if (dragLock != null) return true;

        dragLock = hScroll.onMouseClick(event.position, hScrollHandler, false);
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

    final SetCursor setCursor = SetCursor.wrap(api.window);

    @Override
    public boolean onMouseMove(MouseEvent event) {
      if (dragLock != null) {
        dragLock.accept(event.position);
        return true;
      }

      if (toolbar.onMouseMove(event.position, setCursor)) return true;
      if (vScroll.onMouseMove(event.position, setCursor)) return true;
      if (hScroll.onMouseMove(event.position, setCursor)) return true;
      return setCursor.set(Cursor.text);
    }

    @Override
    public boolean onKey(KeyEvent event) {
      // do not consume browser keyboard to allow page reload and debug
      if (KeyEvent.isCopyPasteRelatedKey(event) || KeyEvent.isBrowserKey(event)) {
        return false;
      }
      // do not process release events
      if (!event.isPressed) return false;

      if (event.keyCode == KeyCode.F10) {
        api.window.addChild("child", DemoEdit::new);
      }

      if (handleDebug(event)) return true;
      if (handleNavigation(event)) return true;
      if (handleEditingKeys(event)) return true;

      if (1 < 0) Debug.consoleInfo("DemoEdit::onKey: "+ event.desc());

      if (event.ctrl && event.keyCode == KeyCode.W) {
        Debug.consoleInfo("Ctrl-W pressed ;)");
        return true;
      }

      if (event.ctrl || event.alt) return false;
      if (event.keyCode == KeyCode.ESC) return false;
      return event.key.length() > 0 && handleInsert(event.key);
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
      String string = caretCodeLine().makeString();
      setText.accept(string);
      if (isCut) {
        document.deleteLine(caretLine);
      }
      return true;
    }

    public Consumer<String> onPastePlainText() {
      return DemoEdit.this::handleInsert;
    }
  }
  private boolean arrowUpDown(int amount, boolean ctrl, boolean alt) {
    if (ctrl && alt) return true;
    if (ctrl) {  //  editorVScrollPos moves, caretLine does not change
      editorVScrollPos = clampScrollPos(editorVScrollPos + amount * lineHeight * 12 / 10, maxEditorVScrollPos());
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
    adjustEditorHScrollToCaret();
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
    caretPos = caretCodeLine().computePixelLocation(caretCharPos, g.mCanvas, fonts);
    caret.startDelay(api.window.timeNow());
    adjustEditorScrollToCaret();
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

  void onClickText(V2i position) {
    caretLine = Numbers.clamp(0,
        (position.y + editorVScrollPos) / lineHeight, document.length() - 1);

    CodeLine line = caretCodeLine();
    int documentXPosition = Math.max(0, position.x - vLineX + editorHScrollPos);
    caretCharPos = line.computeCaretLocation(documentXPosition, g.mCanvas, fonts);
    caretPos = line.computePixelLocation(caretCharPos, g.mCanvas, fonts);
    if (1<0) Debug.consoleInfo(
        "onClickText: caretCharPos = " + caretCharPos + ", caretPos = " + caretPos);
    caret.startDelay(api.window.timeNow());
    adjustEditorScrollToCaret();
  }

  CodeLine caretCodeLine() {
    return document.line(caretLine);
  }
}
