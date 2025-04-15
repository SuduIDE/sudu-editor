package org.sudu.experiments.editor;

public class CompactCodeView extends CodeLineMapping {
  int[] docToView;
  CompactViewIterator cache;

  public CompactCodeView(int[] docToView) {
    this.docToView = docToView;
  }

  int length() {
    return docToView.length;
  }

  @Override
  int docToView(int line) {
    return docToView[line];
  }

  @Override
  int viewToDoc(int line) {
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
