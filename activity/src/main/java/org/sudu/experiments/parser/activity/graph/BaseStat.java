package org.sudu.experiments.parser.activity.graph;

public abstract class BaseStat extends Node implements IStat {
  @Override
  public String toString() {
    StringBuilder acc = new StringBuilder();
    print(acc, 0);
    return acc.toString();
  }
}
