package org.sudu.experiments.ui.fs;

import org.sudu.experiments.FileHandle;

import java.util.Arrays;

public class FileDiffHandler {

  private static final int maxToRead = 128 * 1024 * 1024;
  private static final int maxArraySize = 32 * 1024 * 1024;
  private static final int minArraySize = 16 * 1024;

  int readLength = minArraySize;

  byte[] leftText, rightText;
  DiffResult r;
  FileHandle left, right;
  int start = 0;

  public FileDiffHandler(DiffResult r, FileHandle left, FileHandle right) {
    this.r = r;
    this.left = left;
    this.right = right;
  }

  public void beginCompare() {
    this.leftText = null;
    this.rightText = null;
    left.readAsBytes(this::sendLeft, System.err::println, start, readLength);
    right.readAsBytes(this::sendRight, System.err::println, start, readLength);
  }

  public void sendLeft(byte[] left) {
    this.leftText = left;
    if (this.rightText != null) compare();
  }

  public void sendRight(byte[] right) {
    this.rightText = right;
    if (this.leftText != null) compare();
  }

  private void compare() {
    boolean equals = Arrays.equals(leftText, rightText);
    int leftLength = leftText.length;
    if (!equals) r.onCompared(false);
    else {
      if (leftLength < readLength || start >= maxToRead) {
        if (start == maxToRead) {
          System.err.println("max size hit: \n" +
              "\tl=" + left.getFullPath() + "\n" +
              "\tr=" + right.getFullPath());
        }
        r.onCompared(true);
      } else {
        start += readLength;
        if (readLength * 2 <= maxArraySize) {
          readLength *= 2;
          if (readLength >= maxArraySize / 2) {
            int m = readLength / 1024 / 1024;
            System.err.println(left.getName() + ": readLength = " + m + "M");
          }
        }
        beginCompare();
      }
    }
  }
}
