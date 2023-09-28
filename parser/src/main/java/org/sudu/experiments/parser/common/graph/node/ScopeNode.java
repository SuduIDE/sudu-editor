package org.sudu.experiments.parser.common.graph.node;

import org.sudu.experiments.parser.common.graph.node.decl.DeclNode;
import org.sudu.experiments.parser.common.graph.node.ref.*;
import org.sudu.experiments.parser.common.graph.type.Type;

import java.util.*;
import java.util.function.*;

public class ScopeNode {

  public ScopeNode parent;
  public List<ScopeNode> children;

  public List<RefNode> refList;
  public List<DeclNode> declList;
  public List<Type> importTypes;

  public ScopeNode() {
  }

  public ScopeNode(ScopeNode parent) {
    this.parent = parent;
    this.children = new ArrayList<>();
    this.refList = new ArrayList<>();
    this.declList = new ArrayList<>();
    this.importTypes = new ArrayList<>();
  }

  public ScopeNode getChild(int i) {
    return children.get(i);
  }

  DeclNode declarationWalk(
      Predicate<DeclNode> matcher
  ) {
    for (var decl: declList)
      if (matcher.test(decl)) return decl;
    for (var subScope: children) {
      if (!(subScope instanceof MemberNode member)) continue;
      for (var decl: member.declList)
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
