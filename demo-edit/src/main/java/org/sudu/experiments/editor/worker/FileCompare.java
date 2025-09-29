package org.sudu.experiments.editor.worker;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.function.Consumer;

public interface FileCompare {

  int maxToRead = 1024 * 1024 * 1024;

  void on(double leftSize, double rightSize,
          double diffLocation, String error);

  static void compareFiles(
      WorkerJobExecutor executor,
      FileHandle left, FileHandle right,
      FileCompare result
  ) {
    executor.sendToWorker(r-> {
      if (isMessage(r)) {
        result.on(0,0,0, message(r));
      } else {
        var data = ArgsCast.intArray(r, 0);
        result.on(
            FileHandle.int2Address(data, 0),
            FileHandle.int2Address(data, 2),
            FileHandle.int2Address(data, 4), null);
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

  static void send(
      Consumer<Object[]> r,
      double lSize, double rSize, double diffPos
  ) {
    int[] msg = {
        FileHandle.loGb(lSize), FileHandle.hiGb(lSize),
        FileHandle.loGb(rSize), FileHandle.hiGb(rSize),
        FileHandle.loGb(diffPos), FileHandle.hiGb(diffPos),
    };
    r.accept(new Object[]{msg});
  }

  static void send(Consumer<Object[]> r, String error) {
    r.accept(new Object[]{error});
  }

  static String message(Object[] result) {
    return ArgsCast.string(result, 0);
  }

  static boolean isMessage(Object[] result) {
    return result[0] instanceof String;
  }

  static boolean filesEquals(double size1, double size2, double diffPos) {
    return size1 == size2 && (diffPos < 0 || diffPos == maxToRead);
  }

  static int cmpArrays(byte[] a, byte[] b) {
    int min = Math.min(a.length, b.length);
    var cmp = cmpArrays(a, b, min);
    return cmp < 0 && a.length != b.length ? min : cmp;
  }

  static int cmpArrays(byte[] a, byte[] b, int n) {
    for (int i = 0; i < n; i++) {
      if (a[i] != b[i])
        return i;
    }
    return -1;
  }
}
