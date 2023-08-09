package org.sudu.experiments.parser.common.graph.node.decl;

import org.sudu.experiments.parser.common.Name;
import org.sudu.experiments.parser.common.graph.node.ref.RefNode;
import org.sudu.experiments.parser.common.graph.type.Type;

import java.util.Objects;

/**
 * int a = 10;
 * var b = 11;
 */
public class DeclNode {

  public Name decl;
  public Type type;

  public DeclNode(Name decl, Type type) {
    this.decl = decl;
    this.type = type;
  }

  public boolean match(RefNode ref) {
    return decl.decl.equals(ref.decl.decl);
  }

  @Override
  public String toString() {
    return decl.toString() + ": " + type.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DeclNode declNode = (DeclNode) o;
    return Objects.equals(decl, declNode.decl) && Objects.equals(type, declNode.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(decl, type);
  }
}
