package org.sudu.experiments.diff;

import java.util.Arrays;

public class CodeLineS {

  public final CodeElementS[] elements;
  private final int hash;
  public int lineNum;

  public CodeLineS(CodeElementS[] elements) {
    this.elements = elements;
    this.hash = Arrays.hashCode(elements);
  }

  public int len() {
    return elements.length;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.hashCode() != o.hashCode() || getClass() != o.getClass()) return false;
    CodeLineS codeLine = (CodeLineS) o;
    return Arrays.equals(elements, codeLine.elements);
  }

  @Override
  public int hashCode() {
    return hash;
  }

  @Override
  public String toString() {
    return "(" + (lineNum + 1) + ") " + String.join("", Arrays.stream(elements).map(it -> it.s).toList());
  }

}
