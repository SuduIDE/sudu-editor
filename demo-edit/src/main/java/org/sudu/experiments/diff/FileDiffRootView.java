package org.sudu.experiments.diff;

import org.sudu.experiments.editor.*;
import org.sudu.experiments.editor.Diff;
import org.sudu.experiments.editor.test.MergeButtonsTestModel;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.editor.worker.diff.DiffRange;
import org.sudu.experiments.parser.common.TriConsumer;
import org.sudu.experiments.ui.window.WindowManager;

import java.util.function.Consumer;

class FileDiffRootView extends DiffRootView implements ThemeControl {
  final EditorUi ui;
  final EditorComponent editor1;
  final EditorComponent editor2;

  final DiffSync diffSync;

  DiffInfo diffModel;
  private int modelFlags;

  FileDiffRootView(WindowManager wm) {
    super(wm.uiContext);
    ui = new EditorUi(wm);
    editor1 = new EditorComponent(ui);
    editor2 = new EditorComponent(ui);
    middleLine.setLeftRight(editor1, editor2);
    Consumer<EditorComponent> parseListener = this::fullFileParseListener;
    TriConsumer<EditorComponent, Integer, Integer> iterativeParseListener = this::iterativeParseFileListener;

    editor1.setFullFileParseListener(parseListener);
    editor1.setIterativeParseFileListener(iterativeParseListener);
    editor1.setOnDiffMadeListener(this::onDiffMadeListener);
    editor1.highlightResolveError(false);
    editor1.setMirrored(true);

    editor2.setFullFileParseListener(parseListener);
    editor2.setIterativeParseFileListener(iterativeParseListener);
    editor2.setOnDiffMadeListener(this::onDiffMadeListener);
    editor2.highlightResolveError(false);

    diffSync = new DiffSync(editor1, editor2);
    setViews(editor1, editor2, middleLine);
    setEmptyDiffModel();
  }

  public EditorUi.FontApi fontApi() {
    return new FontApi2(editor1, editor2, ui.windowManager.uiContext);
  }

  private void fullFileParseListener(EditorComponent editor) {
    if (editor1 == editor) modelFlags |= 1;
    if (editor2 == editor) modelFlags |= 2;
    if ((modelFlags & 3) == 3) {
      sendToDiff();
    }
  }

  private void iterativeParseFileListener(EditorComponent editor, int start, int stop) {
    if (diffModel == null) return;
    boolean isL = editor == editor1;
    int startLine = editor.model().document.getLine(start).x;
    int stopLine = editor.model().document.getLine(stop).x;
    var fromRangeInd = diffModel.leftBS(startLine, isL);
    var toRangeInd = diffModel.rightBS(stopLine, isL);

    if (fromRangeInd != 0 && diffModel.ranges[fromRangeInd].type != DiffTypes.DEFAULT) fromRangeInd--;
    if (toRangeInd != diffModel.rangeCount() - 1 && diffModel.ranges[toRangeInd].type != DiffTypes.DEFAULT) toRangeInd++;

    var fromRange = diffModel.ranges[fromRangeInd];
    var toRange = diffModel.ranges[toRangeInd];

    sendIntervalToDiff(fromRange.fromL, toRange.toL(), fromRange.fromR, toRange.toR());
  }

  private void applyDiff(DiffRange range, boolean isL) {
    if (range.type == DiffTypes.DEFAULT) return;
    var fromModel = isL ? editor1.model() : editor2.model();
    int fromStartLine = isL ? range.fromL : range.fromR;
    int fromEndLine = isL ? range.toL() : range.toR();
    var lines = fromModel.document.getLines(fromStartLine, fromEndLine);

    int toStartLine = !isL ? range.fromL : range.fromR;
    int toEndLine = !isL ? range.toL() : range.toR();

    var toModel = !isL ? editor1.model() : editor2.model();
    toModel.document.applyChange(toStartLine, toEndLine, lines);
    sendToDiff();
  }

  private void onDiffMadeListener(EditorComponent editor, Diff diff, boolean isUndo) {
    boolean isDelete = diff.isDelete ^ isUndo;
    boolean isL = editor == editor1;
    if (isDelete) onDeleteDiffMadeListener(diff, isL);
    else onInsertDiffMadeListener(diff, isL);
  }

  private void onInsertDiffMadeListener(Diff diff, boolean isL) {
    if (diffModel != null) diffModel.insertAt(diff.line, diff.lineCount(), isL);
  }

  private void onDeleteDiffMadeListener(Diff diff, boolean isL) {
    if (diffModel != null) diffModel.deleteAt(diff.line, diff.lineCount(), isL);
  }

  @Override
  public void applyTheme(EditorColorScheme theme) {
    ui.setTheme(theme);
    middleLine.setTheme(theme);
    editor1.setTheme(theme);
    editor2.setTheme(theme);
  }

  public void setEmptyDiffModel() {
    var leftLine = new LineDiff(DiffTypes.DEFAULT);
    var rightLine = new LineDiff(DiffTypes.DEFAULT);
    var range = new DiffRange(0, 1, 0, 1, DiffTypes.DEFAULT);
    var diffInfo = new DiffInfo(
        new LineDiff[]{leftLine},
        new LineDiff[]{rightLine},
        new DiffRange[]{range}
    );
    setDiffModel(diffInfo);
  }

  public void setDiffModel(DiffInfo diffInfo) {
    diffModel = diffInfo;
    editor1.setDiffModel(diffModel.lineDiffsL);
    editor2.setDiffModel(diffModel.lineDiffsR);
    diffSync.setModel(diffModel);
    middleLine.setModel(diffModel);

    var pair = MergeButtonsTestModel.getModels(diffInfo, this::applyDiff);
    MergeButtonsTestModel m1 = pair[0], m2 = pair[1];
    editor1.setMergeButtons(m1.actions, m1.lines);
    editor2.setMergeButtons(m2.actions, m2.lines);
  }

  public void updateDiffModel(
      int fromL, int toL,
      int fromR, int toR,
      DiffInfo updateInfo
  ) {
    diffModel.updateDiffInfo(fromL, toL, fromR, toR, updateInfo);
    setDiffModel(diffModel);
  }

  private void sendToDiff() {
    DiffUtils.findDiffs(
        editor1.model().document,
        editor2.model().document,
        this::setDiffModel,
        ui.windowManager.uiContext.window.worker());
  }

  private void sendIntervalToDiff(
      int fromL, int toL,
      int fromR, int toR
  ) {
    DiffUtils.findIntervalDiffs(
        editor1.model().document,
        editor2.model().document,
        (upd) -> updateDiffModel(fromL, toL, fromR, toR, upd),
        ui.windowManager.uiContext.window.worker(),
        fromL, toL, fromR, toR
    );
  }
}
