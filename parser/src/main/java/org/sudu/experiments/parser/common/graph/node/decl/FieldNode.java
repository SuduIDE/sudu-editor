package org.sudu.experiments.parser.common.graph.node.decl;

import org.sudu.experiments.parser.common.Name;
import org.sudu.experiments.parser.common.graph.node.ref.FieldRefNode;
import org.sudu.experiments.parser.common.graph.node.ref.RefNode;
import org.sudu.experiments.parser.common.graph.type.Type;

/**
 * public int a;
 */
public class FieldNode extends DeclNode {

  public FieldNode(Name decl, Type type) {
    super(decl, type);
  }

  public boolean matchField(RefNode ref) {
    return ref instanceof FieldRefNode fieldRef
        && super.match(fieldRef);
  }

}
