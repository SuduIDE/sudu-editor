package org.sudu.experiments.parser.common.stack.graph;

import java.util.List;
import java.util.ArrayList;

public class StackNode {

  public static final int BASE_NODE = 0;
  public static final int SCOPE_NODE = 1;
  public static final int ROOT_NODE = 2;

  public int nodeType;
  public List<StackEdge> inEdges, outEdges;

  public StackNode() {
    this(BASE_NODE);
  }

  public StackNode(int type) {
    this.nodeType = type;
    inEdges = new ArrayList<>();
    outEdges = new ArrayList<>();
  }

}
