package org.sudu.experiments.win32.shobj;

public class IFileOpenDialog extends IFileDialog {

  public static native long CoCreateInstance(int[] hr);

  // returns IShellItemArray
  public static native long GetResults(long _this, int[] hr);
}

