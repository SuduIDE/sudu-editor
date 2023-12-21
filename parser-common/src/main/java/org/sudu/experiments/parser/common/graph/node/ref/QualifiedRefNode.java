package org.sudu.experiments.parser.common.graph.node.ref;

import org.sudu.experiments.parser.common.graph.node.NodeTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.sudu.experiments.parser.common.graph.node.NodeTypes.*;

/**
  ref.a
 */
public class QualifiedRefNode extends RefNode {

  public RefNode begin, cont;

  public QualifiedRefNode(RefNode begin, RefNode cont) {
    super(begin.ref, begin.type, NodeTypes.RefTypes.QUALIFIED);
    this.begin = begin;
    this.cont = cont;
  }

  public QualifiedRefNode(List<RefNode> refs) {
    super(null, null, RefTypes.QUALIFIED);

    var preLast = refs.get(refs.size() - 2);
    var last = refs.get(refs.size() - 1);

    var cur = new QualifiedRefNode(preLast, last);
    for (int i = refs.size() - 3; i >= 0; i--) {
      cur = new QualifiedRefNode(refs.get(i), cur);
    }

    this.begin = cur.begin;
    this.cont = cur.cont;
    this.ref = cur.ref;
  }

  public List<RefNode> flatten() {
    List<RefNode> result = new ArrayList<>();
    flatten(result);
    return result;
  }

  public void addLast(RefNode last) {
    if (cont instanceof QualifiedRefNode qualifiedRef) qualifiedRef.addLast(last);
    else this.cont = new QualifiedRefNode(cont, last);
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