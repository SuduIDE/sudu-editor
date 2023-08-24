package org.sudu.experiments.diff;

import java.util.Objects;

public class CodeElementS {

  public String s;
  public int lineNum, elemNum;

  public CodeElementS(String s) {
    this.s = s;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CodeElementS that = (CodeElementS) o;
    return Objects.equals(s, that.s);
  }

  @Override
  public int hashCode() {
    return Objects.hash(s);
  }

  @Override
  public String toString() {
    return "(" + (lineNum + 1) + ": " + (elemNum + 1) + ") " + s;

  }

}
