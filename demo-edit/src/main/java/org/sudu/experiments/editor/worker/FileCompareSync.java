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
    boolean equals = false;
    try {
      equals = compare();
    } catch (Exception e) {
      error = e.getMessage();
      System.err.println(error);
    } finally {
      closeAll(true);
    }
    if (error != null) {
      FileCompare.send(result, error);
    } else {
      FileCompare.send(result, equals);
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

  private boolean compare() {
    double lSize = left.getSize();
    double rSize = right.getSize();
    if (lSize != rSize) {
      // todo: add diff only in line separators
      return false;
    }
    int iSize = (int) Math.min(lSize, Integer.MAX_VALUE);
    if (iSize != lSize) {
      System.err.println("File is too large to analyze: " + lSize);
      return true;
    }
    byte[] leftText = new byte[Math.min(iSize, maxArraySize)];
    byte[] rightText = new byte[leftText.length];
    for (double pos = 0; pos < iSize; ) {
      int lRead = 0, rRead = 0;
      try {
        lRead = left.read(leftText, pos);
        rRead = right.read(rightText, pos);
      } catch (IOException e) {
        System.err.println(
            "compare: error reading file: " + e.getMessage());
        return false;
      }
      if (lRead != rRead) return false;
      if (lRead == 0) return true;
      boolean equals = Arrays.equals(
          leftText, 0, lRead,
          rightText, 0, rRead);
      if (!equals) return false;
      pos += lRead;
    }
    return true;
  }
}
