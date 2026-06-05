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

  public Diff copyWithNewLine(int oppositeLine, int oldLength, int newLength) {
    StringBuilder changeSB = new StringBuilder(change);
//    if (change.endsWith("\n")) changeSB.deleteCharAt(changeSB.length() - 1);
//    if (oppositeLine + 1 != newLength) changeSB.append("\n");
    return new Diff(oppositeLine, pos, isDelete, changeSB.toString());
  }

  @Override
  public String toString() {
    return change;
  }
}
