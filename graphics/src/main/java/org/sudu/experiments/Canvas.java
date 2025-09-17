package org.sudu.experiments;

import org.sudu.experiments.fonts.FontDesk;

public abstract class Canvas implements GLApi.Canvas, Disposable, CanvasDebug {

  static int globalCounter;

  public final boolean cleartype;
  protected int width, height;

  public Canvas(boolean cleartype) {
    this.cleartype = cleartype;
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

  // todo: do we want measureText(char[]) ? it is more efficient
  public abstract float measureText(String s);

  public int measurePx(FontDesk font, String text, float pad) {
    setFont(font);
    return measurePx(text, pad);
  }

  public int measurePx(String text) {
    return measurePx(text, 15.f / 32);
  }

  public int measurePx(String text, float pad) {
    return (int) (measureText(text) + pad + 0.5f);
  }

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
