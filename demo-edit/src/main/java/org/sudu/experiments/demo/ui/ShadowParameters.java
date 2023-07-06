package org.sudu.experiments.demo.ui;

public class ShadowParameters {
  public final int size;
  public final float w;

  public ShadowParameters(int size, float w) {
    this.size = size;
    this.w = w;
  }

  public static ShadowParameters darkTheme() {
    return new ShadowParameters(1, 0.125f);
  }

  public static ShadowParameters lightTheme() {
    return new ShadowParameters(1, 0.075f);
  }
}
