package org.sudu.experiments.demo;

import org.junit.jupiter.api.Test;
import org.sudu.experiments.demo.ui.RegionTexture;
import org.sudu.experiments.demo.ui.RegionTextureAllocator;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RegionTextureAllocatorTest {

  @Test
  public void newRegionNoCollision() {
    int width = 100, height = 100;
    RegionTexture rt = new RegionTexture(height);
    V4f r1 = rt.alloc(width), r2 = rt.alloc(width);
    assertFalse(r1.x + r1.z > r2.x);
    assertFalse(r2.x - r2.z < r1.x);
  }

  @Test
  public void severalRegionsNoCollision() {
    int width = 100, height = 100;
    RegionTexture rt = new RegionTexture(height);
    ArrayList<V4f> regions = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      regions.add(rt.alloc(width));
    }
    for (int i = 0; i < regions.size(); i++) {
      for (int j = i + 1; j < regions.size(); j++) {
        V4f first = regions.get(i);
        V4f second = regions.get(j);
        if (first.y != second.y) continue;
        assertFalse(first.x + first.z > second.x);
        assertFalse(second.x - second.z < first.x);
      }
    }
  }

  @Test
  public void useFreeRegion() {
    int width = 100, height = 100;
    RegionTexture rt = new RegionTexture(height);
    ArrayList<V4f> regions = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      regions.add(rt.alloc(width));
    }
    V4f element = regions.get(5);
    V4f oldV4f = new V4f(element);
    rt.free(element);
    assertEquals(rt.getFreeRegions().get(0), element);
    assertTrue(oldV4f.equals(rt.alloc(width)));
  }

  @Test
  public void mergeFreeRegionsFromStart() {
    int width = 100, height = 100;
    RegionTexture rt = new RegionTexture(height);
    ArrayList<V4f> regions = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      regions.add(rt.alloc(width));
    }
    for (V4f region : regions) {
      rt.free(region);
    }
    assertEquals(1, rt.getFreeRegions().size());
  }

  @Test
  public void mergeFreeRegionsFromEnd() {
    int width = 100, height = 100;
    RegionTexture rt = new RegionTexture(height);
    ArrayList<V4f> regions = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      regions.add(rt.alloc(width));
    }
    for (int i = regions.size() - 1; i >= 0; i--) {
      rt.free(regions.get(i));
    }
    assertEquals(1, rt.getFreeRegions().size());
  }

  @Test
  public void mergeFreeRegionsWithPeriod() {
    int width = 100, height = 100;
    RegionTexture rt = new RegionTexture(height);
    ArrayList<V4f> regions = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      regions.add(rt.alloc(width));
    }
    for (int i = 0; i < regions.size(); i++) {
      if (i % 2 == 0) {
        rt.free(regions.get(i));
      }
    }
    for (int i = 0; i < regions.size(); i++) {
      if (i % 2 == 1) {
        rt.free(regions.get(i));
      }
    }
    assertEquals(rt.getFreeRegions().size(), 1);
  }

  @Test
  public void correctTextureSize() {
    int width = 300, height = 100, count = 10;
    RegionTexture rt = new RegionTexture(height);
    for (int i = 0; i < count; i++) {
      rt.alloc(width);
    }
    int layers = width * count / RegionTextureAllocator.MAX_TEXTURE_SIZE + 1;
    assertEquals(new V2i(RegionTextureAllocator.MAX_TEXTURE_SIZE, height * layers), rt.getTextureSize());
  }

  @Test
  public void exceedingWidth() {
    int width = RegionTextureAllocator.MAX_TEXTURE_SIZE + 1, height = 100;
    RegionTexture rt = new RegionTexture(height);
    assertThrows(RuntimeException.class, () -> rt.alloc(width));
  }

  @Test
  public void zeroTextHeight() {
    int height = 0;
    assertThrows(IllegalArgumentException.class, () -> new RegionTexture(height));
  }
}
