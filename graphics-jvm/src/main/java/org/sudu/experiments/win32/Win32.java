package org.sudu.experiments.win32;

import org.sudu.experiments.CString;

public class Win32 {

  public static final int CW_USEDEFAULT = 0x80000000;

  public static final int E_INVALIDARG = 0x80070057;

  static native long CreateWindow(
      long id, char[] title,
      int x, int y, int width, int height,
      long iconModule, int iconID);

  public static native boolean DestroyWindow(long hWnd);

  // The first time CreateWindow invokes NewGlobalRef on Win32.class
  // This method cleans this up
  public static native void DeleteClassGlobalRef();

  public static long CreateWindow(
      WindowPeer peer, String title,
      int x, int y, int width, int height,
      long iconModule, int iconID
  ) {
    return CreateWindow(peer.peers.alloc(peer), CString.toChar16CString(title),
        x, y, width, height, iconModule, iconID);
  }

  // returns true if no WM_QUIT received
  public static native boolean PeekTranslateDispatchMessage();

  // * Signature: (JJIJJ)J
  static long jWindowProc(long id, long hWnd, int msg, long wParam, long lParam) {
    WindowPeer peer = WindowPeer.peers.get((int) id);
    if (msg == WindowPeer.WM_DESTROY) WindowPeer.peers.free((int) id);
    return peer.windowProc(hWnd, msg, wParam, lParam);
  }

  public static char LOWORD(long lParam) {
    return (char) lParam;
  }

  public static char HIWORD(long lParam) {
    return (char) (lParam >>> 16);
  }

  public static native long DefWindowProcW(long hWnd, int msg, long wParam, long lParam);

  public static native boolean ValidateRect0(long hWnd);

  public static native boolean IsZoomed(long hWnd);
  public static native boolean GetWindowRect(long hWnd, int[] rect4);
  public static native long SendMessageW(long hWnd, int msg, long wParam, long lParam);

  public static final int SW_NORMAL   = 1;
  public static final int SW_MAXIMIZE = 3;

  public static native boolean ShowWindow(long hWnd, int nCmdShow);

  public static native boolean SetWindowTextW(long hWnd, char[] lpString);

  public static native long SetTimer(long hWnd, long nIDEvent, int uElapse, long lpTimerFunc);
  public static native boolean KillTimer(long hWnd, long nIDEvent);

  public static native long LoadCursorW(long hInstance, long lpCursorName);

  public static native long GetDC(long hWnd);

  public static native long GetCommandLineA();
  public static native long GetCommandLineW();

  public static native long GetModuleHandle0();

  public static native long GetPerformanceCounter();
  public static native long GetPerformanceFrequency();

  public static native char GetKeyState(int nVirtKey);

  public static native long __ImageBase();

  static native int invokeCritical(int index, int[] a);
  static native int invokeStandard(int index, int[] a);

  public static native int CoInitialize();

  public static void coInitialize() {
    int hr = CoInitialize();
    if (hr < 0)
      throw new RuntimeException("CoInitialize failed 0x" + Integer.toHexString(hr));
  }

}
