package org.sudu.experiments.parser.cpp.model;

import org.sudu.experiments.parser.common.Decl;
import org.sudu.experiments.parser.common.Pos;

import java.util.List;
import java.util.Objects;

public class CppMethod extends Decl {

  public List<Decl> arguments;

  public CppMethod(String name, Pos position, List<Decl> arguments) {
    super(name, position);
    this.arguments = arguments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    CppMethod cppMethod = (CppMethod) o;
    return Objects.equals(arguments, cppMethod.arguments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), arguments);
  }
}
