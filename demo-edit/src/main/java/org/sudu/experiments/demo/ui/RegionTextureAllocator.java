package org.sudu.experiments.demo.ui;

import org.sudu.experiments.math.V4f;

import java.util.ArrayList;

public interface RegionTextureAllocator {
  int MAX_TEXTURE_SIZE = 4096;

  ArrayList<V4f> freeRegions = new ArrayList<>();

  V4f alloc(int width);

  void free(V4f location);

}
