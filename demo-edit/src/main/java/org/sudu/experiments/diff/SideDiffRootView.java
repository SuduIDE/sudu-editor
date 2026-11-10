package org.sudu.experiments.diff;

import org.sudu.experiments.editor.EditorComponent;
import org.sudu.experiments.editor.worker.diff.FileDiffModel;
import org.sudu.experiments.ui.window.WindowManager;

class SideDiffRootView extends AbstractFileDiffRootView {
  final EditorComponent editor;
  public final boolean left;

  SideDiffRootView(
      WindowManager wm,
      boolean disableParser,
      boolean isCodeReview,
      boolean left
  ) {
    super(wm, isCodeReview);
    this.left = left;
    editor = new EditorComponent(ui);
    configureEditor(editor, disableParser);
    setViews(editor);
  }

  @Override
  EditorComponent editor() {
    return editor;
  }

  @Override
  EditorComponent editor(boolean isLeft) {
    return isLeft == left ? editor : null;
  }

  @Override
  void attachEditors(FileDiffModel model) {
    editor.setModel(left ? model.leftModel : model.rightModel);
  }

  @Override
  protected void layoutViews() {
    views[0].setPosition(pos, size, dpr);
  }
}
