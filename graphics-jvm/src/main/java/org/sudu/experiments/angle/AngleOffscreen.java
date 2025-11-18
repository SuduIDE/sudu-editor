package org.sudu.experiments.angle;

import org.sudu.experiments.LazyInit;
import org.sudu.experiments.win32.CoInitialize;
import org.sudu.experiments.win32.D2dFactory;
import org.sudu.experiments.win32.Win32Graphics;

public class AngleOffscreen {
  static { CoInitialize.check(); }

  long display, config, context;
  long surface;
  LazyInit<Win32Graphics> grFactory;

  public AngleOffscreen(boolean dumpVersion) {
    if (dumpVersion) dumpVersion(0);
  }

  // return null on success, error otherwise
  public String initialize() {
    display = AngleEGL.getPlatformDisplayD3D11(0);
    if (display == 0)
      return e("AngleEGL.getPlatformDisplayD3D11(0) failed: ");

    if (!AngleEGL.initialize(display))
      return e("AngleEGL.initialize failed: ");

    config = AngleEGL.chooseConfigPBuffer(display);
    if (config == 0)
      return e("Failed to choose PBuffer EGL config: ");

    context = AngleEGL.createContext(display, config, 0, true);
    if (context == 0)
      return e("AngleEGL.createContext failed: ");

    grFactory = Win32Graphics.lazyInit(D2dFactory.create());
    return null;
  }

  public Win32Graphics getGraphics() {
    return grFactory.get();
  }

  public String createSurface(int w, int h) {
    disposeSurface();
    surface = AngleEGL.createPBufferSurface(display, config, w, h);

    return surface == 0 ? e("createPBufferSurface failed: ") : null;
  }

  public boolean makeCurrent() {
    return AngleEGL.makeCurrent(display, surface, surface, context);
  }

  public boolean makeCurrentContext() {
    return AngleEGL.makeCurrent(display, 0, 0, context);
  }

  public boolean removeCurrent() {
    return AngleEGL.makeCurrent(display, 0, 0, 0);
  }

  public void dispose() {
    if (display != 0) {
      disposeSurface();
      if (context != 0) {
        if (!AngleEGL.destroyContext(display, context))
          System.err.println(e("AngleEGL.destroyContext failed: "));
        context = 0;
      }
      if (!AngleEGL.terminate(display))
        System.err.println(e("AngleEGL.terminate failed: "));
      display = 0;
    }
  }

  private void disposeSurface() {
    if (surface != 0) {
      if (!AngleEGL.destroySurface(display, surface))
        System.err.println(e("AngleEGL.destroySurface failed: "));
      surface = 0;
    }
  }

  public static String e(String s) {
    return s.concat(AngleEGL.getErrorString());
  }

  public static void dumpVersion(long display) {
    if (display == 0) {
      String version = AngleEGL.getString(display, AngleEGL.EGL_VERSION);
      System.out.println("Angle.version = " + version);
      String extensions = AngleEGL.getString(display, AngleEGL.EGL_EXTENSIONS);
      System.out.println("Angle.extensions = " + extensions);
    } else {
      String vendor = AngleEGL.getString(display, AngleEGL.EGL_VENDOR);
      String clientApis = AngleEGL.getString(display, AngleEGL.EGL_CLIENT_APIS);
      System.out.println("  Angle.vendor = " + vendor);
      System.out.println("  Angle.clientApis = " + clientApis);
    }
    AngleEGL.dumpError();
  }
}
