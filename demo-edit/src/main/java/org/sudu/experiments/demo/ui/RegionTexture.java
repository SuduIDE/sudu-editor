package org.sudu.experiments.demo.ui;

import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.ToIntFunction;

/**
 * The `RegionTexture` class represents a region texture allocator that is used for allocating
 * regions within a texture for rendering text or other graphical elements.
 * <p>
 * It implements the `RegionTextureAllocator` interface.
 */
public class RegionTexture implements RegionTextureAllocator {

  private final ArrayList<V4f> freeRegions = new ArrayList<>();
  private final int textHeight;
  private int tw = 0;
  private int th = 0;

  /**
   * Constructs a new RegionTexture object with the specified height.
   *
   * @param height the height of the region texture
   */
  public RegionTexture(int height) {
    this.textHeight = height;
  }

  /**
   * Allocates a region within the texture for the given text with the specified height.
   *
   * @param text        The text to be rendered.
   * @param measureText A function that measures the width of the text.
   * @param height      The height of the allocated region.
   * @return The allocated region represented by a `V4f` object.
   */
  public V4f alloc(String text, ToIntFunction<String> measureText, int height) {
    return alloc(measureText.applyAsInt(text));
  }

  /**
   * Allocates a region within the texture with the specified width.
   * <p>
   * The height of the allocated region is the same as the height of the last allocated region.
   * If the height of the last allocated region is zero, returns an empty region.
   *
   * @param width The width of the allocated region.
   * @return The allocated region represented by a `V4f` object.
   * @throws RuntimeException if the specified width exceeds the maximum texture size.
   */
  @Override
  public V4f alloc(int width) {
    if (width >= MAX_TEXTURE_SIZE) {
      throw new RuntimeException("RegionTextureAllocator: current width(" + width +
          ") > MAX_TEXTURE_SIZE(" + MAX_TEXTURE_SIZE + ")");
    }

    V4f region = new V4f();
    if (width == 0 || textHeight == 0) {
      return new V4f();
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
    if (tw + width >= MAX_TEXTURE_SIZE) {
      tw = 0;
      th += textHeight;
    }
    region.set(tw, th, width, textHeight);
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

  /**
   * Returns the size of the texture.
   *
   * @return The size of the texture represented by a `V2i` object.
   */
  public V2i getTextureSize() {
    return new V2i(MAX_TEXTURE_SIZE, th + textHeight);
  }

  /**
   * Returns the list of free regions within the texture.
   *
   * @return The list of free regions represented by an `ArrayList` of `V4f` objects.
   */
  public ArrayList<V4f> getFreeRegions() {
    return freeRegions;
  }
}
