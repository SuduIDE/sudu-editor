package org.sudu.experiments.win32;

import org.sudu.experiments.CString;
import org.sudu.experiments.win32.shobj.IFileDialog.Options;
import org.sudu.experiments.win32.shobj.IFileOpenDialog;
import org.sudu.experiments.win32.shobj.IModalWindow;
import org.sudu.experiments.win32.shobj.IShellItemArray;

@SuppressWarnings("SpellCheckingInspection")
public class Win32FileDialog {
  static native long SHGetKnownDesktopPath(int[] hr);

  public static String getDesktopPath(String onError) {
    int[] hr = new int[1];
    long desktopP = Win32FileDialog.SHGetKnownDesktopPath(hr);
    if (desktopP != 0) {
      String path = CString.fromNativeString16(desktopP);
      Win32.CoTaskMemFree(desktopP);
      return path;
    } else {
      return onError;
    }
  }

  public static String openFileDialog(long hwnd) {
    return singleString(openFileDialog(hwnd, 0));
  }

  public static String openFolderDialog(long hwnd) {
    return singleString(openFileDialog(hwnd, Options.FOS_PICKFOLDERS));
  }

  static String singleString(String[] result) {
    return result.length == 1 ? result[0] : null;
  }

  public static String[] openFilesDialog(long hwnd) {
    return openFileDialog(hwnd, Options.FOS_ALLOWMULTISELECT);
  }

  public static String[] openFoldersDialog(long hwnd) {
    return openFileDialog(hwnd, Options.FOS_ALLOWMULTISELECT | Options.FOS_PICKFOLDERS);
  }

  static String[] openFileDialog(long hwnd, int flags) {
    String[] r = new String[0];
    int[] hr = new int[1];
    int[] options = new int[1];
    long dialog = IFileOpenDialog.CoCreateInstance(hr), results = 0;
    if (dialog != 0) {
      hr[0] = IFileOpenDialog.GetOptions(dialog, options);
      if (Win32.hr(hr[0])) hr[0] = IFileOpenDialog.SetOptions(dialog,
            options[0] | flags | Options.FOS_FORCEFILESYSTEM);
      if (Win32.hr(hr[0])) hr[0] = IFileOpenDialog.Show(dialog, hwnd);
      if (Win32.hr(hr[0])) results = IFileOpenDialog.GetResults(dialog, hr);
      if (results != 0) r = getResults(results, hr);
    }
    IUnknown.safeRelease(results);
    IUnknown.safeRelease(dialog);
    if (hr[0] < 0 && hr[0] != IModalWindow.HR_ERROR_CANCELLED) {
      System.err.println("OFN error: " + Win32.errorToString(hr[0]));
    }
    return r;
  }

  static String[] getResults(long pShellItemArray, int[] hr) {
    int count = IShellItemArray.GetCount(pShellItemArray, hr);
    String[] r = new String[count];
    for (int i = 0; i < r.length; i++) {
      r[i] = IShellItemArray.getFilePath(pShellItemArray, i, hr);
      if (r[i] == null) return new String[0];
    }
    return r;
  }
}
