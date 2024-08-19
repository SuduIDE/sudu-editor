package org.sudu.experiments.editor.test;

import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffRange;

import java.util.function.BiConsumer;

public class MergeButtonsTestModel {
  public int[] lines;
  public Runnable[] actions;

  public MergeButtonsTestModel(int n) {
    actions = new Runnable[n];
    lines = new int[n];
  }

  public static MergeButtonsTestModel[] getModels(DiffInfo diffInfo, BiConsumer<DiffRange, Boolean> applyDiff) {
    int n = 0;
    for (var range: diffInfo.ranges) if (range.type != DiffTypes.DEFAULT) n++;

    var left = new MergeButtonsTestModel(n);
    var right = new MergeButtonsTestModel(n);
    int i = 0;
    for (var range: diffInfo.ranges) {
      if (range.type == DiffTypes.DEFAULT) continue;
      left.lines[i] = range.fromL;
      left.actions[i] = () -> applyDiff.accept(range, true);
      right.lines[i] = range.fromR;
      right.actions[i] = () -> applyDiff.accept(range, false);
      i++;
    }
    return new MergeButtonsTestModel[]{left, right};
  }

  static Runnable action(int pi) {
    return () -> System.out.println("Runnable #" + pi);
  }
}
