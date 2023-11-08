package org.sudu.experiments.win32.d2d;

import org.sudu.experiments.math.V4f;

public class ID2D1RenderTarget {
  // ID2D1SolidColorBrush
  public static native long CreateSolidColorBrush(long _this, float r, float g, float b, float a, int[] hr);

  public static long CreateSolidColorBrush(long _this, V4f color, int[] hr) {
    return CreateSolidColorBrush(_this, color.x, color.y, color.z, color.w, hr);
  }

  public static native void BeginDraw(long _this);

  public static native void Clear(long _this, float r, float g, float b, float a);

  public static native void Clear(long _this);

  public static native void DrawText(
      long _this,
      char[] string, int stringPos, int stringLength,
      long textFormat,
      float rcLeft, float rcTop, float rcRight, float rcBottom,
      long brush);

  public static native int EndDraw(long _this);

  public static native void SetTextAntialiasMode(long _this, int mode);
  public static native int  GetTextAntialiasMode(long _this);

  // returns IDWriteRenderingParams
  public static native long GetTextRenderingParams(long _this);
  public static native void SetTextRenderingParams(long _this, long pDWriteRenderingParams);
}
