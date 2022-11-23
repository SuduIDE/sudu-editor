package org.sudu.experiments.win32.d2d;

public class IDWriteInMemoryFontFileLoader {
  // return IDWriteFontFile
  public static native long CreateInMemoryFontFileReference(
      long _this, long pDWriteFactory, byte[] font, int[] hr);
}
