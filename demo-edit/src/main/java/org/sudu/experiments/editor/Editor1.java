package org.sudu.experiments.editor;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;

public class Editor1 extends Editor0 {

  public Editor1(SceneApi api) {
    super(api);
    StartFile.apply(editor);

    api.input.onKeyPress.add(this::onKeyPress);

    editor.setUpdateModelOnDiffListener(this::onDiffMade);
  }

  private void onDiffMade(EditorComponent editorComponent, Diff diff, Boolean isUndo) {
    System.out.println("onDiffMade: line=" + diff.line + ", lineCount = " + diff.lineCount());
  }

  boolean onKeyPress(KeyEvent event) {
    if (event.keyCode == KeyCode.F10) {
      api.window.addChild("child", Editor1::new);
      return true;
    }
    if (event.controlOnly() && event.keyCode == KeyCode.P) {
      System.out.println("Ctrl P -> parseFullFile");
      editor.model.parseFullFile();
      return true;
    }

    return false;
  }

  @Override
  protected String[] menuFonts() {
    return Fonts.editorFonts(true);
  }
}
