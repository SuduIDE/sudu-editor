package org.sudu.experiments.parser.activity.graph.stat;

public class Random extends Schedule {

  int count;

  public Random(int count) {
    this.count = count;
  }

  @Override
  public String name() {
    return count == 1 ? "random" : "random(" + count + ")";
  }
}
