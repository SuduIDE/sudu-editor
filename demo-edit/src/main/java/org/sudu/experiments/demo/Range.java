package org.sudu.experiments.demo;

public class Range {
  public int endColumn;
  public int endLineNumber;
  public int startColumn;
  public int startLineNumber;

  public Range() {}

  public Range(int endColumn, int endLineNumber, int startColumn, int startLineNumber) {
    this.endColumn = endColumn;
    this.endLineNumber = endLineNumber;
    this.startColumn = startColumn;
    this.startLineNumber = startLineNumber;
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
