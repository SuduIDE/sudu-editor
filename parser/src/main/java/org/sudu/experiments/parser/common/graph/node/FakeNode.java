package org.sudu.experiments.parser.common.graph.node;

import java.util.Collections;

public class FakeNode extends ScopeNode {

  public FakeNode(ScopeNode parent) {
    this.parent = parent;
    this.children = Collections.emptyList();
    this.references = Collections.emptyList();
    this.declarations = Collections.emptyList();
    this.importTypes = Collections.emptyList();
    this.inferences = Collections.emptyList();
  }

}
