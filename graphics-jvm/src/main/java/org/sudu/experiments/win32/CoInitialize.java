package org.sudu.experiments.win32;

public interface CoInitialize {
  int hResult = Win32.CoInitialize();

  static void check() {
    if (hResult < 0)
      throw new RuntimeException(
          "CoInitialize failed: 0x" + Integer.toHexString(hResult));
  }
}
