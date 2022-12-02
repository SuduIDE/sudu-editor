package org.sudu.experiments.win32.d2d;

public class IDWriteLocalizedStrings {

  // Gets the number of language/string pairs.
  public static native int GetCount(long _this);

  // returns BOOL exists
  public static native int FindLocaleName(long _this, char[] localeName, int[] index, int[] hr);

  // returns length
  public static native int GetStringLength(long _this, int index, int[] hr);

  // returns HRESULT
  public static native int GetString(long _this, int index, char[] value);

  public static String[] getAllStrings(long _this, int[] hr) {
    String[] r = new String[GetCount(_this)];
    for (int i = 0; i < r.length; i++) {
      r[i] = getString(_this, i, hr);
    }
    return r;
  }

  public static String getString(long _this, int index, int[] hr) {
    int l = GetStringLength(_this, index, hr);
    if (hr[0] >= 0) {
      char[] data = new char[l + 1];
      hr[0] = GetString(_this, index, data);
      if (hr[0] >= 0) return new String(data, 0, l);
    }
    return null;
  }
}
