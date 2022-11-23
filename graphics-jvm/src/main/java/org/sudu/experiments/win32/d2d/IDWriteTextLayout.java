package org.sudu.experiments.win32.d2d;

public class IDWriteTextLayout {

  // https://learn.microsoft.com/en-us/windows/win32/api/dwrite/ns-dwrite-dwrite_text_metrics

  // struct DWRITE_TEXT_METRICS:
  //   float left, top, width
  //   float widthIncludingTrailingWhitespace
  //   float height, layoutWidth, layoutHeight
  //   UINT32 maxBidiReorderingDepth, lineCount

  public static final int widthIdx = 2;
  public static final int widthIncludingTrailingWhitespaceIdx = 3;
  public static final int heightIdx = 3;

  public static float[] newMetrics() { return new float[9]; }

  public static native int GetMetrics(long _this, float[] DWRITE_TEXT_METRICS);
}
