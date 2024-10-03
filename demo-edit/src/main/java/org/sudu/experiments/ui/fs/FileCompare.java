package org.sudu.experiments.ui.fs;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.editor.worker.ArgsCast;

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

  static void send(Consumer<Object[]> r, String error) {
    r.accept(new Object[]{new int[]{-1}, error});
  }

  static void send(Consumer<Object[]> r, boolean equals, String message) {
    r.accept(new Object[]{new int[]{equals ? 1 : 0}, message});
  }

  public static boolean isEquals(Object[] result) {
    return ArgsCast.intArray(result, 0)[0] == 1;
  }

  public static String message(Object[] result) {
    return result.length > 1 ? ArgsCast.string(result, 1) : null;
  }
}
