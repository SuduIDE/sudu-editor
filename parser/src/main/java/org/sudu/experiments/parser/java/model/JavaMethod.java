package org.sudu.experiments.parser.java.model;

import org.sudu.experiments.parser.common.Decl;
import org.sudu.experiments.parser.common.Pos;

import java.util.List;
import java.util.Objects;

public class JavaMethod extends ClassBodyDecl {

  public List<Decl> arguments;

  public JavaMethod(String name, Pos position, boolean isStatic, List<Decl> arguments) {
    super(name, position, isStatic);
    this.arguments = arguments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    JavaMethod method = (JavaMethod) o;
    return isStatic == method.isStatic && Objects.equals(arguments, method.arguments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), isStatic, arguments);
  }
}
