package org.sudu.experiments.win32;

import org.sudu.experiments.CString;
import org.sudu.experiments.math.V2i;

public class Win32 {

  public static final int E_INVALIDARG   = 0x80070057;
  public static final int E_ACCESSDENIED = 0x80070005;

  public static final int CW_USEDEFAULT = 0x80000000;
  public static final long HWND_DESKTOP = 0;

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

  public static int GET_X_LPARAM(long lParam) { return (short) LOWORD(lParam); }

  public static int GET_Y_LPARAM(long lParam) { return (short) HIWORD(lParam); }

  public static native long DefWindowProcW(long hWnd, int msg, long wParam, long lParam);

  public static native boolean ValidateRect0(long hWnd);

  public static native boolean IsZoomed(long hWnd);

  public static native boolean GetWindowRect(long hWnd, int[] rect4);
  public static native boolean ScreenToClient(long hWnd, int x, int y, int[] result2);

  public static boolean ScreenToClient(long hWnd, V2i point) {
    int[] pt = new int[2];
    boolean r = ScreenToClient(hWnd, point.x, point.y, pt);
    if (r) point.set(pt[0], pt[1]);
    return r;
  }

  public static native long SendMessageW(long hWnd, int msg, long wParam, long lParam);

  public static final int SW_NORMAL   = 1;
  public static final int SW_MAXIMIZE = 3;

  public static native boolean ShowWindow(long hWnd, int nCmdShow);

  public static native boolean SetWindowTextW(long hWnd, char[] lpString);

  public static native long SetTimer(long hWnd, long nIDEvent, int uElapse, long lpTimerFunc);
  public static native boolean KillTimer(long hWnd, long nIDEvent);



  public static native long LoadCursorW(long hInstance, long lpCursorName);
  public static native long SetCursor(long hCursor);

  public static native long GetDC(long hWnd);
  public static native long SetCapture(long hWnd);
  public static native int  ReleaseCapture();
  public static native int  GetDpiForWindow(long hWnd);
  public static native long GetFocus();

  // HMONITOR
  public static final int MONITOR_DEFAULTTONULL    = 0x00000000;
  public static final int MONITOR_DEFAULTTOPRIMARY = 0x00000001;
  public static final int MONITOR_DEFAULTTONEAREST = 0x00000002;

  public static native long MonitorFromWindow(long hWnd, int dwFlags);

  public static native long GetCommandLineA();
  public static native long GetCommandLineW();

  public static native long GetModuleHandle0();

  // clipboard
  public static native boolean OpenClipboard(long hwnd);
  public static native boolean EmptyClipboard();
  public static native boolean CloseClipboard();
  // handle
  public static native long GlobalAlloc(int uFlags, long dwBytes);
  // ptr
  public static native long GlobalLock(long handle);
  public static native boolean GlobalUnlock(long handle);

  public static final int CF_UNICODETEXT = 13;
  public static native long SetClipboardData(int format, long handle);
  public static native long GetClipboardData(int format);

  public static native void CoTaskMemFree(long data);

  public static native long GetPerformanceCounter();
  public static native long GetPerformanceFrequency();

  public static native char GetKeyState(int nVirtKey);
  public static native char GetAsyncKeyState(int nVirtKey);

  public static native long __ImageBase();

  public static native int CoInitialize();

  public static void coInitialize() {
    int hr = CoInitialize();
    if (hr < 0) {
      throw new RuntimeException("CoInitialize failed: 0x" + Integer.toHexString(hr));
    }
  }

  // Shcore.dll
  public static final int PROCESS_DPI_UNAWARE = 0;
  public static final int PROCESS_SYSTEM_DPI_AWARE = 1;
  public static final int PROCESS_PER_MONITOR_DPI_AWARE = 2;
  public static native int SetProcessDpiAwareness(int value);

  public static void setProcessDpiAwareness(int value) {
    int hr = SetProcessDpiAwareness(value);
    if (hr < 0) {
      String name = switch (value) {
        case PROCESS_DPI_UNAWARE -> "PROCESS_DPI_UNAWARE";
        case PROCESS_SYSTEM_DPI_AWARE -> "PROCESS_SYSTEM_DPI_AWARE";
        case PROCESS_PER_MONITOR_DPI_AWARE -> "PROCESS_PER_MONITOR_DPI_AWARE";
        default -> "errorValue " + value;
      };
      System.out.println("SetProcessDpiAwareness(" + name + ") failed: " + errorToString(hr));
    }
  }

  public static String errorToString(int errorCode) {
    return switch (errorCode) {
      case E_INVALIDARG -> "E_INVALIDARG";
      case E_ACCESSDENIED -> "E_ACCESSDENIED";
      default -> "0x" + Integer.toHexString(errorCode);
    };
  }

  public static boolean setClipboardText(long hwnd, String value) {
    if (OpenClipboard(hwnd)) {
      try {
        EmptyClipboard();
        char[] chars = CString.toChar16CString(value);
        long handle = GlobalAlloc(0, chars.length * 2L);
        if (handle != 0) {
          long lockPtr = GlobalLock(handle);
          if (lockPtr != 0) {
            CString.getCharArrayRegion(chars, 0, chars.length, lockPtr);
            GlobalUnlock(handle);
            return 0 != SetClipboardData(CF_UNICODETEXT, handle);
          }
        }
      } finally {
        CloseClipboard();
      }
    }
    return false;
  }

  public static String getClipboardText(long hwnd, String onError) {
    if (OpenClipboard(hwnd)) {
      try {
        long handle = GetClipboardData(CF_UNICODETEXT);
        if (handle != 0) {
          long lockPtr = GlobalLock(handle);
          try {
            if (lockPtr != 0) return CString.fromNativeString16(lockPtr);
          } finally {
            GlobalUnlock(handle);
          }
        }
      } finally {
        CloseClipboard();
      }
    }
    return onError;
  }

  public static boolean hr(int x) { return x >= 0; }
}
