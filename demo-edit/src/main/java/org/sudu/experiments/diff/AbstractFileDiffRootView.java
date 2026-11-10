package org.sudu.experiments.diff;

import org.sudu.experiments.Window;
import org.sudu.experiments.editor.*;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffRange;
import org.sudu.experiments.editor.worker.diff.FileDiffModel;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.common.Pair;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.ui.window.ViewArray;
import org.sudu.experiments.ui.window.WindowManager;

abstract class AbstractFileDiffRootView extends ViewArray
    implements FileDiffModel.ViewToModel
{
  final EditorUi ui;
  public final boolean isCodeReview;

  FileDiffModel fileDiffModel;

  private Runnable onRefresh, onDiffModelSet, onDocumentSizeChange;

  AbstractFileDiffRootView(WindowManager wm, boolean isCodeReview) {
    ui = new EditorUi(wm);
    this.isCodeReview = isCodeReview;
  }

  abstract EditorComponent editor();

  abstract EditorComponent editorForSide(boolean left);

  abstract void attachEditors(FileDiffModel model);

  protected void configureEditor(EditorComponent ed, boolean disableParser) {
    ed.setFullFileLexedListener(this::fullFileLexedListener);
    ed.setIterativeParseFileListener(this::iterativeParseFileListener);
    ed.setUpdateModelOnDiffListener(this::updateModelOnDiffMadeListener);
    ed.setOnDiffMadeListener(this::onDiffMadeListener);
    ed.highlightResolveError(false);
    ed.setDisableParser(disableParser);
    ed.setGetUndoBuffer(this::getUndoBuffer);
  }

  public void setModel(FileDiffModel model) {
    FileDiffModel oldModel = fileDiffModel;
    if (oldModel != null) {
      oldModel.removeView(this);
      if (model != null && model != oldModel) {
        model.setModelFlagsBit(oldModel.getModelFlags());
      }
    }

    fileDiffModel = model;
    if (model == null) {
      clearViewDiff();
      return;
    }
    model.addView(this);
    attachEditors(model);
    if (model.diffModel != null) {
      setDiffModel(model.diffModel);
    } else if (model.modelFlagsReady()) {
      sendToDiff(true);
    }
  }

  protected void clearViewDiff() {
    EditorComponent left = editorForSide(true);
    EditorComponent right = editorForSide(false);
    if (left != null) left.setDiffModel(null);
    if (right != null && right != left) right.setDiffModel(null);
  }

  public void setReadonly(boolean leftReadonly, boolean rightReadonly) {
    EditorComponent left = editorForSide(true);
    EditorComponent right = editorForSide(false);
    if (left != null) left.readonly = leftReadonly;
    if (right != null) right.readonly = rightReadonly;
  }

  public void setDisableParser(boolean disableParser) {
    EditorComponent left = editorForSide(true);
    EditorComponent right = editorForSide(false);
    if (left != null) left.setDisableParser(disableParser);
    if (right != null) right.setDisableParser(disableParser);
  }

  public EditorUi.FontApi fontApi() {
    return editor();
  }

  void fullFileLexedListener(EditorComponent editor) {
    if (fileDiffModel != null) fileDiffModel.fullFileLexedListener(isLeftEditor(editor));
  }

  void iterativeParseFileListener(EditorComponent editor, int start, int stop) {
    if (fileDiffModel != null)
      fileDiffModel.iterativeParseFileListener(isLeftEditor(editor), start, stop);
  }

  void updateModelOnDiffMadeListener(EditorComponent editor, Diff diff, boolean isUndo) {
    if (fileDiffModel != null)
      fileDiffModel.updateModelOnDiffMadeListener(isLeftEditor(editor), diff, isUndo);
  }

  void sendToDiff(boolean cmpOnlyLines) {
    if (fileDiffModel != null) fileDiffModel.sendToDiff(cmpOnlyLines);
  }

  UndoBuffer getUndoBuffer() {
    return fileDiffModel != null ? fileDiffModel.undoBuffer : null;
  }

  protected boolean isLeftEditor(EditorComponent editor) {
    return editor != null && editor == editorForSide(true);
  }

  void onDiffMadeListener(EditorComponent editor) {
    onDiffMade(isLeftEditor(editor));
  }

  protected void onDiffMade(boolean left) {}

  public void applyTheme(EditorColorScheme theme) {
    ui.setTheme(theme);
    boolean sizeChanged = false;
    EditorComponent left = editorForSide(true);
    EditorComponent right = editorForSide(false);
    if (left != null) {
      int h = left.lineHeight();
      left.setTheme(theme);
      sizeChanged |= h != left.lineHeight();
    }
    if (right != null && right != left) {
      int h = right.lineHeight();
      right.setTheme(theme);
      sizeChanged |= h != right.lineHeight();
    }
    if (sizeChanged) onDocumentSizeChange();
  }

  @Override
  public void dispose() {
    if (fileDiffModel != null) {
      fileDiffModel.removeView(this);
      fileDiffModel = null;
    }
    clearViewDiff();
    super.dispose();
  }

  public boolean canNavigateUp(EditorComponent focused) {
    if (fileDiffModel == null || focused == null) return false;
    return fileDiffModel.canNavigateUp(focused.caretLine(), isLeftEditor(focused));
  }

  public void navigateUp(EditorComponent focused) {
    if (fileDiffModel == null || focused == null) return;
    var range = fileDiffModel.navigateUp(focused.caretLine(), isLeftEditor(focused));
    if (range != null) setPositionsAtRange(range);
  }

  public boolean canNavigateDown(EditorComponent focused) {
    if (fileDiffModel == null || focused == null) return false;
    return fileDiffModel.canNavigateDown(focused.caretLine(), isLeftEditor(focused));
  }

  public void navigateDown(EditorComponent focused) {
    if (fileDiffModel == null || focused == null) return;
    var range = fileDiffModel.navigateDown(focused.caretLine(), isLeftEditor(focused));
    if (range != null) setPositionsAtRange(range);
  }

  protected void setPositionsAtRange(DiffRange range) {
    EditorComponent left = editorForSide(true);
    EditorComponent right = editorForSide(false);
    if (left != null) {
      left.setPosition(0, range.fromL);
      left.revealLineInCenter(range.fromL);
    }
    if (right != null) {
      right.setPosition(0, range.fromR);
      right.revealLineInCenter(range.fromR);
    }
    window().repaint();
  }

  public void setOnRefresh(Runnable onRefresh) {
    this.onRefresh = onRefresh;
  }

  public void setOnDiffModelSet(Runnable onDiffModelSet) {
    this.onDiffModelSet = onDiffModelSet;
  }

  public void setOnDocumentSizeChange(Runnable onDocumentSizeChange) {
    this.onDocumentSizeChange = onDocumentSizeChange;
  }

  public void refresh() {
    if (onRefresh != null) onRefresh.run();
  }

  public void undoLastDiff(boolean isRedo) {
    if (fileDiffModel != null) fileDiffModel.undoLastDiff(isRedo);
  }

  @Override
  public void restoreSelection(boolean isLeft, V2i pos, Pair<Pos, Pos> selection) {
    var editor = editorForSide(isLeft);
    if (editor == null) return;
    editor.setCaretLinePos(pos.x, pos.y, false);
    if (selection.first == null || selection.second == null) return;
    editor.setSelection(
        selection.second.charPos, selection.second.line,
        selection.first.charPos, selection.first.line
    );
  }

  @Override
  public int[] getSyncPoints(boolean left) {
    return new int[0];
  }

  @Override
  public void updateModelOnDiff(DiffInfo diffModel) {
    EditorComponent left = editorForSide(true);
    EditorComponent right = editorForSide(false);
    if (left != null) {
      left.setDiffModel(diffModel.lineDiffsL);
      left.setDiffRanges(diffModel.ranges, true);
    }
    if (right != null) {
      right.setDiffModel(diffModel.lineDiffsR);
      right.setDiffRanges(diffModel.ranges, false);
    }
    onDocumentSizeChange();
  }

  @Override
  public void setDiffModel(DiffInfo diffModel) {
    updateModelOnDiff(diffModel);
    onDiffModelApplied(diffModel);
    if (onDiffModelSet != null) onDiffModelSet.run();
    window().repaint();
  }

  protected void onDiffModelApplied(DiffInfo diffModel) {}

  public void onDocumentSizeChange() {
    if (onDocumentSizeChange != null) onDocumentSizeChange.run();
  }

  protected Window window() {
    return ui.windowManager.uiContext.window;
  }

  @Override
  public void setPosition(V2i newPos, V2i newSize, float newDpr) {
    float dpr = this.dpr;
    super.setPosition(newPos, newSize, newDpr);
    if (dpr != newDpr) onDocumentSizeChange();
  }
}
