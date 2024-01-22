package org.sudu.experiments.parser.activity.graph.stat;

import org.sudu.experiments.parser.activity.graph.Dag2Part;
import org.sudu.experiments.parser.activity.graph.EdgeFrom;
import org.sudu.experiments.parser.activity.graph.EmptyNode;
import org.sudu.experiments.parser.activity.graph.IStat;

import java.util.ArrayList;
import java.util.Arrays;

public class Random extends Schedule {

  final RandomSrc random;
  int count;

  public Random(RandomSrc random, int count) {
    this.random = random;
    this.count = count;
  }

  @Override
  public String name() {
    return count == 1 ? "random" : "random(" + count + ")";
  }

  private int[] permuteInterval_0_n(int n) {
    int[] res = new int[n];

    for (int i = 0; i < n; i++) res[i] = i;
    for (int i = 0; i < n; i++) {
      var next = i + random.nextInt(n - i);
      var s = res[i];
      res[i] = res[next];
      res[next] = s;
    }
    return res;
  }

  public Dag2Part toDag2Part() {

    var start = Dag2Part.singleExit(new EmptyNode());
    ArrayList<EdgeFrom> output = new ArrayList<>();
    ArrayList<Dag2Part> acc = new ArrayList<>();
    permutations(new ArrayList<>(block), acc, 0);

    var p = permuteInterval_0_n(acc.size());

    if (count <= 0) {
      count = 1;
    }
    if (count >= p.length) {
      count = p.length - 1;
    }

    for (int i = 0; i < count; i++) {
      Dag2Part b = acc.get(p[i]);
      IStat.joinDag2(start, b);
      output.addAll(b.output);
    }

    System.out.println("seed=" + random.seed() + " " + Arrays.toString(p));

    start.output.clear();
    start.output.addAll(output);
    return IStat.joinDag2(start, Dag2Part.singleExit(new EmptyNode()));
  }
}
