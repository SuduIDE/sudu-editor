package org.sudu.experiments.diff;

import java.util.Arrays;

public class CodeLineS {

  public CodeElementS[] elements;
  public int lineNum;

  public CodeLineS(CodeElementS[] elements) {
    this.elements = elements;
  }

  public int len() {
    return elements.length;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CodeLineS codeLine = (CodeLineS) o;
    return Arrays.equals(elements, codeLine.elements);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(elements);
  }

  @Override
  public String toString() {
    return "(" + (lineNum + 1) + ") " + String.join("", Arrays.stream(elements).map(it -> it.s).toList());
  }

}
