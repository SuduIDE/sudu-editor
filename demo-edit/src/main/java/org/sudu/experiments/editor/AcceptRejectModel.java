package org.sudu.experiments.editor;

import org.sudu.experiments.BooleanConsumer;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffRange;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.XorShiftRandom;

import java.util.Arrays;
import java.util.function.BiConsumer;

public class AcceptRejectModel {
  public int[] lines;
  public BooleanConsumer[] actions;

  public AcceptRejectModel(int n) {
    actions = new BooleanConsumer[n];
    lines = new int[n];
  }

  public static class TestModel extends AcceptRejectModel {
    int logIndex;

    public TestModel(int docLines) {
      this(docLines, new XorShiftRandom());
    }

    public TestModel(int docLines, XorShiftRandom rand) {
      super(docLines / 4);
      int n = lines.length;
      int space = docLines / (1 + n);
      for (int i = 0, pi = 0; i < n; i++) {
        lines[i] = pi;
        actions[i] = action(pi);
        pi += 1 + rand.nextInt(space);
      }
    }

    BooleanConsumer action(int pi) {
      return bool -> System.out.println(
          "[" + (++logIndex) + "] BooleanConsumer(" + bool + ") #" + pi);
    }
  }
}
