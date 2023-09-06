package org.sudu.experiments.demo.ui;

import org.sudu.experiments.DprUtil;
import org.sudu.experiments.math.V4f;

public class ShadowParameters {
  public final V4f color = new V4f();
  public int size;

  public ShadowParameters(int size, float value) {
    this.size = size;
    this.color.setW(value);
  }

  public static ShadowParameters darculaTheme() {
    return new ShadowParameters(1, 0.125f);
  }

  public static ShadowParameters darkTheme() {
    return new ShadowParameters(1, 0.175f);
  }

  public static ShadowParameters lightTheme() {
    return new ShadowParameters(1, 0.075f);
  }

  public int getShadowSize(float dpr) {
    return DprUtil.toPx(size, dpr);
  }
}
