package org.sudu.experiments.editor;

import java.util.Arrays;

public class CompactCodeMapping extends CodeLineMapping {
  CompactViewRange[] data;
  int[] lengths;
  int length;
  CompactViewIterator cache;

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
    if (range.inRange(docLine)) {
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
    if (range.inRange(docLine)) {
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
      return range.visible ? range.startLine : regionIndex(idx);
    }
  }

  @Override
  LineIterator iterateLines(int first) {
    if (cache == null) {
      return new CompactViewIterator(first);
    } else {
      var r = cache;
      r.position = first;
      cache = null;
      return r;
    }
  }

  @Override
  void releaseIterator(LineIterator iter) {
    if (iter instanceof CompactViewIterator cvi)
      cache = cvi;
  }

  class CompactViewIterator extends LineIterator {
    int position;

    public CompactViewIterator(int position) {
      this.position = position;
    }

    @Override
    int getAndIncrement() {
      return outOfRange;
    }
  }
}
