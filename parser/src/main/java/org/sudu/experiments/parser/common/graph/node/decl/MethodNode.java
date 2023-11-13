package org.sudu.experiments.parser.common.graph.node.decl;

import org.sudu.experiments.parser.common.Name;
import org.sudu.experiments.parser.common.graph.node.ref.MethodCallNode;
import org.sudu.experiments.parser.common.graph.node.ref.RefNode;
import org.sudu.experiments.parser.common.graph.type.TypeMap;

import java.util.*;

/**
 * int foo(int a, int b) {}
 */

public class MethodNode extends DeclNode {

  public List<String> argTypes;
  public int callType;
  public static final int METHOD = 1;
  public static final int CREATOR = 2;
  public static final int ARRAY_CREATOR = 3;
  public static final int THIS = 4;
  public static final int SUPER = 5;
  public static final int THIS_CALL = 6;
  public static final int SUPER_CALL = 7;

  public MethodNode(Name decl, String type) {
    this(decl, type, METHOD, Collections.emptyList());
  }

  public MethodNode(Name decl, String type, int callType, List<String> args) {
    super(decl, type, CALLABLE);
    this.argTypes = args;
    this.callType = callType;
  }

  public boolean matchMethodCall(RefNode ref, TypeMap typeMap) {
    return ref instanceof MethodCallNode callRef
        && callRef.callType == callType
        && super.match(callRef, typeMap)
        && matchArgs(callRef.callArgs, typeMap);
  }

  public boolean matchArgs(List<RefNode> callArgs, TypeMap typeMap) {
    if (argTypes.size() != callArgs.size()) return false;
    for (int i = 0; i < argTypes.size(); i++) {
      var callArg = callArgs.get(i);
      var methodType = argTypes.get(i);
      if (callArg == null || callArg.type == null) continue;
      if (!typeMap.matchType(callArg.type, methodType)) return false;
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
