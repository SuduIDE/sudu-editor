package org.sudu.experiments.editor.worker;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.function.Consumer;

public interface FileCompare {

  void on(boolean equals, String error);

  static void asyncCompareFiles(
      WorkerJobExecutor executor,
      FileHandle left, FileHandle right,
      FileCompare result
  ) {
    executor.sendToWorker(r-> {
      if (r.length > 1) {
        result.on(false, message(r));
      } else {
        result.on(isEquals(r), null);
      }
    }, asyncCompareFiles, left, right);
  }

  String asyncCompareFiles = "asyncCompareFiles";

  static void asyncCompareFiles(Object[] a, Consumer<Object[]> r) {
    FileHandle left = ArgsCast.file(a, 0);
    FileHandle right = ArgsCast.file(a, 1);
    if (left.hasSyncAccess() && right.hasSyncAccess()) {
      new FileCompareSync(r, left, right);
    } else {
      new FileCompareAsync(r, left, right);
    }
  }

  static void send(Consumer<Object[]> r, boolean equals) {
    r.accept(new Object[]{new int[]{equals ? 1 : 0}});
  }

  static void send(Consumer<Object[]> r, String error) {
    r.accept(new Object[]{new int[]{-1}, error});
  }

  static boolean isEquals(Object[] result) {
    return ArgsCast.intArray(result, 0)[0] == 1;
  }

  static String message(Object[] result) {
    return result.length > 1 ? ArgsCast.string(result, 1) : null;
  }
}
