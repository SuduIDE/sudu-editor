package org.sudu.experiments.editor.worker;

import org.sudu.experiments.FileHandle;

import java.io.IOException;
import java.util.function.Consumer;

class FileDiffSync extends FileCompareSync {

  final double address;
  final int bytesPerLine;
  final boolean findNext;
  boolean skipDiff;
  double filePos;

  FileDiffSync(
      Consumer<Object[]> r,
      FileHandle left,
      FileHandle right,
      double address,
      int bytesPerLine,
      boolean skipDiff,
      boolean findNext
  ) {
    this.result = r;
    this.address = address;
    this.bytesPerLine = bytesPerLine;
    this.findNext = findNext;
    this.skipDiff = skipDiff;
    Consumer<String> onError = super::onError;
    left.syncAccess(this::leftAccess, onError, false);
    right.syncAccess(this::rightAccess, onError, false);
  }

  @Override
  protected void sendResult(double lSize, double rSize, double diffPos) {
    NextDiffTask.send(result, lSize, rSize, diffPos, filePos, skipDiff);
  }

  @Override
  protected double compare(double lSize, double rSize) {
    return findNext ? findNextDiff(lSize, rSize) : findPrevDiff(lSize, rSize);
  }

  private double findNextDiff(double lSize, double rSize) {
    double size = Math.min(lSize, rSize);
    double sizeLimit = Math.min(size, address + FileCompare.maxToRead);
    int bufferSize = (int) Math.min(sizeLimit, maxArraySize);
    byte[] leftText = new byte[bufferSize];
    byte[] rightText = new byte[bufferSize];
    filePos = address;
    while (filePos < sizeLimit) {
      int lRead, rRead;
      try {
        lRead = left.read(leftText, filePos);
        rRead = right.read(rightText, filePos);
      } catch (IOException e) {
        error = "findNextDiff: error reading file: " + e.getMessage();
        return -1;
      }
      if (lRead == 0 && rRead == 0) return -1;
      int read = Math.min(lRead, rRead);
      for (int i = 0; i < read; ) {
        int to = Math.min(i + bytesPerLine, read);
        int diffPos = FileCompare.cmpArrays(leftText, rightText, i, to);
        if (diffPos >= 0) {
          if (!skipDiff) return filePos + i + diffPos;
        } else skipDiff = false;
        i += bytesPerLine;
      }
      filePos += read;
    }
    return filePos >= size && lSize != rSize && !skipDiff ? size : -1;
  }

  private double findPrevDiff(double lSize, double rSize) {
    double size = Math.min(lSize, rSize);
    double posLimit = Math.max(address - FileCompare.maxToRead, 0);
    int bufferSize = (int) Math.min(Math.min(address, size), maxArraySize);
    byte[] leftText = new byte[bufferSize];
    byte[] rightText = new byte[bufferSize];
    filePos = Math.max(0, address - bufferSize);
    double lastDiffPos = -1;
    while (filePos >= posLimit) {
      int lRead, rRead;
      try {
        lRead = left.read(leftText, filePos);
        rRead = right.read(rightText, filePos);
      } catch (IOException e) {
        error = "findPrevDiff: error reading file: " + e.getMessage();
        return -1;
      }
      if (lRead == 0 && rRead == 0) return -1;
      int read = Math.min(lRead, rRead);
      for (int i = read; i > 0; ) {
        int from = i - bytesPerLine;
        int diffPos = FileCompare.cmpArrays(leftText, rightText, from, i);
        if (diffPos >= 0) {
          lastDiffPos = Math.max(filePos + i + diffPos - bytesPerLine, 0);
        } else if (lastDiffPos != -1) {
          return lastDiffPos;
        }
        i -= bytesPerLine;
      }
      if (filePos <= posLimit) break;
      filePos = Math.max(posLimit, filePos - bufferSize);
    }
    return lastDiffPos;
  }
}
