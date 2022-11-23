package org.sudu.experiments.win32.d2d;

public class IWICBitmapLock {

  // returns stride
  public static native int GetStride(long _this, int[] hr);

  // returns
  //   the blockPtr if (blockSize == expectedSize),
  //    hr[0] <- invalid arg
  public static native long GetDataPointer(long _this, int expectedSize, int[] hr);

  // returns HRESULT and
  //   blockPtr  -> ptr_size[0]
  //   blockSize -> ptr_size[1]
  // ptr_size.length == 2
  public static native int GetDataPointer(long _this, long[] ptr_size);
}
