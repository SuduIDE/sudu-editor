// todo: highlight current line
// todo: ctrl-left-right move by elements

package org.sudu.experiments.editor;

import org.sudu.experiments.*;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.EditorUi.FontApi;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.parser.*;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.*;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.common.graph.ScopeGraph;
import org.sudu.experiments.parser.common.graph.writer.ScopeGraphWriter;
import org.sudu.experiments.ui.Focusable;
import org.sudu.experiments.ui.ScrollBar;
import org.sudu.experiments.ui.SetCursor;
import org.sudu.experiments.ui.UiContext;
import org.sudu.experiments.worker.ArrayView;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class EditorComponent implements Focusable, MouseListener, FontApi {
  final V2i pos = new V2i();
  final V2i size = new V2i();

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
  LineDiff[] diffModel;
  EditorRegistrations registrations = new EditorRegistrations();
  Selection selection = new Selection();
  NavigationStack navStack = new NavigationStack();

  EditorColorScheme colors;

  Canvas renderingCanvas;

  // render cache
  CodeLineRenderer[] lines = new CodeLineRenderer[0];
  int firstLineRendered, lastLineRendered;

  // layout
  static final int vLineXDp = 80;
  int vLineX;
  int vLineW;
  static final int vLineWDp = 1;
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
  boolean drawGap = true;
  int xOffset = CodeLineRenderer.initialOffset;

  // line numbers
  LineNumbersComponent lineNumbers = new LineNumbersComponent();
  //int lineNumLeftMargin = 10;

  boolean fileStructureParsed, firstLinesParsed;
  String tabIndent = "  ";

  public boolean readonly = false;
  boolean mirrored = false;

  private CodeElement definition = null;
  private final List<CodeElement> usages = new ArrayList<>();
  private ExternalHighlights externalHighlights;

  Consumer<String> onError = System.err::println;
  IntConsumer hScrollListener, vScrollListener;
  Consumer<EditorComponent> fullFileParseListener;

  public EditorComponent(UiContext context, EditorUi ui) {
    this.context = context;
    this.g = context.graphics;
    this.ui = ui;

    debugFlags[0] = this::toggleContrast;
    debugFlags[1] = this::toggleBlankLines;
    debugFlags[2] = this::toggleTails;
    debugFlags[3] = this::toggleXOffset;
    debugFlags[4] = this::toggleMirrored;
    debugFlags[5] = () -> drawGap = !drawGap;

    // d2d is very bold, contrast makes font heavier
    applyContrast = context.window.getHost() != Host.Direct2D;
  }

  void setPos(V2i pos, V2i size, float dpr) {
    this.pos.set(pos);
    this.size.set(size);

    internalLayout(pos, size, dpr);
  }

  void setScrollListeners(IntConsumer hListener, IntConsumer vListener) {
    hScrollListener = hListener;
    vScrollListener = vListener;
  }

  void setFullFileParseListener(Consumer<EditorComponent> listener) {
    fullFileParseListener = listener;
  }

  private void internalLayout(V2i pos, V2i size, float dpr) {
    vLineX = DprUtil.toPx(vLineXDp, dpr);
    vLineW = DprUtil.toPx(vLineWDp, dpr);
    vLineLeftDelta = DprUtil.toPx(10, dpr);

    if (mirrored) {
      context.v2i1.set(pos.x + size.x - lineNumbersWidth(), pos.y);
    } else context.v2i1.set(this.pos);
    lineNumbers.setPos(context.v2i1, lineNumbersWidth(), size.y, dpr);

    if (1<0) DebugHelper.dumpFontsSize(g);
    caret.setWidth(DprUtil.toPx(Caret.defaultWidth, dpr));

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
    caret.setColor(theme.editor.cursor);
    vScroll.setColor(theme.editor.scrollBarLine, theme.editor.scrollBarBg);
    hScroll.setColor(theme.editor.scrollBarLine, theme.editor.scrollBarBg);
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

  void setMirrored(boolean b) {
    mirrored = b;
  }

  void toggleMirrored() {
    mirrored = !mirrored;
    lineNumbers.dispose();
    lineNumbers = new LineNumbersComponent();
    internalLayout(pos, size, context.dpr);
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
    int newPixelFontSize = DprUtil.toPx(virtualSize, context.dpr);
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
    return Math.max(editorVirtualHeight() - size.y, 0);
  }

  int maxHScrollPos() {
    return Math.max(fullWidth - editorWidth(), 0);
  }

  int editorWidth() {
    int t = mirrored ? scrollBarWidth() + vLineLeftDelta : 0;
    return Math.max(1, size.x - vLineX - t);
  }

  int lineNumbersWidth() {
    return mirrored ? vLineX : vLineX - vLineLeftDelta;
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

    int newVScrollPos = clampScrollPos(vScrollPos + scrollDown  -  scrollUp,
        maxVScrollPos());
    boolean scrollMoving = vScrollPos != newVScrollPos;
    if (scrollMoving) setVScrollPos(newVScrollPos);

    // repaint only if caret blinking
    // or animation in progress
    return caret.update(timestamp) || scrollMoving
        // || replaceCurrentLine
        || forceMaxFPS;
  }

  void setVScrollPos(int vPos) {
    int newVPos = clampScrollPos(vPos, maxVScrollPos());
    if (newVPos != vScrollPos) {
      vScrollPos = newVPos;
      if (vScrollListener != null) vScrollListener.accept(newVPos);
    }
  }

  void setVScrollPosSilent(int vPos) {
    vScrollPos = clampScrollPos(vPos, maxVScrollPos());
  }

  void setHScrollPos(int hPos) {
    int newHPos = clampScrollPos(hPos, maxHScrollPos());
    if (newHPos != hScrollPos) {
      hScrollPos = newHPos;
      if (hScrollListener != null) hScrollListener.accept(newHPos);
    }
  }

  void setHScrollPosSilent(int hPos) {
    hScrollPos = clampScrollPos(hPos, maxHScrollPos());
  }

  // temp vars
  private final V4f tRegion = new V4f();


  public void paint() {

    int cacheLines = Numbers.iDivRoundUp(size.y, lineHeight) + EditorConst.MIN_CACHE_LINES;
    if (lines.length < cacheLines) {
      lines = CodeLineRenderer.reallocRenderLines(cacheLines, lines, firstLineRendered, lastLineRendered, model.document);
    }

    g.enableBlend(false);
    g.enableScissor(pos, size);

//    vScrollPos = Math.min(vScrollPos, maxVScrollPos());
//    hScrollPos = Math.min(hScrollPos, maxHScrollPos());

    int caretVerticalOffset = (lineHeight - caret.height()) / 2;
    int caretX = caretPos - caret.width() / 2 - hScrollPos;
    int dCaret = mirrored ? vLineW + vLineLeftDelta + scrollBarWidth() : vLineX;
    caret.setPosition(dCaret + caretX, caretVerticalOffset + caretLine * lineHeight - vScrollPos);
    int docLen = model.document.length();

    int firstLine = getFirstLine();
    int lastLine = getLastLine();

    firstLineRendered = firstLine;
    lastLineRendered = lastLine;

    V2i sizeTmp = context.v2i1;
      sizeTmp.set(editorWidth(), lineHeight);
    int dx = mirrored
        ? pos.x + vLineW + vLineLeftDelta + scrollBarWidth()
        : pos.x + vLineX;

    for (int i = firstLine; i <= lastLine && i < docLen; i++) {
      CodeLine nextLine = model.document.line(i);
      CodeLineRenderer line = lineRenderer(i);
      line.updateTexture(nextLine, renderingCanvas, fonts, g, lineHeight, editorWidth(), hScrollPos);
      CodeLine lineContent = line.line;

      fullWidth = Math.max(fullWidth, nextLine.lineMeasure() + (int) (EditorConst.RIGHT_PADDING * context.dpr));
      int yPosition = lineHeight * i - vScrollPos;

      LineDiff diff = diffModel == null ? null : diffModel[i];
      line.draw(
          pos.y + yPosition, dx, g, tRegion, sizeTmp,
          applyContrast ? EditorConst.CONTRAST : 0,
          editorWidth(), lineHeight, hScrollPos,
          colors, getSelLineSegment(i, lineContent),
          definition, usages, caretLine == i, diffModel != null, diff);
    }

    for (int i = firstLine; i <= lastLine && i < docLen && drawTails; i++) {
      CodeLineRenderer line = lineRenderer(i);
      int yPosition = lineHeight * i - vScrollPos;
      boolean isTailSelected = selection.isTailSelected(i);
      Color tailColor = colors.editor.lineTailContent;
      boolean isCurrentLine = caretLine == i && diffModel == null;

      if (isTailSelected) tailColor = colors.editor.selectionBg;
      else if (diffModel != null && i < diffModel.length && diffModel[i] != null) {
        tailColor = (Color) colors.diff.getDiffColor(colors, diffModel[i].type);
      }
      else if (isCurrentLine) tailColor = colors.editor.currentLineBg;

      line.drawTail(g, dx, pos.y + yPosition, lineHeight,
          sizeTmp, hScrollPos, editorWidth(), tailColor);
    }

    // draw bottom 5 invisible lines
    if (renderBlankLines) {
      int nextLine = Math.min(lastLine + 1, docLen);
      int yPosition = lineHeight * nextLine - vScrollPos;
      drawDocumentBottom(yPosition);
    }

    drawVerticalLine();
    drawLineNumbers(firstLine, lastLine);

    if (drawGap) drawGap(firstLine, lastLine, docLen);

    if (hasFocus && caretX >= -caret.width() / 2 && caret.needsPaint(size)) {
      caret.paint(g, pos);
    }

    layoutScrollbar();
    drawScrollBar();

    g.disableScissor();

//    g.checkError("paint complete");
    if (0>1) {
      String s = "fullMeasure:" + CodeLine.cacheMiss + ", cacheHits: " + CodeLine.cacheHits;
      Debug.consoleInfo(s);
      CodeLine.cacheMiss = CodeLine.cacheHits = 0;
    }
  }

  private void drawGap(int firstLine, int lastLine, int docLen) {
    for (int i = firstLine; i <= lastLine && i < docLen; i++) {
      LineDiff currentLineModel = diffModel != null && i < diffModel.length
          ? diffModel[i]
          : null;
      V4f gapColor = currentLineModel != null
          ? colors.diff.getDiffColor(colors, currentLineModel.type)
          : diffModel == null
          ? colors.editor.currentLineBg
          : colors.editor.bg;

      if (caretLine == i || currentLineModel != null) {
        vLineSize.x = mirrored
            ? vLineLeftDelta + scrollBarWidth() + vLineW - xOffset
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
    V2i selLine = selection.getLine(lineInd);
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

    lineNumbers.draw(editorBottom, textHeight, vScrollPos, firstLine, lastLine,
        diffModel != null ? -1 : caretLine, g, colors);
  }

  int getFirstLine() {
    return Math.min(vScrollPos / lineHeight, model.document.length() - 1);
  }

  int getLastLine() {
    return Math.min((vScrollPos + editorHeight() - 1) / lineHeight, model.document.length() - 1);
  }

  private void updateLineNumbersFont() {
    lineNumbers.setFont(fonts[0], lineHeight, g);
    lineNumbers.initTextures(g, getFirstLine(), editorHeight());
  }

  private CodeLineRenderer lineRenderer(int i) {
    return lines[i % lines.length];
  }

  boolean handleTab(boolean shiftPressed) {
    if (shiftPressed) return handleShiftTabOp();
    else return handleTabOp();
  }

  private boolean handleTabOp() {
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

      tabDiffHandler(lines, 0, false, changes, new Pos(caretLine, caretCharPos),
          (l, c) -> model.document.insertAt(l, 0, tabIndent)
      );
      left.charInd += tabIndent.length();
      right.charInd += tabIndent.length();
      setCaretPosWithSelection(caretCharPos + tabIndent.length(), false);
      updateDocumentDiffTimeStamp();
    } else {
      handleInsert(tabIndent);
    }
    return true;
  }

  private boolean handleShiftTabOp() {
    if (selection.isAreaSelected()) {
      shiftTabSelection();
    } else {
      CodeLine codeLine = model.document.line(caretLine);
      if (codeLine.elements.length > 0) {
        String indent = calculateTabIndent(codeLine);
        if (indent == null) return true;
        model.document.makeDiffWithCaretReturn(
            caretLine, 0, true, indent, new Pos(caretLine, caretCharPos)
        );
        codeLine.delete(0, indent.length());
        setCaretPosWithSelection(caretCharPos - indent.length(), false);
      }
    }
    updateDocumentDiffTimeStamp();
    return true;
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
        setCaretPosWithSelection(caretCharPos - indent.length(), false);
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
    if (yPosition < size.y) {
      V2i sizeTmp = context.v2i1;

      sizeTmp.y = size.y - yPosition;
      sizeTmp.x = mirrored ? editorWidth() + vLineW : editorWidth() + xOffset;
      int x = mirrored
          ? pos.x + vLineLeftDelta + scrollBarWidth() + vLineW - xOffset
          : pos.x + vLineX - xOffset;
      g.drawRect(x, pos.y + yPosition, sizeTmp, colors.editor.bg);
    }
  }

  private void layoutScrollbar() {
    int x = mirrored ? pos.x + scrollBarWidth() : pos.x + size.x;
    vScroll.layoutVertical(vScrollPos,
        pos.y,
        editorHeight(), editorVirtualHeight(),
        x, scrollBarWidth());
    x = mirrored ? pos.x + vLineW + vLineLeftDelta + scrollBarWidth() : pos.x + vLineX;
    hScroll.layoutHorizontal(hScrollPos,
        x,
        editorWidth(), fullWidth,
        pos.y + editorHeight(), scrollBarWidth());
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
    int dx1 = mirrored ? size.x - lineNumbersWidth() - vLineW: vLineX - vLineLeftDelta;
    g.drawRect(pos.x + dx1, pos.y, vLineSize, colors.editor.numbersVLine);
    vLineSize.x = mirrored
        ? vLineLeftDelta + scrollBarWidth() + vLineW - xOffset
        : vLineLeftDelta - vLineW - xOffset;
    int dx2 = mirrored ? 0 : vLineX - vLineLeftDelta + vLineW;
    g.drawRect(pos.x + dx2, pos.y, vLineSize, colors.editor.bg);
  }

  static int clampScrollPos(int pos, int maxScrollPos) {
    return Math.min(Math.max(0, pos), maxScrollPos);
  }

  private long parsingTimeStart;

  private void onFileParsed(Object[] result) {
    Debug.consoleInfo("onFileParsed");
    fileStructureParsed = true;
    firstLinesParsed = true;

    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();
    int type = ((ArrayView) result[2]).ints()[0];
    if (result.length >= 5) {
      int[] graphInts = ((ArrayView) result[3]).ints();
      char[] graphChars = ((ArrayView) result[4]).chars();
      ParserUtils.updateDocument(model.document, ints, chars, graphInts, graphChars, false);
      long from = System.currentTimeMillis();
//      model.document.countPrefixes();
      model.document.scopeGraph.resolveAll(model.document::onResolve);
      long to = System.currentTimeMillis();
      System.out.println("Resolving all in " + (to - from) + " ms");
    } else {
      ParserUtils.updateDocument(model.document, ints, chars);
    }

    changeModelLanguage(Languages.getLanguage(type));

    window().setCursor(Cursor.arrow);
    window().repaint();
    Debug.consoleInfo("Full file parsed in " + (System.currentTimeMillis() - parsingTimeStart) + "ms");
    if (fullFileParseListener != null) {
      fullFileParseListener.accept(this);
    }
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

    ParserUtils.updateDocument(model.document, ints, chars, firstLinesParsed);
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

    ParserUtils.updateDocumentInterval(model.document, ints, chars);

    window().setCursor(Cursor.arrow);
    window().repaint();
    Debug.consoleInfo("Viewport parsed in " + (System.currentTimeMillis() - parsingTimeStart) + "ms");
  }

  private Window window() { return context.window; }

  private void onFirstLinesParsed(Object[] result) {
    if (fileStructureParsed) return;
    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();
    ParserUtils.updateDocument(model.document, ints, chars);
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
    diffModel = null;
    lineNumbers.setColors(null);

    model = new Model(
        new String[] {""},
        new Uri("", "", f.getName(), null)
    );
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
      setVScrollPos(vScrollPos + amount * lineHeight * 12 / 10);
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

  void setCaretPosWithSelection(int charPos, boolean shift) {
    Selection prevSelection = new Selection(selection);
    setCaretPos(charPos, shift);
    selection = prevSelection;
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
      setVScrollPos(caretVisibleY0 - lineHeight);
    } else if (caretVisibleY1 > editVisibleYMax - lineHeight) {
      setVScrollPos(caretVisibleY1 - editorHeight() + lineHeight);
    }
  }

  private void adjustEditorHScrollToCaret() {
    int xOffset = Numbers.iRnd(context.dpr * EditorConst.CARET_X_OFFSET);

    int editVisibleXMin = hScrollPos;
    int editVisibleXMax = hScrollPos + editorWidth();
    int caretVisibleX0 = caretPos;
    int caretVisibleX1 = caretPos + xOffset;

    if (caretVisibleX0 < editVisibleXMin + xOffset) {
      setHScrollPos(caretVisibleX0 - xOffset);
    } else if (caretVisibleX1 > editVisibleXMax - xOffset) {
      setHScrollPos(caretVisibleX1 - editorWidth() + xOffset);
    }
  }

  private void computeUsages() {
    Pos caretPos = new Pos(caretLine, caretCharPos);
    Pos elementPos = model.document.getElementStart(caretLine, caretCharPos);
    computeUsages(caretPos, elementPos);

    if ((definition == null || usages.isEmpty()) && caretCharPos > 0) {
      Pos prevCaretPos = new Pos(caretLine, caretCharPos - 1);
      Pos prevElementPos = model.document.getElementStart(caretLine, caretCharPos - 1);
      computeUsages(prevCaretPos, prevElementPos);
    }
  }

  private void computeUsages(Pos caretPos, Pos elementPos) {
    clearUsages();

    Document document = model.document;
    Pos def = document.getDefinition(elementPos);

    if (def == null) def = elementPos;

    List<Pos> usageList = document.getUsagesList(def);
    if (usageList != null) {
      definition = document.getCodeElement(def);
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
      ui.displayNoUsagesPopup(position, this);
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
      ui.displayNoUsagesPopup(position, this);
    } else {
      Pos charPos = computeCharPos(position);
      Pos startPos = model.document.getElementStart(charPos.line, charPos.pos);
      ui.showUsagesWindow(position, locs, this, getElementNameByStartPos(startPos));
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
    int localX = eventPosition.x - pos.x;
    int localY = eventPosition.y - pos.y;

    int line = Numbers.clamp(0, (localY + vScrollPos) / lineHeight, model.document.length() - 1);
    int offset = mirrored ? vLineW + vLineLeftDelta + scrollBarWidth() : vLineX;
    int documentXPosition = Math.max(0, localX - offset + hScrollPos);
    int charPos = model.document.line(line).computeCharPos(documentXPosition, g.mCanvas, fonts);
    return new Pos(line, charPos);
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
      case 0 -> ui.displayNoUsagesPopup(position,this);
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
    selection.startPos.set(range.startLineNumber, range.startColumn);
    selection.endPos.set(range.endLineNumber, range.endColumn);
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
    saveToNavStack();
  }

  void onTripleClickText() {
    selection.selectLine(caretLine);
    navStack.pop();
    saveToNavStack();
  }

  CodeLine caretCodeLine() {
    return model.document.line(caretLine);
  }

  CodeLine codeLine(int n) {
    return model.document.line(n);
  }

  // InputListener methods

  Consumer<ScrollBar.Event> vScrollHandler =
      event -> setVScrollPos(event.getPosition(maxVScrollPos()));

  Consumer<ScrollBar.Event> hScrollHandler =
      event -> setHScrollPos(event.getPosition(maxHScrollPos()));


  public boolean onScroll(float dX, float dY) {
    // chrome sends 150px, firefox send "6 lines"
    int changeY = Numbers.iRnd(lineHeight * 4 * dY / 150);
    int changeX = Numbers.iRnd(dX);
    if (changeY != 0) setVScrollPos(vScrollPos + changeY);
    if (changeX != 0) setHScrollPos(hScrollPos + changeX);
    return true;
  }

  public boolean onMouseUp(MouseEvent event, int button) {
    selection.isSelectionStarted = false;
    return true;
  }

  public Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    if (button == MOUSE_BUTTON_LEFT) {
      var lock = vScroll.onMouseDown(event.position, vScrollHandler, true);
      if (lock != null) return lock;

      lock = hScroll.onMouseDown(event.position, hScrollHandler, false);
      if (lock != null) return lock;

      saveToNavStack();
      V2i eventPosition = event.position;
      Pos pos = computeCharPos(eventPosition);
      Pos elementPos = model.document.getElementStart(pos.line, pos.pos);

      moveCaret(pos);
      computeUsages(pos, elementPos);

      if (!event.shift && !selection.isSelectionStarted) {
        selection.startPos.set(caretLine, caretCharPos);
      }

      selection.isSelectionStarted = true;
      selection.select(caretLine, caretCharPos);
      return this::dragText;
    }
    return null;
  }

  public boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    if (button == MOUSE_BUTTON_LEFT) {
      switch (clickCount) {
        case 1 -> onClickText(event);
        case 2 -> onDoubleClickText(event.position);
        case 3 -> onTripleClickText();
      }
    }
    return true;
  }

  public boolean onMouseMove(MouseEvent event) {
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
//    Debug.consoleInfo("EditorComponent::onKey: "+ event.toString());

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

    if (KeyCode.isFKey(event.keyCode) || event.keyCode == KeyCode.ESC) return false;
    if (event.ctrl || event.alt || event.meta) return false;
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
    } else {
      if (fullFileParseListener != null) {
        fullFileParseListener.accept(this);
      }
    }
  }

  public void onFileIterativeParsed(Object[] result) {
    if (model.document.currentVersion != iterativeVersion) return;
    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();
    int[] graphInts = null;
    char[] graphChars = null;
    if (result.length >= 4) {
      graphInts = ((ArrayView) result[2]).ints();
      graphChars = ((ArrayView) result[3]).chars();
    }
    ParserUtils.updateDocumentInterval(model.document, ints, chars, graphInts, graphChars);
    model.document.defToUsages.clear();
    model.document.usageToDef.clear();
    model.document.countPrefixes();
    model.document.scopeGraph.resolveAll(model.document::onResolve);
    model.document.onReparse();
    computeUsages();
  }

  public void iterativeParsing() {
    String language = model.language();
    if (language == null || Languages.TEXT.equals(language)) {
      model.document.onReparse();
    } else {
      var reparseNode = model.document.tree.getReparseNode();
      if (reparseNode == null) return;

      int[] interval = new int[]{reparseNode.getStart(), reparseNode.getStop(), reparseNode.getType()};
      char[] chars = model.document.getChars();
      int[] type = new int[]{Languages.getType(language)};

      int[] graphInts;
      char[] graphChars;
      if (model.document.scopeGraph.root != null) {
        ScopeGraph oldGraph = model.document.scopeGraph;
        ScopeGraph reparseGraph = new ScopeGraph(reparseNode.scope, oldGraph.types);
        ScopeGraphWriter writer = new ScopeGraphWriter(reparseGraph, reparseNode);
        writer.toInts();
        graphInts = writer.ints;
        graphChars = writer.chars;
      } else {
        graphInts = new int[]{};
        graphChars = new char[]{};
      }
      window().sendToWorker(this::onFileIterativeParsed, FileParser.asyncIterativeParsing, chars, type, interval, graphInts, graphChars);
    }
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
    int x = mirrored ? pos.x + vLineLeftDelta + vLineW + scrollBarWidth() : pos.x + vLineX;
    return Rect.isInside(position,
        x, pos.y,
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
      case KeyCode.ARROW_UP -> arrowUpDown(-1, event.ctrl, event.alt, event.shift);
      case KeyCode.ARROW_DOWN -> arrowUpDown(1, event.ctrl, event.alt, event.shift);
      case KeyCode.PAGE_UP -> pgUp(event);
      case KeyCode.PAGE_DOWN -> pgDown(event);
      case KeyCode.ARROW_LEFT ->
          event.ctrl && event.alt ? navigateBack() :
              moveCaretLeftRight(-1, event.ctrl, event.shift);
      case KeyCode.ARROW_RIGHT ->
          event.ctrl && event.alt ? navigateForward() :
              moveCaretLeftRight(1, event.ctrl, event.shift);
      case KeyCode.HOME -> shiftSelection(event.shift) || setCaretPos(0, event.shift);
      case KeyCode.END -> shiftSelection(event.shift) ||
          setCaretPos(caretCodeLine().totalStrLength, event.shift);
      default -> false;
    };
    if (result && event.shift) selection.endPos.set(caretLine, caretCharPos);
    if (result) computeUsages();
    return result;
  }

  void saveToNavStack() {
    NavigationContext curr = navStack.getCurrentCtx();
    if (curr != null && caretLine == curr.getLine() && caretCharPos == curr.getCharPos()) {
      return;
    }
    navStack.add(new NavigationContext(
        caretLine,
        caretCharPos,
        selection
    ));
  }

  boolean navigateBack() {
    saveToNavStack();
    NavigationContext prev = navStack.getPrevCtx();
    if (prev == null) return true;
    setCaretLinePos(prev.getLine(), prev.getCharPos(), false);
    selection = new Selection(prev.getSelection());
    return true;
  }

  boolean navigateForward() {
    NavigationContext curr = navStack.getNextCtx();
    if (curr == null) return true;
    setCaretLinePos(curr.getLine(), curr.getCharPos(), false);
    selection = new Selection(curr.getSelection());
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
    setVScrollPos(computed);
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
    setVScrollPos(lineNumber > model.document.length()
        ? maxVScrollPos()
        : (lineNumber + 1) * lineHeight - editorHeight());
  }

  private void scrollUpToLine(int lineNumber) {
    setVScrollPos((lineNumber - 2) * lineHeight);
  }

  static String parseJobName(String language) {
    return language != null ? switch (language) {
      case Languages.JAVA -> JavaParser.PARSE_SCOPES;
      case Languages.CPP -> CppParser.PARSE;
      case Languages.JS -> JavaScriptParser.PARSE;
      default -> null;
    } : null;
  }

  public char[] getChars() {
    return model.document.getChars();
  }

  private String getElementNameByStartPos(Pos startPos) {
    CodeElement codeElement = model.document.getCodeElement(startPos);
    if (codeElement != null) return codeElement.s;
    return "";
  }

  // remove later
  public boolean hitTest(V2i point) {
    return Rect.isInside(point, pos, size);
  }

  public void setDiffModel(LineDiff[] lineDiffs) {
    diffModel = lineDiffs;
    System.out.println("setDiffModel");
    if (diffModel != null) {
      byte[] c = new byte[diffModel.length];
      for (int i = 0; i < c.length; i++) {
        LineDiff ld = diffModel[i];
        c[i] = ld != null ? (byte) ld.type : 0;
      }
      lineNumbers.setColors(c);
    } else {
      System.out.println("deleteDiffModel");
      lineNumbers.setColors(null);
    }
  }
}
