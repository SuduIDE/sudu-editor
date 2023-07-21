package org.sudu.experiments.demo.ui;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.math.V4f;

import java.util.function.ToIntFunction;

public interface RegionTextureAllocator {
  int MAX_TEXTURE_SIZE = 2048;

  static ToIntFunction<String> measuringF(Canvas canvas) {
    return text -> (int) (canvas.measureText(text) + 7.f / 8);
  }

  static ToIntFunction<String> measuringWithWPad(Canvas canvas, float width) {
    return text -> (int) (canvas.measureText(text) + width * 2 + 7.f / 8);
  }

  V4f alloc(int width);

  void free(V4f location);

}
