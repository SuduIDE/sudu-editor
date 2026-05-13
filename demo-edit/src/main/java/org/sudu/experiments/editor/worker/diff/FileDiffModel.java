package org.sudu.experiments.editor.worker.diff;

import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.editor.*;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.common.Pair;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.common.TriConsumer;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.Arrays;
import java.util.function.IntConsumer;
import java.util.function.Consumer;

public class FileDiffModel {

  public final Model leftModel, rightModel;
  public final UndoBuffer undoBuffer = new UndoBuffer();
  public DiffInfo diffModel;
  public ViewToModel viewToModel;

  WorkerJobExecutor executor;

  private Consumer<int[]> getLinesInfo;
  private int diffStatus = DiffStatus.NOT_COMPARED;
  private int modelFlags;

  public interface ViewToModel {
    int[] getSyncPoints(boolean left);
    void updateModelOnDiff(DiffInfo diffModel);
    void setDiffModel(DiffInfo diffModel);
  }

  public FileDiffModel(Model leftModel, Model rightModel) {
    this.leftModel = leftModel;
    this.rightModel = rightModel;
  }

  public void sendToDiff(boolean cmpOnlyLines) {
    int[] syncL = getSyncPoints(true);
    int[] syncR = getSyncPoints(false);
    if (syncL.length != syncR.length) return;
    diffStatus = DiffStatus.SENT;
    DiffUtils.findDiffs(
        leftModel.document,
        rightModel.document,
        cmpOnlyLines,
        syncL, syncR,
        this::setDiffModel,
        executor
    );
  }

  protected void sendIntervalToDiff(
      int fromL, int toL,
      int fromR, int toR
  ) {
    if (hasSyncPoints(true) || hasSyncPoints(false)) {
      sendToDiff(false);
    } else {
      DiffUtils.findIntervalDiffs(
          leftModel.document, rightModel.document,
          (upd, versions) -> updateDiffModel(fromL, toL, fromR, toR, versions, upd),
          executor,
          fromL, toL,
          fromR, toR
      );
    }
  }

  public void fullFileLexedListener(boolean left) {
    setModelFlagsBit(left ? 0b01 : 0b10);
    if (modelFlagsReady()) sendToDiff(false);
  }

  public void updateModelOnDiffMadeListener(boolean left, Diff diff, boolean isUndo) {
    unsetModelFlagsBit(left ? 0b01 : 0b10);
    boolean isDelete = diff.isDelete ^ isUndo;

    if (isDelete) onDeleteDiffMadeListener(diff, left);
    else onInsertDiffMadeListener(diff, left);
    if (viewToModel != null) {
      viewToModel.updateModelOnDiff(diffModel);
    }
  }

  public void iterativeParseFileListener(boolean left, int start, int stop) {
    setModelFlagsBit(left ? 0b01 : 0b10);

    if (diffModel == null) {
      if (modelFlagsReady()) sendToDiff(false);
      return;
    }
    var model = left ? leftModel : rightModel;
    int startLine = model.document.getLine(start).x;
    int stopLine = model.document.getLine(stop).x;
    var fromRangeInd = diffModel.leftNotEmptyBS(startLine, left);
    var toRangeInd = diffModel.rightNotEmptyBS(stopLine, left);

    if (fromRangeInd != 0 && diffModel.ranges[fromRangeInd].type != DiffTypes.DEFAULT) fromRangeInd--;
    if (toRangeInd != diffModel.rangeCount() - 1 && diffModel.ranges[toRangeInd].type != DiffTypes.DEFAULT)
      toRangeInd++;

    var fromRange = diffModel.ranges[fromRangeInd];
    var toRange = diffModel.ranges[toRangeInd];

    if (modelFlagsReady()) sendIntervalToDiff(fromRange.fromL, toRange.toL(), fromRange.fromR, toRange.toR());
  }

  public void getLinesInfo(Consumer<int[]> linesInfoConsumer) {
    if (diffStatus == DiffStatus.COMPARED) {
      linesInfoConsumer.accept(diffModel.linesInfo());
      return;
    }
    getLinesInfo = linesInfoConsumer;
    if (diffStatus == DiffStatus.NOT_COMPARED) sendToDiff(true);
  }

  public void syncEditing(boolean left, CpxDiff cpxDiff, boolean isUndo) {
    if (diffModel == null) return;
    Model current = left ? leftModel : rightModel;
    var diffVersion = current.document.lastDiffVersion();
    Model another = !left ? leftModel : rightModel;

    CpxDiff anotherDiff = cpxDiff.copyWithNewLine((l) -> diffModel.oppositeLine(l, left));

    if (anotherDiff.diffs.length == 0) return;
    another.document.doCpxDiff(anotherDiff, !isUndo);
    undoBuffer.addDiff(another.document, anotherDiff, diffVersion.second);
  }

  public void undoLastDiff(boolean isRedo, TriConsumer<Boolean, V2i, Pair<Pos, Pos>> restore) {
    undoBuffer.undoLastDiff(leftModel.document, rightModel.document, restore, isRedo);
  }

  public void setDiffModel(DiffInfo diffInfo, int[] versions) {
    if (!Arrays.equals(versions, docVersions())) return;
    this.diffModel = diffInfo;
    diffStatus = DiffStatus.COMPARED;
    if (getLinesInfo != null) getLinesInfo.accept(diffInfo.linesInfo());
    if (viewToModel != null) viewToModel.setDiffModel(diffModel);
  }

  void updateDiffModel(
      int fromL, int toL,
      int fromR, int toR,
      int[] versions,
      DiffInfo updateInfo
  ) {
    if (!Arrays.equals(versions, docVersions())) return;
    diffModel.updateDiffInfo(fromL, toL, fromR, toR, updateInfo);
    setDiffModel(diffModel, versions);
  }

  private void onInsertDiffMadeListener(Diff diff, boolean left) {
    if (diffModel != null) {
      diffModel.insertAt(diff.line, diff.lineCount(), left);
    }
  }

  private void onDeleteDiffMadeListener(Diff diff, boolean left) {
    if (diffModel != null) {
      diffModel.deleteAt(diff.line, diff.lineCount(), left);
    }
  }

  public boolean isEmpty() {
    return diffModel.isEmpty();
  }

  public void clearCompactView() {
    diffModel.clearCompactView();
  }

  public void buildCompactView(Consumer<IntConsumer> apply) {
    diffModel.buildCompactView(apply);
  }

  int[] getSyncPoints(boolean left) {
    if (viewToModel == null) return new int[]{};
    return viewToModel.getSyncPoints(left);
  }

  boolean hasSyncPoints(boolean left) {
    return getSyncPoints(left).length == 0;
  }

  public DiffRange firstRange(int caretL, int caretR) {
    for (var range: diffModel.ranges) {
      if (range.type == DiffTypes.DEFAULT) continue;
      return (range.inside(caretL, true) || range.inside(caretR, false))
          ? null : range;
    }
    return null;
  }

  public boolean canNavigateUp(int lineInd, boolean left) {
    int rangeInd = diffModel.leftBS(lineInd, left);
    for (int i = rangeInd - 1; i >= 0; i--) {
      if (diffModel.ranges[i].type != DiffTypes.DEFAULT) return true;
    }
    return false;
  }

  public DiffRange navigateUp(int lineInd, boolean left) {
    int rangeInd = diffModel.leftBS(lineInd, left);
    for (int i = rangeInd - 1; i >= 0; i--){
      if (diffModel.ranges[i].type != DiffTypes.DEFAULT) return diffModel.ranges[i];
    }
    return null;
  }

  public boolean canNavigateDown(int lineInd, boolean left) {
    int rangeInd = diffModel.rangeBinSearch(lineInd, left);
    for (int i = rangeInd + 1; i < diffModel.ranges.length; i++) {
      if (diffModel.ranges[i].type != DiffTypes.DEFAULT) return true;
    }
    return false;
  }

  public DiffRange navigateDown(int lineInd, boolean left) {
    int rangeInd = diffModel.rangeBinSearch(lineInd, left);
    for (int i = rangeInd + 1; i < diffModel.ranges.length; i++) {
      if (diffModel.ranges[i].type != DiffTypes.DEFAULT) return diffModel.ranges[i];
    }
    return null;
  }

  public void setExecutor(WorkerJobExecutor executor) {
    this.executor = executor;
  }

  int[] docVersions() {
    return new int[]{leftModel.document.version(), rightModel.document.version()};
  }

  public boolean modelFlagsReady() {
    return (modelFlags & 0b11) == 0b11;
  }

  public void unsetModelFlagsBit(int bit) {
    modelFlags &= ~bit;
  }

  public void setModelFlagsBit(int bit) {
    modelFlags |= bit;
  }
}
