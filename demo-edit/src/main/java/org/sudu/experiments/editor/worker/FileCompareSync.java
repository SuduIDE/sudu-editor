package org.sudu.experiments.editor.worker;

import org.sudu.experiments.FileHandle;

import java.io.IOException;
import java.util.function.Consumer;

import org.sudu.experiments.FileHandle.SyncAccess;

class FileCompareSync {

  static final int maxArraySize = 2 * 1024 * 1024;

  Consumer<Object[]> result;
  SyncAccess left, right;
  String error;

  FileCompareSync() {}

  FileCompareSync(Consumer<Object[]> r, FileHandle left, FileHandle right) {
    result = r;
    Consumer<String> onError = this::onError;
    left.syncAccess(this::leftAccess, onError, false);
    right.syncAccess(this::rightAccess, onError, false);
  }

  protected void onError(String error) {
    System.err.println(error);
    this.error = error;
    closeAll(false);
    FileCompare.send(result, error);
  }

  protected void leftAccess(SyncAccess l) {
    if (error == null) {
      left = l;
      if (right != null) compareAndClose();
    } else {
      l.close();
    }
  }

  protected void rightAccess(SyncAccess r) {
    if (error == null) {
      right = r;
      if (left != null) compareAndClose();
    } else {
      r.close();
    }
  }

  private void compareAndClose() {
    double diffPos = 0;
    double lSize = 0, rSize = 0;
    try {
      lSize = left.getSize();
      rSize = right.getSize();
      diffPos = compare(lSize, rSize);
    } catch (Exception e) {
      error = e.getMessage();
      System.err.println(error);
    } finally {
      closeAll(true);
    }
    if (error != null) {
      FileCompare.send(result, error);
    } else {
      sendResult(lSize, rSize, diffPos);
    }
  }

  protected void sendResult(double lSize, double rSize, double diffPos) {
    FileCompare.send(result, lSize, rSize, diffPos);
  }

  private void closeAll(boolean emitError) {
    boolean c1 = left == null || left.close();
    boolean c2 = right == null || right.close();;
    left = null;
    right = null;
    if (emitError && (!c1 || !c2)) {
      error = "error closing files: left = " + c1 + ", right = " + c2;
    }
  }

  protected double compare(double lSize, double rSize) {
    if (lSize != rSize) {
      return 0;
    }
    double sizeLimit = Math.min(lSize, FileCompare.maxToRead);
    int bufferSize = (int) Math.min(sizeLimit, maxArraySize);
    byte[] leftText = new byte[bufferSize];
    byte[] rightText = new byte[bufferSize];
    double pos = 0;
    while (pos < sizeLimit) {
      int lRead, rRead;
      try {
        lRead = left.read(leftText, pos);
        rRead = right.read(rightText, pos);
      } catch (IOException e) {
        error = "compare: error reading file: " + e.getMessage();
        return 0;
      }
      if (lRead == 0 && rRead == 0) return -1;
      int diffPos = FileCompare.cmpArrays(leftText, rightText, lRead);
      if (diffPos >= 0) return pos + diffPos;
      pos += lRead;
    }
    return pos < lSize ? pos : -1;
  }
}
