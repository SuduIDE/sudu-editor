package org.sudu.experiments.demo;

import org.sudu.experiments.math.V2i;

import java.util.Objects;

public class Selection {

  public final SelPos startPos = new SelPos();
  public final SelPos endPos = new SelPos();
  public boolean isSelectionStarted;

  Selection() {}

  Selection(Selection selection) {
    startPos.set(selection.startPos);
    endPos.set(selection.endPos);
    isSelectionStarted = selection.isSelectionStarted;
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

  // y == -1 -> line is fully selected
  V2i getLine(int line) {
    SelPos left = getLeftPos();
    SelPos right = getRightPos();
    if (left.line <= line && line <= right.line) {
      int start;
      if (line > left.line) start = 0;
      else start = left.charInd;

      int end;
      if (line < right.line) end = -1;
      else end = right.charInd;
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
      SelPos selPos = (SelPos) o;
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
