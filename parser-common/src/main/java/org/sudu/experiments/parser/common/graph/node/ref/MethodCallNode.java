package org.sudu.experiments.parser.common.graph.node.ref;

import org.sudu.experiments.parser.common.Name;

import java.util.List;
import java.util.Objects;
import static org.sudu.experiments.parser.common.graph.node.NodeTypes.*;

public class MethodCallNode extends RefNode {

  public List<RefNode> callArgs;
  public int callType;

  public MethodCallNode(Name ref, List<RefNode> callArgs) {
    this(ref, null, MethodTypes.METHOD, callArgs);
  }

  public MethodCallNode(Name ref, String type, int callType, List<RefNode> callArgs) {
    super(ref, type, RefTypes.CALL);
    this.callType = callType;
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
