package org.sudu.experiments.ui.fs;

import org.sudu.experiments.FileHandle;

public class FileCompare {

  public static void compare(
      DiffResult send,
      FileHandle left, FileHandle right
  ) {
    if (left.hasSyncAccess() && right.hasSyncAccess()) {
      new FileCompareSync(send, left, right);
    } else {
      new FileCompareAsync(send, left, right);
    }
  }
}
