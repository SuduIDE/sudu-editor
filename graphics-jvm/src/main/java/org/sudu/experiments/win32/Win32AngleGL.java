package org.sudu.experiments.win32;

import org.sudu.experiments.GLApi;
import org.sudu.experiments.angle.AngleGL;

public class Win32AngleGL extends AngleGL {
  @Override
  public void texSubImage2D(int target, int level, int xOffset, int yOffset, int format, int type, GLApi.Canvas canvas) {
    if (canvas instanceof D2dCanvas d2dCanvas) {
      d2dCanvas.texSubImage2D(target, level, xOffset, yOffset, format, type);
    } else {
      throw new RuntimeException("unsupported type " + canvas);
    }
  }
}
