package org.sudu.experiments.ui;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.math.V4f;

import java.util.function.ToIntFunction;

public interface RegionTextureAllocator {
  int DEFAULT_TEXTURE_WIDTH = 2048;

  static ToIntFunction<String> measuring(Canvas canvas) {
    return canvas::measurePx;
  }

  static ToIntFunction<String> measuringWithWPad(Canvas canvas, float width) {
    return text -> canvas.measurePx(text, width * 2 + 7.f / 8);
  }

  V4f alloc(int width);

  void free(V4f location);

}
