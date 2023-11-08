package org.sudu.experiments.win32.d2d;

import org.sudu.experiments.win32.IUnknown;

public class IDWriteRenderingParams extends IUnknown {
  public static native float GetClearTypeLevel(long _this);
  public static native float GetEnhancedContrast(long _this);
  public static native float GetGamma(long _this);

  // see D2d.DWRITE_PIXEL_GEOMETRY
  public static native int GetPixelGeometry(long _this);

  // see D2d.DWRITE_RENDERING_MODE
  public static native int GetRenderingMode(long _this);

  // IDWriteRenderingParams1 new method
  // public static native float GetGrayscaleEnhancedContrast(long _this);
}
