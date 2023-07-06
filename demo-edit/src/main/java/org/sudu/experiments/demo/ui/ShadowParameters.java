package org.sudu.experiments.demo.ui;

import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V4f;

public class ShadowParameters {
  public final V4f w;
  public int size;

  public ShadowParameters(int size, V4f w) {
    this.size = size;
    this.w = w;
  }

  public static ShadowParameters darkTheme() {
    return new ShadowParameters(1, new V4f().setW(0.125f));
  }

  public static ShadowParameters lightTheme() {
    return new ShadowParameters(1, new V4f().setW(0.075f));
  }

  public void setShadowSize(float dpr) {
    this.size = Numbers.iRnd(1 * dpr);
  }
}
