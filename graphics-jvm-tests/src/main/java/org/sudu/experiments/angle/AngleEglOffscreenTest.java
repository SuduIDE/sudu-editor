package org.sudu.experiments.angle;

import org.sudu.experiments.*;
import org.sudu.experiments.input.InputListeners;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.tests.WindowSizeTestScene;
import org.sudu.experiments.win32.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.sudu.experiments.angle.AngleEglTest.t;

public class AngleEglOffscreenTest {
  public static void main(String[] args) throws IOException {
    Helper.loadDlls();

    AngleOffscreen offscreen = new AngleOffscreen(true);

    var error = offscreen.initialize();
    if (error != null) throw new RuntimeException(error);

    System.out.println("display = " + Long.toHexString(offscreen.display));
    System.out.println("config  = " + Long.toHexString(offscreen.config));
    System.out.println("context = " + Long.toHexString(offscreen.context));

    V2i size = new V2i(320, 240);

    error = offscreen.createSurface(size.x, size.y);
    if (error != null) throw new RuntimeException(error);

    System.out.println("surface = " + offscreen.surface);
    t(offscreen.makeCurrent());

    System.out.println("AngleEGL.getCurrentContext() = " + Long.toHexString(AngleEGL.getCurrentContext()));

    // init all

    var graphics = offscreen.getGraphics();
    var noWindow = new NoWindow();
    var input = new InputListeners(noWindow::repaint);
    var api = new SceneApi(graphics, input, noWindow);

    graphics.setViewPortAndClientRect(size.x, size.y);

    renderAndSave(api, size);

    // Clean up EGL
    t(offscreen.removeCurrent());
    offscreen.dispose();
  }

  record HueColor(double h, String name) {}

  private static void renderAndSave(SceneApi api, V2i size) throws IOException {

    GL.ImageData image = new GL.ImageData(
        size.x, size.y, GL.ImageData.Format.RGBA);

    HueColor[] tests = {
        new HueColor(1. / 6, "yellow"),
        new HueColor(2. / 6, "green"),
        new HueColor(3. / 6, "skyblue"),
        new HueColor(4. / 6, "blue"),
        new HueColor(5. / 6, "red+blue"),
        new HueColor(0. / 6, "red"),
    };

    int addrLastX = (size.x - 1) * 4;
    int addrLastY = (size.y - 1) * image.bytesPerLine();

    int[] addresses = new int[] {
        0, addrLastX, addrLastY, addrLastY + addrLastX };

    for (HueColor test : tests) {
      // Render something
      WindowSizeTestScene s = new WindowSizeTestScene(api, test.h);
      s.onResize(size, 1.f);
      s.paint();

      // Read pixels from EGL surface
      AngleGL.readPixels(0, 0, size.x, size.y,
          GLApi.Context.RGBA, GLApi.Context.UNSIGNED_BYTE, image.data);

      Color color = new Color(Color.Cvt.fromHSV(test.h, 1, 1));

      // check if corner pixels has correct rgba value
      for (int address : addresses) {
        Color pixel = new Color(
          image.data[address    ], image.data[address + 1],  // RG
          image.data[address + 2], image.data[address + 3]); // BA
        if (!color.equals(pixel))
          System.err.println("Pixel at " + address
              + " differs from color " + color
              + " actual " + pixel);
      }

      // Convert and save to BMP
      var bmp = BmpWriter.toBmp(image);
      var path = Path.of("offscreen " + test.name + ".bmp");
      Files.write(path, bmp);
      System.out.println("saved to " + path);
    }
  }
}
