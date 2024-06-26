package org.sudu.experiments.editor.worker;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.worker.ArrayView;

public class ArgsCast {
  public static ArrayView array(Object[] args, int index) {
    return (ArrayView) args[index];
  }

  public static String string(Object[] args, int index) {
    return (String) args[index];
  }

  public static FileHandle file(Object[] args, int index) {
    return (FileHandle) args[index];
  }

  public static DirectoryHandle dir(Object[] args, int index) {
    return (DirectoryHandle) args[index];
  }
}
