package org.sudu.experiments.editor;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.Debug;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.ui.colors.CodeLineColorScheme;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffRange;
import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.Focusable;
import org.sudu.experiments.ui.ScrollBar;
import org.sudu.experiments.ui.UiContext;
import org.sudu.experiments.ui.window.View;

import java.util.Objects;

public class UnifiedDiffView extends View implements Focusable {

  static final float vLineSpaceDp = 10;
  static final float vLineWDp = 1;
  static final float vLineTextOffsetDp = 10;
  static final float scrollBarWidthDp = 12;

  // | line numbers 1 | line numbers 2 | vLineSpace | vLine | vLineTextOffset - xOffset | xOffset + text |
  static final boolean drawLineNumbersFrame = false;

  final UiContext context;
  final ClrContext lrContext;
  final Caret caret = new Caret();

  final LineNumbersComponent lineNumbers1 = new LineNumbersComponent();
  final LineNumbersComponent lineNumbers2 = new LineNumbersComponent();

  // scrol
  final ScrollBar vScroll = new ScrollBar();
  final ScrollBar hScroll = new ScrollBar();
  int vScrollPos = 0;
  int hScrollPos = 0;

  EditorColorScheme colors;
  CodeLineColorScheme codeLineColors;
  float fontVirtualSize = EditorConst.DEFAULT_FONT_SIZE;
  String fontFamilyName = EditorConst.FONT;
  boolean hasFocus;

  // model data
  Model model1 = new Model(), model2 = model1;
  DiffInfo diffInfo;
  // document lines to view mapping
  int[] docLines;
  boolean[] docIndex;
  // line number values
  int[] ln1Values, ln2Values;
  CodeLines docWrapper = docWrapper();
  

  // layout
  int vLineX, vLineW, scrollBarWidth;
  int vLineTextOffset, textBaseX, textViewWidth;
  int numDigits1, numDigits2;
  int fullWidth = 0;
  V2i vLineSize = new V2i();

  // render cache
  CodeLineRenderer[] lines = new CodeLineRenderer[0];
  int firstViewLine, lastViewLine;
  private int frameId;

  public UnifiedDiffView(UiContext uiContext) {
    context = uiContext;
    lrContext = new ClrContext(uiContext.cleartype);
  }

  @Override
  public void dispose() {
    CodeLineRenderer.disposeLines(lines);
    lrContext.dispose();
    lineNumbers1.dispose();
    lineNumbers2.dispose();
  }

  public void setTheme(EditorColorScheme theme) {
    colors = theme;
    codeLineColors = colors.editorCodeLineScheme();
    caret.setColor(theme.editor.cursor);
    vScroll.setColor(theme.editor.scrollBarLine, theme.editor.scrollBarBg);
    hScroll.setColor(theme.editor.scrollBarLine, theme.editor.scrollBarBg);
    if (!theme.editorFont.equals(fontFamilyName, fontVirtualSize)) {
      changeFont(theme.editorFont.familyName, theme.editorFont.size);
    }
//    if (codeMap != null)
//      buildDiffMap();
  }

  public void changeFont(String name, float virtualSize) {
    if (context.dpr != 0) {
      doChangeFont(name, virtualSize);
      context.window.repaint();
    }
    fontVirtualSize = virtualSize;
    fontFamilyName = name;
  }

  private void doChangeFont(String name, float virtualSize) {
    float newPixelFontSize = virtualSize * dpr;
    float oldPixelFontSize = lrContext.fontSize();
    if (newPixelFontSize != oldPixelFontSize || !Objects.equals(name, fontFamilyName)) {
      lineNumbers1.dispose();
      lineNumbers2.dispose();
      invalidateFont();
      setFont(name, newPixelFontSize);
//      recomputeCaretPosY();
      updateLineNumbersFont();
      internalLayout();
//      adjustEditorScrollToCaret();
    }
  }

  private void updateLineNumbersFont() {
    lineNumbers1.setFont(lrContext);
    lineNumbers2.setFont(lrContext);
  }

  private void invalidateFont() {
//    Debug.consoleInfo("invalidateFont");

    CodeLineRenderer.disposeLines(lines);
    model1.document.invalidateFont();
    model2.document.invalidateFont();
  }

  private void setFont(String name, float pixelSize) {
    lrContext.setFonts(name, pixelSize, context.graphics);
    lrContext.setLineHeight(EditorConst.LINE_HEIGHT_MULTI, context.graphics);
    caret.setHeight(lrContext.font.caretHeight(lrContext.lineHeight));

    Debug.consoleInfo("editor font: " + name + " " + pixelSize
//        + ", ascent+descent = " + lrContext.font.lineHeight()
            + ", lineHeight = " + lrContext.lineHeight
        /* + ", caretHeight = " + caret.height() */ );

    if (CodeLineRenderer.useTop) {
      Debug.consoleInfo("font.topBase(lineHeight) = "
          + lrContext.font.topBase(lrContext.lineHeight));
    }
  }

  @Override
  protected void onTextRenderingSettingsChange() {
    lrContext.enableCleartype(context.cleartype, context.graphics);
    CodeLineRenderer.makeContentDirty(lines);
    lineNumbers1.dispose();
    lineNumbers2.dispose();
    updateLineNumbersFont();
  }

  // | line numbers 1 | line numbers 2 | /**vLineSpace*// | vLine | vLineTextOffset - xOffset | xOffset + text |
  private void internalLayout() {
    int vLineSpace = toPx(vLineSpaceDp);
    vLineTextOffset = toPx(vLineTextOffsetDp);
    vLineW = toPx(vLineWDp);

    scrollBarWidth = toPx(scrollBarWidthDp);

    numDigits1 = Numbers.numDecimalDigits(model1.document.length());
    numDigits2 = Numbers.numDecimalDigits(model2.document.length());
    Canvas mCanvas = context.graphics.mCanvas;
    int lnWidth1 = lineNumbers1.measureDigits(numDigits1, mCanvas, dpr);
    int lnWidth2 = lineNumbers2.measureDigits(numDigits2, mCanvas, dpr);
    int lnPos1 = pos.x, lnPos2 = pos.x + lnWidth1;
    lineNumbers1.setPosition(lnPos1, pos.y, lnWidth1, size.y, dpr);
    lineNumbers2.setPosition(lnPos2, pos.y, lnWidth2, size.y, dpr);
    vLineX = pos.x + lnWidth1 + lnWidth2;
    textBaseX = lnWidth1 + lnWidth2 + vLineW + vLineTextOffset;
    textViewWidth = Math.max(1, size.x - textBaseX);
  }

  @Override
  public void setPosition(V2i pos, V2i size, float newDpr) {
    boolean dprChange = dpr != newDpr;
    super.setPosition(pos, size, newDpr);
    if (dprChange) {
      doChangeFont(fontFamilyName, fontVirtualSize);
      lrContext.setDpr(dpr);
    } else {
      internalLayout();
    }
  }

  @Override
  public boolean onKeyPress(KeyEvent event) {
    return false;
  }

  public void setModel(Model model, int index) {
    docLines = null;
    docIndex = null;
    ln1Values = ln2Values = null;
    if (index == 0) model1 = model; else model2 = model;
    if (!model1.document.isEmpty() && !model2.document.isEmpty()) {
      DiffUtils.findDiffs(model1.document, model2.document, true,
          new int[0], new int[0], this::onDiffs, context.window.worker());
    }
  }

  public CodeLine codeLine(int i) {
    var model = docIndex[i] ? model2 : model1;
    return model.document.lines[docLines[i]];
  }

  CodeLines docWrapper() {
    return new CodeLines() {
      public CodeLine line(int i) {
        return codeLine(i);
      }
    };
  }

  private void buildDocIndex() {
    DiffRange[] ranges = diffInfo.ranges;
    int size = UnifiedDiffOp.unifiedSize(ranges);
    docLines = new int[size];
    docIndex = new boolean[size];
    ln1Values = new int[size];
    ln2Values = new int[size];

    UnifiedDiffOp.buildDocIndex(ranges,
        docLines, docIndex,
        ln1Values, ln2Values);
    lineNumbers1.setColors(LineDiff.colors(model1.diffModel));
    lineNumbers2.setColors(LineDiff.colors(model2.diffModel));
  }

  private void onDiffs(DiffInfo di, int[] versions) {
    if (versions[0] == model1.document.version()
        && versions[1] == model2.document.version()
    ) {
      LineDiff.replaceEdited(di.lineDiffsL, DiffTypes.DELETED);
      LineDiff.replaceEdited(di.lineDiffsR, DiffTypes.INSERTED);
      diffInfo = di;
      model1.diffModel = di.lineDiffsL;
      model2.diffModel = di.lineDiffsR;
      buildDocIndex();
    } else {
      System.out.println("onDiffs: version mismatch: doc1.v = " + model1.document.version() +
          ", got version " + versions[0] + ", doc2.v = " + model2.document.version() +
          ", got version" + versions[1]);
    }
  }

  @Override
  public void draw(WglGraphics g) {
    super.draw(g);
    frameId++;
    drawVLine(g);

    int lineHeight = lrContext.lineHeight;
    int cacheLines = Numbers.iDivRoundUp(size.y, lineHeight) + EditorConst.MIN_CACHE_LINES;
    if (lines.length < cacheLines) {
      lines = CodeLineRenderer.allocRenderLines(
          cacheLines, lines, lrContext,
          firstViewLine, lastViewLine, docWrapper);
    }

    if (docLines == null) return;
    firstViewLine = Math.min(
        vScrollPos / lineHeight, docLines.length - 1);
    lastViewLine = Math.min(
        (vScrollPos + size.y - 1) / lineHeight, docLines.length - 1) + 1;
    int lastViewLine = Math.min(this.lastViewLine, docLines.length);

    if (ln1Values.length != 0)
      drawLineNumbers(g, firstViewLine, lastViewLine);

    System.out.println(
        "draw: firstViewLine = " + firstViewLine + ", lastViewLine = " + lastViewLine);
    int rightPadding = toPx(EditorConst.RIGHT_PADDING);
    int fullWidth = 0;

    for (int i = firstViewLine; i < lastViewLine; i++) {
      int lineIndex = i; // viewToDocMap[i - firstLine];
      if (lineIndex < 0) continue;

      var model = docIndex[i] ? model2 : model1;
      var diffModel = model.diffModel;
      int docLine = docLines[i];
      CodeLine cLine = model.document.lines[docLine];
      CodeLineRenderer line = lineRenderer(i);
      int yPosition = lineHeight * i - vScrollPos;
      int lineMeasure = line.updateTexture(
          cLine, g, lineHeight, textViewWidth, hScrollPos,
          lineIndex, i % lines.length);

      fullWidth = Math.max(fullWidth, lineMeasure + rightPadding);

      LineDiff diff = diffModel == null || docLine >= diffModel.length
          ? null : diffModel[docLine];
      V2i selectionTemp = context.v2i2;
      line.draw(
          pos.y + yPosition, pos.x + textBaseX, g,
          textViewWidth, lineHeight, hScrollPos,
          codeLineColors, null, //  getSelLineSegment(lineIndex, cLine, selectionTemp),
          model.definition, model.usages,
          model.caretLine == lineIndex, null, null,
          diff);
    }
    this.fullWidth = fullWidth;
  }


  private void drawVLine(WglGraphics g) {
    vLineSize.y = size.y;
    vLineSize.x = vLineW;
    g.drawRect(vLineX, pos.y, vLineSize, colors.editor.numbersVLine);
  }

  private void drawLineNumbers(WglGraphics g, int firstViewLine, int lastViewLine) {
    drawLineNumbers(g, firstViewLine, lastViewLine, lineNumbers1, ln1Values,
        colors.codeDiffBg.insertedColor);
    drawLineNumbers(g, firstViewLine, lastViewLine, lineNumbers2, ln2Values,
        colors.codeDiffBg.deletedColor);
  }

  // todo optimize
  private void drawLineNumbers(
      WglGraphics g, int firstViewLine, int lastViewLine,
      LineNumbersComponent lineNumbers, int[] values, Color bg
  ) {
    int lineHeight = lrContext.lineHeight;
    lineNumbers.beginDraw(g, 0);
    for (int i = firstViewLine; i < lastViewLine; i++) {
      int yPosition = lineHeight * i - vScrollPos;
      if (values[i] >= 0) {
        lineNumbers.drawRange(yPosition, values[i], values[i] + 1, g, colors);
      } else {
        lineNumbers.drawEmptyLines(yPosition, yPosition + lineHeight, g, bg);
      }
    }
    lineNumbers.endDraw(g);
  }

  private CodeLineRenderer lineRenderer(int i) {
    return lines[i % lines.length];
  }
}
