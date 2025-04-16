package org.sudu.experiments.editor;

import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.Rect;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

public class Caret {
  public static final float delayK = 1.25f;

  public static final int defaultWidth = 2;

  final V2i pos = new V2i();
  final V2i size = new V2i(defaultWidth, 20);
  final V4f color = new V4f();

  boolean state;
  double frequency = .5;
  private double nextTime = 0;

  public Caret() {
    Color.Cvt.gray(187, color);
  }

  void setHeight(int h) {
    size.y = h;
  }

  void setWidth(int w) {
    size.x = w;
  }

  int height() {
    return size.y;
  }

  int width() {
    return size.x;
  }

  public boolean update(double time) {
    boolean oldState = state;
    if (time > nextTime) {
      do {
        nextTime += frequency;
        state = !state;
      } while (time > nextTime);
    }
    return state != oldState;
  }

  public void setLocal(int x, int y) {
    pos.set(x, y);
  }

  public void startDelay(double timeNow) {
    nextTime = timeNow + frequency * delayK;
    state = true;
  }

  public boolean needsPaint(V2i size) {
    return Rect.isInside(pos, 0, 0, size);
  }

  public void paint(WglGraphics g, V2i base) {
    g.drawRect(pos.x + base.x, pos.y + base.y, size, color);
  }

  public void setColor(Color cursorColor) {
    color.set(cursorColor);
  }
}
