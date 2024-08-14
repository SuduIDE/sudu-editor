package org.sudu.experiments.diff;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.EditorComponent;
import org.sudu.experiments.editor.ThemeControl;
import org.sudu.experiments.editor.WindowScene;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.math.V2i;

public class FileDiffScene extends WindowScene implements ThemeControl {
   EditorColorScheme theme = EditorColorScheme.darkIdeaColorScheme();

   FileDiffWindow w;

  public FileDiffScene(SceneApi api) {
    super(api, false);
  }

  public String[] menuFonts() {
    return Fonts.editorFonts(true);
  }

  protected EditorComponent left() {
    return w.rootView.editor1;
  }

  protected EditorComponent right() {
    return w.rootView.editor2;
  }

  @Override
  public void applyTheme(EditorColorScheme t) {
      theme = t;
      if (w != null) w.applyTheme(t);
  }

  @Override
  public void onResize(V2i newSize, float newDpr) {
    boolean init = windowManager.uiContext.dpr == 0;
    super.onResize(newSize, newDpr);
    if (init) {
      w = new FileDiffWindow(windowManager, theme, this::menuFonts);
      w.window.fullscreen();
    }
  }

  public void setReadonly(boolean flag) {}
}
