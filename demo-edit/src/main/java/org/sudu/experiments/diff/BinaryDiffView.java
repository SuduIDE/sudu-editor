package org.sudu.experiments.diff;

import org.sudu.experiments.*;
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

  public static final float bytePadDp = 3;
  public static final float vLineDp = 2;
  public static final float vLinePadDp = 10;
  public static final int addressDigitPairs = 4;
  public static final boolean debug = false;

  private final UiContext uiContext;
  private EditorColorScheme theme = EditorColorScheme.darkIdeaColorScheme();

  final V2i cellSize = new V2i();
  final V2i lineSize = new V2i();
  final V4f texRect = new V4f();
  final V4f debugColor = new V4f();

  int numBytesPerLine = 16;
  int numLines = 100000;

  UiFont uiFont;
  FontDesk fd;
  GL.Texture texture;

  public BinaryDiffView(UiContext uiContext) {
    this.uiContext = uiContext;
  }

  @Override
  public void dispose() {
    texture = Disposable.dispose(texture);
  }

  public void setTheme(EditorColorScheme colors) {
    theme = colors;
    if (!Objects.equals(uiFont, colors.editorFont)) {
      if (dpr != 0)
        changeFont();
    }
  }

  @Override
  public void setPosition(V2i newPos, V2i newSize, float newDpr) {
    boolean dprChange = dpr != newDpr;
    super.setPosition(newPos, newSize, newDpr);
    if (dprChange && theme != null)
      changeFont();
  }

  // a a d d r e s s │ aa bb cc dd ║ aa bb cc dd
  // a a d d r e s s │ aa bb cc dd ║ aa bb cc dd

  private void layout() {
    if (dpr == 0)
      System.err.println("BinaryDiffView.layout: dpr == 0");
    int vLine = toPx(vLineDp);
    int vLinePad = toPx(vLinePadDp);
    int pairPad = toPx(bytePadDp);
    int bytesW = (numBytesPerLine - 1) * pairPad + numBytesPerLine * cellSize.x;
    int addressW = cellSize.x * addressDigitPairs;
    int width = addressW + vLinePad + vLine + vLinePad
        + bytesW + vLinePad + vLine * 3 + vLinePad + bytesW;
    int height = cellSize.y * numLines;
    setVirtualSize(width, height);
    layoutScroll();
  }

  static char toHex(int v) {
    return (char) (v < 10 ? v + '0' : v - 10 + 'A');
  }

  private void changeFont() {
    uiFont = theme.editorFont.scale(1);
    WglGraphics g = uiContext.graphics;
    fd = g.fontDesk(uiFont, dpr, false);
    char[] cArray = new char[2];
    float[] measure = new float[256];
    int wMax = 0;
    g.mCanvas.setFont(fd);
    for (int y = 0; y < 16; y++) {
      cArray[0] = toHex(y);
      for (int x = 0; x < 16; x++) {
        cArray[1] = toHex(x);
        String digit = new String(cArray);
        float measured = g.mCanvas.measureText(digit);
        measure[y * 16 + x] = measured;
        int m = (int) (measured + 15 / 16f);
        wMax = Math.max(wMax, m);
      }
    }
    int lineHeight = fd.lineHeight(1);
    float baseline = fd.baselineCenterF(lineHeight);
    cellSize.y = lineHeight;
    cellSize.x = wMax;
    Canvas c = g.createCanvas(wMax * 16, lineHeight * 16, true);
    c.setFont(fd);
    for (int y = 0; y < 16; y++) {
      cArray[0] = toHex(y);
      for (int i = 0; i < 16; i++) {
        cArray[1] = toHex(i);
        String digit = new String(cArray);
        float x = i * wMax + 0.5f * (wMax - measure[i * 17]);
        c.drawText(digit, x, baseline + y * lineHeight);
      }
    }
    texture = g.createTexture(c);
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
    int pairPad = toPx(bytePadDp);

    int vLine1 = pos.x + cellSize.x * addressDigitPairs + vLinePad;
    int bytesX1 = vLine1 + vLineW + vLinePad;
    int vLine2 = bytesX1 + numBytesPerLine * cellSize.x
        + numBytesPerLine * pairPad - pairPad + vLinePad;
    int vLine3 = vLine2 + vLineW * 2;
    int bytesX2 = vLine3 + vLineW + vLinePad;

    // find a better place to do this
    if (scrollView != null) {
      scrollView.setScrollColor(
          theme.editor.scrollBarLine,
          theme.editor.scrollBarBg);
    }

    g.enableScissor(pos, size);
    g.drawRect(pos.x, pos.y, size, theme.editor.bg);

    int vScroll = scrollPos.y;
    int firstLine = vScroll / cellSize.y;
    int lastLineT = (vScroll + size.y + cellSize.y - 1) / cellSize.y;
    int lastLine = Math.min(lastLineT, numLines);

    for (int ln = firstLine; ln < lastLine; ln++) {
      int y = ln * cellSize.y - vScroll + pos.y;
      drawLine(g, ln, y, bytesX2, bytesX1, pairPad);
    }

    lineSize.set(vLineW, size.y);
    Color numbersVLine = theme.editor.numbersVLine;
    g.drawRect(vLine1, pos.y, lineSize, numbersVLine);
    g.drawRect(vLine2, pos.y, lineSize, numbersVLine);
    g.drawRect(vLine3, pos.y, lineSize, numbersVLine);

    g.disableScissor();
  }

  static double remInt(double x) {
    return x - (int) x;
  }

  private void drawLine(
      WglGraphics g, int line, int y,
      int bytesX1, int bytesX2, int pairPad
  ) {
    int addr = line * numBytesPerLine;
    int baseX = pos.x;
    int cellW = cellSize.x;
    var editBg = theme.editor.bg;

    if (debug)
      Color.Cvt.fromHSV(0.5 + 0.5 * Math.sin(line / 10.),
          0.75, 0.5, 0, debugColor);
    V4f bgColor = debug ? debugColor : editBg;

    Color addressC = theme.lineNumber.textColor;

    for (int d = 0; d < addressDigitPairs; d++) {
      int addrDigit = (addr >> ((addressDigitPairs - d - 1) * 8)) & 0xFF;
      drawByte(g, baseX + d * cellW, y, addrDigit, addressC, bgColor);
    }

    Color textFg = theme.codeElement[0].colorF;

    for (int i = 0; i < numBytesPerLine; i++) {
      double h = remInt(i / 16.f + line / Math.PI / 100);
      if (debug)
        Color.Cvt.fromHSV(h, 0.75, 0.5, 0, debugColor);
      int x = bytesX1 + i * (cellW + pairPad);
      int b = XorShiftRandom.roll_7_1_9(XorShiftRandom.roll_7_1_9(
          4793 * line + i * 7879 + 2729));
      drawByte(g, x, y, b, textFg, bgColor);
    }

    for (int i = 0; i < numBytesPerLine; i++) {
      double h = remInt(i / 16.f + line / Math.PI / 100);
      if (debug)
        Color.Cvt.fromHSV(h, 0.75, 0.5, 0, debugColor);
      int x = bytesX2 + i * (cellW + pairPad);
      int b = XorShiftRandom.roll_7_1_9(XorShiftRandom.roll_7_1_9(
          4793 * line + (i + numBytesPerLine) * 7879 + 2729));
      drawByte(g, x, y, b, textFg, bgColor);
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
