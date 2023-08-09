package org.sudu.experiments.parser.common.graph.node.ref;

import org.sudu.experiments.parser.common.Name;
import org.sudu.experiments.parser.common.graph.type.Type;

import java.util.List;

public class CreatorCallNode extends MethodCallNode {

  public CreatorCallNode(Name decl, Type type, List<RefNode> callArgs) {
    super(decl, type, callArgs);
  }
}
