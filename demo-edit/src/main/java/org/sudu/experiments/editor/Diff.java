package org.sudu.experiments.editor;

import org.sudu.experiments.math.V2i;
import org.sudu.experiments.text.SplitText;

public class Diff {
  public int line, pos;
  public boolean isDelete;
  V2i caretReturn;  // Caret before diff
  V2i caretPos;     // Caret after diff
  int syncPointDiff = -1;
  String change;

  public Diff(int line, int pos, boolean isDelete, String change) {
    this.line = line;
    this.pos = pos;
    this.caretReturn = makeCaretReturnPos(line, pos, isDelete, change);
    this.isDelete = isDelete;
    this.change = change;
    if (!isDelete) this.caretPos = caretReturn;
    this.caretPos = makeCaretReturnPos(caretReturn.x, caretReturn.y, isDelete, change);
  }

  public Diff(int line, int pos, boolean isDelete, String change, int caretLine, int caretPos) {
    this.line = line;
    this.pos = pos;
    this.caretReturn = new V2i(caretLine, caretPos);
    this.isDelete = isDelete;
    this.change = change;
//    if (!isDelete) this.caretPos = caretReturn;
    this.caretPos = makeCaretReturnPos(caretReturn.x, caretReturn.y, isDelete, change);
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
    int delta = line - oppositeLine;
    Diff copied = new Diff(oppositeLine, pos, isDelete, change);
    copied.caretPos = new V2i(caretPos.x - delta, caretPos.y);
    copied.caretReturn = new V2i(caretReturn.x - delta, caretReturn.y);
    return copied;
  }

  private V2i changeDelta(String change) {
    String[] lines = SplitText.split(change);
    return new V2i(lines.length - 1, lines[lines.length - 1].length());
  }

  private V2i makeCaretReturnPos(int line, int pos, boolean isDelete, String change) {
    if (isDelete || change.isEmpty()) return new V2i(line, pos);
    var delta = changeDelta(change);
    if (delta.x == 0) return new V2i(line, pos + delta.y);
    else return new V2i(line + delta.x, delta.y);
  }
}
