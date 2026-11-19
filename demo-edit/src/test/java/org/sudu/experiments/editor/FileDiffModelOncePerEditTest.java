package org.sudu.experiments.editor;

import org.junit.jupiter.api.Test;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.ui.colors.CodeLineColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffRange;
import org.sudu.experiments.editor.worker.diff.FileDiffModel;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.common.Pair;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileDiffModelOncePerEditTest {

  @Test
  void oneInsertShiftsRangesOnceWithTwoEditorsOnTheSameSide() {
    Model left = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel diff = new FileDiffModel(new NoopExecutor(), left, right);
    diff.diffModel = new DiffInfo(
        new LineDiff[3],
        new LineDiff[3],
        new DiffRange[] {
            new DiffRange(0, 1, 0, 1, DiffTypes.DEFAULT),
            new DiffRange(1, 2, 1, 2, DiffTypes.EDITED)
        }
    );

    CountingView view1 = new CountingView();
    CountingView view2 = new CountingView();
    diff.addView(view1);
    diff.addView(view2);

    CountingEditor editor1 = new CountingEditor();
    CountingEditor editor2 = new CountingEditor();
    left.attachEditor(editor1, null);
    left.attachEditor(editor2, null);

    left.document.insertLines(0, 0, new String[]{"x", "y"});

    assertEquals(2, diff.diffModel.ranges[1].fromL);
    assertEquals(2, diff.diffModel.ranges[0].lenL);
    assertEquals(1, view1.updateModelOnDiff);
    assertEquals(1, view2.updateModelOnDiff);
    assertEquals(1, editor1.updateModelOnDiff);
    assertEquals(1, editor2.updateModelOnDiff);
  }

  @Test
  void twoFileDiffModelsOnTheSameDocumentsBothShiftOnce() {
    Model left = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel first = new FileDiffModel(new NoopExecutor(), left, right);
    FileDiffModel second = new FileDiffModel(new NoopExecutor(), left, right);
    first.diffModel = new DiffInfo(
        new LineDiff[3], new LineDiff[3],
        new DiffRange[] {
            new DiffRange(0, 1, 0, 1, DiffTypes.DEFAULT),
            new DiffRange(1, 2, 1, 2, DiffTypes.EDITED)
        }
    );
    second.diffModel = new DiffInfo(
        new LineDiff[3], new LineDiff[3],
        new DiffRange[] {
            new DiffRange(0, 1, 0, 1, DiffTypes.DEFAULT),
            new DiffRange(1, 2, 1, 2, DiffTypes.EDITED)
        }
    );
    CountingView firstView = new CountingView();
    CountingView secondView = new CountingView();
    first.addView(firstView);
    second.addView(secondView);

    left.document.insertLines(0, 0, new String[]{"x", "y"});

    assertEquals(2, first.diffModel.ranges[1].fromL);
    assertEquals(2, second.diffModel.ranges[1].fromL);
    assertEquals(1, firstView.updateModelOnDiff);
    assertEquals(1, secondView.updateModelOnDiff);

    first.removeView(firstView);
    left.document.insertLines(0, 0, new String[]{"p", "q"});

    assertEquals(2, first.diffModel.ranges[1].fromL);
    assertEquals(3, second.diffModel.ranges[1].fromL);
  }

  @Test
  void addViewAfterLastRemoveResubscribes() {
    Model left = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel diff = new FileDiffModel(new NoopExecutor(), left, right);
    diff.diffModel = editedLaterRange();
    CountingView view = new CountingView();
    diff.addView(view);
    diff.removeView(view);
    diff.addView(view);

    left.document.insertLines(0, 0, new String[]{"x", "y"});

    assertEquals(2, diff.diffModel.ranges[1].fromL);
    assertEquals(1, view.updateModelOnDiff);
  }

  @Test
  void strayRemoveViewDoesNotUnbind() {
    Model left = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel diff = new FileDiffModel(new NoopExecutor(), left, right);
    diff.diffModel = editedLaterRange();
    diff.removeView(new CountingView());

    left.document.insertLines(0, 0, new String[]{"x", "y"});

    assertEquals(2, diff.diffModel.ranges[1].fromL);
  }

  @Test
  void twoFileDiffModelsShareUndoStack() {
    Model left = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel first = new FileDiffModel(new NoopExecutor(), left, right);
    FileDiffModel second = new FileDiffModel(new NoopExecutor(), left, right);
    assertSame(first.undoBuffer, second.undoBuffer);

    left.document.insertLines(0, 0, new String[]{"x"});
    assertEquals("xa", left.document.line(0).makeString());

    first.undoLastDiff(false);
    assertEquals("a", left.document.line(0).makeString());
  }

  @Test
  void rebindAdoptsTheLiveUndoBuffer() {
    Model left = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel first = new FileDiffModel(new NoopExecutor(), left, right);
    CountingView view = new CountingView();
    first.addView(view);
    first.removeView(view);

    FileDiffModel second = new FileDiffModel(new NoopExecutor(), left, right);
    first.addView(view);
    assertSame(first.undoBuffer, second.undoBuffer);

    left.document.insertLines(0, 0, new String[]{"x"});
    assertEquals("xa", left.document.line(0).makeString());

    first.undoLastDiff(false);
    assertEquals("a", left.document.line(0).makeString());
  }

  @Test
  void rebindIgnoresEditorSupplierThatReadsStaleField() {
    Model left = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel first = new FileDiffModel(new NoopExecutor(), left, right);
    CountingView view = new CountingView();
    first.addView(view);
    first.removeView(view);

    FileDiffModel second = new FileDiffModel(new NoopExecutor(), left, right);
    left.prependGetUndoBuffer(() -> first.undoBuffer);
    first.addView(view);
    assertSame(first.undoBuffer, second.undoBuffer);

    left.document.insertLines(0, 0, new String[]{"x"});
    assertEquals("xa", left.document.line(0).makeString());

    first.undoLastDiff(false);
    assertEquals("a", left.document.line(0).makeString());
  }

  @Test
  void rebindWithLiveEditorSupplierDoesNotRecurse() {
    Model left = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel first = new FileDiffModel(new NoopExecutor(), left, right);
    CountingView view = new CountingView();
    first.addView(view);
    first.removeView(view);

    FileDiffModel second = new FileDiffModel(new NoopExecutor(), left, right);
    left.prependGetUndoBuffer(() -> left.undoBuffer());
    first.addView(view);
    assertSame(first.undoBuffer, second.undoBuffer);

    left.document.insertLines(0, 0, new String[]{"x"});
    assertEquals("xa", left.document.line(0).makeString());

    first.undoLastDiff(false);
    assertEquals("a", left.document.line(0).makeString());
  }

  @Test
  void disposingEditorUnregistersUndoSupplier() {
    Model left = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel diff = new FileDiffModel(new NoopExecutor(), left, right);
    Supplier<UndoBuffer> editor = () -> left.undoBuffer();
    left.addGetUndoBuffer(editor);
    assertTrue(left.hasGetUndoBuffer(editor));

    left.removeGetUndoBuffer(editor);
    assertFalse(left.hasGetUndoBuffer(editor));
    assertSame(diff.undoBuffer, left.undoBuffer());
  }

  @Test
  void overlappingPairDoesNotStealRightUndoBuffer() {
    Model left1 = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel first = new FileDiffModel(new NoopExecutor(), left1, right);
    Model left2 = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel second = new FileDiffModel(new NoopExecutor(), left2, right);
    assertSame(first.undoBuffer, right.undoBuffer());
    assertSame(first.undoBuffer, second.undoBuffer);

    right.document.insertLines(0, 0, new String[]{"x"});
    assertEquals("xa", right.document.line(0).makeString());

    first.undoLastDiff(false);
    assertEquals("a", right.document.line(0).makeString());
  }

  @Test
  void joinPairUndoesRightFromItsLiveBuffer() {
    Model left1 = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right1 = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel first = new FileDiffModel(new NoopExecutor(), left1, right1);
    Model left2 = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right2 = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel second = new FileDiffModel(new NoopExecutor(), left2, right2);
    FileDiffModel join = new FileDiffModel(new NoopExecutor(), left1, right2);
    assertSame(second.undoBuffer, right2.undoBuffer());

    left1.document.insertLines(0, 0, new String[]{"x"});
    right2.document.insertLines(0, 0, new String[]{"y"});
    assertEquals("xa", left1.document.line(0).makeString());
    assertEquals("ya", right2.document.line(0).makeString());

    join.undoLastDiff(false);
    assertEquals("xa", left1.document.line(0).makeString());
    assertEquals("a", right2.document.line(0).makeString());

    first.undoLastDiff(false);
    assertEquals("a", left1.document.line(0).makeString());
  }

  @Test
  void joinPairWithEditorSupplierUndoesRightFromLiveBuffer() {
    Model left1 = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right1 = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel first = new FileDiffModel(new NoopExecutor(), left1, right1);
    Model left2 = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right2 = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel second = new FileDiffModel(new NoopExecutor(), left2, right2);
    FileDiffModel join = new FileDiffModel(new NoopExecutor(), left1, right2);
    right2.prependGetUndoBuffer(() -> right2.undoBuffer());
    assertSame(second.undoBuffer, right2.undoBuffer());

    left1.document.insertLines(0, 0, new String[]{"x"});
    right2.document.insertLines(0, 0, new String[]{"y"});
    assertEquals("xa", left1.document.line(0).makeString());
    assertEquals("ya", right2.document.line(0).makeString());

    join.undoLastDiff(false);
    assertEquals("xa", left1.document.line(0).makeString());
    assertEquals("a", right2.document.line(0).makeString());

    first.undoLastDiff(false);
    assertEquals("a", left1.document.line(0).makeString());
  }

  @Test
  void syncEditRunsOnceFromAnEnabledModel() {
    Model left = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel first = new FileDiffModel(new NoopExecutor(), left, right);
    FileDiffModel second = new FileDiffModel(new NoopExecutor(), left, right);
    first.diffModel = equalRange();
    second.diffModel = equalRange();
    first.setEnableSyncEdit(false);
    second.setEnableSyncEdit(true);

    left.document.insertLines(0, 0, new String[]{"x"});

    assertEquals("xa", left.document.line(0).makeString());
    assertEquals("xa", right.document.line(0).makeString());
  }

  @Test
  void twoEnabledSyncEditsMirrorOnce() {
    Model left = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel first = new FileDiffModel(new NoopExecutor(), left, right);
    FileDiffModel second = new FileDiffModel(new NoopExecutor(), left, right);
    first.diffModel = equalRange();
    second.diffModel = equalRange();
    first.setEnableSyncEdit(true);
    second.setEnableSyncEdit(true);

    left.document.insertLines(0, 0, new String[]{"x"});

    assertEquals("xa", left.document.line(0).makeString());
    assertEquals("xa", right.document.line(0).makeString());
  }

  @Test
  void emptyOppositeFallsThroughToAMappedModel() {
    Model left = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel first = new FileDiffModel(new NoopExecutor(), left, right);
    FileDiffModel second = new FileDiffModel(new NoopExecutor(), left, right);
    first.diffModel = editedAllRange();
    second.diffModel = equalRange();
    first.setEnableSyncEdit(true);
    second.setEnableSyncEdit(true);

    left.document.insertLines(0, 0, new String[]{"x"});

    assertEquals("xa", left.document.line(0).makeString());
    assertEquals("xa", right.document.line(0).makeString());
  }

  @Test
  void emptyOppositeDoesNotMirrorOntoADifferentPair() {
    Model left = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right1 = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right2 = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel first = new FileDiffModel(new NoopExecutor(), left, right1);
    FileDiffModel second = new FileDiffModel(new NoopExecutor(), left, right2);
    first.diffModel = editedAllRange();
    second.diffModel = equalRange();
    first.setEnableSyncEdit(true);
    second.setEnableSyncEdit(true);

    left.document.insertLines(0, 0, new String[]{"x"});

    assertEquals("xa", left.document.line(0).makeString());
    assertEquals("a", right1.document.line(0).makeString());
    assertEquals("a", right2.document.line(0).makeString());
  }

  @Test
  void missingDiffClaimsPeerAndDoesNotMirrorOntoAnotherPair() {
    Model left = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right1 = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right2 = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel first = new FileDiffModel(new NoopExecutor(), left, right1);
    FileDiffModel second = new FileDiffModel(new NoopExecutor(), left, right2);
    first.setEnableSyncEdit(true);
    second.diffModel = equalRange();
    second.setEnableSyncEdit(true);

    left.document.insertLines(0, 0, new String[]{"x"});

    assertEquals("xa", left.document.line(0).makeString());
    assertEquals("a", right1.document.line(0).makeString());
    assertEquals("a", right2.document.line(0).makeString());
  }

  static DiffInfo editedLaterRange() {
    return new DiffInfo(
        new LineDiff[3], new LineDiff[3],
        new DiffRange[] {
            new DiffRange(0, 1, 0, 1, DiffTypes.DEFAULT),
            new DiffRange(1, 2, 1, 2, DiffTypes.EDITED)
        }
    );
  }

  static DiffInfo equalRange() {
    return new DiffInfo(
        new LineDiff[3], new LineDiff[3],
        new DiffRange[] {
            new DiffRange(0, 3, 0, 3, DiffTypes.DEFAULT)
        }
    );
  }

  static DiffInfo editedAllRange() {
    return new DiffInfo(
        new LineDiff[3], new LineDiff[3],
        new DiffRange[] {
            new DiffRange(0, 3, 0, 3, DiffTypes.EDITED)
        }
    );
  }

  static final class CountingView implements FileDiffModel.ViewToModel {
    int updateModelOnDiff;

    public int[] getSyncPoints(boolean left) { return new int[0]; }
    public void updateModelOnDiff(DiffInfo diffModel) { updateModelOnDiff++; }
    public void setDiffModel(DiffInfo diffModel) {}
    public void restoreSelection(boolean left, V2i caret, Pair<Pos, Pos> selection) {}
  }

  static final class CountingEditor implements Model.EditorToModel {
    int updateModelOnDiff;

    public void useDocumentHighlightProvider(int line, int column) {}
    public void fireFileLexed() {}
    public void fireFileIterativeParsed(int start, int stop) {}
    public void updateModelOnDiff(Diff diff, boolean isUndo) { updateModelOnDiff++; }
    public void onDiffMade() {}
    public boolean isDisableParser() { return true; }
    public CodeLineColorScheme getColorScheme() { return null; }
  }

  static final class NoopExecutor implements WorkerJobExecutor {
    public void sendToWorker(
        boolean priority, Consumer<Object[]> handler, String method, Object... args
    ) {}
  }
}
