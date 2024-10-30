package org.sudu.experiments.math;

public class ColorOp {

  // linear interpolation from
  // color1 (when factor == 0) to color2 (when factor == 1)
  public static Color lerp(Color color1, Color color2, float factor, Color r) {
    float tt = 1 - factor;
    r.x = color1.x * tt + color2.x * factor;
    r.y = color1.y * tt + color2.y * factor;
    r.z = color1.z * tt + color2.z * factor;
    r.w = color1.w * tt + color2.w * factor;
    r.computeRgba();
    return r;
  }

  public static Color lerp(Color color1, Color color2, float factor) {
    return lerp(color1, color2, factor, new Color(0, 0, 0, 0));
  }

  public static Color blend(Color background, Color color, float factor) {
    Color r = lerp(background, color, factor);
    r.a = background.a;
    r.w = background.w;
    return r;
  }

  public static Color blend(Color background, Color color) {
    return blend(background, color, color.w);
  }
}
