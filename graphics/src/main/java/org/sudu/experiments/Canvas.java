package org.sudu.experiments;

import org.sudu.experiments.math.V4f;

public abstract class Canvas implements GLApi.Canvas, Disposable, CanvasDebug {

  static int globalCounter;
  protected int width, height;

  public Canvas() {
    globalCounter++;
  }

  public int width() {
    return width;
  }

  public int height() {
    return height;
  }

  @Override
  public void dispose() {
    globalCounter--;
  }

  public void setFont(String font, float size) {
    setFont(font, size, FontDesk.WEIGHT_REGULAR, FontDesk.STYLE_NORMAL);
  }

  public abstract void setFont(String font, float size, int weight, int style);
  public abstract void setFont(FontDesk font);

  public abstract float measureText(String s);
  public abstract void drawText(String s, float x, float y);
  public abstract void setTopMode(boolean top);
  public abstract void setFillColor(int r, int g, int b);
  public abstract void clear();
  public abstract void setTextAlign(int align);

  public interface TextAlign {
    int LEFT = 0;
    int CENTER = 1;
    int RIGHT = 2;
  }
}

interface CanvasDebug {
  default String getFont() { return "font"; }
  default void drawSvgSample() {}
}