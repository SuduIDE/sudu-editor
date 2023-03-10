package org.sudu.experiments.win32;

import org.sudu.experiments.*;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.win32.d2d.ID2D1RenderTarget;
import org.sudu.experiments.win32.d2d.IWICBitmap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class D2dDrawTextCTest {
  public static void main(String[] args) throws IOException {
    Helper.loadDlls();
    Win32.coInitialize();

    D2dFactory f = D2dFactory.create(Application.fontLoader(JetBrainsMono.regular()));

    long[] jbMono = new long[11];
    for (int i = 0; i < jbMono.length; i++) {
      jbMono[i] = f.textFormatJb(JetBrainsMono.typeface, 10 + i);
      if (jbMono[i] == 0) throw new RuntimeException();
    }

    long textFormat2 = f.textFormatJb(Fonts.SegoeUI, 15);
    System.out.println("jbMono10 = " + jbMono[0]);
    System.out.println("textFormat2 = " + textFormat2);

    int w = 400, h = 30;

    int[] hr = new int[1];

    long pWicBitmap = f.createBitmap(w, h);
    long pD2dRT = f.createRenderTarget(pWicBitmap);

    V4f color = Color.Cvt.fromHSV(Math.random(), 1, 1);
    long brush = ID2D1RenderTarget.CreateSolidColorBrush(pD2dRT, color, hr);

    ID2D1RenderTarget.BeginDraw(pD2dRT);

    String string = "Wj";
    char[] chars = string.toCharArray();

    int xPos = 0;
    for (int i = 0; i < jbMono.length; i++) {
      ID2D1RenderTarget.DrawText(
          pD2dRT, chars, 0, chars.length,
          jbMono[i], xPos, 0, w, h, brush);
      int measure = (10 + i) * 3 / 2;
      xPos += measure;
    }
    int drawHR = ID2D1RenderTarget.EndDraw(pD2dRT);

    if (drawHR >= 0) {
      save("10-20.bmp", pWicBitmap, w, h);
    }
    System.out.println("drawHR = " + drawHR);
  }

  static void save(String name, long pWicBitmap, int w, int h) throws IOException {
    GL.ImageData image = IWICBitmap.toImage(pWicBitmap, w, h);
    if (image != null) {
      Path path = Paths.get(name);
      Files.write(path, BmpWriter.toBmp(image));
      System.out.println("bmp = " + path.toAbsolutePath());
    }
  }
}
