package org.sudu.experiments.demo;

import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.Rect;
import org.sudu.experiments.math.V2i;

public class Caret {
  public static final float delayK = 1.25f;

  public static final int defaultWidth = 2;

  private final DemoRect shape = new DemoRect(0,0, defaultWidth, 20);
  private double frequency = .5;
  private double nextTime = 0;
  private boolean state;

  public Caret() {
    Color.Cvt.gray(187, shape.color);
  }

  void setHeight(int h) {
    shape.size.y = h;
  }

  void setWidth(int w) {
    shape.size.x = w;
  }

  int height() {
    return shape.size.y;
  }

  int width() {
    return shape.size.x;
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

  public void setPosition(int x, int y) {
    shape.pos.set(x, y);
  }

  public void startDelay(double timeNow) {
    nextTime = timeNow + frequency * delayK;
    state = true;
  }

  public boolean needsPaint(V2i size) {
    return Rect.isInside(shape.pos, 0, 0, size);
  }

  public void paint(WglGraphics g, V2i dXdY) {
    if (state) shape.draw(g, dXdY.x, dXdY.y);
  }

  public void setColor(Color cursorColor) {
    shape.color.set(cursorColor);
  }
}
