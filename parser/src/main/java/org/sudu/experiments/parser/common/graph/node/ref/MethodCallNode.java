package org.sudu.experiments.parser.common.graph.node.ref;

import org.sudu.experiments.parser.common.Name;
import org.sudu.experiments.parser.common.graph.type.Type;

import java.util.List;
import java.util.Objects;

public class MethodCallNode extends RefNode {

  public List<RefNode> callArgs;

  public MethodCallNode(Name decl, List<RefNode> callArgs) {
    super(decl);
    this.callArgs = callArgs;
  }

  public MethodCallNode(Name decl, Type type, List<RefNode> callArgs) {
    super(decl, type);
    this.callArgs = callArgs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    MethodCallNode that = (MethodCallNode) o;
    return Objects.equals(callArgs, that.callArgs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), callArgs);
  }
}
