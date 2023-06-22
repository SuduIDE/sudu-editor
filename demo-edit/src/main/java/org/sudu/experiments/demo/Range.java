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

  @Override
  public String toString() {
    return "Range{ from (line=" + startLineNumber + ", col=" + startColumn + ") "
        + "to (line=" + endLineNumber + ", col=" + endColumn + ")}";
  }
}
