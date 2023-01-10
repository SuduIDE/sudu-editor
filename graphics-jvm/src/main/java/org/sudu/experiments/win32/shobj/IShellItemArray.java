package org.sudu.experiments.win32.shobj;

import org.sudu.experiments.win32.IUnknown;

// https://learn.microsoft.com/en-us/windows/win32/api/shobjidl_core/nn-shobjidl_core-ishellitemarray

public class IShellItemArray extends IUnknown {
  public static native int GetCount(long _this, int[] hr);

  // returns IShellItem
  public static native long GetItemAt(long _this, int index, int[] hr);

  public static String getFilePath(long _this, int index, int[] hr) {
    long pItem = GetItemAt(_this, index, hr);
    String path = pItem != 0 ? IShellItem.getPath(pItem, null, hr) : null;
    IUnknown.safeRelease(pItem);
    return path;
  }
}
