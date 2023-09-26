package org.sudu.experiments.parser.common.graph.node.decl;

import org.sudu.experiments.parser.common.Name;
import org.sudu.experiments.parser.common.graph.node.ref.RefNode;
import org.sudu.experiments.parser.common.graph.type.Type;

public class VarNode extends DeclNode {

  public VarNode(Name decl, Type type) {
    super(decl, type);
  }

  @Override
  public boolean match(RefNode ref) {
    return super.match(ref) && decl.compareTo(ref.ref) < 0;
  }
}
