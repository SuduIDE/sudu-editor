package org.sudu.experiments.parser.common.graph.node.ref;

import java.util.ArrayList;
import java.util.List;

public class ExprRefNode extends RefNode {

  public List<RefNode> refNodes;

  public ExprRefNode(List<RefNode> refNodes) {
    this(refNodes, refNodes.get(0).type);
  }

  public ExprRefNode(List<RefNode> refNodes, String type) {
    super(null, null, -1);
    if (refNodes.isEmpty()) throw new IllegalArgumentException("Empty Expression");
    this.refNodes = new ArrayList<>();
    for (var expr: refNodes) {
      if (expr instanceof ExprRefNode exprRef) {
        this.refNodes.addAll(exprRef.refNodes);
      } else {
        this.refNodes.add(expr);
      }
    }
    this.type = type;
  }

}
