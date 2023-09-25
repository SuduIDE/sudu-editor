package org.sudu.experiments.parser.common.graph.node.ref;

import org.sudu.experiments.parser.common.Name;
import org.sudu.experiments.parser.common.graph.type.Type;

public class FieldRefNode extends RefNode {

  public FieldRefNode(Name decl) {
    super(decl);
  }

  public FieldRefNode(Name decl, Type type) {
    super(decl, type);
  }

}
