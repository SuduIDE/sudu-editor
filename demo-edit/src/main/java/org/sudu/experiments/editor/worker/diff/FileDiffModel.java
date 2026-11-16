package org.sudu.experiments.editor.worker.diff;

import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.editor.*;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.common.Pair;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.common.TriConsumer;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.IntConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FileDiffModel {

  public static final int LEFT_MODEL_BIT = 0b01;
  public static final int RIGHT_MODEL_BIT = 0b10;

  public final Model leftModel, rightModel;
  public UndoBuffer undoBuffer;
  public DiffInfo diffModel;
  public ViewToModel[] viewsToModel;

  WorkerJobExecutor executor;

  private Consumer<int[]> getLinesInfo;
  private Consumer<ApplyChangeInfo> applyRejectListener;
  private int diffStatus = DiffStatus.NOT_COMPARED;
  private boolean enableSyncEdit = EditorConst.DEFAULT_ENABLE_SYNC_EDIT;
  private int modelFlags;
  private boolean boundToDocuments;

  private final BiConsumer<Diff, Boolean> leftOnUpdateModelOnDiff =
      (diff, isUndo) -> updateModelOnDiffMadeListener(true, diff, isUndo);
  private final BiConsumer<Integer, Integer> leftOnIterativeParsed =
      (start, stop) -> iterativeParseFileListener(true, start, stop);
  private final Runnable leftOnFileLexed = () -> fullFileLexedListener(true);
  private final BiConsumer<Diff, Boolean> rightOnUpdateModelOnDiff =
      (diff, isUndo) -> updateModelOnDiffMadeListener(false, diff, isUndo);
  private final BiConsumer<Integer, Integer> rightOnIterativeParsed =
      (start, stop) -> iterativeParseFileListener(false, start, stop);
  private final Runnable rightOnFileLexed = () -> fullFileLexedListener(false);
  private final Supplier<UndoBuffer> getUndoBuffer;
  private final BiPredicate<CpxDiff, Boolean> leftSyncEditing =
      (diff, isUndo) -> syncEditing(true, diff, isUndo);
  private final BiPredicate<CpxDiff, Boolean> rightSyncEditing =
      (diff, isUndo) -> syncEditing(false, diff, isUndo);

  public interface ViewToModel {
    int[] getSyncPoints(boolean left);
    void updateModelOnDiff(DiffInfo diffModel);
    void setDiffModel(DiffInfo diffModel);
    void restoreSelection(boolean left, V2i caret, Pair<Pos, Pos> selection);
  }

  public static class ApplyChangeInfo {
    public int oldFrom, oldTo;
    public int newFrom, newTo;
    public boolean isAccepted;

    public ApplyChangeInfo(
        int oldFrom, int oldTo,
        int newFrom, int newTo,
        boolean isAccepted
    ) {
      this.oldFrom = oldFrom;
      this.oldTo = oldTo;
      this.newFrom = newFrom;
      this.newTo = newTo;
      this.isAccepted = isAccepted;
    }
  }

  public FileDiffModel(WorkerJobExecutor executor, Model leftModel, Model rightModel) {
    this.leftModel = leftModel;
    this.rightModel = rightModel;
    this.getUndoBuffer = () -> this.undoBuffer;
    this.executor = executor;
    bindToDocuments();
    if (leftModel.fileLexed() && rightModel.fileLexed()) sendToDiff(false);
  }

  void adoptUndoBuffer() {
    UndoBuffer existing = leftModel.undoBuffer();
    if (existing == null) existing = rightModel.undoBuffer();
    if (existing != null) undoBuffer = existing;
    else if (undoBuffer == null) undoBuffer = new UndoBuffer();
  }

  void bindToDocuments() {
    if (boundToDocuments) return;
    adoptUndoBuffer();
    leftModel.addOnUpdateModelOnDiff(leftOnUpdateModelOnDiff);
    leftModel.addOnIterativeParsed(leftOnIterativeParsed);
    leftModel.addOnFileLexed(leftOnFileLexed);
    leftModel.addGetUndoBuffer(getUndoBuffer);
    leftModel.addSyncEditing(leftSyncEditing);
    rightModel.addOnUpdateModelOnDiff(rightOnUpdateModelOnDiff);
    rightModel.addOnIterativeParsed(rightOnIterativeParsed);
    rightModel.addOnFileLexed(rightOnFileLexed);
    rightModel.addGetUndoBuffer(getUndoBuffer);
    rightModel.addSyncEditing(rightSyncEditing);
    boundToDocuments = true;
  }

  public void unbindFromDocuments() {
    if (!boundToDocuments) return;
    leftModel.removeOnUpdateModelOnDiff(leftOnUpdateModelOnDiff);
    leftModel.removeOnIterativeParsed(leftOnIterativeParsed);
    leftModel.removeOnFileLexed(leftOnFileLexed);
    leftModel.removeGetUndoBuffer(getUndoBuffer);
    leftModel.removeSyncEditing(leftSyncEditing);
    rightModel.removeOnUpdateModelOnDiff(rightOnUpdateModelOnDiff);
    rightModel.removeOnIterativeParsed(rightOnIterativeParsed);
    rightModel.removeOnFileLexed(rightOnFileLexed);
    rightModel.removeGetUndoBuffer(getUndoBuffer);
    rightModel.removeSyncEditing(rightSyncEditing);
    boundToDocuments = false;
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
    setModelFlagsBit(left);
    if (modelFlagsReady()) sendToDiff(false);
  }

  public void updateModelOnDiffMadeListener(boolean left, Diff diff, boolean isUndo) {
    unsetModelFlagsBit(left);
    boolean isDelete = diff.isDelete ^ isUndo;

    if (isDelete) onDeleteDiffMadeListener(diff, left);
    else onInsertDiffMadeListener(diff, left);
    if (diffModel != null) {
      forEachView(v -> v.updateModelOnDiff(diffModel));
    }
  }

  public void iterativeParseFileListener(boolean left, int start, int stop) {
    setModelFlagsBit(left);

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

  public void applyDiff(DiffRange range, boolean left) {
    if (range.type == DiffTypes.DEFAULT) return;
    var fromModel = left ? leftModel : rightModel;
    var toModel = !left ? leftModel : rightModel;

    int fromStartLine = range.from(left);
    int fromEndLine = range.to(left);
    var lines = fromModel.document.getLines(fromStartLine, fromEndLine);

    int toStartLine = range.from(!left);
    int toEndLine = range.to(!left);

    toModel.document.applyChange(toStartLine, toEndLine, lines);

    if (applyRejectListener == null) return;
    applyRejectListener.accept(
        new ApplyChangeInfo(
            range.fromL, range.toL(),
            range.fromR, range.toR(),
            !left
        )
    );
  }

  public boolean syncEditing(boolean left, CpxDiff cpxDiff, boolean isUndo) {
    if (!enableSyncEdit || diffModel == null) return false;
    Model current = left ? leftModel : rightModel;
    var diffVersion = current.document.lastDiffVersion();
    Model another = !left ? leftModel : rightModel;

    CpxDiff anotherDiff = CpxDiff.DiffMaker.mkOppositeDiff(this, cpxDiff, left);

    if (anotherDiff.isEmpty()) return false;
    another.document.doCpxDiff(anotherDiff, !isUndo);
    undoBuffer.addDiff(another.document, anotherDiff, diffVersion.second);
    return true;
  }

  public void undoLastDiff(boolean isRedo) {
    undoBuffer.undoLastDiff(leftModel.document, rightModel.document, restoreSelection(), isRedo);
  }

  public void setDiffModel(DiffInfo diffInfo, int[] versions) {
    if (!Arrays.equals(versions, docVersions())) return;
    this.diffModel = diffInfo;
    diffStatus = DiffStatus.COMPARED;
    if (getLinesInfo != null) getLinesInfo.accept(diffInfo.linesInfo());
    forEachView(v -> v.setDiffModel(diffModel));
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
    return diffModel == null || diffModel.isEmpty();
  }

  public void clearCompactView() {
    if (diffModel != null) diffModel.clearCompactView();
  }

  public void buildCompactView(Consumer<IntConsumer> apply) {
    if (diffModel != null) diffModel.buildCompactView(apply);
  }

  int[] getSyncPoints(boolean left) {
    if (viewsToModel == null) return new int[0];
    for (ViewToModel v : viewsToModel) {
      int[] sp = v.getSyncPoints(left);
      if (sp != null && sp.length > 0) return sp;
    }
    return new int[0];
  }

  boolean hasSyncPoints(boolean left) {
    return getSyncPoints(left).length > 0;
  }

  public DiffRange firstRange(int caretL, int caretR) {
    if (diffModel == null) return null;
    for (var range: diffModel.ranges) {
      if (range.type == DiffTypes.DEFAULT) continue;
      return (range.inside(caretL, true) || range.inside(caretR, false))
          ? null : range;
    }
    return null;
  }

  public boolean canNavigateUp(int lineInd, boolean left) {
    if (diffModel == null) return false;
    int rangeInd = diffModel.leftBS(lineInd, left);
    for (int i = rangeInd - 1; i >= 0; i--) {
      if (diffModel.ranges[i].type != DiffTypes.DEFAULT) return true;
    }
    return false;
  }

  public DiffRange navigateUp(int lineInd, boolean left) {
    if (diffModel == null) return null;
    int rangeInd = diffModel.leftBS(lineInd, left);
    for (int i = rangeInd - 1; i >= 0; i--) {
      if (diffModel.ranges[i].type != DiffTypes.DEFAULT) return diffModel.ranges[i];
    }
    return null;
  }

  public boolean canNavigateDown(int lineInd, boolean left) {
    if (diffModel == null) return false;
    int rangeInd = diffModel.rangeBinSearch(lineInd, left);
    for (int i = rangeInd + 1; i < diffModel.ranges.length; i++) {
      if (diffModel.ranges[i].type != DiffTypes.DEFAULT) return true;
    }
    return false;
  }

  public DiffRange navigateDown(int lineInd, boolean left) {
    if (diffModel == null) return null;
    int rangeInd = diffModel.rangeBinSearch(lineInd, left);
    for (int i = rangeInd + 1; i < diffModel.ranges.length; i++) {
      if (diffModel.ranges[i].type != DiffTypes.DEFAULT) return diffModel.ranges[i];
    }
    return null;
  }

  int[] docVersions() {
    return new int[]{leftModel.document.version(), rightModel.document.version()};
  }

  public boolean modelFlagsReady() {
    return (modelFlags & 0b11) == 0b11;
  }

  public int getModelFlags() {
    return modelFlags;
  }

  public void setModelFlagsBit(boolean left) {
    setModelFlagsBit(left ? LEFT_MODEL_BIT : RIGHT_MODEL_BIT);
  }

  public void unsetModelFlagsBit(boolean left) {
    unsetModelFlagsBit(left ? LEFT_MODEL_BIT : RIGHT_MODEL_BIT);
  }

  public void unsetModelFlagsBit(int bit) {
    modelFlags &= ~bit;
  }

  public void setModelFlagsBit(int bit) {
    modelFlags |= bit;
  }

  public void setEnableSyncEdit(boolean enableSyncEdit) {
    this.enableSyncEdit = enableSyncEdit;
  }

  public void setApplyRejectListener(Consumer<ApplyChangeInfo> applyRejectListener) {
    this.applyRejectListener = applyRejectListener;
  }

  public TriConsumer<Boolean, V2i, Pair<Pos, Pos>> restoreSelection() {
    if (viewsToModel == null) return null;
    return (left, caret, selection) ->
        forEachView(v -> v.restoreSelection(left, caret, selection));
  }

  public void addView(ViewToModel view) {
    if (view == null) return;
    bindToDocuments();
    if (viewsToModel == null) {
      viewsToModel = new ViewToModel[]{view};
      return;
    }
    if (ArrayOp.indexOf(viewsToModel, view) >= 0) return;
    viewsToModel = ArrayOp.add(viewsToModel, view);
  }

  public void removeView(ViewToModel view) {
    ViewToModel[] before = viewsToModel;
    viewsToModel = ArrayOp.remove(viewsToModel, view);
    if (before != null && viewsToModel == null) unbindFromDocuments();
  }

  private void forEachView(Consumer<ViewToModel> action) {
    if (viewsToModel == null) return;
    for (ViewToModel v : viewsToModel) action.accept(v);
  }
}
