package org.sudu.experiments.diff;

public class CodeElementS {

  public final String s;
  private final int hash;
  public int lineNum, elemNum;

  public CodeElementS(String s) {
    this.s = s;
    this.hash = s.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.hashCode() != o.hashCode() || getClass() != o.getClass()) return false;
    CodeElementS that = (CodeElementS) o;
    return s.equals(that.s);
  }

  @Override
  public int hashCode() {
    return hash;
  }

  @Override
  public String toString() {
    return "(" + (lineNum + 1) + ": " + (elemNum + 1) + ") " + s;
  }
}
