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
  public void NewRegionNoCollision() {
    RegionTexture rt = new RegionTexture();
    int width = 100, height = 100;
    V4f r1 = rt.alloc(width, height), r2 = rt.alloc(width, height);
    assertFalse(r1.x + r1.z > r2.x);
    assertFalse(r2.x - r2.z < r1.x);
  }

  @Test
  public void SeveralRegionsNoCollision() {
    RegionTexture rt = new RegionTexture();
    int width = 100, height = 100;
    ArrayList<V4f> regions = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      regions.add(rt.alloc(width, height));
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
  public void UseFreeRegion() {
    RegionTexture rt = new RegionTexture();
    int width = 100, height = 100;
    ArrayList<V4f> regions = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      regions.add(rt.alloc(width, height));
    }
    V4f element = regions.get(5);
    V4f oldV4f = new V4f(element);
    rt.free(element);
    assertEquals(rt.getFreeRegions().get(0), element);
    assertTrue(oldV4f.equals(rt.alloc(width, height)));
  }

  @Test
  public void MergeFreeRegionsFromStart() {
    RegionTexture rt = new RegionTexture();
    int width = 100, height = 100;
    ArrayList<V4f> regions = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      regions.add(rt.alloc(width, height));
    }
    for (V4f region : regions) {
      rt.free(region);
    }
    assertEquals(1, rt.getFreeRegions().size());
  }

  @Test
  public void MergeFreeRegionsFromEnd() {
    RegionTexture rt = new RegionTexture();
    int width = 100, height = 100;
    ArrayList<V4f> regions = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      regions.add(rt.alloc(width, height));
    }
    for (int i = regions.size() - 1; i >= 0; i--) {
      rt.free(regions.get(i));
    }
    assertEquals(1, rt.getFreeRegions().size());
  }

  @Test
  public void MergeFreeRegionsWithPeriod() {
    RegionTexture rt = new RegionTexture();
    int width = 100, height = 100;
    ArrayList<V4f> regions = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      regions.add(rt.alloc(width, height));
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
  public void CorrectTextureSize() {
    RegionTexture rt = new RegionTexture();
    int width = 300, height = 100, count = 10;
    for (int i = 0; i < count; i++) {
      rt.alloc(width, height);
    }
    int layers = width * count / RegionTextureAllocator.MAX_TEXTURE_SIZE + 1;
    assertEquals(rt.getTextureSize(), new V2i(RegionTextureAllocator.MAX_TEXTURE_SIZE, height * layers));
  }

  @Test
  public void ExceedingWidth(){
    RegionTexture rt = new RegionTexture();
    int width = RegionTextureAllocator.MAX_TEXTURE_SIZE + 1, height = 100;
    assertThrows(RuntimeException.class, () -> rt.alloc(width, height));
  }
}
