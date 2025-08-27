package org.sudu.experiments.diff;

import org.sudu.experiments.*;
import org.sudu.experiments.editor.EditorConst;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.math.XorShiftRandom;
import org.sudu.experiments.ui.SetCursor;
import org.sudu.experiments.ui.UiContext;
import org.sudu.experiments.ui.UiFont;
import org.sudu.experiments.ui.window.ScrollContent;

import java.util.Objects;

public class BinaryDiffView extends ScrollContent {

  public static final float pairPadDp = 3;
  public static final float vLineDp = 1;
  public static final float vLinePadDp = 1;
  public static final int addressDigitPairs = 4;

  private final UiContext uiContext;
  private EditorColorScheme theme = EditorColorScheme.darkIdeaColorScheme();

  Color editBg, textFg, vLine;
  V4f tColor = new V4f();

  final V2i cellSize = new V2i();
  final V4f texRect = new V4f();

  int numBytesPerLine = 8;
  int numLines = 1000;

  UiFont uiFont;
  FontDesk fd;
  GL.Texture texture;

  public BinaryDiffView(UiContext uiContext) {
    this.uiContext = uiContext;
    readTheme();
  }

  @Override
  public void dispose() {
    texture = Disposable.dispose(texture);
  }

  public void setTheme(EditorColorScheme colors) {
    theme = colors;
    UiFont oldFont = uiFont;
    readTheme();

    boolean sameFont = Objects.equals(oldFont, colors.editorFont);
    if (!sameFont) {
      if (dpr != 0)
        changeFont();
    }
  }

  private void readTheme() {
    editBg = theme.editor.bg;
    vLine = theme.editor.numbersVLine;
    textFg = theme.codeElement[0].colorF;
    uiFont = theme.editorFont.scale(1.5f);
  }

  @Override
  protected void onDprChange(float olDpr, float newDpr) {
    if (uiFont != null) {
      changeFont();
    }
  }

  // a a d d r e s s │ aa bb cc dd ║ aa bb cc dd
  // a a d d r e s s │ aa bb cc dd ║ aa bb cc dd

  private void layout() {
    if (dpr == 0)
      System.err.println("BinaryDiffView.layout: dpr == 0");
    int vLine = toPx(vLineDp);
    int vLinePad = toPx(vLinePadDp);
    int pairPad = toPx(pairPadDp);
    int digitW = pairPad + cellSize.x;
    int addressW = cellSize.x * addressDigitPairs;
    int width = numBytesPerLine * digitW * 2
        + addressW + pairPad + vLine * 3 + vLinePad;
    int height = cellSize.y * numLines;
    setVirtualSize(width, height);
    layoutScroll();
  }

  private void changeFont() {
//    .fontDesk(name, pixelSize, weightRegular, FontDesk.STYLE_NORMAL)
    WglGraphics g = uiContext.graphics;
    fd = g.fontDesk(uiFont, dpr, false);
    String all = "0123456789ABCDEF";
    char[] cArray = new char[2];
    float[] measure = new float[256];
    int wMax = 0;
    g.mCanvas.setFont(fd);
    for (int y = 0; y < 16; y++) {
      cArray[0] = all.charAt(y);
      for (int x = 0; x < 16; x++) {
        cArray[1] = all.charAt(x);
        String digit = new String(cArray);
        float measured = g.mCanvas.measureText(digit);
        measure[y * 16 + x] = measured;
        int m = (int) (measured + 15 / 16f);
        wMax = Math.max(wMax, m);
//        System.out.println("s = " + digit + ", m = " + m + "px");
      }
    }
    int lineHeight = fd.lineHeight(EditorConst.LINE_HEIGHT_MULTI);
    float baseline = fd.baselineCenterF(lineHeight);
//    System.out.println("baseline = " + baseline);
//    System.out.println("wMax = " + wMax);
    cellSize.y = lineHeight;
    cellSize.x = wMax;
    Canvas c = g.createCanvas(wMax * 16, lineHeight * 16, true);
    c.setFont(fd);
    for (int y = 0; y < 16; y++) {
      cArray[0] = all.charAt(y);
      for (int i = 0; i < 16; i++) {
        cArray[1] = all.charAt(i);
        String digit = new String(cArray);
        float x = i * wMax + 0.5f * (wMax - measure[i * 17]);
//      System.out.println("s = " + digit + ", x = " + x + ", m = " + measure[i] + "px");
        c.drawText(digit, x, baseline + y * lineHeight);
      }
    }
    texture = g.createTexture(c);
//    System.out.println("texture.size() = " + texture.size());
    c.dispose();

    layout();
  }

  @Override
  public V2i minimalSize() {
    int px20 = DprUtil.toPx(20, dpr);
    return new V2i(px20, px20);
  }

  @Override
  public void draw(WglGraphics g) {
    int vLineW = toPx(vLineDp);
    int vLinePad = toPx(vLinePadDp);
    int pairPad = toPx(pairPadDp);

    int fileStart1 = cellSize.x * addressDigitPairs + pairPad * 2 + vLineW;

    // find a better place to do this
    if (scrollView != null) {
      scrollView.setScrollColor(
          theme.editor.scrollBarLine,
          theme.editor.scrollBarBg);
    }

    g.enableScissor(pos, size);
    g.drawRect(pos.x, pos.y, size, editBg);

    int vScroll = scrollPos.y;
    int firstLine = vScroll / cellSize.y;
    int lastLineT = (vScroll + size.y + cellSize.y - 1) / cellSize.y;
    int lastLine = Math.min(lastLineT, numLines);

    System.out.println("firstLine = " + firstLine);
    System.out.println("lastLine = " + lastLine);

    for (int ln = firstLine; ln < lastLine; ln++) {
      int y = ln * cellSize.y - vScroll + pos.y;
      drawLine(g, ln, y, fileStart1, pairPad);
    }

    g.disableScissor();
  }

  static double remInt(double x) {
    return x - (int) x;
  }

  private void drawLine(
      WglGraphics g, int line, int y,
      int fileStart1, int pairPad
  ) {
    int addr = line * numBytesPerLine;
    int baseX = pos.x;
    for (int d = 0; d < addressDigitPairs; d++) {
      int addrDigit = (addr >> ((addressDigitPairs - d - 1) * 8)) & 0xFF;
      drawByte(g, baseX + d * cellSize.x, y, addrDigit, textFg, tColor);
    }

    for (int i = 0; i < 16; i++) {
      double h = remInt(i / 16.f + line / Math.PI / 100);
      Color.Cvt.fromHSV(h, 0.75, 0.5, 0, tColor);
      int x = baseX + fileStart1 + i * (cellSize.x + pairPad);
      int b = XorShiftRandom.roll_7_1_9(
          XorShiftRandom.roll_7_1_9(4793 * line + i * 7879 + 2729));
      drawByte(g, x, y, b, textFg, tColor);
    }
  }

  private void drawByte(WglGraphics g, int x, int y, int value, V4f color, V4f bgColor) {
    int d0 = value & 0xF;
    int d1 = (value >> 4) & 0xF;
    texRect.set(d0 * cellSize.x,d1 * cellSize.y,
        cellSize.x, cellSize.y);
    g.drawText(x, y, cellSize, texRect, texture,
        color, bgColor, true);
  }

  @Override
  public void onMouseMove(MouseEvent event, SetCursor setCursor) {
    if (hitTest(event.position))
      setCursor.set(null);
  }
}
