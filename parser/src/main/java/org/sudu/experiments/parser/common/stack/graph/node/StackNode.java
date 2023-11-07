package org.sudu.experiments.parser.common.stack.graph.node;

public class StackNode {

  public static final int SCOPE_NODE = 0;
  public static final int ROOT_NODE = 1;
  public static final int PUSH_NODE = 2;
  public static final int POP_NODE = 3;

  public int nodeType;
  public String symbol;
  public int position;

  public StackNode(String symbol, int position, int type) {
    this.symbol = symbol;
    this.position = position;
    this.nodeType = type;
  }

}
