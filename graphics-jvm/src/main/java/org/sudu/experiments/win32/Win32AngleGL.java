package org.sudu.experiments.win32;

import org.sudu.experiments.GLApi;
import org.sudu.experiments.angle.AngleGL;

public class Win32AngleGL extends AngleGL {

  private int drawCallCount = 0;
  private int drawCallsLastFrame = 0;

  @Override
  public void texSubImage2D(int target, int level, int xOffset, int yOffset, int format, int type, GLApi.Canvas canvas) {
    if (canvas instanceof D2dCanvas d2dCanvas) {
      d2dCanvas.texSubImage2D(target, level, xOffset, yOffset, format, type);
    } else {
      throw new RuntimeException("unsupported type " + canvas);
    }
  }

  @Override
  public void drawElements(int mode, int count, int type, int offset) {
    super.drawElements(mode, count, type, offset);
    drawCallCount++;
  }

  public int getDrawCallCount() {
    return drawCallsLastFrame;
  }

  public void notifyNewFrame() {
    drawCallsLastFrame = drawCallCount;
    drawCallCount = 0;
  }
}
