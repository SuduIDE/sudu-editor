package org.sudu.experiments.parser.common.graph.node.ref;

import org.sudu.experiments.parser.common.Name;

import java.util.Objects;

public class RefNode {

  public Name ref;
  public String type;
  public int refType;
  public static final int BASE = 1;
  public static final int CALL = 2;
  public static final int TYPE = 3;
  public static final int THIS = 4;
  public static final int SUPER = 5;

  public RefNode(Name decl) {
    this(decl, null, BASE);
  }

  public RefNode(Name decl, String type, int refType) {
    this.ref = decl;
    this.type = type;
    this.refType = refType;
  }

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
