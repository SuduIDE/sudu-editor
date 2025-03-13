package org.sudu.experiments.editor.worker;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.function.Consumer;

public interface SizeScanner {

  String asyncSizeScanner = "asyncSizeScanner";

  interface SizeScanResult {
    void on(double sizeL, double sizeR, String error);
  }

  static void scan(
      WorkerJobExecutor workers,
      FileHandle leftFile, FileHandle rightFile,
      SizeScanResult cb
  ) {
    workers.sendToWorker(
        r -> {
          int[] size = ArgsCast.intArray(r, 0);
          String error = ArgsCast.string(r, 1);
          cb.on(size[0], size[1], error);
        }, asyncSizeScanner, leftFile, rightFile);
  }

  static void asyncSizeScanner(Object[] args, Consumer<Object[]> r) {
    FileHandle lFile = ArgsCast.file(args, 0);
    FileHandle rFile = ArgsCast.file(args, 1);
    Result result = new Result();
    Consumer<String> onError = error -> result.on(error, r);
    lFile.getSize(s -> result.on(0, s, r), onError);
    rFile.getSize(s -> result.on(1, s, r), onError);
  }

  class Result {
    double[] result = {-1, -1};
    String error;
    int fire = 2;

    void on(int idx, double size, Consumer<Object[]> r) {
      result[idx] = size;
      fire(r);
    }

    void on(String e, Consumer<Object[]> r) {
      error = error == null ? e : error + "," + e;
      fire(r);
    }

    private void fire(Consumer<Object[]> r) {
      if (--fire == 0)
        r.accept(new Object[]{result, error});
    }
  }
}
