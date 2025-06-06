package org.sudu.experiments.editor;

import org.sudu.experiments.math.V2i;

public class Diff {
  public int line, pos;
  public boolean isDelete;
  V2i caretReturn;
  int syncPointDiff = -1;
  String change;

  public Diff(int line, int pos, boolean isDelete, String change) {
    this.line = line;
    this.pos = pos;
    this.caretReturn = makeCaretReturnPos(line, pos, isDelete, change);
    this.isDelete = isDelete;
    this.change = change;
  }

  public Diff(int line, int pos, boolean isDelete, String change, int caretLine, int caretPos) {
    this.line = line;
    this.pos = pos;
    this.caretReturn = new V2i(caretLine, caretPos);
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

  private V2i makeCaretReturnPos(int line, int pos, boolean isDelete, String change) {
    String[] lines = change.split("\n", 0);
    if (isDelete && lines.length > 0) {
      if (lines.length == 1) {
        return new V2i(line, pos + lines[0].length());
      } else {
        return new V2i(line + lines.length - 1, lines[lines.length - 1].length());
      }
    }
    return new V2i(line, pos);
  }
}
