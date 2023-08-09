package org.sudu.experiments.parser.common.graph.node;

import java.util.Collections;

public class FakeNode extends ScopeNode {

  public FakeNode(ScopeNode parent) {
    this.parent = parent;
    this.childList = Collections.emptyList();
    this.refList = Collections.emptyList();
    this.declList = Collections.emptyList();
    this.importTypes = Collections.emptyList();
  }

}
