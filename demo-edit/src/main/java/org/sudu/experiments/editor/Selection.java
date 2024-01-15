package org.sudu.experiments.editor;

import org.sudu.experiments.math.V2i;

import java.util.Objects;

public class Selection {

  public final SelPos startPos = new SelPos();
  public final SelPos endPos = new SelPos();
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

  SelPos getLeftPos() {
    if (startPos.compareTo(endPos) <= 0)
      return startPos;
    else
      return endPos;
  }

  SelPos getRightPos() {
    if (startPos.compareTo(endPos) >= 0)
      return startPos;
    else
      return endPos;
  }

  boolean isEmpty() {
    return startPos.line == endPos.line
        && startPos.charInd == endPos.charInd;
  }

  // y == -1 -> line is fully selected
  V2i getLine(int line) {
    if (isEmpty()) return null;
    SelPos left = getLeftPos();
    SelPos right = getRightPos();
    if (left.line <= line && line <= right.line) {
      int start = line > left.line ? 0 : left.charInd;
      int end = line < right.line ? -1 : right.charInd;
      return new V2i(start, end);
    } else return null;
  }

  boolean isAreaSelected() {
    return !startPos.equals(endPos);
  }

  boolean isTailSelected(int line) {
    if (!isAreaSelected()) return false;
    SelPos left = getLeftPos();
    SelPos right = getRightPos();
    return left.line <= line && line < right.line;
  }

  public static class SelPos implements Comparable<SelPos> {
    public int line;
    public int charInd;

    public SelPos() {}

    public SelPos(int lineInd, int charInd) {
      this.line = lineInd;
      this.charInd = charInd;
    }

    SelPos(SelPos selPos) {
      this.line = selPos.line;
      this.charInd = selPos.charInd;
    }

    public void set(int lineInd, int charInd) {
      this.line = lineInd;
      this.charInd = charInd;
    }

    public void set(SelPos selPos) {
      this.line = selPos.line;
      this.charInd = selPos.charInd;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      return equals((SelPos) o);
    }

    public boolean equals(SelPos selPos) {
      return line == selPos.line && charInd == selPos.charInd;
    }

    @Override
    public int hashCode() {
      return Objects.hash(line, charInd);
    }

    @Override
    public int compareTo(SelPos another) {
      int lineCmp = Integer.compare(this.line, another.line);
      if (lineCmp != 0) return lineCmp;
      else return Integer.compare(this.charInd, another.charInd);
    }
  }
}
