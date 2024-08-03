package org.sudu.experiments.editor.test;

import org.sudu.experiments.math.XorShiftRandom;

public class MergeButtonsTestModel {
  public int[] lines;
  public Runnable[] actions;

  public MergeButtonsTestModel(int docLines) {
    int n = docLines / 4;
    actions = new Runnable[n];
    lines = new int[n];
    XorShiftRandom rand = new XorShiftRandom();
    int space = docLines / (1 + n);
    for (int i = 0, pi = 0; i < n; i++) {
      lines[i] = pi;
      actions[i] = action(pi);
      pi += 1 + rand.nextInt(space);
    }
  }

  static Runnable action(int pi) {
    return () -> System.out.println("Runnable #" + pi);
  }
}
