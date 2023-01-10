package org.sudu.experiments.win32.d2d;

import org.sudu.experiments.win32.Win32;

public class IDWriteFontCollection {

  // returns BOOL
  public static native int FindFamilyName(long _this, char[] name, int[] familyIndex, int[] hr);

  public static native int GetFontFamilyCount(long _this);
  // returns IDWriteFontFamily
  public static native long GetFontFamily(long _this, int index, int[] hr);

  public static long[] getAllFontFamilies(long _this, int[] hr) {
    int count = GetFontFamilyCount(_this);
    long[] result = new long[count];
    for (int i = 0; i < count; i++) {
      result[i] = IDWriteFontCollection.GetFontFamily(_this, i, hr);
      if (!Win32.hr(hr[0])) {
        System.err.println("IDWriteFontCollection.GetFontFamily failed: "
            + D2d.errorToString(hr[0]));
      }
    }
    return result;
  }
}
