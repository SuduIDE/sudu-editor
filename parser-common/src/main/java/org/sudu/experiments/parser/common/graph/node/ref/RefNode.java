package org.sudu.experiments.parser.common.graph.node.ref;

import org.sudu.experiments.parser.common.Name;

import java.util.Objects;
import static org.sudu.experiments.parser.common.graph.node.NodeTypes.*;

public class RefNode {

  public Name ref;
  public String type;
  public int refType;


  public RefNode(Name decl) {
    this(decl, null, RefTypes.BASE);
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
