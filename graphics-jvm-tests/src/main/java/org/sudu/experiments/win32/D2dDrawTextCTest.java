package org.sudu.experiments.win32;

import org.sudu.experiments.BmpWriter;
import org.sudu.experiments.GL;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.fonts.JetBrainsMono;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.win32.d2d.D2d;
import org.sudu.experiments.win32.d2d.ID2D1RenderTarget;
import org.sudu.experiments.win32.d2d.IDWriteRenderingParams;
import org.sudu.experiments.win32.d2d.IWICBitmap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class D2dDrawTextCTest {
  public static void main(String[] args) throws IOException {
    Helper.loadDlls();
    Win32.coInitialize();


    long monitor = Win32.MonitorFromWindow(Win32.HWND_DESKTOP, Win32.MONITOR_DEFAULTTOPRIMARY);
    System.out.println("monitor = " + monitor);

    D2dFactory f = D2dFactory.create(JetBrainsMono.regular());

    long[] textFormats = new long[11];
    for (int i = 0; i < textFormats.length; i++) {
      textFormats[i] = f.textFormatJb(JetBrainsMono.typeface, 10 + i);
      if (textFormats[i] == 0) throw new RuntimeException();
    }

    System.out.println("textFormats[0] = " + textFormats[0]);
    System.out.println("textFormat(SegoeUI) = " + f.textFormatJb(Fonts.SegoeUI, 15));

    int w = 400, h = 30;

    int[] hr = new int[1];

    long pWicBitmap = f.createBitmap(w, h);
    long pD2dRT = f.createRenderTarget(pWicBitmap);

    char[] chars = "Wjbk".toCharArray();

    V4f white = new V4f(1,1,1,1);
    V4f red   = new V4f(1,0,0,1);
    V4f green = new V4f(0,1,0,1);
    V4f blue  = new V4f(0,0,1,1);
    long brushW = ID2D1RenderTarget.CreateSolidColorBrush(pD2dRT, white, hr);
    long brushR = ID2D1RenderTarget.CreateSolidColorBrush(pD2dRT, red, hr);
    long brushG = ID2D1RenderTarget.CreateSolidColorBrush(pD2dRT, green, hr);
    long brushB = ID2D1RenderTarget.CreateSolidColorBrush(pD2dRT, blue, hr);

    dumpTextAntialiasMode(pD2dRT);

    drawAndSave(
        pD2dRT, textFormats, chars, w, h, brushR, pWicBitmap,
        "no-cleartype-red.bmp", new V4f());
    drawAndSave(
        pD2dRT, textFormats, chars, w, h, brushG, pWicBitmap,
        "no-cleartype-green.bmp", new V4f());
    drawAndSave(
        pD2dRT, textFormats, chars, w, h, brushB, pWicBitmap,
        "no-cleartype-blue.bmp", new V4f());

    drawAndSave(
        pD2dRT, textFormats, chars, w, h, brushW, pWicBitmap,
        "no-cleartype.bmp", new V4f());

    System.out.println("SetTextAntialiasMode(D2D1_TEXT_ANTIALIAS_MODE_CLEARTYPE)");
    ID2D1RenderTarget.SetTextAntialiasMode(pD2dRT, D2d.D2D1_TEXT_ANTIALIAS_MODE_CLEARTYPE);
    dumpTextAntialiasMode(pD2dRT);

    GL.ImageData clt_0000 = drawAndSave(
        pD2dRT, textFormats, chars, w, h, brushW, pWicBitmap,
        "cleartype-0000.bmp", new V4f());

    GL.ImageData clt_0001 = drawAndSave(
        pD2dRT, textFormats, chars, w, h, brushW, pWicBitmap,
        "cleartype-0001.bmp", new V4f(0, 0, 0, 1));

    boolean diff = hasDiffRgb(clt_0000, clt_0001);

    System.out.println(diff
        ? "cleartype-0000 rgb differs from cleartype-0001"
        : "cleartype-0000 rgb same as cleartype-0001");


    long textRenderingParams = ID2D1RenderTarget.GetTextRenderingParams(pD2dRT);
    System.out.println("textRenderingParams = " + Long.toHexString(textRenderingParams));
    IUnknown.safeRelease(textRenderingParams);

    long defaultRenderingParams = f.createDefaultRenderingParams();
    long monitorRenderingParams0 = f.createMonitorRenderingParams(0);
    long monitorRenderingParams = f.createMonitorRenderingParams(monitor);
    System.out.println("defaultRenderingParams  = " + Long.toHexString(defaultRenderingParams));
    System.out.println("monitorRenderingParams0 = " + Long.toHexString(monitorRenderingParams0));
    System.out.println("monitorRenderingParams  = " + Long.toHexString(monitorRenderingParams));
    if (defaultRenderingParams != 0) {
      dumpRenderingParams(defaultRenderingParams, "defaultRenderingParams");
    }

    if (defaultRenderingParams != 0) {
      ID2D1RenderTarget.SetTextRenderingParams(pD2dRT, defaultRenderingParams);
      IUnknown.release(defaultRenderingParams);

      drawAndSave(
          pD2dRT, textFormats, chars, w, h, brushW, pWicBitmap,
          "cleartype-defaultParams.bmp", new V4f());
    }


    if (monitorRenderingParams0 != 0) {
      dumpRenderingParams(monitorRenderingParams0, "monitorRenderingParams0");
    }
    if (monitorRenderingParams != 0) {
      dumpRenderingParams(monitorRenderingParams, "monitorRenderingParams");
    }

  }

  static boolean hasDiffRgb(GL.ImageData clt0000, GL.ImageData clt0001) {
    byte[] d0 = clt0000.data, d1 = clt0001.data;
    if (d0.length != clt0001.data.length) return true;
    for (int i = 0, n = d0.length; i < n; i += 4) {
      if (d0[i] != d1[i] || d0[i + 1] != d1[i + 1]
          || d0[i + 2] != d1[i + 2]) return true;
    }
    return false;
  }

  static GL.ImageData drawAndSave(
      long pD2dRT, long[] jbMono,
      char[] chars,
      int w, int h,
      long brush, long pWicBitmap,
      String name,
      V4f clearArgs
  ) throws IOException {
    int xPos = 0;

    ID2D1RenderTarget.BeginDraw(pD2dRT);
    ID2D1RenderTarget.Clear(pD2dRT, clearArgs.x, clearArgs.y, clearArgs.z, clearArgs.w);

    for (int i = 0; i < jbMono.length; i++) {
      ID2D1RenderTarget.DrawText(
          pD2dRT, chars, 0, chars.length,
          jbMono[i], xPos, 0, w, h, brush);
      int measure = (10 + i) * 5 / 2;
      xPos += measure;
    }

    int drawHR = ID2D1RenderTarget.EndDraw(pD2dRT);

    if (drawHR >= 0) {
      return save(name, pWicBitmap, w, h);
    } else {
      System.out.println("drawHR error: " + drawHR);
      return null;
    }
  }

  private static void dumpTextAntialiasMode(long pD2dRT) {
    int mode = ID2D1RenderTarget.GetTextAntialiasMode(pD2dRT);
    System.out.println("TextAntialiasMode = " + mode);
  }

  static GL.ImageData save(String name, long pWicBitmap, int w, int h) throws IOException {
    GL.ImageData image = IWICBitmap.toImage(pWicBitmap, w, h);
    if (image != null) {
      Path path = Paths.get("graphics-jvm-tests", name);
      Files.write(path, BmpWriter.toBmp(image));
      System.out.println("writing image to" + path.toAbsolutePath());
      GL.ImageData alpha = getAlpha(image);
      String nameAlpha = name.substring(0, name.length() - 4) + "-alpha.bmp";
      path = Paths.get("graphics-jvm-tests", nameAlpha);
      Files.write(path, BmpWriter.toBmp(alpha));
      System.out.println("writing alpha to " + path.toAbsolutePath());
    }
    return image;
  }

  static GL.ImageData getAlpha(GL.ImageData image) {
    GL.ImageData alpha = new GL.ImageData(
        image.width, image.height, GL.ImageData.Format.RGBA);
    byte[] aData = alpha.data, sData = image.data;
      int bpp = GL.ImageData.bytesPerLine(image.width, GL.ImageData.Format.RGBA);
      for (int y = 0; y < image.height; y++) {
        int yPos = bpp * y;
        for (int x = 0; x < image.width; x++) {
          int pxPos = yPos + x * 4;
          byte a = sData[pxPos + 3];
          aData[pxPos] = a;
          aData[pxPos + 1] = a;
          aData[pxPos + 2] = a;
          aData[pxPos + 3] = (byte) 255;
        }
      }

    return alpha;
  }

  static void dumpRenderingParams(long params, String t) {
    System.out.println("params: " + t);
    System.out.println("  gamma = "
        + IDWriteRenderingParams.GetGamma(params));
    System.out.println("  enhancedContrast = "
        + IDWriteRenderingParams.GetEnhancedContrast(params));
    System.out.println("  clearTypeLevel = "
        + IDWriteRenderingParams.GetClearTypeLevel(params));
    System.out.println("  pixelGeometry = "
        + dwPixelGeometryName(IDWriteRenderingParams.GetPixelGeometry(params)));
    System.out.println("  renderingMode = "
        + dwRenderingModeName(IDWriteRenderingParams.GetRenderingMode(params)));
  }


  // DWRITE_PIXEL_GEOMETRY_*
  static String dwPixelGeometryName(int pixelGeometry) {
    return switch (pixelGeometry) {
      case D2d.DWRITE_PIXEL_GEOMETRY_FLAT -> "FLAT";
      case D2d.DWRITE_PIXEL_GEOMETRY_RGB -> "RGB";
      case D2d.DWRITE_PIXEL_GEOMETRY_BGR -> "BGR";
      default -> "DWRITE_PIXEL_GEOMETRY_" + pixelGeometry;
    };
  }

  // DWRITE_RENDERING_MODE_*
  static String dwRenderingModeName(int renderingMode) {
    return switch (renderingMode) {
      case D2d.DWRITE_RENDERING_MODE_DEFAULT -> "DEFAULT";
      case D2d.DWRITE_RENDERING_MODE_ALIASED -> "ALIASED";
      case D2d.DWRITE_RENDERING_MODE_GDI_CLASSIC -> "GDI_CLASSIC";
      case D2d.DWRITE_RENDERING_MODE_GDI_NATURAL -> "GDI_NATURAL";
      case D2d.DWRITE_RENDERING_MODE_NATURAL -> "NATURAL";
      case D2d.DWRITE_RENDERING_MODE_NATURAL_SYMMETRIC -> "NATURAL_SYMMETRIC";
      default -> "DWRITE_RENDERING_MODE_" + renderingMode;
    };
  }
}
