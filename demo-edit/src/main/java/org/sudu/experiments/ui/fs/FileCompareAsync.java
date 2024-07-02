package org.sudu.experiments.ui.fs;

import org.sudu.experiments.FileHandle;

import java.util.Arrays;
import java.util.function.Consumer;

class FileCompareAsync {

  static final int maxToRead = 128 * 1024 * 1024;
  static final int maxArraySize = 16 * 1024 * 1024;
  static final int minArraySize = 64 * 1024;

  DiffResult result;
  FileHandle left, right;
  Consumer<String> onError = this::onError;
  Consumer<byte[]> sendLeft = this::sendLeft;
  Consumer<byte[]> sendRight = this::sendRight;

  int readLength = minArraySize;
  byte[] leftText, rightText;
  int filePos = 0;

  FileCompareAsync(
      DiffResult result,
      FileHandle left, FileHandle right
  ) {
    this.result = result;
    this.left = left;
    this.right = right;
    nextRequest();
  }

  private void nextRequest() {
    left.readAsBytes(sendLeft, onError, filePos, readLength);
    right.readAsBytes(sendRight, onError, filePos, readLength);
  }

  private void onError(String cause) {
    System.err.println(cause);
    result.onCompared(false);
    leftText = rightText = null;
  }

  public void sendLeft(byte[] left) {
    leftText = left;
    if (rightText != null)
      compare(leftText, rightText);
  }

  public void sendRight(byte[] right) {
    rightText = right;
    if (leftText != null)
      compare(leftText, rightText);
  }

  private void compare(byte[] leftT, byte[] rightT) {
    boolean equals = Arrays.equals(leftT, rightT);
    boolean eof = leftT.length < readLength;
    leftText = null;
    rightText = null;
    if (!equals) {
      result.onCompared(false);
    } else {
      if (eof || filePos >= maxToRead) {
        if (filePos == maxToRead) {
          System.err.println("max size hit: \n" +
              "\tl=" + left.getFullPath() + "\n" +
              "\tr=" + right.getFullPath());
        }

        result.onCompared(true);
      } else {
        filePos += readLength;
        if (readLength * 4 <= maxArraySize) {
          readLength *= 4;
          if (readLength >= maxArraySize / 4) {
            int m = readLength / 1024 / 1024;
            System.out.println(
                "FileCompare: " + left.getName() + " readLength " + m + "M");
          }
        }
        nextRequest();
      }
    }
  }
}
