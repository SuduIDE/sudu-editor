package org.sudu.experiments.angle;

import org.sudu.experiments.BmpWriter;
import org.sudu.experiments.GL;
import org.sudu.experiments.GLApi;
import org.sudu.experiments.win32.Helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.sudu.experiments.angle.AngleEglTest.*;

public class AngleEglOffscreenTest {
  public static void main(String[] args) throws IOException {
    Helper.loadDlls();

    dumpVersion(0);

    long display = AngleEGL.getPlatformDisplayD3D11(0);
    System.out.println("display = " + Long.toHexString(display));
    if (display == 0)
      throw new RuntimeException(e("Failed to getPlatformDisplayD3D11(0): "));

    if (!AngleEGL.initialize(display))
      throw new RuntimeException(e("Failed to initialize EGL: "));

    long config = AngleEGL.chooseConfigPBuffer(display);
    if (config == 0)
      throw new RuntimeException(e("Failed to choose PBuffer EGL config: "));

    System.out.println("config = " + Long.toHexString(config));

    long surface = AngleEGL.createPBufferSurface(display, config, 512, 512);
    if (surface == 0)
        throw new RuntimeException(e("Failed to create EGL surface: "));

    System.out.println("surface = " + surface);

    long context = AngleEGL.createContext(display, config, 0, true);
    if (context == 0)
      throw new RuntimeException(e("AngleEGL.createContext failed: "));

    System.out.println("context = " + context);

    boolean makeCurrent = t(AngleEGL.makeCurrent(display, surface, surface, context));

    System.out.println("AngleEGL.getCurrentContext() = " + Long.toHexString(AngleEGL.getCurrentContext()));

    // Render something
    AngleGL gl = new AngleGL() {};

    gl.clearColor(0.0f, 0.0f, 1.0f, 1.0f);
    gl.clear(AngleGL.COLOR_BUFFER_BIT);

    // Read pixels from EGL surface
    GL.ImageData image = new GL.ImageData(512, 512, GL.ImageData.Format.RGBA);

    AngleGL.readPixels(0, 0, 512, 512, GLApi.Context.RGBA, GLApi.Context.UNSIGNED_BYTE, image.data);

    // Clean up EGL
    AngleEGL.makeCurrent(display, 0, 0, 0);
    t(AngleEGL.destroySurface(display, surface));
    t(AngleEGL.destroyContext(display, context));
    t(AngleEGL.terminate(display));

    // Convert and save to BMP
    var bmp = BmpWriter.toBmp(image);
    Files.write(Path.of(AngleEglOffscreenTest.class.getName() + ".bmp"), bmp);
  }
}
