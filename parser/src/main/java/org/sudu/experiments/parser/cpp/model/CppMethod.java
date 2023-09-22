package org.sudu.experiments.parser.cpp.model;

import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.common.TypedDecl;

import java.util.List;
import java.util.Objects;

public class CppMethod extends TypedDecl {

  public List<String> argsTypes;

  public CppMethod(String name, Pos position, String type, List<String> argsTypes) {
    super(name, position, type);
    this.argsTypes = argsTypes;
  }

  public boolean match(String name, List<String> argsTypes) {
    if (!this.name.equals(name)) return false;
    return matchArgs(argsTypes);
  }

  public boolean matchArgs(List<String> argsTypes) {
    if (argsTypes.size() != this.argsTypes.size()) return false;
    for (int i = 0; i < argsTypes.size(); i++) {
      String expected = this.argsTypes.get(i);
      String actual = argsTypes.get(i);
      if (actual == null) continue;
      if (!expected.equals(actual)) return false;
    }
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    CppMethod method = (CppMethod) o;
    return Objects.equals(argsTypes, method.argsTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), argsTypes);
  }

  @Override
  public String toString() {
    return type + " " + name + " "
        + "(" +  String.join(", ", argsTypes) + ") "
        + position;
  }

}
