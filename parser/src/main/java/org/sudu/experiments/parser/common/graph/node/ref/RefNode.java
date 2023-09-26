package org.sudu.experiments.parser.common.graph.node.ref;

import org.sudu.experiments.parser.common.Name;
import org.sudu.experiments.parser.common.graph.type.Type;

import java.util.Objects;

public class RefNode {

  public final Name ref;
  public Type type;

  public RefNode(Name decl) {
    this(decl, null);
  }

  public RefNode(Name decl, Type type) {
    this.ref = decl;
    this.type = type;
  }

//  public void updateType(Type another) {
//    if (another == null) return;
//    type.type = another.type;
//    type.supertypes = another.supertypes;
//    type.associatedScope = another.associatedScope;
//  }

  @Override
  public String toString() {
    return ref + ": " + type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RefNode refNode = (RefNode) o;
    return Objects.equals(ref, refNode.ref) && Objects.equals(type, refNode.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ref, type);
  }
}
