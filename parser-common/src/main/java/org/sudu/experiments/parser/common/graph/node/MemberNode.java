package org.sudu.experiments.parser.common.graph.node;

import org.sudu.experiments.parser.common.graph.node.decl.DeclNode;

import java.util.List;

public class MemberNode extends ScopeNode {

  public <D extends DeclNode> MemberNode(ScopeNode parent, List<D> members) {
    super(parent);
    this.declarations.addAll(members);
  }

  public MemberNode(ScopeNode parent, DeclNode member) {
    super(parent);
    this.declarations.add(member);
  }

}
