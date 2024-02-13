package org.sudu.experiments.editor;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.fonts.Fonts;

public class Editor1 extends Editor0 {

  public Editor1(SceneApi api) {
    super(api);
    StartFile.apply(editor);
  }

  @Override
  protected String[] menuFonts() {
    return Fonts.editorFonts(true);
  }
}
