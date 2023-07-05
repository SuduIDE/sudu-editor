package org.sudu.experiments.parser.java.model;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.sudu.experiments.parser.common.Decl;
import org.sudu.experiments.parser.common.Pos;

import java.util.Objects;

public class TypedDecl extends Decl {

  public String type;

  public TypedDecl(String name, Pos position, String type) {
    super(name, position);
    this.type = type;
  }

  public static TypedDecl fromNode(TerminalNode node, String type) {
    return new TypedDecl(node.getText(), Pos.fromNode(node), type);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    TypedDecl typedDecl = (TypedDecl) o;
    return Objects.equals(type, typedDecl.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), type);
  }

  @Override
  public String toString() {
    return type + " " + super.toString();
  }
}
