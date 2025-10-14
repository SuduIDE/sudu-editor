package org.sudu.experiments.editor.worker;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.function.Consumer;

import static org.sudu.experiments.FileHandle.hiGb;
import static org.sudu.experiments.FileHandle.loGb;
import static org.sudu.experiments.editor.worker.FileCompare.*;

public class NextDiffTask {

  public WorkerJobExecutor executor;
  public FileHandle left;
  public FileHandle right;
  public double address;
  public int bytesPerLine;
  public boolean skipDiff;
  public boolean findNext;
  public Consumer<String> sendNotification;
  public FileCompare result;

  public NextDiffTask(
      WorkerJobExecutor executor,
      FileHandle left,
      FileHandle right,
      double address,
      int bytesPerLine,
      boolean skipDiff,
      boolean findNext,
      Consumer<String> sendNotification,
      FileCompare result
  ) {
    this.executor = executor;
    this.left = left;
    this.right = right;
    this.address = address;
    this.bytesPerLine = bytesPerLine;
    this.skipDiff = skipDiff;
    this.findNext = findNext;
    this.sendNotification = sendNotification;
    this.result = result;
    send();
  }

  public void send() {
    executor.sendToWorker(this::onNextDiffFound, asyncFindNextDiff, left, right, ints());
  }

  void onNextDiffFound(Object[] r) {
    System.out.println("onNextDiffFound: address = " + address);
    if (isMessage(r)) {
      result.on(0, 0, -1, message(r));
      return;
    }
    var data = ArgsCast.intArray(r, 0);
    double sizeL = FileHandle.int2Address(data, 0);
    double sizeR = FileHandle.int2Address(data, 2);
    double diffPos = FileHandle.int2Address(data, 4);
    address = FileHandle.int2Address(data, 6);
    skipDiff = data[8] == 1;
    if (diffPos != -1) {
      result.on(sizeL, sizeR, diffPos, null);
      return;
    }
    double size = Math.min(sizeL, sizeR);
    boolean scanToEnd = findNext ? address >= size : address <= 0;
    if (scanToEnd) {
      String msg = findNext ? "No next change" : "No previous change";
      sendNotification.accept(msg);
    } else {
      String msg = findNext ? "next" : "previous";
      sendNotification.accept("No change found yet, searching for " + msg + " change");
      send();
    }
  }

  private int[] ints() {
    return new int[]{
        loGb(address), hiGb(address),
        bytesPerLine,
        skipDiff ? 1 : 0,
        findNext ? 1 : 0
    };
  }

  public static void send(
      Consumer<Object[]> r,
      double lSize,
      double rSize,
      double diffPos,
      double address,
      boolean skipDiff
  ) {
    int[] msg = {
        loGb(lSize), hiGb(lSize),
        loGb(rSize), hiGb(rSize),
        loGb(diffPos), hiGb(diffPos),
        loGb(address), hiGb(address),
        skipDiff ? 1 : 0
    };
    r.accept(new Object[]{msg});
  }
}
