package org.sudu.experiments.editor.worker;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.function.Consumer;

import static org.sudu.experiments.FileHandle.hiGb;
import static org.sudu.experiments.FileHandle.loGb;

public interface FileCompare {

  int maxToRead = 1024 * 1024 * 1024;

  void on(double leftSize, double rightSize,
          double diffLocation, String error);

  static void compareFiles(
      WorkerJobExecutor executor,
      FileHandle left, FileHandle right,
      FileCompare result
  ) {
    executor.sendToWorker(r -> onBinaryDiffCompared(r, result), asyncCompareFiles, left, right);
  }

  String asyncFindNextDiff = "asyncFindNextDiff";

  static void asyncFindNextDiff(Object[] a, Consumer<Object[]> r) {
    FileHandle left = ArgsCast.file(a, 0);
    FileHandle right = ArgsCast.file(a, 1);
    int[] ints = ArgsCast.intArray(a, 2);
    double address = FileHandle.int2Address(ints[0], ints[1]);
    int bytesPerLine = ints[2];
    boolean skipDiff = ints[3] == 1, findNext = ints[4] == 1;

    if (left.hasSyncAccess() && right.hasSyncAccess()) {
      new FileDiffSync(r, left, right, address, bytesPerLine, skipDiff, findNext);
    } else {
      new FileDiffAsync(r, left, right, address, bytesPerLine, skipDiff, findNext);
    }
  }

  static void onBinaryDiffCompared(Object[] r, FileCompare result) {
    if (isMessage(r)) {
      result.on(0, 0, 0, message(r));
    } else {
      var data = ArgsCast.intArray(r, 0);
      result.on(
          FileHandle.int2Address(data, 0),
          FileHandle.int2Address(data, 2),
          FileHandle.int2Address(data, 4), null);
    }
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
        loGb(lSize), hiGb(lSize),
        loGb(rSize), hiGb(rSize),
        loGb(diffPos), hiGb(diffPos),
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
    return cmpArrays(a, b, 0, n);
  }

  static int cmpArrays(byte[] a, byte[] b, int from, int to) {
    for (int i = from; i < to; i++) {
      if (a[i] != b[i])
        return i - from;
    }
    return -1;
  }
}
