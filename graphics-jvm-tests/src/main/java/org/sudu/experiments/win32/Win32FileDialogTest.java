package org.sudu.experiments.win32;

public class Win32FileDialogTest {

  public static void main(String[] args) {
    Helper.loadDlls();
    Win32.CoInitialize();
    System.out.println("desktopPath = " + Win32FileDialog.getDesktopPath(null));

//    return Win32FileDialog.openFileDialog(Win32.HWND_DESKTOP, "зыхкуе");

    String file = Win32FileDialog.openFileDialog(Win32.HWND_DESKTOP);
    System.out.println("file = " + file);

    String folder = Win32FileDialog.openFolderDialog(Win32.HWND_DESKTOP);
    System.out.println("folder = " + folder);

    String[] files = Win32FileDialog.openFilesDialog(Win32.HWND_DESKTOP);

    System.out.println(files.length > 0 ? "files: " : "no files");
    for (int i = 0; i < files.length; i++) {
      System.out.println("  [" + i + "] = " + files[i]);
    }

    String[] folders = Win32FileDialog.openFoldersDialog(Win32.HWND_DESKTOP);
    System.out.println(folders.length > 0 ? "folders: " : "no folders");
    for (int i = 0; i < folders.length; i++) {
      System.out.println(" [" + i + "] = " + folders[i]);
    }
  }
}
