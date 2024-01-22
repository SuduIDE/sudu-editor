package org.sudu.experiments.parser.activity.graph.stat;

import org.sudu.experiments.math.XorShiftRandom;

public class RandomSrc {

  public static final int defaultSeed = 42;

  private int seed = defaultSeed;
  private XorShiftRandom random;

  public RandomSrc() {
    setSeed(seed);
  }

  public void setSeed(int seed) {
    this.seed = seed;
    random = new XorShiftRandom(this.seed, "activity".hashCode());
  }

  public int seed() {
    return seed;
  }

  public int nextInt(int limit) {
    return random.nextInt(limit);
  }
}
