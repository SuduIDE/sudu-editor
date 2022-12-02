package org.sudu.experiments.win32.d2d;

import org.sudu.experiments.win32.IUnknown;

public class IDWriteFontFamily {
  // returns IDWriteFont
  public static native long GetFirstMatchingFont(
      long _this, int fontWeight, int fontStretch, int fontStyle, int[] hr);

  // returns IDWriteLocalizedStrings
  public static native long GetFamilyNames(long _this, int[] hr);

  public static String getFamilyName(long _this, int[] hr) {
    long names = GetFamilyNames(_this, hr);
    if (names != 0) {
      String value = IDWriteLocalizedStrings.getString(names, 0, hr);
      IUnknown.Release(names);
      if (value != null) return value;
    }

    return "<error: " + D2d.errorToString(hr[0]) + ">";
  }

}
