package org.sudu.experiments.editor;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.Debug;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.editor.ui.colors.CodeLineColorScheme;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffRange;
import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.Focusable;
import org.sudu.experiments.ui.ScrollBar;
import org.sudu.experiments.ui.UiContext;
import org.sudu.experiments.ui.window.View;

import java.util.Objects;

public class UnifiedDiffView extends View implements Focusable
{

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
  int[] docLines;
  boolean[] docIndex;

  // layout
  int vLineX, vLineW, scrollBarWidth;
  int vLineTextOffset;
  int numDigits1, numDigits2;
  V2i vLineSize = new V2i();

  // render cache
  CodeLineRenderer[] lines = new CodeLineRenderer[0];
  int firstLineRendered, lastLineRendered;

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
    int lnWidth2 = lineNumbers1.measureDigits(numDigits1, mCanvas, dpr);
    int lnPos1 = pos.x, lnPos2 = pos.x + lnWidth1;
    lineNumbers1.setPosition(lnPos1, pos.y, lnWidth1, size.y, dpr);
    lineNumbers1.setPosition(lnPos2, pos.y, lnWidth2, size.y, dpr);
    vLineX = pos.x + lnWidth1 + lnWidth2;

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
    if (index == 0) model1 = model; else model2 = model;
    if (!model1.document.isEmpty() && !model2.document.isEmpty()) {
      DiffUtils.findDiffs(model1.document, model2.document, true,
          new int[0], new int[0], this::onDiffs, context.window.worker());
    }
  }

  private void buildDocIndex() {
    DiffRange[] ranges = diffInfo.ranges;
    int size = UnifiedDiffOp.unifiedSize(ranges);
    docLines = new int[size];
    docIndex = new boolean[size];
    UnifiedDiffOp.buildDocIndex(ranges, docLines, docIndex);
  }

  private void onDiffs(DiffInfo di, int[] versions) {
    if (versions[0] == model1.document.version()
        && versions[1] == model2.document.version()
    ) {
      diffInfo = di;
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
    drawVLine(g);
  }

  private void drawVLine(WglGraphics g) {
    vLineSize.y = size.y;
    vLineSize.x = vLineW;
    g.drawRect(vLineX, pos.y, vLineSize, colors.editor.numbersVLine);
  }

}
