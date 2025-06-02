package org.sudu.experiments.editor;

import java.util.Arrays;

public class CompactCodeMapping extends CodeLineMapping {
  CompactViewRange[] data;
  int[] lengths;
  int length;

  public CompactCodeMapping(CompactViewRange[] data) {
    this.data = data;
    lengths = new int[data.length];
    length = 0;
    for (int i = 0; i < data.length; i++) {
      lengths[i] = length;
      CompactViewRange range = data[i];
      length += range.visible ? range.length() : 1;
    }
  }

  @Override
  public int length() {
    return length;
  }

  @Override
  public int docToView(int docLine) {
    int idx = CompactViewRange.binSearch(docLine, data);
    if (idx >= data.length)
      return outOfRange;
    var range = data[idx];
    if (range.contains(docLine)) {
      return range.visible
          ?  lengths[idx] + docLine - range.startLine
          : regionIndex(idx);
    }
    return outOfRange;
  }

  @Override
  public int docToViewCursor(int docLine) {
    int idx = CompactViewRange.binSearch(docLine, data);
    if (idx >= data.length)
      return outOfRange;
    var range = data[idx];
    if (range.contains(docLine)) {
      int lengthIdx = lengths[idx];
      return range.visible
          ?  lengthIdx + docLine - range.startLine : lengthIdx;
    }
    return outOfRange;
  }

  @Override
  public int viewToDoc(int viewLine) {
    if (viewLine < 0)
      throw new IllegalArgumentException("viewLine < 0");

    int idx = Arrays.binarySearch(lengths, viewLine);
    // you always click in visible region or
    // the 1st line of collapsed, so
    // when idx < 0 -> range.visible === true
    if (idx < 0) {
      int p = -idx - 2;
      if (p < 0 || p >= data.length)
        return outOfRange;

      var range = data[p];
      int offset = viewLine - lengths[p];
      int doc = range.startLine + offset;
      return doc >= range.endLine ? outOfRange : doc;
    } else {
      var range = data[idx];
      while (idx + 1 < lengths.length && range.length() == 0)
        range = data[++idx];
      return range.visible ? range.startLine : regionIndex(idx);
    }
  }

  @Override
  public void viewToDocLines(int viewBegin, int viewEnd, int[] result) {
    if (viewBegin < 0)
      throw new IllegalArgumentException("viewLine < 0");

    for (int i = viewBegin; i < viewEnd; i++) {
      int idx = Arrays.binarySearch(lengths, i);
      if (idx < 0) { // inside a region
        int p = -idx - 2;
        if (p < 0 || p >= data.length)
          result[i - viewBegin] = outOfRange;
        else {
          var range = data[p];
          int offset = i - lengths[p];
          int doc = range.startLine + offset;
          int endLine = range.endLine;
          if (doc >= endLine) {
            result[i - viewBegin] = outOfRange;
          } else {
            i = fillRgn(viewBegin, i, viewEnd, result, doc, endLine);
          }
        }
      } else {
        var range = data[idx];
        while (idx + 1 < lengths.length && range.length() == 0)
          range = data[++idx];
        if (range.visible) {
          i = fillRgn(viewBegin, i, viewEnd,
              result, range.startLine, range.endLine);
        } else {
          result[i - viewBegin] = regionIndex(idx);
        }
      }
    }
  }

  static int fillRgn(
      int viewBegin, int viewPos, int viewEnd,
      int[] result, int docPos, int docEnd
  ) {
    do {
      result[viewPos - viewBegin] = docPos++;
      viewPos++;
    } while (viewPos < viewEnd && docPos < docEnd);
    return viewPos - 1;
  }
}
