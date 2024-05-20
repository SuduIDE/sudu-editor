package org.sudu.experiments.editor.worker.diff;

import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.LineDiff;
import java.util.ArrayList;
import java.util.List;

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

    if (high != 0) {
      // The last range interval right border is inclusive
      if (isL && ranges[high - 1].toL() == lineKey) return high;
      if (!isL && ranges[high - 1].toR() == lineKey) return high;
    }
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

  // TODO add left & right bin search
  public int leftBS(int lineKey, boolean isL) {
    int ind = rangeBinSearch(lineKey, isL);
    while (ind - 1 >= 0) {
      var range = ranges[ind - 1];
      int len = isL ? range.lenL : range.lenR;
      if (len == 0) ind--;
      else break;
    }
    return ind;
  }

  public int rightBS(int lineKey, boolean isL) {
    int ind = rangeBinSearch(lineKey, isL);
    while (ind + 1 < ranges.length) {
      var range = ranges[ind + 1];
      int len = isL ? range.lenL : range.lenR;
      if (len == 0) ind++;
      else break;
    }
    return ind;
  }

  public void insertAt(int lineKey, int lines, boolean isL) {
    if (isL) this.lineDiffsL = insert(lineKey, lines, lineDiffsL);
    else this.lineDiffsR = insert(lineKey, lines, lineDiffsR);

    var rangeInd = rangeBinSearch(lineKey, isL);
    if (isL) ranges[rangeInd].lenL += lines;
    else ranges[rangeInd].lenR += lines;
    for (int i = rangeInd + 1; i < ranges.length; i++) {
      if (isL) ranges[i].fromL += lines;
      else ranges[i].fromR += lines;
    }
  }

  public void deleteAt(int lineKey, int lines, boolean isL) {
    if (isL) this.lineDiffsL = delete(lineKey, lines, lineDiffsL);
    else this.lineDiffsR = delete(lineKey, lines, lineDiffsR);

    var fromInd = rangeBinSearch(lineKey, isL);
    var toInd = rangeBinSearch(lineKey + lines, isL);

    if (fromInd == toInd) {
      if (isL) ranges[fromInd].lenL -= lines;
      else ranges[fromInd].lenR -= lines;
    } else updateDeleteRanges(lineKey, lines, fromInd, toInd, isL);

    for (int i = toInd + 1; i < ranges.length; i++) {
      if (isL) ranges[i].fromL -= lines;
      else ranges[i].fromR -= lines;
    }
  }

  public void updateDiffInfo(
      int fromL, int toL,
      int fromR, int toR,
      DiffInfo updInfo
  ) {
    System.arraycopy(updInfo.lineDiffsL, 0, lineDiffsL, fromL, toL - fromL);
    System.arraycopy(updInfo.lineDiffsR, 0, lineDiffsR, fromR, toR - fromR);

    ArrayList<DiffRange> newRanges = new ArrayList<>();
    int i = 0;
    DiffRange range;
    while (i < ranges.length && (range = ranges[i]).fromL != fromL && range.fromR != fromR) {
      newRanges.add(range);
      i++;
    }
    for (var nRange: updInfo.ranges) {
      nRange.fromL += fromL;
      nRange.fromR += fromR;
      newRanges.add(nRange);
    }
    while (i < ranges.length && !((range = ranges[i]).toL() == toL && range.toR() == toR)) i++;
    if (i < ranges.length) i++;
    while (i < ranges.length) merge(newRanges, ranges[i++]);
    this.ranges = newRanges.toArray(DiffRange[]::new);
  }

  public int rangeCount() {
    return ranges.length;
  }

  private void merge(List<DiffRange> ranges, DiffRange newRange) {
    DiffRange left = ranges.get(ranges.size() - 1);
    int leftType = left.type, rightType = newRange.type;
    if (leftType == DiffTypes.DEFAULT) {
      if (rightType == DiffTypes.DEFAULT) {
        left.lenL += newRange.lenL;
        left.lenR += newRange.lenR;
      } else ranges.add(newRange);
    } else {
      if (rightType != DiffTypes.DEFAULT) {
        left.lenL += newRange.lenL;
        left.lenR += newRange.lenR;
        left.type = DiffTypes.EDITED;
      } else ranges.add(newRange);
    }
  }

  private void updateDeleteRanges(
      int lineKey, int lines,
      int fromInd, int toInd,
      boolean isL
  ) {
    if (isL) {
      int newLen = lineKey - ranges[fromInd].fromL;
      lines -= (ranges[fromInd].lenL - newLen);
      ranges[fromInd].lenL = newLen;
    }
    else {
      int newLen = lineKey - ranges[fromInd].fromR;
      lines -= (ranges[fromInd].lenR - newLen);
      ranges[fromInd].lenR = newLen;
    }

    for (int i = fromInd + 1; i < toInd; i++) {
      var range = ranges[i];
      if (isL) {
        range.fromL = lineKey;
        lines -= range.lenL;
        range.lenL = 0;
      } else {
        range.fromR = lineKey;
        lines -= range.lenR;
        range.lenR = 0;
      }
    }
    if (isL) {
      ranges[toInd].fromL = lineKey;
      ranges[toInd].lenL -= lines;
    } else {
      ranges[toInd].fromR = lineKey;
      ranges[toInd].lenR -= lines;
    }
  }

  private LineDiff[] insert(int lineKey, int lines, LineDiff[] diffs) {
    LineDiff[] tmp = new LineDiff[diffs.length + lines];
    System.arraycopy(diffs, 0, tmp, 0, lineKey);
    System.arraycopy(diffs, lineKey, tmp, lineKey + lines, diffs.length - lineKey);
    return tmp;
  }

  private LineDiff[] delete(int lineKey, int lines, LineDiff[] diffs) {
    LineDiff[] tmp = new LineDiff[diffs.length - lines];
    System.arraycopy(diffs, 0, tmp, 0, lineKey);
    System.arraycopy(diffs, lineKey + lines, tmp, lineKey, diffs.length - (lineKey + lines));
    return tmp;
  }
}
