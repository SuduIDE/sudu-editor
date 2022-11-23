package org.sudu.experiments.angle;

import org.sudu.experiments.math.V2i;
import org.sudu.experiments.nativelib.AngleDll;
import org.sudu.experiments.nativelib.SuduDll;
import org.sudu.experiments.win32.Win32;
import org.sudu.experiments.win32.Win32AngleGL;
import org.sudu.experiments.win32.WindowPeer;

import java.util.function.Supplier;

public class AngleWindowSizeTest {

  static boolean opened = true;
  static boolean repaint = true;
  static int windowWidth = 1280;
  static int windowHeight = 720;
  static Supplier<V2i> angleSize;
  static Runnable requestAnimFrame;
  static long timerId;

  public static void main(String[] args) throws InterruptedException {
    AngleDll.require();
    SuduDll.require();

    long hWnd = Win32.CreateWindow(AngleWindowSizeTest::windowProc, "egl window",
        Win32.CW_USEDEFAULT, Win32.CW_USEDEFAULT, windowWidth, windowHeight,
        Win32.GetModuleHandle0(), 2000);

    long hDC = Win32.GetDC(hWnd);

    long display = AngleEGL.getPlatformDisplayD3D11(hDC);
    t(display != 0, "AngleEGL.getPlatformDisplayD3D11 failed: ");

    boolean initialize = AngleEGL.initialize(display);
    t(initialize, "AngleEGL.initialize(display) failed: ");

    long config = AngleEGL.chooseConfig8888(display);
    t(config != 0, "AngleEGL.chooseConfig8888 failed: ");

    long surface = AngleEGL.createWindowSurface(display, config, hWnd, null);
    t(surface != 0, "AngleEGL.createWindowSurface failed");

    angleSize = () -> getSurfaceSize(display, surface);

    long context = AngleEGL.createContext(display, config, 0, true);
    t(context != 0, "AngleEGL.createContext failed: ");

    boolean makeCurrent = AngleEGL.makeCurrent(display, surface, surface, context);
    t(makeCurrent, "AngleEGL.makeCurrent ");

    V2i size = getSurfaceSize(display, surface);
    System.out.println("AngleEGL.querySurfaceSize = " + size);

    t(size.x == windowWidth && size.y == windowHeight);

    t(AngleEGL.swapInterval(display, 0));
    Win32.ShowWindow(hWnd, Win32.SW_NORMAL);
    t(AngleEGL.swapBuffers(display, surface));

    Win32AngleGL gl = new Win32AngleGL();

    requestAnimFrame = () -> requestAnimationFrame(display, surface, size, gl);

    while (Win32.PeekTranslateDispatchMessage() && opened) {
      if (!requestAnimationFrame(display, surface, size, gl)) {
        Thread.sleep(1);
      }
    }

    t(AngleEGL.makeCurrent(display, 0, 0, 0));
    t(AngleEGL.destroyContext(display, context));
    t(AngleEGL.destroySurface(display, surface));
    t(AngleEGL.terminate(display));
  }

  static boolean requestAnimationFrame(long display, long surface, V2i size, Win32AngleGL gl) {
    V2i sizeNow = getSurfaceSize(display, surface);
    if (!size.equals(sizeNow)) {
      System.out.println("requestAnimationFrame: Angle surface NEW size = "
          + sizeNow + ", repaint = " + repaint);
      size.set(sizeNow);
      repaint = true;
    }

    boolean sizeEquals = sizeNow.x == windowWidth && sizeNow.y == windowHeight;

    if (!sizeEquals) {
      System.out.println("requestAnimationFrame: sizeEquals = " + sizeEquals);
    }

    if (!repaint) return false;

    dumpSize(sizeNow, "  repaint");
    gl.viewport(0,0, sizeNow.x, sizeNow.y);
    gl.clearColor(0, 0, 0.25f, 1);
    gl.clear(AngleGL.COLOR_BUFFER_BIT);
    AngleEGL.swapBuffers(display, surface);
    repaint = false;
    return true;
  }

  static V2i getSurfaceSize(long display, long surface) {
    int[] value = new int[1];
    boolean q1 = AngleEGL.querySurface(display, surface, AngleEGL.EGL_WIDTH, value);
    int surfaceWidth = q1 ? value[0] : 0;
    boolean q2 = AngleEGL.querySurface(display, surface, AngleEGL.EGL_HEIGHT, value);
    int surfaceHeight = q2 ? value[0] : 0;
    V2i size = AngleEGL.querySurfaceSize(display, surface);
    if (size == null || size.x != surfaceWidth || size.y != surfaceHeight)
      throw new RuntimeException("AngleEGL.querySurfaceSize does not match AngleEGL.querySurface");
    return size;
  }

  static void dumpSize(V2i sizeNow, String title) {
    boolean sizeEquals = sizeNow.x == windowWidth && sizeNow.y == windowHeight;
    System.out.println(title
        + ": window(" + windowWidth + " * " + windowHeight + ") "
        + (sizeEquals ? "==" : "NOT EQUALS")
        + " AngleSurface(" + sizeNow.x + " * " + sizeNow.y + ")");
  }

  static long windowProc(long hWnd, int msg, long wParam, long lParam) {
    if (msg != WindowPeer.WM_MOUSEMOVE && msg != WindowPeer.WM_SETCURSOR && msg != WindowPeer.WM_TIMER)
      System.out.println("msg = " + WindowPeer.wmToString(msg));

    long result = Win32.DefWindowProcW(hWnd, msg, wParam, lParam);

    switch (msg) {
      case WindowPeer.WM_SIZE -> {
        windowWidth = Win32.LOWORD(lParam);
        windowHeight = Win32.HIWORD(lParam);
        System.out.println("  WM_SIZE: " + windowWidth + " x " + windowHeight);
        dumpSize(angleSize.get(), "  WM_SIZE");
        repaint = true;
      }
      case WindowPeer.WM_PAINT -> {
        dumpSize(angleSize.get(), "  WM_PAINT");
        repaint = true;
      }
      case WindowPeer.WM_CLOSE -> opened = false;

      case WindowPeer.WM_ENTERSIZEMOVE -> timerId = Win32.SetTimer(hWnd, 0, 16, 0);
      case WindowPeer.WM_EXITSIZEMOVE -> Win32.KillTimer(hWnd, 0);
      case WindowPeer.WM_TIMER -> requestAnimFrame.run();
    }

    return result;
  }

  static void t(boolean value, String title) {
    if (!value) throw new RuntimeException(title + AngleEGL.getErrorString());
  }

  static void t(boolean value) { t(value, "egl operation failed:"); }
}
