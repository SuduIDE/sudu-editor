package org.sudu.experiments.ui.fs;

import org.sudu.experiments.FileHandle;

import java.util.function.Consumer;

public class FileCompare {

  public static void compare(
      Consumer<Object[]> send,
      FileHandle left, FileHandle right
  ) {
    if (left.hasSyncAccess() && right.hasSyncAccess()) {
      new FileCompareSync(send, left, right);
    } else {
      new FileCompareAsync(send, left, right);
    }
  }

  static void send(Consumer<Object[]> r, boolean equals) {
    r.accept(new Object[]{new int[]{equals ? 1 : 0}});
  }
}
