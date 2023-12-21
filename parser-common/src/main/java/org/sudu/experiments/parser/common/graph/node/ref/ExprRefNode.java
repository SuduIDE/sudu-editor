package org.sudu.experiments.parser.common.graph.node.ref;

import org.sudu.experiments.parser.common.graph.type.TypeMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static org.sudu.experiments.parser.common.graph.node.NodeTypes.*;

public class ExprRefNode extends RefNode {

  public List<RefNode> refNodes;

  public ExprRefNode(List<RefNode> refNodes) {
    this(refNodes, RefTypes.BASE_EXPRESSION);
  }

  public ExprRefNode(List<RefNode> refNodes, int refType) {
    this(refNodes, refNodes.isEmpty() || refNodes.get(0) == null ? null : refNodes.get(0).type, refType);
  }

  public ExprRefNode(List<RefNode> refNodes, String type) {
    this(refNodes, type, RefTypes.BASE_EXPRESSION);
  }

  public ExprRefNode(List<RefNode> refNodes, String type, int refType) {
    super(null, null, refType);
    this.refNodes = new ArrayList<>();
    for (var expr: refNodes) {
      if (expr instanceof ExprRefNode exprRef) {
        this.refNodes.addAll(exprRef.refNodes);
      } else {
        this.refNodes.add(expr);
      }
    }
    this.type = type != null && refType == RefTypes.ARRAY_INDEX
        ? TypeMap.getArrayElemType(type) : type;
  }

  @Override
  public String toString() {
    return refNodes.stream().map(it -> refNodes.toString()).collect(Collectors.joining(", ", "", ""));
  }
}
