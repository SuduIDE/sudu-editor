package org.sudu.experiments.parser.java.model;

import org.sudu.experiments.parser.common.Pos;

import java.util.List;
import java.util.Objects;

public class JavaMethod extends ClassBodyDecl {

  public List<String> argsTypes;

  public JavaMethod(String name, Pos position, String type, boolean isStatic, List<String> argsTypes) {
    super(name, position, type, isStatic);
    this.argsTypes = argsTypes;
  }

  public boolean match(String name, List<String> argsTypes) {
    if (!this.name.equals(name)) return false;
    if (this.argsTypes.size() != argsTypes.size()) return false;
    return matchArgs(argsTypes);
  }

  public boolean matchArgs(List<String> argsTypes) {
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
    JavaMethod method = (JavaMethod) o;
    return isStatic == method.isStatic && Objects.equals(argsTypes, method.argsTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), isStatic, argsTypes);
  }

  @Override
  public String toString() {
    return type + " " + name + " "
        + "(" +  String.join(", ", argsTypes) + ") "
        + position;
  }

}
