package org.sudu.experiments.win32;

import org.sudu.experiments.Application;
import org.sudu.experiments.Canvas;
import org.sudu.experiments.FontLoaderJvm;
import org.sudu.experiments.JetBrainsMono;

public class D2dAddFontFileCTest {
  public static void main(String[] args) {
    Helper.loadDlls();
    Win32.coInitialize();

    D2dFactory factory = D2dFactory.create();

    FontLoaderJvm loader = Application.fontLoader(JetBrainsMono.all());
    byte[] font = loader.loader.apply(JetBrainsMono.Medium);
    if (font == null)
      throw new RuntimeException("font == null");

    boolean fontOk = factory.addFontFile(font);
    if (!fontOk)
      throw new RuntimeException("addFontFile failed: ".concat(factory.errorString()));

    long fontCollection = factory.getFontCollection();
    if (fontCollection == 0)
      throw new RuntimeException("getFontCollection failed: ".concat(factory.errorString()));

    Canvas canvas = factory.create(10, 10);
    if (canvas == null)
      throw new RuntimeException("factory.createCanvas(10, 10) failed");

    canvas.dispose();
    factory.dispose();
    System.out.println("D2dCanvasFactoryTest pass");
  }
}
