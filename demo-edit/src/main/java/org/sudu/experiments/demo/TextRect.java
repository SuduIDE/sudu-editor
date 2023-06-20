package org.sudu.experiments.demo;

import org.sudu.experiments.GL;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.math.Color;
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

  public void drawWord(WglGraphics g, GL.Texture texture, int dx, int dy, float contrast, V4f color) {
    g.drawText(pos.x + dx, pos.y + dy, size, textureRegion, texture, color, bgColor, contrast);
  }
  public void drawText(WglGraphics g, GL.Texture texture, int dx, int dy, float contrast) {
    g.drawText(pos.x + dx, pos.y + dy, size, textureRegion, texture, color, bgColor, contrast);
  }

  public void drawRectShowAlpha(WglGraphics g, GL.Texture texture, int dx, int dy, float contrast) {
    g.drawRectShowAlpha(pos.x + dx, pos.y + dy, size, texture, contrast);
  }

  void setColor(Color c) {
    color.set(c);
  }

  void setColors(Color cf, Color cb) {
    color.set(cf);
    bgColor.set(cb);
  }

  void setBgColor(Color c) {
    bgColor.set(c);
  }
}
