package org.sudu.experiments.parser.common.graph.node.ref;

import java.util.List;

public class ExprRefNode extends RefNode {

  public List<RefNode> refNodes;

  public ExprRefNode(List<RefNode> refNodes) {
    super(null, null, -1);
    if (refNodes.isEmpty()) throw new IllegalArgumentException("Empty Expression");
    this.refNodes = refNodes;
  }

}
