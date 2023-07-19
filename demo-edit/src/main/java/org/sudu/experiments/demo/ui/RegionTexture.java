package org.sudu.experiments.demo.ui;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.GL;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.ArrayList;

public class RegionTexture implements RegionTextureAllocator{
  private GL.Texture texture;
  private FontDesk font;
  private Canvas mCanvas;
  private int textHeight;
  private static int tw = 0;
  private static int th = 0;
  private int maxW;

  public void setContext(Canvas canvas, FontDesk font, int textHeight, int maxW) {
    this.mCanvas = canvas;
    this.font = font;
    this.textHeight = textHeight;
    this.maxW = maxW;
  }

  public V4f alloc(String text){
    int w = (int) (mCanvas.measureText(text) + 7.f / 8);
    return alloc(w + Numbers.iRnd(font.WWidth));
  }

  @Override
  public V4f alloc(int width){
    V4f region = new V4f();
    if (tw >= MAX_TEXTURE_SIZE) {
      tw = 0;
      th += textHeight;
    }
    region.set(tw, th, width, textHeight);
    tw += width;
    return region;
  }

  @Override
  public void free(V4f location) {
    freeRegions.add(location);
  }

  public V2i getTextureSize() {
    return new V2i(MAX_TEXTURE_SIZE, th + textHeight);
  }

  native void canvasDraw(FontDesk fd, V4f location, String text);
  native void updateTexture();
  native void draw(
      V4f allocation,
      int x, int y,
      V4f colorF,
      V4f colorB,
      float contrast
  );

}
