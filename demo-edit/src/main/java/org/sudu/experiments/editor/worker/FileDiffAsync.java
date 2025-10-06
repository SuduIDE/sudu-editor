package org.sudu.experiments.editor.worker;

import org.sudu.experiments.FileHandle;

import java.util.function.Consumer;

class FileDiffAsync extends FileCompareAsync {

  final double address;
  final int bytesPerLine;
  final boolean findNext;
  boolean skipDiff;

  private double lastDiffPos = -1;
  private final double posLimit;

  FileDiffAsync(
      Consumer<Object[]> result,
      FileHandle left,
      FileHandle right,
      double address,
      int bytesPerLine,
      boolean skipDiff,
      boolean findNext
  ) {
    this.result = result;
    this.left = left;
    this.right = right;
    this.address = address;
    this.skipDiff = skipDiff;
    this.bytesPerLine = bytesPerLine;
    this.findNext = findNext;
    posLimit = Math.max(address - FileCompare.maxToRead, 0);
    left.getSize(this::setLeftSize, onError);
    right.getSize(this::setRightSize, onError);
  }

  @Override
  protected void startCompare() {
    if (findNext) filePos = address;
    else {
      readLength = (int) Math.min(address, readLength);
      filePos = Math.max(0, address - readLength);
    }
    nextRequest();
  }

  @Override
  protected void compareBytes(byte[] leftT, byte[] rightT) {
    if (findNext) findNextDiff(leftT, rightT);
    else findPrevDiff(leftT, rightT);
  }

  private void findNextDiff(byte[] leftT, byte[] rightT) {
    int read = Math.min(leftT.length, rightT.length);
    for (int i = 0; i < read; ) {
      int to = Math.min(i + bytesPerLine, read);
      int diffPos = FileCompare.cmpArrays(leftText, rightText, i, to);
      if (diffPos >= 0) {
        if (!skipDiff) {
          FileCompare.send(result, leftSize, rightSize, filePos + i + diffPos);
          return;
        }
      } else skipDiff = false;
      i += bytesPerLine;
    }
    double sizeLimit = Math.min(Math.min(leftSize, rightSize), FileCompare.maxToRead);
    boolean eof = filePos >= sizeLimit;
    leftText = null;
    rightText = null;
    if (eof) {
      double diffPos = leftSize == rightSize || skipDiff ? -1 : Math.min(leftSize, rightSize);
      FileCompare.send(result, leftSize, rightSize, diffPos);
    } else {
      if (readLength * 4 <= maxArraySize) readLength *= 4;
      filePos += read;
      if (filePos - address + readLength > FileCompare.maxToRead)
        readLength = (int) (FileCompare.maxToRead - filePos);
      nextRequest();
    }
  }

  private void findPrevDiff(byte[] leftT, byte[] rightT) {
    int read = Math.min(leftT.length, rightT.length);
    for (int i = read; i > 0; ) {
      int from = i - bytesPerLine;
      int diffPos = FileCompare.cmpArrays(leftText, rightText, from, i);
      if (diffPos >= 0) {
        lastDiffPos = Math.max(filePos + i + diffPos - bytesPerLine, 0);
      } else if (lastDiffPos != -1) {
        FileCompare.send(result, leftSize, rightSize, lastDiffPos);
        return;
      }
      i -= bytesPerLine;
    }

    leftText = null;
    rightText = null;
    boolean eof = filePos <= posLimit;
    if (eof) {
      FileCompare.send(result, leftSize, rightSize, lastDiffPos);
    } else {
      double oldFilePos = filePos;
      if (readLength * 4 <= maxArraySize) readLength *= 4;
      filePos = Math.max(posLimit, filePos - readLength);
      readLength = (int) (oldFilePos - filePos);
      nextRequest();
    }
  }
}
