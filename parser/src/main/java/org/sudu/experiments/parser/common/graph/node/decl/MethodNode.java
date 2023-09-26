package org.sudu.experiments.parser.common.graph.node.decl;

import org.sudu.experiments.parser.common.Name;
import org.sudu.experiments.parser.common.graph.node.ref.MethodCallNode;
import org.sudu.experiments.parser.common.graph.node.ref.RefNode;
import org.sudu.experiments.parser.common.graph.type.Type;

import java.util.*;

/**
 * int foo(int a, int b) {}
 */

public class MethodNode extends DeclNode {

  public List<ArgNode> args;

  public MethodNode(Name decl, Type type) {
    this(decl, type, new ArrayList<>());
  }

  public MethodNode(Name decl, Type type, List<ArgNode> args) {
    super(decl, type);
    this.args = args;
  }

  public boolean matchMethodCall(RefNode ref) {
    if (!(ref instanceof MethodCallNode methodCallNode) ||
        !super.match(ref) ||
        args.size() != methodCallNode.callArgs.size()
    ) return false;
    return matchArgs(methodCallNode.callArgs);
  }

  public boolean matchArgs(List<RefNode> callArgs) {
    for (int i = 0; i < args.size(); i++) {
      var callArg = callArgs.get(i);
      var methodType = args.get(i).type;
      if (callArg == null || callArg.type == null) continue;
      if (!callArg.type.match(methodType)) return false;
    }
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    MethodNode that = (MethodNode) o;
    return Objects.equals(args, that.args);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), args);
  }
}
