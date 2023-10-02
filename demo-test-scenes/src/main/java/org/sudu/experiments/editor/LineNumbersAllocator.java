package org.sudu.experiments.editor;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.Disposable;
import org.sudu.experiments.GL;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.ui.colors.LineNumbersColors;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.Rect;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.ui.RegionTexture;
import org.sudu.experiments.ui.RegionTextureAllocator;
import org.sudu.experiments.ui.SetCursor;
import org.sudu.experiments.ui.UiContext;

import java.util.function.ToIntFunction;

public class LineNumbersAllocator implements Disposable {

  private final V2i pos = new V2i();
  private final V2i size = new V2i();
  private FontDesk fontDesk;
  private int lineHeight;
  private int rightPad;
  private double devicePR;

  private byte[] colors = new byte[0];
  private LineNumbersRenderer[] view = new LineNumbersRenderer[0];
  RegionTexture regionTexture = new RegionTexture(0);
  private GL.Texture texture;
  private final V2i textureSize = new V2i();
  private final UiContext context;

  public LineNumbersAllocator(UiContext context) {
    this.context = context;
  }

  public void setColors(byte[] c) {
    this.colors = c == null ? new byte[0] : c;
  }

  public void draw(
      int editorHeight, int textHeight,
      int scrollPos, int firstLine,
      int lastLine, int caretLine,
      WglGraphics g, EditorColorScheme colors
  ) {
    g.enableScissor(pos, size);
    draw(g, scrollPos, textHeight, firstLine, lastLine, caretLine, colors);
    drawBottom(textHeight, editorHeight, colors.lineNumber, g);
    g.disableScissor();
  }

  private void measure() {
    Canvas mCanvas = context.mCanvas();
    mCanvas.setFont(fontDesk);
    var measureWithPad = RegionTextureAllocator.measuringWithWPad(mCanvas, fontDesk.WWidth);
    rightPad = measureWithPad.applyAsInt("1");
  }

  public void draw(
      WglGraphics g,
      int scrollPos, int editorHeight, int firstLine,
      int lastLine, int caretLine, EditorColorScheme colorScheme
  ) {
    Canvas mCanvas = context.mCanvas();
    mCanvas.setFont(fontDesk);
    var measureWithPad = RegionTextureAllocator.measuringWithWPad(mCanvas, fontDesk.WWidth);
    LineNumbersColors lineNumber = colorScheme.lineNumber;

    g.drawRect(pos.x, pos.y, size, lineNumber.bgColor);

    int cacheLines = Numbers.iDivRoundUp(editorHeight, lineHeight) + 30;

    if (view.length < cacheLines) {
      view = LineNumbersRenderer.reallocRenderLines(
          cacheLines,
          view,
          firstLine,
          lastLine,
          regionTexture,
          measureWithPad
      );
      textureSize.set(regionTexture.getTextureSize());
      renderTexture(g);
    }

    if (view.length == 0) return;
    checkCached(firstLine, lastLine, measureWithPad);

    int yOffset = scrollPos % lineHeight;
    int xBase = pos.x;
    int padding = (int) (EditorConst.LINE_NUMBERS_RIGHT_PADDING * devicePR);

    for (int i = firstLine; i <= lastLine; i++) {
      LineNumbersRenderer item = itemRenderer(i);
      int y = (i - firstLine) * lineHeight - yOffset;
      int x = xBase + size.x - item.size.x + rightPad - padding;
      V4f c;
      V4f textColor;
      if (i == caretLine) {
        c = lineNumber.caretBgColor;
        textColor = lineNumber.caretTextColor;
      } else {
        c = getItemColor(colorScheme, colors, i, lineNumber);
        textColor = lineNumber.textColor;
      }
      drawBg(g, y, c);
      item.size.set(item.size.x, lineHeight);
      g.drawText(x, y, item.size, item.tContent, texture, textColor, c, 0);
    }
  }

  private void drawBg(WglGraphics g, int y, V4f bgColor) {
    V2i temp = context.v2i1;
    temp.set(size.x, lineHeight);
    g.drawRect(pos.x, y, temp, bgColor);
  }

  private static V4f getItemColor(EditorColorScheme colorScheme, byte[] colors, int i, LineNumbersColors lineNumber) {
    return i >= colors.length || colors[i] == 0
        ? lineNumber.bgColor
        : colorScheme.diff.getDiffColor(colorScheme, colors[i]);
  }


  private void checkCached(int firstLine, int lastLine, ToIntFunction<String> m) {
    boolean rerender = false;
    for (int i = firstLine; i <= lastLine; i++) {
      LineNumbersRenderer item = itemRenderer(i);
      if (item == null || item.num != i+1) {
        LineNumbersRenderer.setNewItem(view, i, regionTexture, m);
        rerender = true;
      }
    }

    if (rerender) {
      textureSize.set(regionTexture.getTextureSize());
      renderTexture(context.graphics);
    }
  }

  private void renderTexture(WglGraphics g) {
    Canvas canvas = g.createCanvas(textureSize.x + 150, textureSize.y);
    canvas.setFont(fontDesk);
    float baseline = CodeLineRenderer.baselineShift(fontDesk, lineHeight);
    for (var item: view) {
      if (item == null) continue;
      canvas.drawText(String.valueOf(item.num), item.tContent.x, baseline + item.tContent.y);
    }
    texture = Disposable.assign(texture, g.createTexture());
    texture.setContent(canvas);
    canvas.dispose();
  }

  private LineNumbersRenderer itemRenderer(int i) {
    return view[i % view.length];
  }

  private void drawBottom(
      int textHeight, int editorBottom,
      LineNumbersColors colorScheme, WglGraphics g
  ) {
    if (textHeight < editorBottom) {
      g.drawRect(pos.x, pos.y + textHeight,
        new V2i(size.x, editorBottom - textHeight),
        colorScheme.bgColor);
    }
  }

  public void setFont(FontDesk font, int lineHeight) {
    this.fontDesk = font;
    this.lineHeight = lineHeight;
    this.regionTexture = new RegionTexture(lineHeight);
    measure();
  }

  @Override
  public void dispose() {
    texture = Disposable.assign(texture, null);
    textureSize.set(0, 0);
    regionTexture = null;
    view = new LineNumbersRenderer[0];
  }

  public boolean onMouseMove(V2i position, SetCursor setCursor) {
    return Rect.isInside(position, pos, size) && setCursor.setDefault();
  }

  void setPos(V2i pos, int width, int height, double dpr) {
    this.pos.set(pos);
    size.set(width, height);
    devicePR = dpr;
  }
}
