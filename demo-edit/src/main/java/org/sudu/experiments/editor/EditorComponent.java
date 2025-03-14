// todo: ctrl-left-right move by elements

package org.sudu.experiments.editor;

import org.sudu.experiments.*;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.EditorUi.FontApi;
import org.sudu.experiments.editor.ui.colors.CodeLineColorScheme;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.ui.colors.MergeButtonsColors;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.*;
import org.sudu.experiments.math.*;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.common.TriConsumer;
import org.sudu.experiments.text.SplitText;
import org.sudu.experiments.ui.Focusable;
import org.sudu.experiments.ui.ScrollBar;
import org.sudu.experiments.ui.SetCursor;
import org.sudu.experiments.ui.UiContext;
import org.sudu.experiments.ui.window.View;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EditorComponent extends View implements
    Focusable,
    MouseListener,
    FontApi,
    Model.EditorToModel,
    DiffRef
{

  final UiContext context;
  final WglGraphics g;
  final EditorUi ui;

  boolean forceMaxFPS = false;
  Runnable[] debugFlags = new Runnable[10];
  static final boolean dumpFontsOnResize = false;

  final Caret caret = new Caret();
  boolean hasFocus;

  float fontVirtualSize = EditorConst.DEFAULT_FONT_SIZE;
  String fontFamilyName = EditorConst.FONT;
  FontDesk font;
  final FontDesk[] fonts;
  int lineHeight, scrollBarWidth;

  Model model = new Model();
  EditorRegistrations registrations = new EditorRegistrations();
  EditorColorScheme colors;
  CodeLineColorScheme codeLineColors;
  MergeButtonsColors mbColors;

  // render cache
  CodeLineRenderer[] lines = new CodeLineRenderer[0];
  int firstLineRendered, lastLineRendered;

  // layout
  static final int vLineXDp = 80;
  static final int vLineWDp = 1;
  static final int vLineLeftDeltaDp = 10;
  static final int codeMapWidthDp = 15;
  static final float scrollBarWidthDp = 12;

  int vLineX;
  int vLineW;
  int vLineLeftDelta;
  int mergeWidth;

  V2i vLineSize = new V2i(1, 0);

  ScrollBar vScroll = new ScrollBar();
  ScrollBar hScroll = new ScrollBar();

  int fullWidth = 0;

  boolean renderBlankLines = true;
  int scrollDown, scrollUp;
  boolean drawTails = true;
  boolean drawGap = true;
  boolean printResolveTime = true;
  int xOffset = CodeLineRenderer.initialOffset;

  // line numbers
  LineNumbersComponent lineNumbers = new LineNumbersComponent();
  MergeButtons mergeButtons;
  //int lineNumLeftMargin = 10;

  String tabIndent = "  ";

  public boolean readonly = false;
  boolean mirrored = false;
  private ExternalHighlights externalHighlights;

  Consumer<String> onError = System.err::println;
  Runnable hScrollListener, vScrollListener;
  Consumer<EditorComponent> fullFileParseListener;
  TriConsumer<EditorComponent, Integer, Integer> iterativeParseFileListener;
  TriConsumer<EditorComponent, Diff, Boolean> updateModelOnDiffListener;
  Consumer<EditorComponent> onDiffMadeListener;
  int vScrollPos = 0;

  final ClrContext lrContext;
  InputListeners.KeyHandler onKey;

  GL.Texture codeMap;
  final V2i codeMapSize = new V2i();

  public EditorComponent(EditorUi ui) {
    this.context = ui.windowManager.uiContext;
    this.g = context.graphics;
    this.ui = ui;
    lrContext = new ClrContext(context.cleartype);
    fonts = lrContext.fonts;

    debugFlags[1] = this::toggleBlankLines;
    debugFlags[2] = this::toggleTails;
    debugFlags[3] = this::toggleXOffset;
    debugFlags[4] = this::toggleMirrored;
    debugFlags[5] = () -> drawGap = !drawGap;
    debugFlags[6] = () -> printResolveTime = !printResolveTime;

    model.setEditor(this, window().worker());
  }

  /*Disposable*/ void registerMouseScroll(InputListeners input) {
    InputListeners.ScrollHandler onScroll = this::onScroll;
    input.onMouse.add(this);
    input.onScroll.add(onScroll);
//    return () -> {
//      input.onMouse.remove(this);
//      input.onScroll.remove(onScroll);
//    };
  }

  @Override
  public void setPosition(V2i pos, V2i size, float dpr) {
    super.setPosition(pos, size, dpr);
    internalLayout();
  }

  @Override
  protected void onDprChange(float olDpr, float newDpr) {
    doChangeFont(fontFamilyName, fontVirtualSize);
    lrContext.setSinDpr(newDpr);
  }

  public void setScrollListeners(Runnable hListener, Runnable vListener) {
    hScrollListener = hListener;
    vScrollListener = vListener;
  }

  public void setFullFileParseListener(Consumer<EditorComponent> listener) {
    fullFileParseListener = listener;
  }

  public void setIterativeParseFileListener(TriConsumer<EditorComponent, Integer, Integer> listener) {
    iterativeParseFileListener = listener;
  }

  public void setUpdateModelOnDiffListener(TriConsumer<EditorComponent, Diff, Boolean> listener) {
    updateModelOnDiffListener = listener;
  }

  public void setOnDiffMadeListener(Consumer<EditorComponent> listener) {
    onDiffMadeListener = listener;
  }

  private void internalLayout() {
    boolean hasMerge = mergeButtons != null;
    mergeWidth = (hasMerge && !mirrored) ? mergeWidth() : 0;
    vLineX = toPx(vLineXDp) + mergeWidth;
    vLineW = toPx(vLineWDp);
    vLineLeftDelta = toPx(vLineLeftDeltaDp);
    scrollBarWidth = toPx(scrollBarWidthDp);

    int lineNumbersWidth = lineNumbersWidth();
    int lineNumbersX = mirrored ? pos.x + size.x - lineNumbersWidth : pos.x;

    lineNumbers.setPosition(lineNumbersX, pos.y,
        Math.min(lineNumbersWidth, size.x), size.y, dpr);
    if (hasMerge)
      layoutMergeButtons();

    if (dumpFontsOnResize) DebugHelper.dumpFontsSize(g);
    caret.setWidth(toPx(Caret.defaultWidth));

    codeMapSize.x = Math.min(size.x, toPx(codeMapWidthDp));
    codeMapSize.y = size.y;
    if (LineDiff.notEmpty(model.diffModel) && size.y > 0) {
      if (codeMap == null || codeMap.height() != size.y)
        buildDiffMap();
    }
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

  public boolean isFocused() {
    return hasFocus;
  }

  private void startBlinking() {
    caret.startDelay(window().timeNow());
  }

  private void undoLastDiff() {
    if (selection().isAreaSelected()) setSelectionToCaret();
    var caretDiff = model.document.undoLastDiff();
    if (caretDiff == null) return;
    setCaretLinePos(caretDiff.x, caretDiff.y, false);
    updateDocumentDiffTimeStamp();
    onDiffMade();
  }

  public void setTheme(EditorColorScheme theme) {
    colors = theme;
    codeLineColors = colors.editorCodeLineScheme();
    if (colors != null)
      mbColors = colors.codeDiffMergeButtons();
    caret.setColor(theme.editor.cursor);
    vScroll.setColor(theme.editor.scrollBarLine, theme.editor.scrollBarBg);
    hScroll.setColor(theme.editor.scrollBarLine, theme.editor.scrollBarBg);
    if (!theme.editorFont.equals(fontFamilyName, fontVirtualSize)) {
      changeFont(theme.editorFont.familyName, theme.editorFont.size);
    }
    if (codeMap != null)
      buildDiffMap();
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

  public void setMirrored(boolean b) {
    mirrored = b;
  }

  void toggleMirrored() {
    mirrored = !mirrored;
    lineNumbers.dispose();
    lineNumbers = new LineNumbersComponent();
    updateLineNumbersFont();
    if (mergeButtons != null && lineHeight != 0)
      setMergeButtonsFont();
    internalLayout();
  }

  private void toggleTopTextRenderMode() {
    CodeLineRenderer.useTop = !CodeLineRenderer.useTop;
    Debug.consoleInfo("CodeLineRenderer.useTop = " + CodeLineRenderer.useTop);
    invalidateFont();
  }

  public void increaseFont() {
    changeFont(lrContext.font.name, fontVirtualSize + 1);
  }

  public void decreaseFont() {
    if (fontVirtualSize <= EditorConst.MIN_FONT_SIZE) return;
    changeFont(lrContext.font.name, fontVirtualSize - 1);
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

  public float getFontVirtualSize() {
    return fontVirtualSize;
  }

  public String getFontFamily() {
    return fontFamilyName;
  }

  private void setFont(String name, float pixelSize) {
    lrContext.setFonts(name, pixelSize, g);
    font = lrContext.font;
    lineHeight = lrContext.setLineHeight(EditorConst.LINE_HEIGHT_MULTI, g);
    caret.setHeight(lrContext.font.caretHeight(lineHeight));

    Debug.consoleInfo("editor font: " + name + " " + pixelSize
//        + ", ascent+descent = " + lrContext.font.lineHeight()
        + ", lineHeight = " + lineHeight
        /* + ", caretHeight = " + caret.height() */ );

    if (CodeLineRenderer.useTop) {
      Debug.consoleInfo("font.topBase(lineHeight) = "
          + lrContext.font.topBase(lineHeight));
    }
  }

  public void changeFont(String name) {
    changeFont(name, getFontVirtualSize());
  }

  @Override
  public void setFontPow(float p) {
    context.setFontPow(p);
  }

  public void changeFont(String name, float virtualSize) {
    if (context.dpr != 0) {
      doChangeFont(name, virtualSize);
      window().repaint();
    }
    fontVirtualSize = virtualSize;
    fontFamilyName = name;
  }

  @Override
  public void onTextRenderingSettingsChange() {
    lrContext.enableCleartype(context.cleartype, g);
    CodeLineRenderer.makeContentDirty(lines);

    lineNumbers.dispose();
    if (mergeButtons != null) {
      mergeButtons.onTextRenderingSettingsChange();
    }
    updateLineNumbersFont();
  }

  private void doChangeFont(String name, float virtualSize) {
    float newPixelFontSize = virtualSize * dpr;
    float oldPixelFontSize = lrContext.fontSize();
    if (newPixelFontSize != oldPixelFontSize || !Objects.equals(name, fontFamilyName)) {
      lineNumbers.dispose();
      invalidateFont();
      setFont(name, newPixelFontSize);
      model.caretPos = caretCodeLine().computePixelLocation(model.caretCharPos, g.mCanvas, fonts);
      if (mergeButtons != null)
        setMergeButtonsFont();
      internalLayout();
      adjustEditorScrollToCaret();
      updateLineNumbersFont();
    }
  }

  private void invalidateFont() {
//    Debug.consoleInfo("invalidateFont");

    CodeLineRenderer.disposeLines(lines);
    model.document.invalidateFont();
  }

  public void dispose() {
    clearCodeMap();
    CodeLineRenderer.disposeLines(lines);
    lrContext.dispose();
    lineNumbers.dispose();
    mergeButtons = Disposable.assign(mergeButtons, null);
  }

  int editorVirtualHeight() {
    return (model.document.length() + EditorConst.BLANK_LINES) * lineHeight;
  }

  int maxVScrollPos() {
    return Math.max(editorVirtualHeight() - size.y, 0);
  }

  int maxHScrollPos() {
    return Math.max(fullWidth - editorWidth(), 0);
  }

  int editorWidth() {
    int t = mirrored ? scrollBarWidth + vLineLeftDelta : 0;
    return Math.max(1, size.x - vLineX - t);
  }

  int lineNumbersWidth() {
    return
        mirrored ? vLineX : vLineX - vLineLeftDelta - mergeWidth;
  }

  int editorHeight() {
    return size.y;
  }

  private int iterativeVersion;

  public boolean update(double timestamp) {
    if (model.document.needReparse(timestamp) && iterativeVersion != model.document.currentVersion) {
      iterativeVersion = model.document.currentVersion;
      iterativeParsing();
    }

    if (lineHeight != 0) parseViewport();

    int newVScrollPos = clampScrollPos(vScrollPos + scrollDown  -  scrollUp,
        maxVScrollPos());
    boolean scrollMoving = vScrollPos != newVScrollPos;
    if (scrollMoving) setScrollPosY(newVScrollPos);

    // repaint only if caret blinking
    // or animation in progress
    return caret.update(timestamp) || scrollMoving
        // || replaceCurrentLine
        || forceMaxFPS;
  }

  void setScrollPosX(int hPos) {
    if (setHScrollPosSilent(hPos) && hScrollListener != null) {
      hScrollListener.run();
    }
  }

  void setScrollPosY(int vPos) {
    if (setVScrollPosSilent(vPos) && vScrollListener != null) {
      vScrollListener.run();
    }
  }

  public boolean setHScrollPosSilent(int hPos) {
    int newHPos = clampScrollPos(hPos, maxHScrollPos());
    boolean change = newHPos != model.hScrollPos;
    if (change) model.hScrollPos = newHPos;
    return change;
  }

  public boolean setVScrollPosSilent(int vPos) {
    int newVPos = clampScrollPos(vPos, maxVScrollPos());
    boolean change = newVPos != vScrollPos;
    if (change) {
      vScrollPos = newVPos;
      model.vScrollLine = (double) vScrollPos / lineHeight;
    }
    return change;
  }

  public int getVScrollPos() {
    return vScrollPos;
  }

  @Override
  public V2i minimalSize() {
    return new V2i(lineNumbersWidth() + vLineW + vLineLeftDelta, lineHeight);
  }

  @Override
  public int lineHeight() { return lineHeight; }

  @Override
  public void draw(WglGraphics g) { paint(); }

  public void paint() {

    int cacheLines = Numbers.iDivRoundUp(size.y, lineHeight) + EditorConst.MIN_CACHE_LINES;
    if (lines.length < cacheLines) {
      lines = CodeLineRenderer.allocRenderLines(
          cacheLines, lines, lrContext,
          firstLineRendered, lastLineRendered, model.document);
    }

    g.enableBlend(false);
    g.enableScissor(pos, size);

    int caretVerticalOffset = (lineHeight - caret.height()) / 2;
    int caretX = model.caretPos - caret.width() / 2 - model.hScrollPos;
    int dCaret = mirrored ? vLineW + vLineLeftDelta + scrollBarWidth : vLineX;
    caret.setPosition(
        dCaret + caretX,
        caretVerticalOffset + model.caretLine * lineHeight - vScrollPos);
    int docLen = model.document.length();

    int firstLine = getFirstLine();
    int lastLine = getLastLine();

    firstLineRendered = firstLine;
    lastLineRendered = lastLine;

    int dx = mirrored
        ? pos.x + vLineW + vLineLeftDelta + scrollBarWidth
        : pos.x + vLineX;

    int editorWidth = editorWidth();
    LineDiff[] diffModel = model.diffModel;
    int rightPadding = toPx(EditorConst.RIGHT_PADDING);

    for (int i = firstLine; i <= lastLine && i < docLen; i++) {
      CodeLine cLine = model.document.line(i);
      CodeLineRenderer line = lineRenderer(i);

      int lineMeasure = line.updateTexture(
          cLine, g, lineHeight, editorWidth, model.hScrollPos,
          i, i % lines.length);

      fullWidth = Math.max(fullWidth, lineMeasure + rightPadding);

      int yPosition = lineHeight * i - vScrollPos;
      LineDiff diff = diffModel == null || i >= diffModel.length ? null : diffModel[i];
      line.draw(
          pos.y + yPosition, dx, g,
          editorWidth, lineHeight, model.hScrollPos,
          codeLineColors, getSelLineSegment(i, cLine),
          model.definition, model.usages,
          model.caretLine == i, null, null,
              diff);
    }

    V2i sizeTmp = context.v2i1;
    for (int i = firstLine; i <= lastLine && i < docLen && drawTails; i++) {
      CodeLineRenderer line = lineRenderer(i);
      int yPosition = lineHeight * i - vScrollPos;
      boolean isTailSelected = selection().isTailSelected(i);
      V4f tailColor = colors.editor.bg;
      boolean isCurrentLine = model.caretLine == i;

      if (isTailSelected) tailColor = colors.editor.selectionBg;
      else if (diffModel != null && i < diffModel.length && diffModel[i] != null && !diffModel[i].isDefault()) {
        tailColor = colors.codeDiffBg.getDiffColor(colors, diffModel[i].type);
      }
      else if (isCurrentLine) tailColor = colors.editor.currentLineBg;

      line.drawTail(g, dx, pos.y + yPosition, lineHeight,
          sizeTmp, model.hScrollPos, editorWidth, tailColor);
    }

    // draw bottom 5 invisible lines
    if (renderBlankLines) {
      int nextLine = Math.min(lastLine + 1, docLen);
      int yPosition = lineHeight * nextLine - vScrollPos;
      drawDocumentBottom(yPosition, editorWidth);
    }

    drawVerticalLine();

    if (!mirrored)
      drawGap(firstLine, lastLine, docLen);

    if (hasFocus && caretX >= -caret.width() / 2 && caret.needsPaint(size)) {
      caret.paint(g, pos);
    }

    if (codeMap != null)
      drawCodeMap();
    layoutScrollbar();
    drawScrollBar();

    g.disableScissor();

    drawLineNumbers(firstLine, lastLine);
    if (mergeButtons != null) {
      mergeButtons.setScrollPos(vScrollPos);
      mergeButtons.draw(
          firstLine, lastLine, model.caretLine,
          g, mbColors, lrContext, hasFocus);
    }

//    g.checkError("paint complete");
    if (0>1) {
      String s = "fullMeasure:" + CodeLine.cacheMiss + ", cacheHits: " + CodeLine.cacheHits;
      Debug.consoleInfo(s);
      CodeLine.cacheMiss = CodeLine.cacheHits = 0;
    }
  }

  private void drawCodeMap() {
    g.enableBlend(true);
    g.drawRect(codeMapX(), codeMapY(), codeMapSize, codeMap);
  }

  private int codeMapX() {
    return pos.x + (mirrored ? 0 : size.x - codeMapSize.x);
  }

  private int codeMapY() {
    return pos.y;
  }

  private void drawGap(int firstLine, int lastLine, int docLen) {
    LineDiff[] diffModel = model.diffModel;
    for (int i = firstLine; i <= lastLine && i < docLen; i++) {
      LineDiff currentLineModel = diffModel != null && i < diffModel.length
          ? diffModel[i]
          : null;

      if (model.caretLine == i || currentLineModel != null) {
        V4f gapColor =
            currentLineModel != null && currentLineModel.type != 0
                ? colors.codeDiffBg.getDiffColor(colors, currentLineModel.type)
                : colors.editor.currentLineBg;
        vLineSize.x = mirrored
            ? vLineLeftDelta + scrollBarWidth + vLineW - xOffset
            : vLineLeftDelta - vLineW - xOffset;
        vLineSize.y = lineHeight;
        int dx2 = mirrored ? 0 : vLineX - vLineLeftDelta + vLineW;
        int yPosition = lineHeight * i - vScrollPos;
        g.drawRect(pos.x + dx2,
            pos.y + yPosition,
            vLineSize,
            gapColor
        );
      }
    }
  }

  private V2i getSelLineSegment(int lineInd, CodeLine line) {
    V2i selLine = selection().getLine(lineInd);
    if (selLine != null) {
      if (selLine.y == -1) selLine.y = line.totalStrLength;
      selLine.x = line.computePixelLocation(selLine.x, g.mCanvas, fonts);
      selLine.y = line.computePixelLocation(selLine.y, g.mCanvas, fonts);
    }
    return selLine;
  }

  private void drawLineNumbers(int firstLine, int lastLine) {
    int editorBottom = size.y;
    int textHeight = Math.min(editorBottom, model.document.length() * lineHeight - vScrollPos);
    int caretLine = model.caretLine;
    lineNumbers.draw(textHeight, vScrollPos, firstLine, lastLine, caretLine, g, colors);
  }

  public int getFirstLine() {
    return Math.min(vScrollPos / lineHeight, model.document.length() - 1);
  }

  public int getLastLine() {
    return Math.min((vScrollPos + editorHeight() - 1) / lineHeight, model.document.length() - 1);
  }

  public int lineToPos(int line) {
    return lineHeight * line - vScrollPos + pos.y;
  }

  public V2i pos() { return pos; }
  public V2i size() { return size; }

  private void updateLineNumbersFont() {
    lineNumbers.setFont(lrContext.font, lineHeight, context.cleartype);
  }

  private CodeLineRenderer lineRenderer(int i) {
    return lines[i % lines.length];
  }

  boolean handleTab(boolean shiftPressed) {
    if (shiftPressed) return handleShiftTabOp();
    else return handleTabOp();
  }

  private boolean handleTabOp() {
    if (selection().isAreaSelected()) {
      Selection.SelPos left = selection().getLeftPos();
      Selection.SelPos right = selection().getRightPos();
      int size = right.line - left.line + 1;
      int[] lines = new int[size];
      String[] changes = new String[size];
      int i = 0;
      for (int l = left.line; l <= right.line; l++) {
        lines[i] = l;
        changes[i++] = tabIndent;
      }

      tabDiffHandler(lines, 0, false, changes, new Pos(model.caretLine, model.caretCharPos),
          (l, c) -> model.document.insertAt(l, 0, tabIndent)
      );
      left.charInd += tabIndent.length();
      right.charInd += tabIndent.length();
      setCaretPosWithSelection(model.caretCharPos + tabIndent.length());
      updateDocumentDiffTimeStamp();
    } else {
      handleInsert(tabIndent);
    }
    return true;
  }

  private boolean handleShiftTabOp() {
    if (selection().isAreaSelected()) {
      shiftTabSelection();
    } else {
      CodeLine codeLine = model.document.line(model.caretLine);
      if (codeLine.elements.length > 0) {
        String indent = calculateTabIndent(codeLine);
        if (indent == null) return true;
        model.document.makeDiffWithCaretReturn(
            model.caretLine, 0, true, indent, new Pos(model.caretLine, model.caretCharPos)
        );
        codeLine.delete(0, indent.length());
        setCaretPosWithSelection(model.caretCharPos - indent.length());
      }
    }
    updateDocumentDiffTimeStamp();
    return true;
  }

  private void shiftTabSelection() {
    Selection.SelPos left = selection().getLeftPos();
    Selection.SelPos right = selection().getRightPos();
    int initSize = right.line - left.line + 1;
    int[] lines = new int[initSize];
    String[] changes = new String[initSize];
    int prevCaretPos = model.caretCharPos;
    int prevCaretLine = model.caretLine;
    int size = 0;
    for (int l = left.line; l <= right.line; l++) {
      CodeLine codeLine = model.document.line(l);
      if (codeLine.elements.length > 0) {
        String indent = calculateTabIndent(codeLine);
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
        setCaretPosWithSelection(model.caretCharPos - indent.length());
      }
    }
    tabDiffHandler(lines, 0, true, changes, new Pos(prevCaretLine, prevCaretPos),
        (l, c) -> {
          CodeLine codeLine = model.document.line(l);
          codeLine.delete(0, c.length());
        }
    );
  }

  private String calculateTabIndent(CodeLine codeLine) {
    int count = Numbers.clamp(0, tabIndent.length(), codeLine.getBlankStartLength());
    return count == 0 ? null : " ".repeat(count);
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
    model.document.makeComplexDiff(
        lines,
        from,
        areDeletes,
        changes,
        caretPosition,
        editorAction
    );
  }

  boolean handleEnter() {
    if (selection().isAreaSelected()) deleteSelectedArea();
    model.document.line(model.caretLine).invalidateCache();
    model.document.newLineOp(model.caretLine, model.caretCharPos);
    updateDocumentDiffTimeStamp();
    return setCaretLinePos(model.caretLine + 1, 0, false);
  }

  boolean handleDelete() {
    if (selection().isAreaSelected()) deleteSelectedArea();
    else model.document.deleteChar(model.caretLine, model.caretCharPos);
    adjustEditorScrollToCaret();
    updateDocumentDiffTimeStamp();
    return true;
  }

  boolean handleBackspace() {
    if (selection().isAreaSelected()) {
      deleteSelectedArea();
      return true;
    } else {
      if (model.caretCharPos == 0 && model.caretLine == 0) return true;

      int cLine, cPos;
      if (model.caretCharPos == 0) {
        cLine = model.caretLine - 1;
        cPos = model.document.strLength(cLine);
        model.document.concatLines(cLine);
      } else {
        cLine = model.caretLine;
        cPos = model.caretCharPos - 1;
        model.document.deleteChar(cLine, cPos);
      }
      updateDocumentDiffTimeStamp();
      return setCaretLinePos(cLine, cPos, false);
    }
  }

  public boolean handleInsert(String s) {
    if (readonly) return false;
    if (selection().isAreaSelected()) deleteSelectedArea();
    String[] lines = SplitText.split(s);

    model.document.insertLines(model.caretLine, model.caretCharPos, lines);

    int newCaretLine = model.caretLine + lines.length - 1;
    int newCaretPos;
    if (newCaretLine == model.caretLine) newCaretPos = model.caretCharPos + lines[0].length();
    else newCaretPos = lines[lines.length - 1].length();

    setCaretLinePos(newCaretLine, newCaretPos, false);
    setSelectionToCaret();
    updateDocumentDiffTimeStamp();
    return true;
  }

  private void deleteSelectedArea() {
    var leftPos = selection().getLeftPos();
    model.document.deleteSelected(selection());
    setCaretLinePos(leftPos.line, leftPos.charInd, false);
    setSelectionToCaret();
    updateDocumentDiffTimeStamp();
  }

  private void setSelectionToCaret() {
    selection().isSelectionStarted = false;
    selection().startPos.set(model.caretLine, model.caretCharPos);
    selection().endPos.set(model.caretLine, model.caretCharPos);
  }

  private void drawDocumentBottom(int yPosition, int editorWidth) {
    if (yPosition < size.y) {
      V2i sizeTmp = context.v2i1;

      sizeTmp.y = size.y - yPosition;
      sizeTmp.x = mirrored ? editorWidth + vLineW : editorWidth + xOffset;
      int x = mirrored
          ? pos.x + vLineLeftDelta + scrollBarWidth + vLineW - xOffset
          : pos.x + vLineX - xOffset;
      g.drawRect(x, pos.y + yPosition, sizeTmp, colors.editor.bg);
    }
  }

  private void layoutScrollbar() {
    int x = mirrored ? pos.x + scrollBarWidth : pos.x + size.x;
    vScroll.layoutVertical(vScrollPos,
        pos.y,
        editorHeight(), editorVirtualHeight(),
        x, scrollBarWidth);
    x = mirrored ? pos.x + vLineW + vLineLeftDelta + scrollBarWidth : pos.x + vLineX;
    hScroll.layoutHorizontal(model.hScrollPos,
        x,
        editorWidth(), fullWidth,
        pos.y + editorHeight(), scrollBarWidth);
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
    vLineSize.y = size.y;
    vLineSize.x = vLineW;
    int drawLineX = mirrored
        ? size.x - lineNumbers.width() - vLineW
        : vLineX - vLineLeftDelta;
    g.drawRect(pos.x + drawLineX, pos.y, vLineSize, colors.editor.numbersVLine);
    vLineSize.x = mirrored
        ? vLineLeftDelta + scrollBarWidth + vLineW - xOffset
        : vLineLeftDelta - vLineW - xOffset;
    int dx2 = mirrored ? 0 : vLineX - vLineLeftDelta + vLineW;
    g.drawRect(pos.x + dx2, pos.y, vLineSize, colors.editor.bg);
  }

  static int clampScrollPos(int pos, int maxScrollPos) {
    return Math.min(Math.max(0, pos), maxScrollPos);
  }

  public void resolveAll() {
    model.resolveAll();
  }

  private Window window() { return context.window; }

  public void openFile(FileHandle f, Runnable onComplete) {
    Debug.consoleInfo("opening file " + f.getName());
    FileHandle.readTextFile(f,
        (source, encoding) -> {
          openFile(source, f.getFullPath(), encoding);
          onComplete.run();
        },
        System.err::println
    );
  }

  public void openFile(String source, String name, String encoding) {
    setCaretLinePos(0, 0, false);
    Model newModel = new Model(source, new Uri(name));
    newModel.setEncoding(encoding);
    setModel(newModel);
  }

  private void onNewModel() {
    externalHighlights = null;
    lineNumbers.setColors(null);
    if (mergeButtons != null)
      mergeButtons.setColors(lineNumbers.colors());
  }

  boolean arrowUpDown(int amount, boolean ctrl, boolean alt, boolean shiftPressed) {
    if (shiftSelection(shiftPressed)) return true;
    if (ctrl && alt) return true;
    if (ctrl) {  //  editorVScrollPos moves, caretLine does not change
      setScrollPosY(vScrollPos + amount * lineHeight * 12 / 10);
    } else if (alt) {
      // todo: smart move to prev/next method start
    } else {
      setCaretLine(model.caretLine + amount, shiftPressed);
      adjustEditorVScrollToCaret();
    }
    return true;
  }

  private boolean moveCaretLeftRight(int shift, boolean ctrl, boolean shiftPressed) {
    if (shiftSelection(shiftPressed)) return true;
    var caretCodeLine = caretCodeLine();
    int newPos = ctrl
            ? shift < 0
                ? caretCodeLine.prevPos(model.caretCharPos)
                : caretCodeLine.nextPos(model.caretCharPos)
            : model.caretCharPos + shift;

    if (newPos > caretCodeLine.totalStrLength) { // goto next line
      if (model.caretLine + 1 < model.document.length()) {
        setCaretLinePos(model.caretLine + 1, 0, shiftPressed);
      }
    } else if (newPos < 0) {  // goto prev line
      if (model.caretLine > 0) {
        int pos = model.document.line(model.caretLine - 1).totalStrLength;
        setCaretLinePos(model.caretLine - 1, pos, shiftPressed);
      }
    } else {
      setCaretPos(newPos, shiftPressed);
    }
    adjustEditorHScrollToCaret();
    return true;
  }

  private boolean shiftSelection(boolean shift) {
    if (selection().isAreaSelected() && !shift) {
      setSelectionToCaret();
      adjustEditorScrollToCaret();
      return true;
    }
    if (!shift || !selection().isAreaSelected()) setSelectionToCaret();
    return false;
  }

  private boolean setCaretLinePos(int line, int pos, boolean shift) {
    model.caretCharPos = pos;
    return setCaretLine(line, shift);
  }

  private boolean setCaretLine(int value, boolean shift) {
    model.caretLine = Numbers.clamp(0, value, model.document.length() - 1);
    return setCaretPos(model.caretCharPos, shift);
  }

  private boolean setCaretPos(int charPos, boolean shift) {
    model.caretCharPos = Numbers.clamp(0, charPos, caretCodeLine().totalStrLength);
    model.caretPos = dpr == 0 ? 0
        : caretCodeLine().computePixelLocation(model.caretCharPos, g.mCanvas, fonts);
    startBlinking();
    adjustEditorScrollToCaret();
    if (shift) selection().isSelectionStarted = true;
    selection().select(model.caretLine, model.caretCharPos);
    selection().isSelectionStarted = false;
    return true;
  }

  void setCaretPosWithSelection(int charPos) {
    Selection prevSelection = new Selection(selection());
    setCaretPos(charPos, false);
    selection().set(prevSelection);
  }

  private void adjustEditorScrollToCaret() {
    adjustEditorVScrollToCaret();
    adjustEditorHScrollToCaret();
  }

  private void adjustEditorVScrollToCaret() {
    int editVisibleYMin = vScrollPos;
    int editVisibleYMax = vScrollPos + editorHeight();
    int caretVisibleY0 = model.caretLine * lineHeight;
    int caretVisibleY1 = model.caretLine * lineHeight + lineHeight;

    if (caretVisibleY0 < editVisibleYMin + lineHeight) {
      setScrollPosY(caretVisibleY0 - lineHeight);
    } else if (caretVisibleY1 > editVisibleYMax - lineHeight) {
      setScrollPosY(caretVisibleY1 - editorHeight() + lineHeight);
    }
  }

  private void adjustEditorHScrollToCaret() {
    int xOffset = Numbers.iRnd(context.dpr * EditorConst.CARET_X_OFFSET);

    int editVisibleXMin = model.hScrollPos;
    int editVisibleXMax = model.hScrollPos + editorWidth();
    int caretVisibleX0 = model.caretPos;
    int caretVisibleX1 = model.caretPos + xOffset;

    if (caretVisibleX0 < editVisibleXMin + xOffset) {
      setScrollPosX(caretVisibleX0 - xOffset);
    } else if (caretVisibleX1 > editVisibleXMax - xOffset) {
      setScrollPosX(caretVisibleX1 - editorWidth() + xOffset);
    }
  }

  private void applyHighlights() {
    clearUsages();
    externalHighlights.buildUsages(model.document, model.usages);
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
    Pos startPos = model.document.getElementStart(pos.line, pos.pos);
    String elementName = getElementNameByStartPos(startPos);

    if (provider != null) {
      provider.provide(model, pos.line, pos.pos,
          (locs) -> gotoDefinition(position, locs, elementName), onError);
      return;
    }

    Pos def = model.document.usageToDef.get(startPos);
    if (def != null) {
      gotoUsage(def);
      return;
    }

    List<Pos> usages = model.document.defToUsages.get(startPos);
    if (usages == null || usages.isEmpty()) {
      ui.displayNoUsagesPopup(position);
    } else {
      ui.showUsagesWindow(position, usages, this, elementName);
    }
  }

  private void showUsagesViaLocations(V2i position, Location[] locs) {
    List<Pos> pos = new ArrayList<>();
    for (Location loc : locs) {
      pos.add(new Pos(loc.range.startLineNumber, loc.range.startColumn));
    }
    if (pos.isEmpty()) {
      ui.displayNoUsagesPopup(position);
    } else {
      Pos charPos = computeCharPos(position);
      Pos startPos = model.document.getElementStart(charPos.line, charPos.pos);
      ui.showUsagesWindow(position, locs, this, getElementNameByStartPos(startPos));
    }
  }

  public final void gotoUsage(Pos defPos) {
    setCaretLinePos(defPos.line, defPos.pos, false);
    int nextPos = caretCodeLine().nextPos(model.caretCharPos);
    selection().endPos.set(model.caretLine, nextPos);
    selection().startPos.set(model.caretLine, model.caretCharPos);
    model.computeUsages();
  }

  public void useDocumentHighlightProvider(int line, int column) {
    var p = registrations.findDocumentHighlightProvider(model.language(), model.uriScheme());
    if (p != null) {
      Model saveModel = model;
      p.provide(model, line, column,
          highlights -> setHighlights(saveModel, line, column, highlights),
          onError);
    }
  }

  void setHighlights(Model saveModel, int line, int column, DocumentHighlight[] highlights) {
    if (model != saveModel || model.caretLine != line || model.caretCharPos != column) return; // late reply
    externalHighlights = new ExternalHighlights(line, column, highlights);
    applyHighlights();
  }

  Pos computeCharPos(V2i eventPosition) {
    int localX = eventPosition.x - pos.x;
    int localY = eventPosition.y - pos.y;

    int line = Numbers.clamp(0, (localY + vScrollPos) / lineHeight, model.document.length() - 1);
    int offset = mirrored ? vLineW + vLineLeftDelta + scrollBarWidth : vLineX;
    int documentXPosition = Math.max(0, localX - offset + model.hScrollPos);
    int charPos = model.document.line(line).computeCharPos(documentXPosition, g.mCanvas, fonts);
    return new Pos(line, charPos);
  }

  private void dragText(MouseEvent event) {
    Pos pos = computeCharPos(event.position);
    moveCaret(pos);
    selection().select(model.caretLine, model.caretCharPos);
    adjustEditorScrollToCaret();
  }

  private void moveCaret(Pos pos) {
    model.caretLine = pos.line;
    model.caretCharPos = pos.pos;
    model.caretPos = model.document.line(pos.line)
        .computePixelLocation(model.caretCharPos, g.mCanvas, fonts);
    startBlinking();
  }

  private boolean gotoByLocalProvider(V2i position, Pos elementStart, String elementName) {
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
          ui.showUsagesWindow(position, usagesList, this, elementName);
          return true;
        }
      }
    }
    return false;
  }

  private void gotoDefinition(V2i position, Location[] locs, String elementName) {
    switch (locs.length) {
      case 0 -> ui.displayNoUsagesPopup(position);
      case 1 -> gotoDefinition(locs[0]);
      default -> ui.showUsagesWindow(position, locs, this, elementName);
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
    selection().startPos.set(range.startLineNumber, range.startColumn);
    selection().endPos.set(range.endLineNumber, range.endColumn);
  }

  void onClickText(MouseEvent event) {
    if (!event.ctrl) return;

    V2i eventPosition = event.position;
    Pos pos = computeCharPos(eventPosition);
    Pos startPos = model.document.getElementStart(pos.line, pos.pos);
    String elementName = getElementNameByStartPos(startPos);

    var provider = registrations.findDefinitionProvider(model.language(), model.uriScheme());
    if (provider != null) {
      provider.provide(model, pos.line, pos.pos,
          (locs) -> gotoDefinition(eventPosition, locs, elementName), onError);
    } else {
      // Default def provider
      if (gotoByLocalProvider(eventPosition, startPos, elementName)) return;
    }
  }

  void onDoubleClickText(V2i eventPosition) {
    Pos pos = computeCharPos(eventPosition);
    CodeLine line = codeLine(pos.line);
    int wordStart = line.getElementStart(model.caretCharPos);
    int wordEnd = line.nextPos(model.caretCharPos);
    CodeElement elem = line.getCodeElement(wordStart);

    // Select line without tale if double-clicking the end of the line
    if (wordEnd - 1 == line.totalStrLength) {
      selection().startPos.set(model.caretLine, line.getBlankStartLength());
      selection().endPos.set(model.caretLine, line.totalStrLength);
      return;
    }

    // Select adjacent CodeElements if one ' ', or the whole line
    if (elem != null && elem.s.isBlank()) {
      if (wordStart == model.caretCharPos) {
        wordStart = line.getElementStart(wordStart - 1);
        wordEnd = line.nextPos(wordStart);
      } else if (wordEnd == model.caretCharPos) {
        wordStart = line.getElementStart(wordEnd + 1);
        wordEnd = line.nextPos(wordStart);
      } else {
        selection().selectLine(model.caretLine);
        return;
      }
    }

    // Select CodeElement that holds the caret inside
    selection().startPos.set(model.caretLine, wordStart);
    selection().isSelectionStarted = true;
    setCaretLinePos(model.caretLine, wordEnd, false);
    selection().isSelectionStarted = false;
    saveToNavStack();
  }

  void onTripleClickText() {
    selection().selectLine(model.caretLine);
    model.navStack.pop();
    saveToNavStack();
  }

  CodeLine caretCodeLine() {
    return model.document.line(model.caretLine);
  }

  CodeLine codeLine(int n) {
    return model.document.line(n);
  }

  // InputListener methods

  Consumer<ScrollBar.Event> vScrollHandler =
      event -> setScrollPosY(event.getPosition(maxVScrollPos()));

  Consumer<ScrollBar.Event> hScrollHandler =
      event -> setScrollPosX(event.getPosition(maxHScrollPos()));


  @Override
  public boolean onScroll(MouseEvent event, float dX, float dY) {
    // chrome sends 150px, firefox send "6 lines"
    int changeY = Numbers.iRnd(lineHeight * 4 * dY / 150);
    int changeX = Numbers.iRnd(dX);
    if (changeY != 0) setScrollPosY(vScrollPos + changeY);
    if (changeX != 0) setScrollPosX(model.hScrollPos + changeX);
    return true;
  }

  public boolean onMouseUp(MouseEvent event, int button) {
    if (mergeButtons != null && mergeButtons.onMouseUp(event, button))
      return true;
    selection().isSelectionStarted = false;
    return true;
  }

  public Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    if (!context.isFocused(this))
      context.setFocus(this);

    if (button == MOUSE_BUTTON_LEFT) {
      if (mergeButtons != null) {
        var lock = mergeButtons.onMouseDown(event, button, context.windowCursor);
        if (lock != null) return lock;
      }

      V2i mousePos = event.position;
      int codeMapLine = hitTestFindDiff(mousePos);

      // when clicked to a change on codeMap -> forward to vScroll only if hit button
      if (codeMapLine < 0 || vScroll.hitButton(mousePos)) {
        var lock = vScroll.onMouseDown(mousePos, vScrollHandler, true);
        if (lock != null) return lock;
      }

      // when clicked to a change on codeMap -> forward to hScroll only if hit button
      if (codeMapLine < 0 || hScroll.hitButton(mousePos)) {
        var lock = hScroll.onMouseDown(mousePos, hScrollHandler, false);
        if (lock != null) return lock;
      }

      if (codeMapLine >= 0) {
        revealLineInCenter(codeMapLine);
        return MouseListener.Static.emptyConsumer;
      }

      if (lineNumbers.hitTest(mousePos))
        return MouseListener.Static.emptyConsumer;

      saveToNavStack();
      V2i eventPosition = mousePos;
      Pos pos = computeCharPos(eventPosition);

      moveCaret(pos);
      model.computeUsages();

      if (!event.shift && !selection().isSelectionStarted) {
        selection().startPos.set(model.caretLine, model.caretCharPos);
      }

      selection().isSelectionStarted = true;
      selection().select(model.caretLine, model.caretCharPos);
      return this::dragText;
    }
    return null;
  }

  private boolean hitMergeButtons(V2i pos) {
    return mergeButtons != null && mergeButtons.hitTest(pos);
  }

  public boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    if (button == MOUSE_BUTTON_LEFT) {
      if (lineNumbers.hitTest(event.position)
          || hitMergeButtons(event.position))
        return true;
      switch (clickCount) {
        case 1 -> onClickText(event);
        case 2 -> onDoubleClickText(event.position);
        case 3 -> onTripleClickText();
      }
    }
    return true;
  }

  @Override
  public boolean onMouseMove(MouseEvent event) {
    onMouseMove(event, context.windowCursor);
    return false;
  }

  @Override
  protected void onMouseLeaveWindow() {
    if (mergeButtons != null)
      mergeButtons.onMouseLeave();
//     lineNumbers.onMouseLeave();
  }

  private int hitTestFindDiff(V2i mousePos) {
    return codeMap == null || model.diffModel == null ? -1 :
        DiffImage.findDiff(
            mousePos, codeMapX(), codeMapY(), codeMapSize,
            model.document.length(), model.diffModel);
  }

  boolean onMouseMoveCodeMap(V2i mousePos, SetCursor setCursor) {
    return hitTestFindDiff(mousePos) >= 0 && setCursor.set(Cursor.pointer);
  }

  public void onMouseMove(MouseEvent event, SetCursor setCursor) {
    V2i mousePos = event.position;

    var codeMap = onMouseMoveCodeMap(mousePos, setCursor);
    var scroll = !codeMap && (
        vScroll.onMouseMove(mousePos, setCursor) |
            hScroll.onMouseMove(mousePos, setCursor));

    if (scroll || codeMap) {
      onMouseLeaveWindow();
    } else {
      var mb = mergeButtons != null
          && mergeButtons.onMouseMove(event, setCursor)
          || lineNumbers.onMouseMove(event, setCursor);

      if (!mb && hitTest(mousePos)) {
        if (isInsideText(mousePos)) {
          if (event.ctrl) {
            Pos pos = computeCharPos(mousePos);
            model.document.moveToElementStart(pos);
            boolean hasUsage = model.document.hasDefOrUsagesForElementPos(pos);
            setCursor.set(hasUsage ? Cursor.pointer : Cursor.text);
          } else {
            setCursor.set(Cursor.text);
          }
        } else {
          setCursor.setDefault();
        }
      }
    }
  }

  public void onKey(InputListeners.KeyHandler onKey) {
    this.onKey = onKey;
  }

  public boolean onKeyPress(KeyEvent event) {
//    Debug.consoleInfo("EditorComponent::onKey: "+ event.toString());
    if (onKey != null) {
      if (onKey.onKeyPress(event)) return true;
      if (event.prevented) return false;
    }

    if (event.ctrl && event.keyCode == KeyCode.A) return selectAll();

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

    if (KeyCode.isFKey(event.keyCode) || event.keyCode == KeyCode.ESC) return false;
    if (event.ctrl || event.alt || event.meta) return false;
    return event.key.length() > 0 && handleInsert(event.key);
  }

  void debugPrintDocumentIntervals() {
    model.document.printIntervals();
  }

  void parseViewport() {
    model.parseViewport(getFirstLine(), getLastLine());
  }

  public void parseFullFile() {
    model.parseFullFile();
  }

  public void iterativeParsing() {
    model.iterativeParsing();
  }

  public boolean onCopy(Consumer<String> setText, boolean isCut) {
    if (isCut && readonly) return false;
    var left = selection().getLeftPos();
    int line = left.line;
    String result;

    if (!selection().isAreaSelected()) {
      result = model.document.copyLine(line);
      int newLine = Math.min(model.document.length() - 1, line);

      selection().endPos.set(newLine, 0);
      if (line < model.document.length() - 1)
        selection().startPos.set(newLine + 1, 0);
      else
        selection().endPos.set(newLine, model.document.strLength(newLine));

      if (isCut) deleteSelectedArea();
      else setCaretLinePos(line, 0, false);
    } else {
      result = model.document.copy(selection(), isCut);
      if (isCut) {
        setCaretLinePos(left.line, left.charInd, false);
        setSelectionToCaret();
        updateDocumentDiffTimeStamp();
      }
    }

    setText.accept(result);
    return true;
  }

  @Override
  public Consumer<String> onPaste() {
    return this::handleInsert;
  }

  private boolean isInsideText(V2i position) {
    int dx = mirrored ? vLineLeftDelta + vLineW + scrollBarWidth : vLineX;
    return Rect.isInside(position,
        pos.x + dx, pos.y,
        editorWidth(), editorHeight());
  }

  private boolean handleSpecialKeys(KeyEvent event) {
//    if (KeyCode.F1 <= event.keyCode && event.keyCode <= KeyCode.F12) return true;
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
      case KeyCode.TAB -> handleTab(event.shift);
      case KeyCode.ENTER -> handleEnter();
      case KeyCode.DELETE -> handleDelete();
      case KeyCode.BACKSPACE -> handleBackspace();
      default -> false;
    };
  }

  private boolean handleNavigation(KeyEvent event) {
    boolean result = switch (event.keyCode) {
      case KeyCode.ARROW_UP ->
          arrowUpDown(-1, event.ctrl, event.alt, event.shift);
      case KeyCode.ARROW_DOWN ->
          arrowUpDown(1, event.ctrl, event.alt, event.shift);
      case KeyCode.PAGE_UP -> pgUp(event);
      case KeyCode.PAGE_DOWN -> pgDown(event);
      case KeyCode.ARROW_LEFT ->
          event.ctrl && event.alt ? navigateBack() :
              moveCaretLeftRight(-1, event.ctrl, event.shift);
      case KeyCode.ARROW_RIGHT ->
          event.ctrl && event.alt ? navigateForward() :
              moveCaretLeftRight(1, event.ctrl, event.shift);
      case KeyCode.HOME ->
          shiftSelection(event.shift)
              || setCaretPos(0, event.shift);
      case KeyCode.END -> shiftSelection(event.shift) ||
          setCaretPos(caretCodeLine().totalStrLength, event.shift);
      default -> false;
    };
    if (result && event.shift)
      selection().endPos.set(model.caretLine, model.caretCharPos);
    if (result) model.computeUsages();
    return result;
  }

  void saveToNavStack() {
    NavigationContext curr = model.navStack.getCurrentCtx();
    if (curr != null && model.caretLine == curr.getLine() && model.caretCharPos == curr.getCharPos()) {
      return;
    }
    model.navStack.add(new NavigationContext(
        model.caretLine,
        model.caretCharPos,
        selection()
    ));
  }

  boolean navigateBack() {
    saveToNavStack();
    NavigationContext prev = model.navStack.getPrevCtx();
    if (prev == null) return true;
    setCaretLinePos(prev.getLine(), prev.getCharPos(), false);
    selection().set(prev.getSelection());
    return true;
  }

  boolean navigateForward() {
    NavigationContext curr = model.navStack.getNextCtx();
    if (curr == null) return true;
    setCaretLinePos(curr.getLine(), curr.getCharPos(), false);
    selection().set(curr.getSelection());
    return true;
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
      setCaretPos(model.caretCharPos - 1, false);
      return true;
    }
    if (event.key.equals("(")) {
      handleInsert("()");
      setCaretPos(model.caretCharPos - 1, false);
      return true;
    }
    if (event.key.equals("[")) {
      handleInsert("[]");
      setCaretPos(model.caretCharPos - 1, false);
      return true;
    }
    if (event.key.equals("<")) {
      handleInsert("<>");
      setCaretPos(model.caretCharPos - 1, false);
      return true;
    }
    if (event.key.equals("\"")) {
      handleInsert("\"\"");
      setCaretPos(model.caretCharPos - 1, false);
      return true;
    }
    if (event.key.equals("'")) {
      handleInsert("''");
      setCaretPos(model.caretCharPos - 1, false);
      return true;
    }
    return false;
  }

  public boolean selectAll() {
    int line = model.document.length() - 1;
    int charInd = model.document.strLength(line);
    selection().startPos.set(0, 0);
    selection().endPos.set(model.document.length() - 1, charInd);
    return true;
  }

  private void updateDocumentDiffTimeStamp() {
    model.document.setLastDiffTimestamp(window().timeNow());
  }

  public void setPosition(int column, int lineNumber) {
    setCaretLinePos(lineNumber, column, false);
  }

  public int caretLine() { return model.caretLine; }
  public int caretCharPos() { return model.caretCharPos; }

  public void setSelection(
      int endColumn,
      int endLineNumber,
      int startColumn,
      int startLineNumber
  ) {
    selection().getLeftPos().set(startLineNumber,startColumn);
    selection().getRightPos().set(endLineNumber, endColumn);
  }

  public EditorRegistrations registrations() { return registrations; }

  public void setModel(Model model) {
    // todo: remove model.clearUsages() from  onNewModel
    onNewModel();

    Model oldModel = this.model;
    this.model = model;
    oldModel.setEditor(null, null);
    model.setEditor(this, window().worker());
    registrations.fireModelChange(oldModel, model);
    vScrollPos = Numbers.iRnd(model.vScrollLine * lineHeight);
  }

  private void clearUsages() {
    model.clearUsages();
  }

  public Model model() { return model; }

  public void setLanguage(String language) {
    model.setLanguage(language);
    model.parseFullFile();
  }

  public void revealLineInCenter(int lineNumber) {
    if (lineNumber <= 0) return;
    int computed = lineHeight * (lineNumber - (editorHeight() / (lineHeight * 2)) - 1);
    setScrollPosY(computed);
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
    setScrollPosY(lineNumber > model.document.length()
        ? maxVScrollPos()
        : (lineNumber + 1) * lineHeight - editorHeight());
  }

  private void scrollUpToLine(int lineNumber) {
    setScrollPosY((lineNumber - 2) * lineHeight);
  }

  public char[] getChars() {
    return model.document.getChars();
  }

  private String getElementNameByStartPos(Pos startPos) {
    CodeElement codeElement = model.document.getCodeElement(startPos);
    if (codeElement != null) return codeElement.s;
    return "";
  }

  public void setDiffModel(LineDiff[] lineDiffs) {
    model.diffModel = lineDiffs;
    // todo: we can improve this by adding shareble
    // diff map between LineNumberComponent and codeMapTexture
    updateLineNumbersColors();
    if (LineDiff.notEmpty(lineDiffs)) {
      if (size.y > 0)
        buildDiffMap();
    } else {
      clearCodeMap();
    }
  }

  public void updateLineNumbersColors() {
    if (LineDiff.notEmpty(model.diffModel)) {
      byte[] c = new byte[model.diffModel.length];
      for (int i = 0; i < c.length; i++) {
        LineDiff ld = model.diffModel[i];
        c[i] = ld != null ? (byte) ld.type : 0;
      }
      lineNumbers.setColors(c);
      if (mergeButtons != null) mergeButtons.setColors(c);
    } else {
      lineNumbers.setColors(null);
      if (mergeButtons != null) mergeButtons.setColors(lineNumbers.colors());
    }
  }

  public void highlightResolveError(boolean highlight) {
    model.highlightResolveError = highlight;
  }

  public LineDiff[] diffModel() {
    return model.diffModel;
  }

  public Selection selection() {
    return model.selection;
  }

  public void fireFullFileParsed() {
    if (fullFileParseListener != null) {
      fullFileParseListener.accept(this);
    }
    window().repaint();
  }

  @Override
  public void fireFileIterativeParsed(int start, int stop) {
    if (iterativeParseFileListener != null) {
      iterativeParseFileListener.accept(this, start, stop);
    }
    window().repaint();
  }

  @Override
  public void updateModelOnDiff(Diff diff, boolean isUndo) {
    if (updateModelOnDiffListener != null) {
      updateModelOnDiffListener.accept(this, diff, isUndo);
    }
    window().repaint();
  }

  public void onDiffMade() {
    if (onDiffMadeListener != null) {
      onDiffMadeListener.accept(this);
    }
    window().repaint();
  }

  @Override
  public String toString() {
    Uri uri = model().uri;
    String s = super.toString();
    return uri != null ? s + " - " + uri.path : s;
  }

  private void setMergeButtonsFont() {
    mergeButtons.setFont(lineHeight, !mirrored, fonts[CodeElement.bold]);
  }

  private void layoutMergeButtons() {
    int x = mirrored ? lineNumbers.pos.x
        : lineNumbers.pos.x + lineNumbers.size.x;
    int mWidth = mergeWidth();
    mergeButtons.setPosition(x, lineNumbers.pos.y, mWidth, lineNumbers.size.y, dpr);
    mergeButtons.setScrollPos(vScrollPos);
  }

  private int mergeWidth() {
    return mergeButtons.measure(fonts[CodeElement.bold], g.mCanvas, dpr);
  }

  public void setMergeButtons(Runnable[] actions, int[] lines) {
     if (mergeButtons == null) {
       mergeButtons = new MergeButtons();
       if (colors != null)
         mbColors = colors.codeDiffMergeButtons();
       if (dpr != 0) {
         setMergeButtonsFont();
         internalLayout();
       }
     }
     mergeButtons.setModel(actions, lines);
     mergeButtons.setColors(lineNumbers.colors());
  }

  void buildDiffMap() {
    if (codeMap == null)
      codeMap = g.createTexture();
    var img = DiffImage.diffImage(model.diffModel, size.y, colors.codeMapBg);
    codeMap.setContent(img);
  }

  void clearCodeMap() {
    if (codeMap != null)
      codeMap = Disposable.assign(codeMap, null);
  }

  public void setCodeMap() {
    // todo: later...
  }
}
