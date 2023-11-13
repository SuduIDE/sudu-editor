package org.sudu.experiments.parser.common.graph.node.ref;

import org.sudu.experiments.parser.common.Name;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
  ref.a
 */
public class QualifiedRefNode extends RefNode {

  public RefNode begin, cont;

  public QualifiedRefNode(RefNode begin, RefNode cont) {
    super(begin.ref, begin.type, -1);
    this.begin = begin;
    this.cont = cont;
  }

  public List<RefNode> flatten() {
    List<RefNode> result = new ArrayList<>();
    flatten(result);
    return result;
  }

  protected void flatten(List<RefNode> result) {
    if (!(begin instanceof QualifiedRefNode qualifiedRef)) result.add(begin);
    else qualifiedRef.flatten(result);
    if (!(cont instanceof QualifiedRefNode qualifiedRef)) result.add(cont);
    else qualifiedRef.flatten(result);
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