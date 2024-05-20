package org.sudu.experiments.diff;

import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.editor.*;
import org.sudu.experiments.editor.Diff;
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

  public void applyDiff(DiffRange range, boolean isL) {
    if (range.type == DiffTypes.DEFAULT) return;
    var oldModel = isL ? editor1.model() : editor2.model();
    int fromOld = isL ? range.fromL : range.fromR;
    int toOld = isL ? range.toL() : range.toR();

    var newModel = !isL ? editor1.model() : editor2.model();
    int fromNew = !isL ? range.fromL : range.fromR;
    int toNew = !isL ? range.toL() : range.toR();

    var oldLines = oldModel.document.linesToStrings(fromOld, toOld);
    newModel.document.deleteLines(fromNew, toNew + 1);
    newModel.document.insertLines(fromNew, 0, oldLines);
    System.out.println();
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

  public void setDiffModel(DiffInfo diffInfo) {
//    System.out.println("setDiffModel diffInfo = " + diffInfo);
    diffModel = diffInfo;
    editor1.setDiffModel(diffModel.lineDiffsL);
    editor2.setDiffModel(diffModel.lineDiffsR);
    diffSync.setModel(diffModel);
    middleLine.setModel(diffModel);
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
        ui.windowManager.uiContext.window);
  }

  private void sendIntervalToDiff(
      int fromL, int toL,
      int fromR, int toR
  ) {
    DiffUtils.findIntervalDiffs(
        editor1.model().document,
        editor2.model().document,
        (upd) -> updateDiffModel(fromL, toL, fromR, toR, upd),
        ui.windowManager.uiContext.window,
        fromL, toL, fromR, toR
    );
  }
}
