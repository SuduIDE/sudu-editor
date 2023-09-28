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

  public List<Type> argTypes;
  public int callType;
  public static final int METHOD = 1;
  public static final int CREATOR = 2;
  public static final int THIS = 3;
  public static final int SUPER = 4;
  public static final int THIS_CALL = 5;
  public static final int SUPER_CALL = 6;

  public MethodNode(Name decl, Type type) {
    this(decl, type, METHOD, Collections.emptyList());
  }

  public MethodNode(Name decl, Type type, int callType, List<Type> args) {
    super(decl, type, CALLABLE);
    this.argTypes = args;
    this.callType = callType;
  }

  public boolean matchMethodCall(RefNode ref) {
    return ref instanceof MethodCallNode callRef
        && callRef.callType == callType
        && super.match(callRef)
        && matchArgs(callRef.callArgs);
  }

  public boolean matchArgs(List<RefNode> callArgs) {
    if (argTypes.size() != callArgs.size()) return false;
    for (int i = 0; i < argTypes.size(); i++) {
      var callArg = callArgs.get(i);
      var methodType = argTypes.get(i);
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
    return Objects.equals(argTypes, that.argTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), argTypes);
  }
}
