package org.sudu.experiments.demo.ui;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.GL;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.ArrayList;
import java.util.function.ToIntFunction;

public class RegionTexture implements RegionTextureAllocator{
  private GL.Texture texture;
  private FontDesk font;
  private Canvas mCanvas;
  private int textHeight;
  private static int tw = 0;
  private static int th = 0;
  private final ArrayList<V4f> freeRegions = new ArrayList<>();

  // TODO textHeight should be deleted
  public void setContext(Canvas canvas, FontDesk font, int textHeight) {
    this.mCanvas = canvas;
    this.font = font;
    this.textHeight = textHeight;
  }

  public V4f alloc(String text, ToIntFunction<String> measureText) {
    return alloc(measureText.applyAsInt(text));
  }

  @Override
  public V4f alloc(int width) {
    if (width >= MAX_TEXTURE_SIZE) {
      throw new RuntimeException("RegionTextureAllocator: current width(" + width + ") > MAX_TEXTURE_SIZE(" + MAX_TEXTURE_SIZE + ")");
    }

    V4f region = new V4f();

    if (freeRegions.size() > 0) {
      for (V4f freeRegion : freeRegions) {
        if (freeRegion.z >= width) {
          region.set(freeRegion.x, freeRegion.y, width, textHeight);
          freeRegion.x += width;
          freeRegion.z -= width;
          if (freeRegion.z == 0) {
            freeRegions.remove(freeRegion);
          }
          return region;
        }
      }
    }

    if (tw + width >= MAX_TEXTURE_SIZE) {
      tw = 0;
      th += textHeight;
    }
    region.set(tw, th, width, textHeight);
    tw += width;
    System.out.println("th = " + th);
    return region;
  }

  @Override
  public void free(V4f location) {
    if (freeRegions.size() > 0) {
      for (V4f freeRegion : freeRegions) {
        if (freeRegion.y == location.y) {
          if (freeRegion.x + freeRegion.z == location.x) {
            freeRegion.z += location.z;
            return;
          }
          if (location.x + location.z == freeRegion.x) {
            freeRegion.x = location.x;
            freeRegion.z += location.z;
            return;
          }
        }
      }
    }
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
