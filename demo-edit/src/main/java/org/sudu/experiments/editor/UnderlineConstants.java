package org.sudu.experiments.editor;

import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V4f;

interface UnderlineConstants {
  float amplitude = .75f;
  float extend = 1.25f;
  float pow = 3.f / 8;

  float minExtend = extend;
  float maxPow  = 1.f / 2;
  float maxPowScale = 4;
  double scalePow = Numbers.log(Numbers.log(maxPow, pow), maxPowScale);
  float collapsedExtend = 4.f / 16;
  float collapsedExtendBold = 7.f / 16;

  // x = 2 * Pi * frequency
  // y = amplitude
  // z = extend = width / 2
  // z = power: basePow - brighter, 1.0 - no change
  static void underlineParams(V4f result, float dpr) {
    result.set((float)Math.PI / 3, amplitude, extend, pow);
    scaleUnderlineParams(result, dpr, result);
  }

  static void sinParamsCollapsed(
      V4f result, float lineHeight, boolean hover
  ) {
    result.set((float)Math.PI / 1.25f, .50f,
        hover ? collapsedExtendBold : collapsedExtend, pow);
    scaleSinParams(result, lineHeight * .25f, result, 0.5f);
  }

  static void scaleUnderlineParams(V4f arg, float scale, V4f result) {
    scaleSinParams(arg, scale, result, minExtend);
  }

  static void scaleSinParams(V4f arg, float scale, V4f result, float minExtend) {
    // to reduce noise and improve readability
    // we use scale = .25 when scale below .5
    float fScale = scale < 0.5f ? .25f : scale;
    // pow ranges from basePow to maxPow
    // basePow when scale == 1, maxPow when scale == maxPowScale
    float pow = fScale >= maxPowScale ? maxPow
        : (float) Math.pow(arg.w, Math.pow(fScale, scalePow));
    result.set(
        arg.x / fScale, arg.y * fScale,
        Math.max(arg.z * fScale, minExtend), pow
    );
  }

  static float offset(V4f params) {
    return (params.y - (int) params.y) < .25f ? .5f : 0;
  }

  static int boxExtend(V4f params) {
    return (int) (params.y + params.z + 1.5f);
  }
}
