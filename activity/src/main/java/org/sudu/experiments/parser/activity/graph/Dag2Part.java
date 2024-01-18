package org.sudu.experiments.parser.activity.graph;

import java.util.ArrayList;

public class Dag2Part {
  public final Node input;
  public final ArrayList<EdgeFrom> output = new ArrayList<>();

  public Dag2Part(Node input) {
    this.input = input;
  }

  public Dag2Part(Node input, ArrayList<EdgeFrom> output) {
    this.input = input;
    this.output.addAll(output);
  }

  public static Dag2Part singleExit(Node node) {
    if (node == null)
      return null;

    var res = new Dag2Part(node);
    res.output.add(new EdgeFrom(node, null));
    return res;
  }
}
