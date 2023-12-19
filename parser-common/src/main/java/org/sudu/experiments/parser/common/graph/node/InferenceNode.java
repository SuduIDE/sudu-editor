package org.sudu.experiments.parser.common.graph.node;

import org.sudu.experiments.parser.common.graph.node.decl.DeclNode;
import org.sudu.experiments.parser.common.graph.node.ref.RefNode;

public class InferenceNode {

  public DeclNode decl;
  public RefNode ref;
  public int inferenceType;
  public static final int INFERENCE = 1;
  public static final int FOR_EACH = 2;

  public InferenceNode(DeclNode decl, RefNode ref) {
    this(decl, ref, INFERENCE);
  }

  public InferenceNode(DeclNode decl, RefNode ref, int inferenceType) {
    this.decl = decl;
    this.ref = ref;
    this.inferenceType = inferenceType;
  }

}
