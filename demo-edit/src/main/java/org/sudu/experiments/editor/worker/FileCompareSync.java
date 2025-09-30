package org.sudu.experiments.editor.worker;

import org.sudu.experiments.FileHandle;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

import org.sudu.experiments.FileHandle.SyncAccess;

class FileCompareSync {

  static final int maxArraySize = 2 * 1024 * 1024;

  Consumer<Object[]> result;
  SyncAccess left, right;
  String error;

  FileCompareSync(Consumer<Object[]> r, FileHandle left, FileHandle right) {
    result = r;
    Consumer<String> onError = this::onError;
    left.syncAccess(this::leftAccess, onError, false);
    right.syncAccess(this::rightAccess, onError, false);
  }

  private void onError(String error) {
    System.err.println(error);
    this.error = error;
    closeAll(false);
    FileCompare.send(result, error);
  }

  private void leftAccess(SyncAccess l) {
    if (error == null) {
      left = l;
      if (right != null) compareAndClose();
    } else {
      l.close();
    }
  }

  private void rightAccess(SyncAccess r) {
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
      FileCompare.send(result, lSize, rSize, diffPos);
    }
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

  private double compare(double lSize, double rSize) {
    if (lSize != rSize) {
      return 0;
    }
    int iSize = (int) Math.min(lSize, FileCompare.maxToRead);
    if (iSize != lSize && iSize != FileCompare.maxToRead) {
      // todo: rework to read first FileCompareAsync.maxToRead bytes
      error = "compare: File is too large to analyze: " + Math.max(lSize, iSize);
      return 0;
    }
    int size = Math.min(iSize, maxArraySize);
    byte[] leftText = new byte[size];
    byte[] rightText = new byte[size];
    double pos = 0;
    while (pos < iSize) {
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
