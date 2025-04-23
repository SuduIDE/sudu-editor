package org.sudu.experiments.editor;

public abstract class CodeLineMapping {

  // negative return value is outOfRange or regionIndex
  static final int outOfRange = -1;
  static int regionIndex(int r) { return -r - 2; }
  static boolean isCompacted(int r) { return r < 0 && r != outOfRange; }

  public abstract int length();

  public abstract int docToView(int docLine);
  public abstract int docToViewCursor(int docLine);
  public abstract int viewToDoc(int viewLine);

  public void viewToDocLines(
      int viewBegin, int viewEnd, int[] result
  ) {
    for (int i = viewBegin; i < viewEnd; i++)
      result[i - viewBegin] = viewToDoc(i);
  }

  static class Id extends CodeLineMapping {
    final Model model;

    public Id(Model model) {
      this.model = model;
    }

    @Override
    public int length() {
      return model.document.length();
    }

    @Override
    public int docToView(int docLine) {
      return docLine;
    }

    @Override
    public int docToViewCursor(int docLine) {
      return docLine;
    }

    @Override
    public int viewToDoc(int viewLine) {
      return viewLine;
    }

    @Override
    public void viewToDocLines(int viewBegin, int viewEnd, int[] result) {
      for (int i = viewBegin; i < viewEnd; i++)
        result[i - viewBegin] = i;
    }
  }
}
