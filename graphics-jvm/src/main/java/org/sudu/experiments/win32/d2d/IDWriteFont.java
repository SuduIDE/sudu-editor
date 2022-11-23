package org.sudu.experiments.win32.d2d;

public class IDWriteFont {
  // https://learn.microsoft.com/en-us/windows/win32/api/dwrite/ns-dwrite-dwrite_font_metrics

  // struct DWRITE_FONT_METRICS:
  //  UINT16 designUnitsPerEm, ascent, descent
  //  INT16  lineGap
  //  UINT16 capHeight, xHeight
  //  INT16  underlinePosition
  //  UINT16 underlineThickness
  //  INT16  strikethroughPosition
  //  UINT16 strikethroughThickness

  // DWRITE_FONT_METRICS.length == 10
  public static char[] newMetrics() { return new char[10]; }

  public static native int GetMetrics(long _this, char[] DWRITE_FONT_METRICS);

  public static float ascent(char[] metrics) {
    return (float) metrics[1] / metrics[0];
  }

  public static float descent(char[] metrics) {
    return (float) metrics[2] / metrics[0];
  }
}
