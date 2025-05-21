package org.sudu.experiments.editor;

public class CompactViewRange {
  public int startLine, endLine; // [ .. )
  public boolean visible;

  public CompactViewRange(int startLine, int endLine, boolean visible) {
    this.startLine = startLine;
    this.endLine = endLine;
    this.visible = visible;
  }

  public boolean inRange(int line) {
    return startLine <= line && line < endLine;
  }

  public int length() {
    return endLine - startLine;
  }

  public boolean contains(int line) {
    return startLine <= line && line < endLine;
  }

  public void shift(int delta) {
    startLine += delta;
    endLine += delta;
  }

  @Override
  public String toString() {
    return "[" + startLine + ", " + endLine + ")" + (visible ? " visible" : "");
  }

  // data.length > 0
  // the method assume no intersections between ranges
  public static int binSearch(int line, CompactViewRange[] data) {
    int low = 0;
    int high = data.length - 1;
    while (low <= high) {
      int mid = (low + high) >>> 1;
      CompactViewRange midRange = data[mid];
      if (midRange.endLine <= line) {
        low = mid + 1;
      } else if (line < midRange.startLine) {
        high = mid - 1;
      } else return mid;
    }
    return low;
  }

  public static void insertLines(int at, int count, CompactViewRange[] data) {
    int length = data.length;
    if (length == 0) return;
    int search = binSearch(at, data);
    if (search < length) {
      var range = data[search];
      if (range.contains(at)) {
        range.endLine += count;
        search++;
      }
      for (; search < length; search++) {
        data[search].shift(count);
      }
    } else {
      // insert at end
      if (search == length && data[length - 1].endLine == at) {
        data[length - 1].endLine += count;
      }
    }
  }

  public static void deleteLines(int at, int count, CompactViewRange[] data) {
    int length = data.length, countLeft = count;
    if (length == 0) return;
    int search = binSearch(at, data);
    if (search < length) {
      var range = data[search];
      if (range.contains(at)) {
        int toRemove = Math.min(range.endLine - countLeft, countLeft);
        range.endLine -= toRemove;
        countLeft -= toRemove;
        at += toRemove;
        search++;
      }
      for (; search < length; search++) {
        range = data[search];
        // todo
      }
    }
  }

  static void transferLeft(int from, int length, CompactViewRange[] ranges) {
    CompactViewRange r = ranges[from];
    CompactViewRange b = ranges[from + 1];
    if (r.length() < length || r.endLine != b.startLine)
      throw new UnsupportedOperationException();
    r.endLine -= length;
    b.startLine -= length;
  }

  static void transferRight(int from, int length, CompactViewRange[] ranges) {
    CompactViewRange r = ranges[from];
    CompactViewRange a = ranges[from - 1];
    if (r.length() < length || a.endLine != r.startLine)
      throw new UnsupportedOperationException();
    a.endLine += length;
    r.startLine += length;
  }

  public static void expand(
      int extend, int index, CompactViewRange[] l, CompactViewRange[] r
  ) {
    CompactViewRange lRange = l[index];
    CompactViewRange rRange = r[index];
    int lenL = lRange.length();
    int lenR = rRange.length();
    if (index > 0) {
      CompactViewRange.transferRight(index, Math.min(lenL, extend), l);
      CompactViewRange.transferRight(index, Math.min(lenR, extend), r);
      lenL = lRange.length();
      lenR = rRange.length();
    }
    if (index + 1 < l.length) {
      CompactViewRange.transferLeft(index, Math.min(lenL, extend), l);
      lenL = lRange.length();
    }
    if (index + 1 < l.length) {
      CompactViewRange.transferLeft(index, Math.min(lenR, extend), r);
      lenR = rRange.length();
    }
    if (lenR == 0) rRange.visible = true;
    if (lenL == 0) lRange.visible = true;
  }
}
