package org.sudu.experiments.editor;

public class CompactViewRange {
  public int startLine, endLine; // [ .. )
  public boolean visible;

  public CompactViewRange(int startLine, int endLine, boolean visible) {
    this.startLine = startLine;
    this.endLine = endLine;
    this.visible = visible;
  }

  public int visibleLineCount() {
    return visible ? endLine - startLine : 1;
  }

  // data.length > 0
  public static int binSearch(int line, CompactViewRange[] data) {
    int low = 0;
    int high = data.length - 1;
    while (low <= high) {
      int mid = (low + high) >>> 1;
      CompactViewRange midRange = data[mid];
      if (midRange.endLine < line) {
        low = mid + 1;
      } else if (line < midRange.startLine) {
        high = mid - 1;
      } else return mid;
    }
    return low;
  }
}
