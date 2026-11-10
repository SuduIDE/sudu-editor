package org.sudu.experiments.diff;

import org.sudu.experiments.BooleanConsumer;
import org.sudu.experiments.editor.*;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.*;
import org.sudu.experiments.ui.window.WindowManager;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

class FileDiffRootView extends AbstractFileDiffRootView {
  static final boolean showNavigateLog = false;

  final EditorComponent editor1;
  final EditorComponent editor2;
  final MiddleLine middleLine;
  final DiffSync diffSync;

  boolean compactViewRequest;

  Consumer<Model> onLeftDiffMade, onRightDiffMade;

  boolean firstDiffRevealed = false, needScrollSync = false;
  private boolean mergeLeftToRight = true, mergeRightToLeft = true;

  FileDiffRootView(WindowManager wm, boolean disableParser, boolean isCodeReview) {
    super(wm, isCodeReview);
    editor1 = new EditorComponent(ui);
    editor2 = new EditorComponent(ui);
    middleLine = new MiddleLine(wm.uiContext);
    middleLine.setLeftRight(editor1, editor2);
    SyncPoints syncPoints = new SyncPoints(() -> sendToDiff(false));

    configureEditor(editor1, disableParser);
    editor1.setMirrored(true);
    editor1.setSyncPoints(syncPoints, true);

    configureEditor(editor2, disableParser);
    editor2.setSyncPoints(syncPoints, false);

    diffSync = new DiffSync(editor1, editor2, true);
    middleLine.setOnMidSyncPointHover(i -> onMidSyncLineHover(syncPoints, i));
    middleLine.setOnMidSyncPointClick(i -> onMidSyncLineClick(syncPoints, i));

    middleLine.setSyncPoints(syncPoints);
    setViews(editor1, editor2, middleLine);
    setEmptyDiffModel();
  }

  @Override
  EditorComponent editor() {
    return editor1;
  }

  @Override
  EditorComponent editor(boolean left) {
    return left ? editor1 : editor2;
  }

  @Override
  void attachEditors(FileDiffModel model) {
    editor1.setModel(model.leftModel);
    editor2.setModel(model.rightModel);
  }

  public void setModel(Model m1, Model m2) {
    setModel(new FileDiffModel(window().worker(), m1, m2));
  }

  public void setDefaultModel() {
    setModel(editor1.model(), editor2.model());
  }

  public void setEmptyDiffModel() {
    clearViewDiff();
    diffSync.setModel(null);
    middleLine.setModel(null);
    firstDiffRevealed = false;
  }

  public void onFileOpened(boolean left) {
    fileDiffModel.setModelFlagsBit(left);
    setDefaultModel();
  }

  public Model getLeftModel() {
    return editor1.model();
  }

  public Model getRightModel() {
    return editor2.model();
  }

  public void enableMergeButtons(boolean leftToRight, boolean rightToLeft) {
    this.mergeLeftToRight = leftToRight;
    this.mergeRightToLeft = rightToLeft;
  }

  @Override
  public EditorUi.FontApi fontApi() {
    return new FontApi2(editor1, editor2, ui.windowManager.uiContext);
  }

  private void applyDiff(DiffRange range, boolean left) {
    if (fileDiffModel == null) return;
    fileDiffModel.applyDiff(range.copy(), left);
  }

  @Override
  protected void onDiffMade(boolean left) {
    if (left) onLeftDiffMade();
    else onRightDiffMade();
  }

  @Override
  public void applyTheme(EditorColorScheme theme) {
    middleLine.setTheme(
        theme.codeDiffBg,
        theme.editor.bg,
        theme.lineNumber.syncPoint,
        theme.lineNumber.midLineHoverSyncPoint,
        theme.lineNumber.currentSyncPoint,
        theme.lineNumber.hoverSyncPoint
    );
    super.applyTheme(theme);
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

  @Override
  protected void setPositionsAtRange(DiffRange range) {
    super.setPositionsAtRange(range);
    if (showNavigateLog) {
      System.out.println("Navigated on lines " + (range.fromL + 1) + " and " + (range.fromR + 1));
    }
  }

  @Override
  public void refresh() {
    System.out.println("FileDiffRootView.refresh");
    needScrollSync = true;
    super.refresh();
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
      onDocumentSizeChange();
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
    super.updateModelOnDiff(diffModel);
    diffSync.setModel(diffModel);
    middleLine.setModel(diffModel);
  }

  @Override
  protected void onDiffModelApplied(DiffInfo diffModel) {
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
  protected void layoutViews() {
    DiffRootView.layoutLeftMiddleRight(views, pos, size, dpr);
  }
}
