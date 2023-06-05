package org.sudu.experiments.demo;

import org.sudu.experiments.GL;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.math.Rect;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

public class DemoRect {
  public final V2i pos = new V2i();
  public final V2i size = new V2i();
  public final V4f color = new V4f();
  public final V4f bgColor = new V4f();

  public DemoRect() {}

  public DemoRect(int x, int y, int w, int h) {
    set(x, y, w, h);
  }

  public void set(int x, int y, int w, int h) {
    pos.set(x, y);
    size.set(w, h);
  }

  public void makeEmpty() {
    size.set(0, 0);
  }

  public boolean isEmpty() {
    return size.x * size.y == 0;
  }

  public boolean isInside(V2i p) {
    return Rect.isInside(p, pos, size);
  }

  public void draw(WglGraphics g, int dx, int dy) {
    g.drawRect(pos.x + dx, pos.y + dy, size, color);
  }

  public void draw(WglGraphics g, GL.Texture texture) {
    g.drawRect(pos.x, pos.y, size, texture);
  }

  public void draw(WglGraphics g, GL.Texture texture, int dx, int dy) {
    g.drawRect(pos.x + dx, pos.y + dy, size, texture);
  }

  public void drawGrayIcon(WglGraphics g, GL.Texture texture, int dx, int dy, float contrast) {
    g.drawRectGrayIcon(pos.x + dx, pos.y + dy, size, texture,
        bgColor, color, contrast);
  }

  public void drawUV(WglGraphics g, int dx, int dy) {
    g.drawRectUV(pos.x + dx, pos.y + dy, size);
  }
}
