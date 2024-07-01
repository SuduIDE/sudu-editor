package org.sudu.experiments.ui.fs;

import org.sudu.experiments.FileHandle;

import java.util.Arrays;
import java.util.function.Consumer;

import org.sudu.experiments.FileHandle.SyncAccess;

public class FileCompare {

  static final int maxArraySize = 2 * 1024 * 1024;

  DiffResult result;
  SyncAccess left, right;
  String error;

  public FileCompare(DiffResult r, FileHandle left, FileHandle right) {
    result = r;
    Consumer<String> onError = this::onError;
    left.syncAccess(this::leftAccess, onError);
    right.syncAccess(this::rightAccess, onError);
  }

  private void onError(String error) {
    System.err.println(error);
    this.error = error;
    if (left != null) left = close(left);
    if (right != null) right = close(right);
    result.onCompared(false);
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
    try {
      boolean equals = compare();
      result.onCompared(equals);
    } catch (Exception e) {
      System.err.println(e.getMessage());
      result.onCompared(false);
    } finally {
      left = close(left);
      right = close(right);
    }
  }

  private SyncAccess close(SyncAccess file) {
    try { file.close(); } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    return null;
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
      int lRead = (int) left.read(leftText, pos);
      int rRead = (int) right.read(rightText, pos);
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
