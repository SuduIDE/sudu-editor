package org.sudu.experiments.ui.window;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.Disposable;
import org.sudu.experiments.GL;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.ui.UiContext;
import org.sudu.experiments.ui.UiFont;
import org.sudu.experiments.ui.WindowColors;

import java.util.Objects;

public class TextLineView extends View {

  public final UiContext context;
  private String title;
  private UiFont uiFont;
  private FontDesk font;
  private float margin;
  private boolean textureRenderRequest;

  private int measured;
  private GL.Texture texture;
  private final V4f texRect = new V4f();

  public TextLineView(UiContext context) {
    this.context = context;
  }

  public void setText(String title, UiFont uiFont, float margin) {
    boolean fontChange = !Objects.equals(this.uiFont, uiFont);
    boolean titleChange = !Objects.equals(this.title, title);
    boolean marginChange = margin != this.margin;
    if (fontChange) {
      this.uiFont = uiFont;
      font = null;
      setHeight(0);
    }
    textureRenderRequest = fontChange || titleChange || marginChange;
    this.title = title;
    this.margin = margin;
    this.measured = 0;
  }

  public int computeAndSetHeight() {
    requireFont();
    int margin = context.toPx(this.margin);
    setHeight(font.lineHeight() + margin * 2);

    return size.y;
  }

  public void setWidth(int width) {
    size.x = width;

    if (texture != null && width != texture.width()) {
      if (width < measured || texture.width() < measured) {
        textureRenderRequest = true;
      }
    }
  }

  public void draw(WglGraphics g, WindowColors theme) {
    if (sizeEmpty()) return;
    if (!isEmpty()) {
      if (textureRenderRequest || texture == null) {
        renderTexture(g);
      }
    }
    V4f bgColor = theme.windowTitleBgColor;
    if (texture != null) {
      int width = texture.width();
      V4f textC = theme.windowTitleTextColor;
      if (context.cleartype) {
        g.drawTextCT(pos.x, pos.y, texture.size(),
            texRect, texture, textC, bgColor);
      } else {
        g.drawText(pos.x, pos.y, texture.size(),
            texRect, texture, textC, bgColor, 0);
      }
      if (width < size.x) {
        drawBg(g, width, size.x - width, bgColor);
      }
    } else {
      drawBg(g, 0, size.x, bgColor);
    }
  }

  public boolean sizeEmpty() {
    return size.x == 0 || size.y == 0;
  }

  public boolean isEmpty() {
    return uiFont == null || title == null || title.isEmpty();
  }

  private void setHeight(int height) {
    size.y = height;
  }

  private void drawBg(WglGraphics g, int x0, int sizeX, V4f bgColor) {
    V2i rSize = context.v2i1;
    rSize.set(sizeX, size.y);
    g.drawRect(pos.x + x0, pos.y, rSize, bgColor);
  }

  private void requireFont() {
    if (font == null) {
      font = context.fontDesk(uiFont);
    }
  }

  private void renderTexture(WglGraphics g) {
    textureRenderRequest = false;
    requireFont();
    float lineHeightF = font.lineHeightF();
    float lrPadding = (lineHeightF + 5f) / 10;
    int margin = context.toPx(this.margin);
    measured = margin + g.mCanvas.measurePx(font, title, lrPadding * 2);
    int width = Numbers.clamp(0, measured, size.x);
    if (width == 0) return;
    Canvas canvas = g.createCanvas(width, size.y, context.cleartype);
    canvas.setFont(font);
    canvas.drawText(title,
        margin + lrPadding,
        margin + font.uiBaseline());
    var t = texture != null ? texture : (texture = g.createTexture());
    t.setContent(canvas);
    canvas.dispose();
    texRect.set(0, 0, texture.width(), texture.height());
  }

  public void onDprChange() {
    if (uiFont != null) {
      font = null;
      measured = 0;
      textureRenderRequest = true;
    }
  }

  @Override
  protected void onTextRenderingSettingsChange() {
    textureRenderRequest = true;
  }

  @Override
  public void dispose() {
    texture = Disposable.assign(texture, null);
  }
}
