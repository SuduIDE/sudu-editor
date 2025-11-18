package org.sudu.experiments.angle;

import org.sudu.experiments.math.V2i;
import org.sudu.experiments.win32.Helper;
import org.sudu.experiments.win32.Win32;

@SuppressWarnings("ConstantConditions")
public class AngleEglTest {
  public static void main(String[] args) {
    Helper.loadDlls();

    AngleOffscreen.dumpVersion(0);

    int windowWidth = 1280;
    int windowHeight = 720;

    long hWnd = Win32.CreateWindow(Win32::DefWindowProcW, "egl window",
        Win32.CW_USEDEFAULT, Win32.CW_USEDEFAULT, windowWidth, windowHeight,
        Win32.GetModuleHandle0(), 2000);

    long hDC = Win32.GetDC(hWnd);
    AngleEGL.dumpError();

    long display = AngleEGL.getPlatformDisplayD3D11(hDC);
    long display1 = AngleEGL.getPlatformDisplayD3D11(hDC);
    long display2 = AngleEGL.getPlatformDisplayD3D11(hDC);
    System.out.println("display = " + Long.toHexString(display));

    t(display1 == display && display2 == display);

    if (display == 0) throw new RuntimeException(e("AngleEGL.getPlatformDisplayD3D11 failed: "));

    AngleEGL.dumpError();

    boolean initialized = t(AngleEGL.initialize(display));
    System.out.println("initialized = " + initialized);
    AngleEGL.dumpError();

    AngleOffscreen.dumpVersion(display);

    long config = AngleEGL.chooseConfig8888(display);
    System.out.println("chooseConfig8888 = " + Long.toHexString(config));

    if (config == 0) throw new RuntimeException(e("AngleEGL.chooseConfig8888 failed: "));

    long surface = AngleEGL.createWindowSurface(display, config, hWnd, null);
    if (surface == 0) throw new RuntimeException(e("AngleEGL.createWindowSurface failed"));

    System.out.println("surface = " + Long.toHexString(surface));
    AngleEGL.dumpError();

    long context = AngleEGL.createContext(display, config, 0, true);

    if (context == 0) throw new RuntimeException(e("AngleEGL.createContext failed: "));

    System.out.println("context = " + Long.toHexString(context));
    System.out.println("AngleEGL.getCurrentContext() = " + Long.toHexString(AngleEGL.getCurrentContext()));
    AngleEGL.dumpError();

    boolean makeCurrent1 = t(AngleEGL.makeCurrent(display, surface, surface, context));
    System.out.println("makeCurrent1 = " + makeCurrent1);
    System.out.println("EGL_GetError = " + AngleEGL.getErrorString());

    System.out.println("AngleEGL.getCurrentContext() = " + Long.toHexString(AngleEGL.getCurrentContext()));
    System.out.println("EGL_GetError = " + AngleEGL.getErrorString());

    boolean makeCurrent2 = t(AngleEGL.makeCurrent(display, surface, surface, context));
    System.out.println("makeCurrent2 = " + makeCurrent2);

    V2i surfaceSize = AngleEGL.querySurfaceSize(display, surface);

    System.out.println("surfaceWidth = " + surfaceSize.x);
    System.out.println("surfaceHeight = " + surfaceSize.y);

    t(surfaceSize.x == windowWidth && surfaceSize.y == windowHeight);

    boolean swapInterval = t(AngleEGL.swapInterval(display, 0));
    System.out.println("swapInterval = " + swapInterval);

    boolean swapBuffers = t(AngleEGL.swapBuffers(display, surface));
    System.out.println("swapBuffers = " + swapBuffers);

    boolean makeCurrent0 = t(AngleEGL.makeCurrent(display, 0, 0, 0));
    System.out.println("makeCurrent0 = " + makeCurrent0);
    System.out.println("AngleEGL.getCurrentContext() = " + Long.toHexString(AngleEGL.getCurrentContext()));

    boolean destroyContext = t(AngleEGL.destroyContext(display, context));
    System.out.println("destroyContext = " + destroyContext);

    boolean destroySurface = t(AngleEGL.destroySurface(display, surface));
    System.out.println("destroySurface = " + destroySurface);

    boolean terminate = t(AngleEGL.terminate(display));
    System.out.println("terminate = " + terminate);
  }


  static String e(String s) {
    return s.concat(AngleEGL.getErrorString());
  }

  static boolean t(boolean value) {
    return t(value, "egl operation failed: ");
  }

  static boolean t(boolean value, String title) {
    if (!value) throw new RuntimeException(e(title));
    return value;
  }
}
