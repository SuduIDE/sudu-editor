package org.sudu.experiments.win32.d2d;

public class IDWriteTextFormat {
  public static native int GetFontFamilyNameLength(long _this);
  public static native int GetFontFamilyName(long _this, char[] chars);

  public static String getFontFamilyName(long _this) {
    int length = GetFontFamilyNameLength(_this);
    char[] text = new char[length + 1];
    return D2d.hr(GetFontFamilyName(_this, text))
        ? new String(text, 0, length) : "error";
  }
}
