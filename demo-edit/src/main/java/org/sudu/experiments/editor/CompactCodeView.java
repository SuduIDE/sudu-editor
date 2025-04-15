package org.sudu.experiments.editor;

public class CompactCodeView extends CodeLineMapping {
  CompactViewRange[] data;
  int[] lengths;
  int length;
  CompactViewIterator cache;

  public CompactCodeView(CompactViewRange[] data) {
    this.data = data;
    lengths = new int[data.length];
    length = 0;
    for (int i = 0; i < data.length; i++) {
      lengths[i] = length;
      CompactViewRange range = data[i];
      length += range.visibleLineCount();
    }
  }

  int length() {
    return length;
  }

  @Override
  int docToView(int docLine) {
    return -1;
  }

  @Override
  int viewToDoc(int viewLine) {
    return 0;
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
      return 0;
    }
  }

}
