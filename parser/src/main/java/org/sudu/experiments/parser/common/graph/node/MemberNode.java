package org.sudu.experiments.parser.common.graph.node;

import org.sudu.experiments.parser.common.graph.node.decl.DeclNode;

import java.util.List;

public class MemberNode extends ScopeNode {

  public DeclNode member;

  public <D extends DeclNode> MemberNode(ScopeNode parent, List<D> members) {
    super(parent);
    if (members.isEmpty()) throw new IllegalStateException("Member should contains at least 1 decl");
    this.member = members.get(0);
    this.declarations.addAll(members);
  }

  public MemberNode(ScopeNode parent, DeclNode member) {
    super(parent);
    this.member = member;
    this.declarations.add(member);
  }

}
