package org.sudu.experiments.win32.d2d;

public class IDWriteFontSetBuilder1 {
  public static native int AddFontFile(long _this, long pDWriteFontFile);
  // returns IDWriteFontSet
  public static native long CreateFontSet(long _this, int[] hr);
}
