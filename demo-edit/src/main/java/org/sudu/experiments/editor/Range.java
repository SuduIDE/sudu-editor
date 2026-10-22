package org.sudu.experiments.editor;

public class Range {
  public int startLineNumber;
  public int startColumn;
  public int endLineNumber;
  public int endColumn;

  public Range() {}

  public Range(int startLineNumber, int startColumn, int endLineNumber, int endColumn) {
    this.startLineNumber = startLineNumber;
    this.startColumn = startColumn;
    this.endLineNumber = endLineNumber;
    this.endColumn = endColumn;
  }

  public Selection toSelection() {
    Selection sel = new Selection();
    sel.getLeftPos().set(startLineNumber, startColumn);
    sel.getRightPos().set(endLineNumber, endColumn);
    return sel;
  }

  @Override
  public String toString() {
    return "Range{ from (line=" + startLineNumber + ", col=" + startColumn + ") "
        + "to (line=" + endLineNumber + ", col=" + endColumn + ")}";
  }
}
