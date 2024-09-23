package org.sudu.experiments.diff;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.ThemeControl;
import org.sudu.experiments.editor.WindowScene;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.math.V2i;

public class FolderDiffScene extends WindowScene implements ThemeControl {

  FolderDiffWindow w;

  public FolderDiffScene(SceneApi api) {
    super(api);
    var theme = EditorColorScheme.darkIdeaColorScheme();
    w = new FolderDiffWindow(theme, windowManager, this::menuFonts);
  }

  public String[] menuFonts() {
    return Fonts.editorFonts(false);
  }

  @Override
  public void onResize(V2i newSize, float newDpr) {
    boolean init = windowManager.uiContext.dpr == 0;
    super.onResize(newSize, newDpr);
    if (init) {
      w.window.fullscreen();
    }
  }

  public void setReadonly(boolean leftReadonly, boolean rightReadonly) {}

  @Override
  public void applyTheme(EditorColorScheme theme) {
    w.applyTheme(theme);
  }

  public EditorColorScheme getTheme() {
    return w.getTheme();
  }
}
