package org.sudu.experiments.editor;

import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V4f;

interface UnderlineConstants {
  float basePow = 3.f / 8;
  float maxPow  = 1.f / 2;
  float maxPowScale = 4;
  double scalePow = Numbers.log(Numbers.log(maxPow, basePow), maxPowScale);

  // x = 2 * Pi / frequency
  // y = amplitude
  // z = width / 2
  // z = power: basePow - brighter, 1.0 - no change
  static void sinParamsDefault(V4f result) {
    result.set((float)Math.PI / 3, 1, 1.25f, basePow);
  }

  static void scaleSinParams(V4f arg, float scale, V4f result) {
    // pow ranges from basePow to maxPow
    // basePow when scale == 1, maxPow when scale == maxPowScale
    float pow = scale >= maxPowScale ? maxPow
        : (float) Math.pow(arg.w, Math.pow(scale, scalePow));
    result.set(
        arg.x / scale,
        arg.y * scale,
        arg.z * scale, pow
    );
  }

  static float offset(V4f params) {
    return (params.y - (int) params.y) < .25f ? .5f : 0;
  }

  static int boxExtend(V4f params) {
    return (int) (params.y + params.z + 1.5f);
  }
}
