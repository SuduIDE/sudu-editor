package org.sudu.experiments.parser.common.stack.graph;

import org.sudu.experiments.parser.common.Name;

public class StackEdge {

  public static final int BASE_EDGE = 0;
  public static final int PUSH_EDGE = 1;
  public static final int POP_EDGE = 2;

  public StackNode from, to;
  public Name symbol;
  public int edgeType;

  public StackEdge(StackNode from, StackNode to) {
    this(from, to, null, BASE_EDGE);
  }

  public StackEdge(StackNode from, StackNode to, Name symbol, int type) {
    this.from = from;
    this.to = to;
    this.symbol = symbol;
    this.edgeType = type;
  }

  public boolean isPushEdge() {
    return edgeType == PUSH_EDGE;
  }

  public boolean isPopEdge() {
    return edgeType == POP_EDGE;
  }

}
