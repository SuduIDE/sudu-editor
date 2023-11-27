package org.sudu.experiments.parser.common.stack.graph;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class StackNode {

  public static final int BASE_NODE = 0;
  public static final int SCOPE_NODE = 1;
  public static final int ROOT_NODE = 2;

  public int nodeType;
  List<StackEdge> inEdges, outEdges;

  public StackNode() {
    this(BASE_NODE);
  }

  void addInEdge(StackNode from) {
    inEdges.add(new StackEdge(from, this));
  }

  void addInEdges(Collection<StackNode> from) {
    inEdges.addAll(from.stream().map(fr -> new StackEdge(fr, this)).toList());
  }

  void addOutEdge(StackNode to) {
    inEdges.add(new StackEdge(this, to));
  }

  void addOutEdges(Collection<StackNode> to) {
    inEdges.addAll(to.stream().map(t -> new StackEdge(this, t)).toList());
  }

  void removeToEdges(StackNode to) {
    outEdges.removeIf(it -> it.to == to);
  }

  public StackNode(int type) {
    this.nodeType = type;
    inEdges = new ArrayList<>();
    outEdges = new ArrayList<>();
  }

}
