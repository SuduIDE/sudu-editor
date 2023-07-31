package org.sudu.experiments.demo.ui;

import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.ToIntFunction;

public class RegionTexture implements RegionTextureAllocator {
  private int tw = 0;
  private int th = 0;
//   The basic implementation uses ArrayList, in the future it is desirable to replace
//   the list of free regions with a more suitable structure, e.g. linked list, red-black tree
  private final ArrayList<V4f> freeRegions = new ArrayList<>();
  private int textHeight;

  public V4f alloc(String text, ToIntFunction<String> measureText, int textHeight) {
    return alloc(measureText.applyAsInt(text), textHeight);
  }

  @Override
  public V4f alloc(int width, int height) {
    if (width >= MAX_TEXTURE_SIZE) {
      throw new RuntimeException("RegionTextureAllocator: current width(" + width + ") > MAX_TEXTURE_SIZE(" + MAX_TEXTURE_SIZE + ")");
    }
    textHeight = height;

    V4f region = new V4f();
    if (freeRegions.size() > 0) {
      for (V4f freeRegion : freeRegions) {
        if (freeRegion.z >= width) {
          region.set(freeRegion.x, freeRegion.y, width, height);
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
      th += height;
    }
    region.set(tw, th, width, height);
    tw += width;
    return region;
  }

  @Override
  public void free(V4f location) {
    V4f currentLocation = new V4f(location);
    if (freeRegions.size() > 0) {
      Iterator<V4f> iter = freeRegions.iterator();
      while (iter.hasNext()) {
        V4f freeRegion = iter.next();
        if (freeRegion.y == currentLocation.y) {
          if (freeRegion.x + freeRegion.z == currentLocation.x) {
            currentLocation.x = freeRegion.x;
            currentLocation.z += freeRegion.z;
            iter.remove();
          } else if (currentLocation.x + currentLocation.z == freeRegion.x) {
            currentLocation.z += freeRegion.z;
            iter.remove();
          }
        }
      }
    }
    freeRegions.add(currentLocation);
  }

  public V2i getTextureSize() {
    return new V2i(MAX_TEXTURE_SIZE, th + textHeight);
  }

  public ArrayList<V4f> getFreeRegions() {
    return freeRegions;
  }
}
