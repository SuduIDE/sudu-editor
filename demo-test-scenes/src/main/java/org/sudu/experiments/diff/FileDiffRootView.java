package org.sudu.experiments.diff;

import org.sudu.experiments.editor.EditorComponent;
import org.sudu.experiments.editor.EditorUi;
import org.sudu.experiments.editor.ThemeControl;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffUtils;
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
    editor1.setFullFileParseListener(parseListener);
    editor2.setFullFileParseListener(parseListener);
    editor1.highlightResolveError(false);
    editor2.highlightResolveError(false);
    editor1.setMirrored(true);
    diffSync = new DiffSync(editor1, editor2);
    setViews(editor1, middleLine, editor2);
  }

  private void fullFileParseListener(EditorComponent editor) {
    if (editor1 == editor) modelFlags |= 1;
    if (editor2 == editor) modelFlags |= 2;
    if ((modelFlags & 3) == 3) {
      sendToDiff();
    }
  }

  @Override
  public void applyTheme(EditorColorScheme theme) {
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

  private void sendToDiff() {
    DiffUtils.findDiffs(
        editor1.model().document,
        editor2.model().document,
        this::setDiffModel,
        ui.windowManager.uiContext.window);
  }
}
