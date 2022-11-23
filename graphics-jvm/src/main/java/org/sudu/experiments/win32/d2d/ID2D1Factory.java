package org.sudu.experiments.win32.d2d;

public class ID2D1Factory {
  public static final int D2D1_FACTORY_TYPE_SINGLE_THREADED = 0;

  public static native long D2D1CreateFactory(int flags, int[] hr);

  public static native long CreateWicBitmapRenderTarget(long _this, long target, int format, int alphaMode, int[] hr);

  public static long CreateWicBitmapRenderTargetPre(long _this, long target, int[] hr) {
    return CreateWicBitmapRenderTarget(_this, target,
        D2d.DXGI_FORMAT_R8G8B8A8_UNORM, D2d.D2D1_ALPHA_MODE_PREMULTIPLIED, hr);
  }
}
