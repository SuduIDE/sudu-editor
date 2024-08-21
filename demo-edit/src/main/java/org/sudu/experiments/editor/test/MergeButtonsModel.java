package org.sudu.experiments.editor.test;

import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffRange;

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
      left.lines[i] = range.fromL;
      left.actions[i] = () -> applyDiff.accept(range, true);
      right.lines[i] = range.fromR;
      right.actions[i] = () -> applyDiff.accept(range, false);
      i++;
    }
    return new MergeButtonsModel[]{left, right};
  }
}
