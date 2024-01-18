package org.sudu.experiments.parser.activity.graph;

public class EmptyNode extends Node {
  @Override
  public String name() {
    return "<empty>";
  }

  @Override
  public String drawDagNode() {
    return "(((*)))";
  }
}
