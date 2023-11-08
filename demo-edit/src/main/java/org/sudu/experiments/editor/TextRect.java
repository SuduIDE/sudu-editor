package org.sudu.experiments.editor;

import org.sudu.experiments.GL;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.math.V4f;

public class TextRect extends DemoRect {
  public final V4f textureRegion = new V4f();

  public TextRect() {}

  public TextRect(int x, int y, int w, int h) {
    set(x, y, w, h);
  }

  public void setSizeToTextureRegion() {
    size.set(
        (int) (textureRegion.z - textureRegion.x),
        (int) (textureRegion.w - textureRegion.y));
  }

  public void setTextureRegionDefault(GL.Texture texture) {
    setTextureRegion(0, 0, texture.width(), texture.height());
  }

  public void setTextureRegion(int x, int y, int w, int h) {
    textureRegion.set(x, y, w, h);
  }

  public void drawText(WglGraphics g, GL.Texture texture, int dx, int dy, float contrast) {
    g.drawText(pos.x + dx, pos.y + dy, size, textureRegion, texture, color, bgColor, contrast);
  }

  public void drawTextCT(WglGraphics g, GL.Texture texture) {
    g.drawTextCT(pos.x, pos.y, size, textureRegion, texture, color, bgColor);
  }

  public void drawTextCT(WglGraphics g, int x, int y, GL.Texture texture, boolean inverse) {
    g.drawTextCT(x, y, size, textureRegion, texture,
        inverse ? bgColor : color,
        inverse ? color : bgColor);
  }

  public void drawRectShowAlpha(WglGraphics g, GL.Texture texture, int dx, int dy, float contrast) {
    g.drawAlpha(pos.x + dx, pos.y + dy, size, texture, contrast);
  }

  public void drawText(WglGraphics g, int x, int y, GL.Texture texture, boolean inverse) {
    g.drawText(x, y, size, textureRegion, texture,
        inverse ? bgColor : color,
        inverse ? color : bgColor, 1);
  }

  void setColor(V4f c) {
    color.set(c);
  }

  public void setColors(V4f cf, V4f cb) {
    color.set(cf);
    bgColor.set(cb);
  }

  void setBgColor(V4f c) {
    bgColor.set(c);
  }
}
