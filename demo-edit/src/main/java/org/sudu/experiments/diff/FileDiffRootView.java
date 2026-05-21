package org.sudu.experiments.diff;

import org.sudu.experiments.BooleanConsumer;
import org.sudu.experiments.Window;
import org.sudu.experiments.editor.*;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.*;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.common.Pair;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.common.TriConsumer;
import org.sudu.experiments.ui.window.WindowManager;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

class FileDiffRootView extends DiffRootView implements FileDiffModel.ViewToModel {
  static final boolean showNavigateLog = false;

  final EditorUi ui;
  final EditorComponent editor1;
  final EditorComponent editor2;

  final DiffSync diffSync;

  boolean compactViewRequest;

  Consumer<Model> onLeftDiffMade, onRightDiffMade;

  boolean firstDiffRevealed = false, needScrollSync = false;
  private Runnable onRefresh, onDiffModelSet, onDocumentSizeChange;
  public final boolean isCodeReview;
  public final boolean enableSyncEditing;
  private boolean mergeLeftToRight = true, mergeRightToLeft = true;

  FileDiffModel fileDiffModel;

  FileDiffRootView(WindowManager wm, boolean disableParser, boolean isCodeReview) {
    super(wm.uiContext);
    ui = new EditorUi(wm);
    this.isCodeReview = isCodeReview;
//    this.enableSyncEditing = isCodeReview;
    this.enableSyncEditing = EditorConst.DEFAULT_ENABLE_SYNC_EDIT;
    editor1 = new EditorComponent(ui);
    editor2 = new EditorComponent(ui);
    middleLine.setLeftRight(editor1, editor2);
    Consumer<EditorComponent> lexerListener = this::fullFileLexedListener;
    TriConsumer<EditorComponent, Integer, Integer> iterativeParseListener = this::iterativeParseFileListener;
    SyncPoints syncPoints = new SyncPoints(() -> sendToDiff(false));

    editor1.setFullFileLexedListener(lexerListener);
    editor1.setIterativeParseFileListener(iterativeParseListener);
    editor1.setUpdateModelOnDiffListener(this::updateModelOnDiffMadeListener);
    editor1.setOnDiffMadeListener(this::onDiffMadeListener);
    editor1.highlightResolveError(false);
    editor1.setMirrored(true);
    editor1.setSyncPoints(syncPoints, true);
    editor1.setDisableParser(disableParser);
    editor1.setSyncEditing((diff, isUndo) -> this.syncEditing(true, diff, isUndo));
    editor1.setGetUndoBuffer(this::getUndoBuffer);

    editor2.setFullFileLexedListener(lexerListener);
    editor2.setIterativeParseFileListener(iterativeParseListener);
    editor2.setUpdateModelOnDiffListener(this::updateModelOnDiffMadeListener);
    editor2.setOnDiffMadeListener(this::onDiffMadeListener);
    editor2.highlightResolveError(false);
    editor2.setSyncPoints(syncPoints, false);
    editor2.setDisableParser(disableParser);
    editor2.setSyncEditing((diff, isUndo) -> this.syncEditing(false, diff, isUndo));
    editor2.setGetUndoBuffer(this::getUndoBuffer);

    diffSync = new DiffSync(editor1, editor2, true);
    middleLine.setOnMidSyncPointHover(i -> onMidSyncLineHover(syncPoints, i));
    middleLine.setOnMidSyncPointClick(i -> onMidSyncLineClick(syncPoints, i));

    middleLine.setSyncPoints(syncPoints);
    setViews(editor1, editor2, middleLine);
    setEmptyDiffModel();
  }

  public void setModel(FileDiffModel model) {
    FileDiffModel oldModel = fileDiffModel;
    if (oldModel != null) {
      fileDiffModel.viewToModel = null;
      model.setModelFlagsBit(oldModel.getModelFlags());
    }

    fileDiffModel = model;
    fileDiffModel.viewToModel = this;
    editor1.setModel(model.leftModel);
    editor2.setModel(model.rightModel);
    if (model.modelFlagsReady()) sendToDiff(true);
  }

  public void setModel(Model m1, Model m2) {
    setModel(new FileDiffModel(window().worker(), m1, m2));
  }

  public void setDefaultModel() {
    setModel(editor1.model(), editor2.model());
  }

  public void setEmptyDiffModel() {
    if (fileDiffModel != null) fileDiffModel.diffModel = null;
    editor1.setDiffModel(null);
    editor2.setDiffModel(null);
    diffSync.setModel(null);
    middleLine.setModel(null);
    firstDiffRevealed = false;
  }

  public void onFileOpened(boolean left) {
    var model = new FileDiffModel(window().worker(), editor1.model(), editor2.model());
    model.setModelFlagsBit(left ? 0b01 : 0b10);
    setDefaultModel();
  }

  public Model getLeftModel() {
    return editor1.model();
  }

  public Model getRightModel() {
    return editor2.model();
  }

  public void setReadonly(boolean leftReadonly, boolean rightReadonly) {
    editor1.readonly = leftReadonly;
    editor2.readonly = rightReadonly;
  }

  public void enableMergeButtons(boolean leftToRight, boolean rightToLeft) {
    this.mergeLeftToRight = leftToRight;
    this.mergeRightToLeft = rightToLeft;
  }

  public void setDisableParser(boolean disableParser) {
    editor1.setDisableParser(disableParser);
    editor2.setDisableParser(disableParser);
  }

  public EditorUi.FontApi fontApi() {
    return new FontApi2(editor1, editor2, ui.windowManager.uiContext);
  }

  void fullFileLexedListener(EditorComponent editor) {
    if (fileDiffModel != null) fileDiffModel.fullFileLexedListener(editor == editor1);
  }

  void iterativeParseFileListener(EditorComponent editor, int start, int stop) {
    if (fileDiffModel != null) fileDiffModel.iterativeParseFileListener(editor == editor1, start, stop);
  }

  void updateModelOnDiffMadeListener(EditorComponent editor, Diff diff, boolean isUndo) {
    if (fileDiffModel != null) fileDiffModel.updateModelOnDiffMadeListener(editor == editor1, diff, isUndo);
  }

  void sendToDiff(boolean cmpOnlyLines) {
    if (fileDiffModel != null) fileDiffModel.sendToDiff(cmpOnlyLines);
  }

  UndoBuffer getUndoBuffer() {
    return fileDiffModel.undoBuffer;
  }

  private void applyDiff(DiffRange range, boolean isL) {
    if (range.type == DiffTypes.DEFAULT) return;
    var fromModel = isL ? editor1.model() : editor2.model();
    int fromStartLine = range.from(isL);
    int fromEndLine = isL ? range.toL() : range.toR();
    var lines = fromModel.document.getLines(fromStartLine, fromEndLine);

    int toStartLine = range.from(!isL);
    int toEndLine = range.to(!isL);

    var toModel = !isL ? editor1.model() : editor2.model();
    toModel.document.applyChange(toStartLine, toEndLine, lines);
  }

  private void syncEditing(boolean left, CpxDiff cpxDiff, boolean isUndo) {
    if (!enableSyncEditing || fileDiffModel == null) return;
    fileDiffModel.syncEditing(left, cpxDiff, isUndo);
  }

  private void onDiffMadeListener(EditorComponent editor) {
    boolean left = editor == editor1;
    if (left) onLeftDiffMade();
    else onRightDiffMade();
  }

  public void applyTheme(EditorColorScheme theme) {
    ui.setTheme(theme);
    middleLine.setTheme(
        theme.codeDiffBg,
        theme.editor.bg,
        theme.lineNumber.syncPoint,
        theme.lineNumber.midLineHoverSyncPoint,
        theme.lineNumber.currentSyncPoint,
        theme.lineNumber.hoverSyncPoint
    );
    int lineHeight1 = editor1.lineHeight();
    int lineHeight2 = editor2.lineHeight();
    editor1.setTheme(theme);
    editor2.setTheme(theme);
    if (lineHeight1 != editor1.lineHeight() || lineHeight2 != editor2.lineHeight()) {
      onDocumentSizeChange();
    }
  }

  @Override
  public void dispose() {
    setEmptyDiffModel();
    super.dispose();
  }

  public void setOnDiffMade(
      Consumer<Model> onLeftDiffMade,
      Consumer<Model> onRightDiffMade
  ) {
    this.onLeftDiffMade = onLeftDiffMade;
    this.onRightDiffMade = onRightDiffMade;
  }

  private void onLeftDiffMade() {
    if (onLeftDiffMade != null)
      onLeftDiffMade.accept(editor1.model());
  }

  private void onRightDiffMade() {
    if (onRightDiffMade != null)
      onRightDiffMade.accept(editor2.model());
  }

  private void revealFirstDiff() {
    if (fileDiffModel == null) return;
    var range = fileDiffModel.firstRange(editor1.caretLine(), editor2.caretLine());
    if (range == null) return;
    setPositionsAtRange(range);
    firstDiffRevealed = true;
  }

  public boolean canNavigateUp(EditorComponent focused) {
    if (fileDiffModel == null) return false;
    return fileDiffModel.canNavigateUp(focused.caretLine(), focused == editor1);
  }

  public void navigateUp(EditorComponent focused) {
    if (fileDiffModel == null) return;
    var range = fileDiffModel.navigateUp(focused.caretLine(), focused == editor1);
    if (range != null) setPositionsAtRange(range);
  }

  public boolean canNavigateDown(EditorComponent focused) {
    if (fileDiffModel == null) return false;
    return fileDiffModel.canNavigateDown(focused.caretLine(), focused == editor1);
  }

  public void navigateDown(EditorComponent focused) {
    if (fileDiffModel == null) return;
    var range = fileDiffModel.navigateDown(focused.caretLine(), focused == editor1);
    if (range != null) setPositionsAtRange(range);
  }

  private void setPositionsAtRange(DiffRange range) {
    editor1.setPosition(0, range.fromL);
    editor1.revealLineInCenter(range.fromL);
    editor2.setPosition(0, range.fromR);
    editor2.revealLineInCenter(range.fromR);
    window().repaint();
    if (showNavigateLog) {
      System.out.println("Navigated on lines " + (range.fromL + 1) + " and " + (range.fromR + 1));
    }
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
    System.out.println("FileDiffRootView.refresh");
    needScrollSync = true;
    if (onRefresh != null) onRefresh.run();
  }

  public void undoLastDiff(boolean isRedo) {
    fileDiffModel.undoLastDiff(isRedo, this::restore);
  }

  private void restore(boolean isLeft, V2i pos, Pair<Pos, Pos> selection) {
    var editor = isLeft ? editor1 : editor2;
    editor.setCaretLinePos(pos.x, pos.y, false);
    if (selection.first == null || selection.second == null) return;
    editor.setSelection(
        selection.second.charPos, selection.second.line,
        selection.first.charPos, selection.first.line
    );
  }

  public void onMidSyncLineClick(SyncPoints syncPoints, int line) {
    syncPoints.remove(line);
  }

  public void onMidSyncLineHover(SyncPoints syncPoints, int line) {
    syncPoints.midLineHoverSyncPoint = line;
  }

  public void setCompactView(boolean compact) {
    compactViewRequest = compact;
    if (fileDiffModel == null || fileDiffModel.diffModel == null) return;
    if (compact) {
      if (!fileDiffModel.isEmpty()) {
        buildCompactModel();
        onDocumentSizeChange();
      }
    } else {
      fileDiffModel.clearCompactView();
      editor1.clearCompactViewModel();
      editor2.clearCompactViewModel();
      editor1.revealLineInCenter(editor1.caretLine());
      editor2.revealLineInCenter(editor2.caretLine());
      if (onDocumentSizeChange != null)
        onDocumentSizeChange.run();
    }
    window().repaint();
  }

  @Override
  public int[] getSyncPoints(boolean left) {
    return (left ? editor1 : editor2)
        .getSyncPoints()
        .copiedSyncPoints();
  }

  @Override
  public void updateModelOnDiff(DiffInfo diffModel) {
    editor1.setDiffModel(diffModel.lineDiffsL);
    editor2.setDiffModel(diffModel.lineDiffsR);
    diffSync.setModel(diffModel);
    middleLine.setModel(diffModel);
    onDocumentSizeChange();
  }

  @Override
  public void setDiffModel(DiffInfo diffModel) {
    updateModelOnDiff(diffModel);

    boolean compact = compactViewRequest;
    var pair = MergeButtonsModel.getModels(
        diffModel,
        !mergeRightToLeft, !mergeLeftToRight,
        editor1.syncPoints(), editor2.syncPoints(),
        this::applyDiff
    );
    MergeButtonsModel m1 = pair.first[0], m2 = pair.first[1];
    BooleanConsumer[] acceptReject = pair.second;
    editor1.setMergeButtons(m1.actions, null, m1.lines, false);
    editor2.setMergeButtons(m2.actions, null, m2.lines, isCodeReview);

    if (compact && !diffModel.isEmpty()) buildCompactModel();
    if (!firstDiffRevealed) revealFirstDiff();
    if (onDiffModelSet != null) onDiffModelSet.run();
    window().repaint();
  }

  public void onDocumentSizeChange() {
    if (onDocumentSizeChange != null) onDocumentSizeChange.run();
  }

  private Window window() {
    return ui.windowManager.uiContext.window;
  }

  private void buildCompactModel() {
    if (fileDiffModel != null) fileDiffModel.buildCompactView(this::applyCodeMapping);
  }

  private void applyCodeMapping(IntConsumer actions) {
    if (fileDiffModel.diffModel == null) return;
    editor1.setCompactViewModel(fileDiffModel.diffModel.codeMappingL, actions);
    editor2.setCompactViewModel(fileDiffModel.diffModel.codeMappingR, actions);
    onDocumentSizeChange();
  }

  public boolean isCompactedView() {
    return compactViewRequest;
  }

  @Override
  public void setPosition(V2i newPos, V2i newSize, float newDpr) {
    float dpr = this.dpr;
    super.setPosition(newPos, newSize, newDpr);
    if (dpr != newDpr) onDocumentSizeChange();
  }
}
