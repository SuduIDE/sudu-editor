package org.sudu.experiments.editor.worker.diff;

import org.sudu.experiments.diff.LineDiff;

public class DiffInfo {

  public LineDiff[] lineDiffsL;
  public LineDiff[] lineDiffsR;
  public DiffRange[] ranges;

  public DiffInfo(LineDiff[] lineDiffsL, LineDiff[] lineDiffsR, DiffRange[] ranges) {
    this.lineDiffsL = lineDiffsL;
    this.lineDiffsR = lineDiffsR;
    this.ranges = ranges;
  }

  public DiffRange range(int lineKey, boolean isL) {
    return ranges[rangeBinSearch(lineKey, isL)];
  }

  public int rangeBinSearch(int lineKey, boolean isL) {
    int low = 0;
    int high = ranges.length - 1;

    while (low <= high) {
      int mid = (low + high) >>> 1;
      var midRange = ranges[mid];
      int midFrom = isL ? midRange.fromL : midRange.fromR;
      int midLen = isL ? midRange.lenL : midRange.lenR;

      if (midFrom <= lineKey && lineKey < midFrom + midLen) return mid;
      if (midFrom < lineKey) low = mid + 1;
      else if (midFrom > lineKey) high = mid - 1;
      else return mid;
    }
    return Math.min(low, ranges.length - 1);
  }
}
