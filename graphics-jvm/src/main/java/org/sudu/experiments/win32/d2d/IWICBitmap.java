package org.sudu.experiments.win32.d2d;

import org.sudu.experiments.CString;
import org.sudu.experiments.GL;
import org.sudu.experiments.win32.IUnknown;

public class IWICBitmap {
  public static final int WICBitmapLockRead  = 0x1;
  public static final int WICBitmapLockWrite = 0x2;

  // returns IWICBitmapLock
  public static native long Lock(long _this, int width, int height, int flags, int[] hr);

  public static GL.ImageData toImage(long _this, int width, int height) {
    long lock = IWICBitmap.Lock(_this, width, height, WICBitmapLockRead, null);
    if (lock != 0) {
      GL.ImageData image = new GL.ImageData(width, height);
      long pointer = IWICBitmapLock.GetDataPointer(lock, image.data.length, null);
      if (pointer != 0) {
        CString.setByteArrayRegion(image.data, 0, image.data.length, pointer);
      }
      IUnknown.Release(lock);
      return image;
    }
    return null;
  }
}
