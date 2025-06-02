package org.sudu.experiments.editor;

public class CompactViewRange {
  public int startLine, endLine; // [ .. )
  public boolean visible;

  public CompactViewRange(int startLine, int endLine, boolean visible) {
    this.startLine = startLine;
    this.endLine = endLine;
    this.visible = visible;
  }

  public int length() {
    return endLine - startLine;
  }

  public boolean contains(int line) {
    return startLine <= line && line < endLine;
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
    addRemoveLines(at, count, data);
    // insert at end
    if (data.length > 0)
      data[data.length - 1].insertLinesAtEnd(at, count);
  }

  private void insertLinesAtEnd(int at, int count) {
    if (endLine == at)
      endLine += count;
  }

  public static void deleteLines(int at, int count, CompactViewRange[] data) {
    addRemoveLines(at, -count, data);
  }

  static void addRemoveLines(int at, int count, CompactViewRange[] data) {
    int length = data.length;
    if (length == 0) return;
    int search = binSearch(at, data);
    for (; search < length; search++) {
      var range = data[search];
      range.addRemoveLines(at, count);
    }
  }

  void addRemoveLines(int at, int count) {
    if (at < startLine)
      startLine = Math.max(at, startLine + count);

    if (at < endLine)
      endLine = Math.max(at, endLine + count);
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

  static void expand(int extend, int index, CompactViewRange[] m) {
    CompactViewRange range = m[index];
    boolean first = index == 0, last = index == m.length - 1;
    int minSize = (first || last) ? extend + 1 : extend * 2 + 1;
    if (range.length() > minSize) {
      if (!first) transferRight(index, extend, m);
      if (!last) transferLeft(index, extend, m);
    } else {
      range.visible = true;
    }
  }

  public static void expand(
      int extend, int index,
      CompactViewRange[] l, CompactViewRange[] r
  ) {
    expand(extend, index, l);
    expand(extend, index, r);
  }
}
