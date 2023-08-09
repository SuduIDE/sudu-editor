package org.sudu.experiments.parser.common.graph.node.decl;

import org.sudu.experiments.parser.common.Name;
import org.sudu.experiments.parser.common.graph.node.ref.CreatorCallNode;
import org.sudu.experiments.parser.common.graph.node.ref.RefNode;
import org.sudu.experiments.parser.common.graph.type.Type;

import java.util.List;

public class CreatorNode extends MethodNode {

  public CreatorNode(Name decl, Type type) {
    super(decl, type);
  }

  public CreatorNode(Name decl, Type type, List<ArgNode> args) {
    super(decl, type, args);
  }

  public boolean matchCreatorCall(RefNode ref) {
    if (!(ref instanceof CreatorCallNode creatorCallNode) ||
        !super.match(ref) ||
        args.size() != creatorCallNode.callArgs.size()
    ) return false;
    return matchArgs(creatorCallNode.callArgs);
  }

  @Override
  public boolean matchMethodCall(RefNode ref) {
    return false;
  }
}
