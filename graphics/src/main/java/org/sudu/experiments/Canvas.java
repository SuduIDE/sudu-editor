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

  public abstract void setFont(int size, String font);
  public abstract void setFont(FontDesk font);

  public abstract Object platformFont(String font, int size);

  //  returns the following metrics:
  //    new V4f(fontAscent, fontDescent, WCharWidth, spaceWidth)
  public abstract V4f getFontMetrics();
  public abstract float measureText(String s);
  public abstract void drawText(String s, float x, float y);
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
  String getFont();
  void drawSvgSample();
}