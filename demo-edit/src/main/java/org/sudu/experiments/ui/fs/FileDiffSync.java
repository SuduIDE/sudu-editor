package org.sudu.experiments.ui.fs;

import org.sudu.experiments.FileHandle;

import java.util.Arrays;
import org.sudu.experiments.FileHandle.SyncAccess;

public class FileDiffSync {

  private static final int maxToRead = 128 * 1024 * 1024;
  private static final int maxArraySize = 32 * 1024 * 1024;
  private static final int minArraySize = 16 * 1024;

  byte[] leftText, rightText;
  DiffResult result;
  SyncAccess left, right;
  int start = 0;

  public FileDiffSync(DiffResult r, FileHandle left, FileHandle right) {
    result = r;
    left.syncAccess(this::sendLeft, System.err::println);
    right.syncAccess(this::sendRight, System.err::println);
  }

  private void sendLeft(SyncAccess left) {
    this.left = left;
    if (this.right != null)
      result.onCompared(compare());
  }

  public void sendRight(SyncAccess right) {
    this.right = right;
    if (this.left != null)
      result.onCompared(compare());
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
    boolean equals = Arrays.equals(leftText, rightText);
    int leftLength = leftText.length;
    if (!equals) result.onCompared(false);
    else {
      if (leftLength < readLength || start >= maxToRead) {
        if (start == maxToRead) {
          System.err.println("max size hit: \n" +
              "\tl=" + left.getFullPath() + "\n" +
              "\tr=" + right.getFullPath());
        }
        result.onCompared(true);
      } else {
        start += readLength;
        if (readLength * 2 <= maxArraySize) {
          readLength *= 2;
          if (readLength >= maxArraySize / 2) {
            int m = readLength / 1024 / 1024;
            System.err.println(left.getName() + ": readLength = " + m + "M");
          }
        }
      }
    }
  }
}
