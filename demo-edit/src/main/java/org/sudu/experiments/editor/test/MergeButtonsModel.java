package org.sudu.experiments.editor.test;

import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffRange;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.XorShiftRandom;

import java.util.function.BiConsumer;

public class MergeButtonsModel {
  public int[] lines;
  public Runnable[] actions;

  public MergeButtonsModel(int n) {
    actions = new Runnable[n];
    lines = new int[n];
  }

  public static MergeButtonsModel[] getModels(DiffInfo diffInfo, BiConsumer<DiffRange, Boolean> applyDiff) {
    int n = 0;
    for (var range: diffInfo.ranges) if (range.type != DiffTypes.DEFAULT) n++;

    var left = new MergeButtonsModel(n);
    var right = new MergeButtonsModel(n);
    int i = 0;
    for (var range: diffInfo.ranges) {
      if (range.type == DiffTypes.DEFAULT) continue;
      left.lines[i] = line(range.fromL, diffInfo.lineDiffsL.length);
      left.actions[i] = () -> applyDiff.accept(range, true);
      right.lines[i] = line(range.fromR, diffInfo.lineDiffsR.length);
      right.actions[i] = () -> applyDiff.accept(range, false);
      i++;
    }
    return new MergeButtonsModel[]{left, right};
  }

  private static int line(int line, int docLen) {
    return Numbers.clamp(0, line, docLen - 1);
  }

  public static class TestModel extends MergeButtonsModel {
    public TestModel(int docLines) {
      super(docLines);
      int n = docLines / 4;
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
}
