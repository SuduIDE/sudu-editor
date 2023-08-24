package org.sudu.experiments.win32;

import org.sudu.experiments.Cursor;

public interface Win32Cursors {

  // https://learn.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-loadcursorw

  long IDC_ARROW    = Win32.LoadCursorW(0, 32512);
  long IDC_HAND     = Win32.LoadCursorW(0, 32649);
  long IDC_IBEAM    = Win32.LoadCursorW(0, 32513);
  long IDC_SIZENS   = Win32.LoadCursorW(0, 32645);
  long IDC_SIZEWE   = Win32.LoadCursorW(0, 32644);
  long IDC_SIZENWSE = Win32.LoadCursorW(0, 32642);
  long IDC_SIZENESW = Win32.LoadCursorW(0, 32643);
  long IDC_HELP     = Win32.LoadCursorW(0, 32651);

  static long toWin32(String cursor) {
    return cursor != null ? switch (cursor) {
      case Cursor.pointer -> IDC_HAND;
      case Cursor.text -> IDC_IBEAM;
      case Cursor.ew_resize -> IDC_SIZEWE;
      case Cursor.ns_resize -> IDC_SIZENS;
      case Cursor.nesw_resize -> IDC_SIZENESW;
      case Cursor.nwse_resize -> IDC_SIZENWSE;
      default -> IDC_ARROW;
    } : IDC_ARROW;
  }
}
