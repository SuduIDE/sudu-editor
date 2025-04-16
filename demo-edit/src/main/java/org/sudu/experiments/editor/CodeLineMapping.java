package org.sudu.experiments.editor;

public abstract class CodeLineMapping {

  // negative return value is outOfRange or regionIndex
  static final int outOfRange = -1;
  static int regionIndex(int r) { return -r - 2; }
  static boolean isCompacted(int r) { return r < 0 && r != outOfRange; }

  abstract int length();

  abstract int docToView(int docLine);
  abstract int viewToDoc(int viewLine);

  void clickDelimiter(int index) {}

  abstract LineIterator iterateLines(int first);
  abstract void releaseIterator(LineIterator iter);

  // translate view range to document space
  static abstract class LineIterator {
    // returns index in document space
    abstract int getAndIncrement();
  }

  static class Id extends CodeLineMapping {
    final Model model;
    TrivialIterator cache;

    public Id(Model model) {
      this.model = model;
    }

    @Override
    int length() {
      return model.document.length();
    }

    @Override
    int docToView(int docLine) {
      return docLine;
    }

    @Override
    int viewToDoc(int viewLine) {
      return viewLine;
    }

    static class TrivialIterator extends LineIterator {
      int pos;

      public TrivialIterator(int pos) {
        this.pos = pos;
      }
      @Override
      int getAndIncrement() {
        return pos++;
      }
    }

    LineIterator iterateLines(int first) {
      if (cache == null) {
        return new TrivialIterator(first);
      } else {
        cache.pos = first;
        LineIterator i = cache;
        cache = null;
        return i;
      }
    }

    @Override
    void releaseIterator(LineIterator iter) {
      if (iter instanceof TrivialIterator ti)
        cache = ti;
    }
  }
}
