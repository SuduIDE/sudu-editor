package org.sudu.experiments.win32.d2d;

public class IWICImagingFactory {
  public static native long CoCreateInstance(int[] hr);

  // returns IWICBitmap
  public static native long CreateBitmapPreRGBA(long _this, int w, int h, int[] hr);
}
