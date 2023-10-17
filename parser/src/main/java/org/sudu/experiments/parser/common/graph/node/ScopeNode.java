package org.sudu.experiments.parser.common.graph.node;

import org.sudu.experiments.parser.common.graph.node.decl.DeclNode;
import org.sudu.experiments.parser.common.graph.node.ref.*;
import org.sudu.experiments.parser.common.graph.type.TypeMap;

import java.util.*;
import java.util.function.*;

public class ScopeNode {

  public ScopeNode parent;
  public List<ScopeNode> children;

  public List<RefNode> references;
  public List<DeclNode> declarations;
  public List<InferenceNode> inferences;
  public List<String> importTypes;
  public String type;

  public ScopeNode() {
  }

  public ScopeNode(ScopeNode parent) {
    this.parent = parent;
    this.children = new ArrayList<>();
    this.references = new ArrayList<>();
    this.declarations = new ArrayList<>();
    this.importTypes = new ArrayList<>();
    this.inferences = new ArrayList<>();
  }

  public void removeInParent() {
    parent.children.remove(this);
  }

  public ScopeNode getChild(int i) {
    return children.get(i);
  }

  public void referenceWalk(Function<RefNode, DeclNode> resolve) {
    for (var infer: inferences) {
      var resolved = resolve.apply(infer.ref);
      if (resolved != null && resolved.type != null) {
        if (infer.inferenceType == InferenceNode.INFERENCE) infer.decl.type = resolved.type;
        else infer.decl.type = TypeMap.getArrayElemType(resolved.type);
      }
    }
    for (var ref: references) resolve.apply(ref);
  }

  public DeclNode declarationWalk(
      Predicate<DeclNode> matcher
  ) {
    for (var decl: inferences)
      if (matcher.test(decl.decl)) return decl.decl;
    for (var decl: declarations)
      if (matcher.test(decl)) return decl;
    for (var subScope: children) {
      if (!(subScope instanceof MemberNode member)) continue;
      for (var decl: member.declarations)
        if (matcher.test(decl)) return decl;
    }
    return null;
  }

  public void print(int depth) {
    System.out.print("—".repeat(depth));
    if (this instanceof FakeNode) System.out.println("F");
    else if (this instanceof MemberNode) System.out.println("M");
    else System.out.println("S");
    for (var child: children) child.print(depth + 1);
  }

}
