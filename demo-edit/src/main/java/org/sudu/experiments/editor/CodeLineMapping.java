package org.sudu.experiments.editor;

public abstract class CodeLineMapping {
  abstract int length();
  abstract int docToView(int line);
  abstract int viewToDoc(int line);

  abstract LineIterator iterateLines(int first);
  abstract void releaseIterator(LineIterator iter);

  static abstract class LineIterator {
    abstract int getAndIncrement();
  }

  static CodeLineMapping fromModel(Model model) {
    return new CodeLineMapping() {
      TrivialIterator cache;
      @Override
      int length() {
        return model.document.length();
      }

      @Override
      int docToView(int line) {
        return line;
      }

      @Override
      int viewToDoc(int line) {
        return line;
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
    };
  }
}
