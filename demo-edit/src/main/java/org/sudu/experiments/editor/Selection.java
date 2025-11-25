package org.sudu.experiments.editor;

import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.common.Pos;

import java.util.Objects;

public class Selection {

  public final Pos startPos = new Pos();
  public final Pos endPos = new Pos();
  public boolean isSelectionStarted;

  Selection() {}

  Selection(Selection selection) {
    set(selection);
  }

  void set(Selection from) {
    startPos.set(from.startPos);
    endPos.set(from.endPos);
    isSelectionStarted = from.isSelectionStarted;
  }

  void select(int caretLine, int caretCharPos) {
    endPos.set(caretLine, caretCharPos);
    if (!isSelectionStarted) {
      startPos.set(caretLine, caretCharPos);
    }
  }

  void selectLine(int line) {
    startPos.set(line, 0);
    endPos.set(line + 1, 0);
  }

  Pos getLeftPos() {
    return startPos.compareTo(endPos) <= 0 ? startPos : endPos;
  }

  Pos getRightPos() {
    return startPos.compareTo(endPos) >= 0 ? startPos : endPos;
  }

  boolean isEmpty() {
    return startPos.line == endPos.line
        && startPos.charPos == endPos.charPos;
  }

  // y == -1 -> line is fully selected
  V2i getLine(int line, V2i rv) {
    if (isEmpty()) return null;
    Pos left = getLeftPos();
    Pos right = getRightPos();
    if (left.line <= line && line <= right.line) {
      int start = line > left.line ? 0 : left.charPos;
      int end = line < right.line ? -1 : right.charPos;
      rv.set(start, end);
      return rv;
    } else return null;
  }

  boolean isAreaSelected() {
    return !startPos.equals(endPos);
  }

  boolean isTailSelected(int line) {
    if (!isAreaSelected()) return false;
    Pos left = getLeftPos();
    Pos right = getRightPos();
    return left.line <= line && line < right.line;
  }
}
