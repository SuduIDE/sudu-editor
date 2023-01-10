package org.sudu.experiments.win32.shobj;

// https://learn.microsoft.com/en-us/windows/win32/api/shobjidl_core/nn-shobjidl_core-ifiledialog
// https://learn.microsoft.com/en-us/windows/win32/api/shobjidl_core/ne-shobjidl_core-_fileopendialogoptions

public class IFileDialog extends IModalWindow {
  // returns HRESULT
  public static native int GetOptions(long _this, int[] options);

  // returns HRESULT
  public static native int SetOptions(long _this, int options);

  @SuppressWarnings({"SpellCheckingInspection", "unused"})
  public interface Options {
    int FOS_OVERWRITEPROMPT = 0x2;
    int FOS_STRICTFILETYPES = 0x4;
    int FOS_NOCHANGEDIR = 0x8;
    int FOS_PICKFOLDERS = 0x20;
    int FOS_FORCEFILESYSTEM = 0x40;
    int FOS_ALLNONSTORAGEITEMS = 0x80;
    int FOS_NOVALIDATE = 0x100;
    int FOS_ALLOWMULTISELECT = 0x200;
    int FOS_PATHMUSTEXIST = 0x800;
    int FOS_FILEMUSTEXIST = 0x1000;
    int FOS_CREATEPROMPT = 0x2000;
    int FOS_SHAREAWARE = 0x4000;
    int FOS_NOREADONLYRETURN = 0x8000;
    int FOS_NOTESTFILECREATE = 0x10000;
    int FOS_HIDEMRUPLACES = 0x20000;
    int FOS_HIDEPINNEDPLACES = 0x40000;
    int FOS_NODEREFERENCELINKS = 0x100000;
    int FOS_OKBUTTONNEEDSINTERACTION = 0x200000;
    int FOS_DONTADDTORECENT = 0x2000000;
    int FOS_FORCESHOWHIDDEN = 0x10000000;
    int FOS_DEFAULTNOMINIMODE = 0x20000000;
    int FOS_FORCEPREVIEWPANEON = 0x40000000;
    int FOS_SUPPORTSTREAMABLEITEMS = 0x80000000;
  }
}
