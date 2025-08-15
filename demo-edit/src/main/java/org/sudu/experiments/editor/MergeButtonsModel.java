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

public class MergeButtonsModel {
  public int[] lines;
  public Runnable[] actions;
  public BooleanConsumer[] acceptReject;

  public MergeButtonsModel(int n) {
    actions = new Runnable[n];
    lines = new int[n];
  }

  public static MergeButtonsModel[] getModels(
      DiffInfo diffInfo,
      boolean leftReadonly,
      boolean rightReadonly,
      int[] syncL, int[] syncR,
      BiConsumer<DiffRange, Boolean> applyDiff
  ) {
    int n = 0;
    for (var range: diffInfo.ranges) if (range.type != DiffTypes.DEFAULT) n++;

    var left = new MergeButtonsModel(rightReadonly ? 0 : n);
    var right = new MergeButtonsModel(leftReadonly ? 0 : n);
    right.acceptReject = new BooleanConsumer[leftReadonly && rightReadonly ? 0 : n];
    int i = 0;
    for (var range: diffInfo.ranges) {
      if (range.type == DiffTypes.DEFAULT) continue;
      int leftS = Arrays.binarySearch(syncL, range.toL());
      int rightS = Arrays.binarySearch(syncR, range.toR());
      if (leftS < 0) leftS = -leftS - 1;
      if (rightS < 0) rightS = -rightS - 1;
      if (!rightReadonly) {
        int line = range.fromL;
        if (range.type == DiffTypes.DELETED && leftS > rightS) line--;
        left.lines[i] = line(line, diffInfo.lineDiffsL.length);
        left.actions[i] = () -> applyDiff.accept(range, true);
        if (i > 0 && left.lines[i - 1] > left.lines[i]) {
          ArrayOp.swap(left.lines, i, i - 1);
          ArrayOp.swap(left.actions, i, i - 1);
        }
      }
      if (!leftReadonly) {
        int line = range.fromR;
        if (range.type == DiffTypes.INSERTED && rightS > leftS) line--;
        right.lines[i] = line(line, diffInfo.lineDiffsR.length);
        right.actions[i] = () -> applyDiff.accept(range, false);
        if (i > 0 && right.lines[i - 1] > right.lines[i]) {
          ArrayOp.swap(right.lines, i, i - 1);
          ArrayOp.swap(right.actions, i, i - 1);
        }
      }
      var acceptAction = right.actions[i];
      var rejectAction = left.actions[i];
      right.acceptReject[i] = (accepted) -> {
        if (accepted) acceptAction.run();
        else rejectAction.run();
      };
      i++;
    }
    return new MergeButtonsModel[]{left, right};
  }

  public static MergeButtonsModel[] getFolderModels(
      DiffInfo diffInfo,
      FolderDiffModel[] leftDiffs,
      FolderDiffModel[] rightDiffs,
      byte[] leftColors,
      byte[] rightColors,
      boolean leftReadonly,
      boolean rightReadonly,
      BiConsumer<FolderDiffModel, Boolean> applyDiff
  ) {
    MergeButtonsModel left, right;

    if (!rightReadonly) {
      int n = 0;
      for (int i = 0; i < diffInfo.lineDiffsL.length; i++) {
        var line = diffInfo.lineDiffsL[i];
        var model = leftDiffs[i];
        if (line.type != DiffTypes.DEFAULT && model.isCompared()) n++;
      }
      left = new MergeButtonsModel(n);
      for (int lineInd = 0, modelInd = 0; lineInd < diffInfo.lineDiffsL.length; lineInd++) {
        var leftLine = diffInfo.lineDiffsL[lineInd];
        var leftModel = leftDiffs[lineInd];
        if (leftLine.type == DiffTypes.DEFAULT || !leftModel.isCompared()) continue;
        left.lines[modelInd] = line(lineInd, diffInfo.lineDiffsL.length);
        left.actions[modelInd] = () -> applyDiff(leftModel, true, applyDiff);
        leftColors[lineInd] = (byte) diffInfo.lineDiffsL[lineInd].type;  //DiffTypes.FOLDER_ALIGN_DIFF_TYPE;
        modelInd++;
      }
    } else {
      left = new MergeButtonsModel(0);
    }

    if (!leftReadonly) {
      int m = 0;
      for (int i = 0; i < diffInfo.lineDiffsR.length; i++) {
        var line = diffInfo.lineDiffsR[i];
        var model = rightDiffs[i];
        if (line.type != DiffTypes.DEFAULT && model.isCompared()) m++;
      }
      right = new MergeButtonsModel(m);
      for (int lineInd = 0, modelInd = 0; lineInd < diffInfo.lineDiffsR.length; lineInd++) {
        var rightLine = diffInfo.lineDiffsR[lineInd];
        var rightModel = rightDiffs[lineInd];
        if (rightLine.type == DiffTypes.DEFAULT || !rightModel.isCompared()) continue;
        right.lines[modelInd] = line(lineInd, diffInfo.lineDiffsR.length);
        right.actions[modelInd] = () -> applyDiff(rightModel, false, applyDiff);
        rightColors[lineInd] = (byte) diffInfo.lineDiffsR[lineInd].type;  //DiffTypes.FOLDER_ALIGN_DIFF_TYPE;
        modelInd++;
      }
    } else {
      right = new MergeButtonsModel(0);
    }
    return new MergeButtonsModel[]{left, right};
  }

  private static void applyDiff(
      FolderDiffModel model,
      boolean left,
      BiConsumer<FolderDiffModel, Boolean> applyDiff
  ) {
    if (applyDiff == null) return;
    applyDiff.accept(model, left);
  }

  private static int line(int line, int docLen) {
    return Numbers.clamp(0, line, docLen - 1);
  }

  public static class TestModel extends MergeButtonsModel {
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

    Runnable action(int pi) {
      return () -> System.out.println("[" + (++logIndex) + "] Runnable #" + pi);
    }
  }
}
