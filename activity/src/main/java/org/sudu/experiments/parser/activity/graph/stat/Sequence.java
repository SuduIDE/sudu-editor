package org.sudu.experiments.parser.activity.graph.stat;

import org.sudu.experiments.parser.activity.graph.Dag2Part;
import org.sudu.experiments.parser.activity.graph.IStat;

public class Sequence extends ComplexStat {

  @Override
  public String name() {
    return "sequence";
  }

  @Override
  public String toDag1() {
    StringBuilder acc = new StringBuilder();
    acc.append(super.toDag1());
    IStat.toDag1Seq(acc, block());
    return acc.toString();
  }

  public Dag2Part toDag2Part() {
    Dag2Part res = null;
    for (var b: block) {
      res = IStat.joinDag2(res, b.toDag2Part());
    }

    return res;
  }

}
