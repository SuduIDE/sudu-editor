package org.sudu.experiments.editor.worker;

import org.sudu.experiments.FileHandle;

import java.util.function.Consumer;

class FileCompareAsync {

  static final int maxArraySize = 16 * 1024 * 1024;
  static final int minArraySize = 64 * 1024;

  Consumer<Object[]> result;
  FileHandle left, right;
  Consumer<String> onError = this::onError;
  Consumer<byte[]> sendLeft = this::sendLeft;
  Consumer<byte[]> sendRight = this::sendRight;

  int readLength = minArraySize;
  byte[] leftText, rightText;
  double filePos = 0;
  double leftSize = -1, rightSize = -1;

  FileCompareAsync(
      Consumer<Object[]> result,
      FileHandle left, FileHandle right
  ) {
    this.result = result;
    this.left = left;
    this.right = right;
    left.getSize(this::setLeftSize, onError);
    right.getSize(this::setRightSize, onError);
    nextRequest();
  }

  private void setLeftSize(double size) {
    leftSize = size;
    if (rightSize >= 0)
      startCompare();
  }

  private void setRightSize(double size) {
    rightSize = size;
    if (leftSize >= 0)
      startCompare();
  }

  private void startCompare() {
    if (leftSize != rightSize) {
      FileCompare.send(result, leftSize, rightSize, -1);
    } else {
      nextRequest();
    }
  }

  private void nextRequest() {
    left.readAsBytes(sendLeft, onError, filePos, readLength);
    right.readAsBytes(sendRight, onError, filePos, readLength);
  }

  private void onError(String cause) {
    System.err.println(cause);
    FileCompare.send(result, cause);
    leftText = rightText = null;
  }

  public void sendLeft(byte[] left) {
    leftText = left;
    if (rightText != null)
      compareBytes(leftText, rightText);
  }

  public void sendRight(byte[] right) {
    rightText = right;
    if (leftText != null)
      compareBytes(leftText, rightText);
  }

  private void compareBytes(byte[] leftT, byte[] rightT) {
    var diffPos = FileCompare.cmpArrays(leftT, rightT);
    boolean eof = leftT.length < readLength || rightT.length < readLength;
    leftText = null;
    rightText = null;
    if (diffPos >= 0) {
      FileCompare.send(result,
          leftSize, rightSize, filePos + diffPos);
    } else {
      filePos += readLength;
      if (eof || filePos >= FileCompare.maxToRead) {
        if (filePos == FileCompare.maxToRead) {
          System.err.println("max size hit: \n" +
              "\tl=" + left.getFullPath() + "\n" +
              "\tr=" + right.getFullPath());
        }
        FileCompare.sendEquals(result, leftSize, rightSize);
      } else {
        if (readLength * 4 <= maxArraySize) {
          readLength *= 4;
          if (readLength >= maxArraySize / 4) {
            int m = readLength / 1024 / 1024;
            System.out.println(
                "FileCompare: " + left.getName() + " readLength " + m + "M");
          }
        }
        if (filePos + readLength > FileCompare.maxToRead) {
          readLength = (int) (FileCompare.maxToRead - filePos);
        }
        nextRequest();
      }
    }
  }
}
