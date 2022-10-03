package org.sudu.experiments.demo;

import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.math.Color;

public class Caret {
  public static final int grayColor = 187;
  public static final float delayK = 1.25f;

  private final DemoRect shape = new DemoRect(0,0, 2, 20);
  private double frequency = .5;
  private double nextTime = 0;
  private boolean state;

  public Caret() {
    Color.Cvt.gray(grayColor, shape.color);
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

  public void paint(WglGraphics g) {
    if (state) shape.draw(g, 0, 0);
  }
}
