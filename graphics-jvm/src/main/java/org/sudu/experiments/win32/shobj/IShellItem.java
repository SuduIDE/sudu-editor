package org.sudu.experiments.win32.shobj;

import org.sudu.experiments.CString;
import org.sudu.experiments.win32.IUnknown;
import org.sudu.experiments.win32.Win32;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
public class IShellItem extends IUnknown {

  // Call CoTaskMemFree to free the memory.
  public static native long GetDisplayName(long _this, int sigdnName, int[] hr);

  public static String getPath(long _this, String valueIfError, int[] hr) {
    return get(_this, SIGDN_FILESYSPATH, valueIfError, hr);
  }

  public static String getURL(long _this, String valueIfError, int[] hr) {
    return get(_this, SIGDN_URL, valueIfError, hr);
  }

  public static String getParentRelative(long _this, String valueIfError, int[] hr) {
    return get(_this, SIGDN_PARENTRELATIVE, valueIfError, hr);
  }

  static String get(long _this, int sigdnName, String valueIfError, int[] hr) {
    long pName = GetDisplayName(_this, sigdnName, hr);
    String path = CString.fromNativeString16(pName, valueIfError);
    if (pName != 0) Win32.CoTaskMemFree(pName);
    return path;
  }

  // https://learn.microsoft.com/en-us/windows/win32/api/shobjidl_core/ne-shobjidl_core-sigdn
  static final int SIGDN_NORMALDISPLAY  = 0;
  static final int SIGDN_FILESYSPATH    = 0x80058000;
  static final int SIGDN_URL            = 0x80068000;
  static final int SIGDN_PARENTRELATIVE = 0x80080001;
}
