package org.sudu.experiments.parser.common.graph.node;

import org.sudu.experiments.parser.common.graph.node.decl.DeclNode;
import org.sudu.experiments.parser.common.graph.node.ref.RefNode;

public class InferenceNode {

  public DeclNode decl;
  public RefNode ref;

  public InferenceNode(DeclNode decl, RefNode ref) {
    this.decl = decl;
    this.ref = ref;
  }

}
