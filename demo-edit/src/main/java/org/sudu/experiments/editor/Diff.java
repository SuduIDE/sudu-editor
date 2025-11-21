package org.sudu.experiments.editor;

public class Diff {

  public final int line, pos;
  public final boolean isDelete;
  public final String change;
  int syncPointDiff = -1;

  public Diff(
      int line, int pos,
      boolean isDelete,
      String change
  ) {
    this.line = line;
    this.pos = pos;
    this.isDelete = isDelete;
    this.change = change;
  }

  public int length() {
    return change.length();
  }

  public int lineCount() {
    int lineCnt = 0;
    for (int i = 0; i < change.length(); i++)
      if (change.charAt(i) == '\n') lineCnt++;
    return lineCnt;
  }

  public Diff copyWithNewLine(int oppositeLine) {
    return new Diff(oppositeLine, pos, isDelete, change);
  }
}
