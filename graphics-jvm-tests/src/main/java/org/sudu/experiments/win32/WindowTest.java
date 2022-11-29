package org.sudu.experiments.win32;

import java.util.Arrays;
import java.util.function.BooleanSupplier;

public class WindowTest {

  static class A {
    public static void main(String[] args) {
      Helper.loadDlls();
      System.out.println("Win32.CreateWindow:");

      long hWnd = createWindow(WindowTest::windowProc);

      int[] windowRect = new int[4];
      boolean wRect = Win32.GetWindowRect(hWnd, windowRect);
      System.out.println("GetWindowRect -> " + wRect +
          ", windowRect = " + Arrays.toString(windowRect));

      dispatchWindows("Win32.PeekTranslateDispatchMessage() -> ");

      System.out.println("DestroyWindow ... ");
      boolean dr = Win32.DestroyWindow(hWnd);
      System.out.println("DestroyWindow = " + dr);

      dispatchWindows("last Win32.PeekTranslateDispatchMessage() -> ");

      Win32.DeleteClassGlobalRef();
    }
  }

  static class B {
    static boolean destroyed = false;
    static boolean opened = true;

    public static void main(String[] args) throws InterruptedException {
      Helper.loadDlls();

      long hWnd = createWindow(WindowTest.B::windowProc);
      Win32.ShowWindow(hWnd, Win32.SW_NORMAL);

      run(() -> opened);

      System.out.println("DestroyWindow ... , destroyed = " + destroyed);
      boolean dr = !destroyed && Win32.DestroyWindow(hWnd);
      System.out.println("DestroyWindow = " + dr);

      dispatchWindows("last Win32.PeekTranslateDispatchMessage() -> ");

      Win32.DeleteClassGlobalRef();
    }
    static long windowProc(long hWnd, int msg, long wParam, long lParam) {
      if (msg == WindowPeer.WM_MOUSEMOVE) {
        return Win32.DefWindowProcW(hWnd, msg, wParam, lParam);
      }

      switch (msg) {
        case WindowPeer.WM_CLOSE:
          opened = false;
          System.out.println("WindowPeer.WM_CLOSE");
          break;

        case WindowPeer.WM_DESTROY:
          destroyed = true;
          System.out.println("WindowPeer.WM_DESTROY");
          break;

        case WindowPeer.WM_SETCURSOR:
          return Win32.DefWindowProcW(hWnd, msg, wParam, lParam);
      }
      return WindowTest.windowProc(hWnd, msg, wParam, lParam);
    }
  }

  static class C {
    static int opened = 0;

    public static void main(String[] args) throws InterruptedException {
      Helper.loadDlls();

      long[] hWnd = {
          createWindow(WindowTest.C::windowProc),
          createWindow(WindowTest.C::windowProc),
          createWindow(WindowTest.C::windowProc),
          createWindow(WindowTest.C::windowProc),
      };

      for (long wnd : hWnd) {
        Win32.ShowWindow(wnd, Win32.SW_NORMAL);
      }

      run(() -> opened > 0);

      dispatchWindows("last Win32.PeekTranslateDispatchMessage() -> ");
      String peers = WindowPeer.peers.toString();
      System.out.println("peers = " + peers);

      Win32.DeleteClassGlobalRef();
    }

    static long windowProc(long hWnd, int msg, long wParam, long lParam) {
      if (msg == WindowPeer.WM_MOUSEMOVE) {
        return Win32.DefWindowProcW(hWnd, msg, wParam, lParam);
      }

      switch (msg) {
        case WindowPeer.WM_CREATE:
          opened++;
          System.out.println("WindowPeer.WM_CREATE");
          break;
        case WindowPeer.WM_CLOSE:
          System.out.println("WindowPeer.WM_CLOSE");
          break;

        case WindowPeer.WM_DESTROY:
          opened--;
          break;

        case WindowPeer.WM_SETCURSOR:
          return Win32.DefWindowProcW(hWnd, msg, wParam, lParam);
      }
      return WindowTest.windowProc(hWnd, msg, wParam, lParam);
    }
  }

  static class E {
    public static void main(String[] args) {
      Helper.loadDlls();

      long[] hWnd = new long[3];

      for (int i = 0; i < hWnd.length; i++) {
        hWnd[i] = createWindow(Win32::DefWindowProcW);
        System.out.println("hWnd[" + i + "] = " + Long.toHexString(hWnd[i]));

      }
      for (int i = 0; i < hWnd.length; i++) {
        long hDC = Win32.GetDC(hWnd[i]);
        System.out.println("hDC[" + i + "] = " + Long.toHexString(hDC));
      }

      for (long l : hWnd) {
        Win32.DestroyWindow(l);
      }
      Win32.DeleteClassGlobalRef();
    }
  }

  static long createWindow(WindowPeer windowProc) {
    return Win32.CreateWindow(windowProc, "window peer = " + windowProc,
        Win32.CW_USEDEFAULT, Win32.CW_USEDEFAULT, Win32.CW_USEDEFAULT, Win32.CW_USEDEFAULT,
        Win32.GetModuleHandle0(), 2000);
  }

  static void dispatchWindows(String x) {
    System.out.println(x);
    boolean r = Win32.PeekTranslateDispatchMessage();
    System.out.println(x + r);
  }

  static long windowProc(long hWnd, int msg, long wParam, long lParam) {
    System.out.println("jWindowProc: hWnd = " + Long.toHexString(hWnd) + ", msg = " +
        WindowPeer.wmToString(msg));
    return Win32.DefWindowProcW(hWnd, msg, wParam, lParam);
  }

  static void run(BooleanSupplier appPr) throws InterruptedException {
    while (Win32.PeekTranslateDispatchMessage() && appPr.getAsBoolean()) {
      Thread.sleep(1);
    }
  }
}
