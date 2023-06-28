package org.sudu.experiments.parser.java.model;

import org.sudu.experiments.parser.common.Pos;

import java.util.Objects;

public class JavaMethod extends ClassBodyDecl {

  public int numberOfArgs;

  public JavaMethod(String name, Pos position, boolean isStatic, int numberOfArgs) {
    super(name, position, isStatic);
    this.numberOfArgs = numberOfArgs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    JavaMethod method = (JavaMethod) o;
    return isStatic == method.isStatic && Objects.equals(numberOfArgs, method.numberOfArgs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), isStatic, numberOfArgs);
  }
}
