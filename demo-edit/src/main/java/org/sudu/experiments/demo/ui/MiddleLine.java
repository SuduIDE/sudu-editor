package org.sudu.experiments.demo.ui;

import org.sudu.experiments.math.V2i;

public class MiddleLine {
  public static final int middleLineThicknessDp = 20;
  public final V2i pos = new V2i();
  public final V2i size = new V2i();
  public final V2i p11 = new V2i();
  public final V2i p12 = new V2i();
  public final V2i p21 = new V2i();
  public final V2i p22 = new V2i();

  public MiddleLine() {
  }

  public void setShaderPos(
      int yLeftStartPosition, int yLeftLastPosition,
      int yRightStartPosition, int yRightLastPosition
  ) {
    p11.set(pos.x, yLeftStartPosition);
    p21.set(pos.x, yLeftLastPosition);
    p12.set(pos.x + size.x, yRightStartPosition);
    p22.set(pos.x + size.x, yRightLastPosition);
  }
}
