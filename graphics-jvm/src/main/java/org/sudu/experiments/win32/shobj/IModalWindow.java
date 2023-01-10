package org.sudu.experiments.win32.shobj;

// https://learn.microsoft.com/en-us/windows/win32/api/shobjidl_core/nn-shobjidl_core-imodalwindow

import org.sudu.experiments.win32.IUnknown;

public class IModalWindow extends IUnknown {
  public static final int HR_ERROR_CANCELLED = 0x800704c7;
  public static native int Show(long _this, long hwndOwner);
}
