package org.sudu.experiments.win32;

public interface Win32HitTest {
  int HTCLIENT    = 1;
  int HTCAPTION   = 2;
  int HTSYSMENU   = 3;
  int HTMINBUTTON = 8;
  int HTMAXBUTTON = 9;
  int HTCLOSE     = 20;
  static boolean hitClient(long lParam) {
    short hitTest = (short) Win32.LOWORD(lParam);
    return hitTest == Win32HitTest.HTCLIENT;
  }
}
