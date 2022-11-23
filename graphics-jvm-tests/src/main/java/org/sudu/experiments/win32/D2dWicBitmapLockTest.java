package org.sudu.experiments.win32;

import org.sudu.experiments.BmpWriter;
import org.sudu.experiments.CString;
import org.sudu.experiments.GL;
import org.sudu.experiments.math.XorShiftRandom;
import org.sudu.experiments.win32.d2d.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.sudu.experiments.win32.d2d.D2d.*;

public class D2dWicBitmapLockTest {
  static int[] hr = new int[1];
  public static void main(String[] args) {
    Helper.loadDlls();
    Win32.coInitialize();

    long pD2D1Factory = hr(ID2D1Factory.D2D1CreateFactory(ID2D1Factory.D2D1_FACTORY_TYPE_SINGLE_THREADED, hr));
    long pWicFactory = hr(IWICImagingFactory.CoCreateInstance(hr));

    testBitmap(pD2D1Factory, pWicFactory, 1, 1);
    XorShiftRandom xr = new XorShiftRandom();
    for (int i = 0; i < 10; i++) {
      int width = 1 + xr.nextInt(20);
      int height = 1 + xr.nextInt(20);
      testBitmap(pD2D1Factory, pWicFactory, width, height);
    }

    IUnknown.release(pWicFactory);
    IUnknown.release(pD2D1Factory);
  }

  static void testBitmap(long pD2D1Factory, long pWicFactory, int w, int h) {
    long pWicBitmap = hr(IWICImagingFactory.CreateBitmapPreRGBA(pWicFactory, w, h, hr));
    long pWicRT = hr(ID2D1Factory.CreateWicBitmapRenderTargetPre(pD2D1Factory, pWicBitmap, hr));

    ID2D1RenderTarget.BeginDraw(pWicRT);
    ID2D1RenderTarget.Clear(pWicRT, 0.5f, 0.5f, 0.5f, 1);

    int hrE = ID2D1RenderTarget.EndDraw(pWicRT);
    if (hrE < 0) {
      System.err.println("ID2D1RenderTarget_EndDraw failed: " + D2d.errorToString(hrE));
    }

    long bitmapLock = hr(IWICBitmap.Lock(pWicBitmap, w, h, IWICBitmap.WICBitmapLockRead, hr));
    System.out.println("bitmapLock = 0x" + Long.toHexString(bitmapLock));

    if (bitmapLock != 0) {
      long[] pointer_size = new long[2];
      int stride = IWICBitmapLock.GetStride(bitmapLock, hr);
      if (stride != w*4) System.out.println("stride != w*4: stride=" + stride + ", w*4=" + w*4);
      int HR = IWICBitmapLock.GetDataPointer(bitmapLock, pointer_size);
      int expectedSize = w * h * 4;
      if (pointer_size[1] == expectedSize) {
        System.out.println("actual size equals expectedSize");
        long pointer = IWICBitmapLock.GetDataPointer(bitmapLock, expectedSize, hr);
        if (pointer != pointer_size[0]) throw new RuntimeException("pointer != pointer_size[0]");
        }
      if (HR < 0) {
        System.err.println("IWICBitmapLock_GetDataPointer failed " + D2d.errorToString(HR));
      } else {
//        System.out.println("surface " + w + "x" + h);
        System.out.println("stride = " + stride + ", w * 4 = " + w * 4
            + ((stride == w*4) ? " - MATCH !" : " - not MATCH !!"));
        System.out.println("size = " + pointer_size[1] + ", w*h*4=" + w*h*4);
        System.out.println("pointer = 0x" + Long.toHexString(pointer_size[0]));
      }
      IUnknown.release(bitmapLock);
    }

    IUnknown.release(pWicRT);
    IUnknown.release(pWicBitmap);
  }

  static long hr(long p) {
    if (hr[0] < 0) throw new RuntimeException("D2D error " + errorToString(hr[0]));
    return p;
  }

  static class ClearTest {
    public static void main(String[] args) throws IOException {
      Helper.loadDlls();
      Win32.coInitialize();
      long pD2D1Factory = hr(ID2D1Factory.D2D1CreateFactory(ID2D1Factory.D2D1_FACTORY_TYPE_SINGLE_THREADED, hr));
      long pWicFactory = hr(IWICImagingFactory.CoCreateInstance(hr));
      long pWicBitmap = IWICImagingFactory.CreateBitmapPreRGBA(pWicFactory, 4, 4, hr);
      long pWicRT = hr(ID2D1Factory.CreateWicBitmapRenderTargetPre(pD2D1Factory, pWicBitmap, hr));

      GL.ImageData imageData = new GL.ImageData(4, 4);

      int[] values_1_255 = new int[256];

      read(pWicBitmap, pWicRT, imageData, values_1_255, 255.f);

      for (int i = 0; i < values_1_255.length; i++) {
        if (values_1_255[i] != i) {
          throw new RuntimeException("error, value after clear is not expected: [" +
              i + "] = " + values_1_255[i]);
        }
        System.out.println("values_1_255[" + i + "] = " + values_1_255[i]);
      }

      Path bmp = Paths.get("imageData.bmp");
      Files.write(bmp, BmpWriter.toBmp(imageData));
      System.out.println("bmp written to " + bmp.toAbsolutePath());
    }

    static void read(long pWicBitmap, long pWicRT, GL.ImageData imageData, int[] values, float denominator) {
      for (int i = 0; i < values.length; i++) {
        float v = i / denominator;
        ID2D1RenderTarget.BeginDraw(pWicRT);
        ID2D1RenderTarget.Clear(pWicRT, v, v, v, 1);
        int hrEndDraw = ID2D1RenderTarget.EndDraw(pWicRT);
        if (hrEndDraw >= 0) {
          long lock = IWICBitmap.Lock(pWicBitmap, 4, 4, IWICBitmap.WICBitmapLockRead, hr);
          if (lock != 0) {
            long dataPtr = IWICBitmapLock.GetDataPointer(lock, imageData.data.length, hr);
            if (dataPtr != 0) {
              CString.setByteArrayRegion(imageData.data, 0, imageData.data.length, dataPtr);
            }
            IUnknown.Release(lock);
          }
          int value = imageData.data[0] & 0xFF;
          values[i] = value;
        }
      }
    }
  }

  static class PerfTest {
    public static void main(String[] args) {
      Helper.loadDlls();
      Win32.coInitialize();

      long pWicFactory = hr(IWICImagingFactory.CoCreateInstance(hr));

      long[] surfaces = new long[100];
      XorShiftRandom xr = new XorShiftRandom();
      for (int i = 0; i < surfaces.length; i++) {
        int width = 1 + xr.nextInt(20);
        int height = 1 + xr.nextInt(20);
        surfaces[i] = IWICImagingFactory.CreateBitmapPreRGBA(
            pWicFactory, width, height, hr);

        // IWICBitmap_Lock(surfaces[i], )
      }

      for (int i = 0; i < surfaces.length; i++) {
        surfaces[i] = IUnknown.release(surfaces[i]);
      }

      System.out.println("OK");
    }
  }
}
