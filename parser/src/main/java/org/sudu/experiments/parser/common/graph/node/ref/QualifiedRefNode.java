package org.sudu.experiments.parser.common.graph.node.ref;

import org.sudu.experiments.parser.common.Name;

import java.util.Objects;

/**
  ref.a
 */
public class QualifiedRefNode extends RefNode {

  public RefNode begin, cont;

  public QualifiedRefNode(RefNode begin, RefNode cont) {
    super(begin.ref, null, -1);
    this.begin = begin;
    this.cont = cont;
  }

  public static Name getDecl(RefNode cont) {
    if (cont instanceof QualifiedRefNode qualifiedRefNode) return getDecl(qualifiedRefNode.cont);
    else return cont.ref;
  }

  @Override
  public String toString() {
    return begin.ref.name + "." + cont;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    QualifiedRefNode that = (QualifiedRefNode) o;
    return Objects.equals(begin, that.begin) && Objects.equals(cont, that.cont);
  }

  @Override
  public int hashCode() {
    return Objects.hash(begin, cont);
  }
}