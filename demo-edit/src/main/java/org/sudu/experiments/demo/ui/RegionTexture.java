package org.sudu.experiments.demo.ui;

import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.ToIntFunction;

/**
 * The `RegionTexture` class represents a region texture allocator that is used for allocating
 * regions within a texture for rendering text or other graphical elements.
 */
public class RegionTexture implements RegionTextureAllocator {
  //   The basic implementation uses ArrayList, in the future it is desirable to replace
  //   the list of free regions with a more suitable structure, e.g. linked list, red-black tree
  private final ArrayList<V4f> freeRegions = new ArrayList<>();
  private final int textHeight;
  private int tw = 0;
  private int textureHeight = 0;
  private int textureWidth = 0;

  /**
   * Constructs a new RegionTexture object with the specified height.
   *
   * @param textHeight The text height.
   */
  public RegionTexture(int textHeight) {
    this.textureWidth = DEFAULT_TEXTURE_WIDTH;
    this.textHeight = textHeight;
  }

  /**
   * Constructs a new RegionTexture object with the specified text height and texture width.
   *
   * @param textHeight The text height.
   * @param textureWidth The width of the region texture.
   */
  public RegionTexture(int textHeight, int textureWidth) {
    this.textureWidth = textureWidth;
    this.textHeight = textHeight;
  }

  /**
   * Allocates a region within the texture for the given text with the specified height.
   *
   * @param text        The text to be rendered.
   * @param measureText A function that measures the width of the text.
   * @return The allocated region represented by a `V4f` object.
   */
  public V4f alloc(String text, ToIntFunction<String> measureText) {
    return alloc(measureText.applyAsInt(text));
  }

  /**
   * Allocates a region within the texture with the specified width.
   *
   * @param width The width of the text to be allocated.
   * @return The allocated region represented by a `V4f` object.
   * @throws RuntimeException if the specified width exceeds the maximum texture size.
   * @throws IllegalArgumentException if the text height is zero.
   */
  @Override
  public V4f alloc(int width) {
    if (width >= textureWidth) {
      throw new RuntimeException("RegionTextureAllocator: current width(" + width +
          ") greater than the allowable value of texture width(" + textureWidth + ")");
    }
    if (textHeight == 0) {
      throw new IllegalArgumentException("RegionTexture: Text height cannot be zero.");
    }

    V4f region = new V4f();
    if (width == 0) {
      return region;
    }

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
    if (tw + width >= textureWidth) {
      tw = 0;
      textureHeight += textHeight;
    }
    region.set(tw, textureHeight, width, textHeight);
    tw += width;
    return region;
  }

  /**
   * Frees the specified location within the texture.
   *
   * @param location The location to be freed represented by a `V4f` object.
   */
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
    return new V2i(textureWidth, textureHeight + textHeight);
  }

  public ArrayList<V4f> getFreeRegions() {
    return freeRegions;
  }
}
