package org.sudu.experiments.ui.fs;

import org.sudu.experiments.FileHandle;

import java.util.Arrays;
import org.sudu.experiments.FileHandle.SyncAccess;

public class FileDiffSync {

  static final int maxArraySize = 2 * 1024 * 1024;

  DiffResult result;
  SyncAccess left, right;

  public FileDiffSync(DiffResult r, FileHandle left, FileHandle right) {
    result = r;
    left.syncAccess(this::sendLeft, System.err::println);
    right.syncAccess(this::sendRight, System.err::println);
  }

  private void sendLeft(SyncAccess left) {
    this.left = left;
    if (this.right != null) compareAndClose();
  }

  public void sendRight(SyncAccess right) {
    this.right = right;
    if (this.left != null) compareAndClose();
  }

  private void compareAndClose() {
    try {
      boolean equals = compare();
      result.onCompared(equals);
    } catch (Exception e) {
      System.err.println(e.getMessage());
      result.onCompared(false);
    } finally {
      close(left);
      close(right);
    }
  }

  private void close(SyncAccess file) {
    try { file.close(); } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  private boolean compare() {
    double lSize = left.getSize();
    double rSize = right.getSize();
    if (lSize != rSize) {
      // todo: add diff only in line separators
      return false;
    }
    int iSize = (int) Math.max(lSize, Integer.MAX_VALUE);
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
