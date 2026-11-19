package org.sudu.experiments.diff;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.EditorConst;
import org.sudu.experiments.editor.WindowScene;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.math.V2i;

public class SideDiff extends WindowScene {

  protected SideDiffWindow w;

  public SideDiff(SceneApi api, boolean left) {
    this(api, EditorConst.DEFAULT_DISABLE_PARSER, left);
  }

  public SideDiff(SceneApi api, boolean disableParser, boolean left) {
    super(api);
    var theme = EditorColorScheme.darkIdeaColorScheme();
    w = new SideDiffWindow(windowManager, theme, this::menuFonts, disableParser, true, left);
    w.processEsc = false;
  }

  public String[] menuFonts() {
    return Fonts.editorFonts(true);
  }

  @Override
  public void onResize(V2i newSize, float newDpr) {
    boolean init = windowManager.uiContext.dpr == 0;
    super.onResize(newSize, newDpr);
    if (init) {
      w.window.fullscreen();
    }
  }
}
