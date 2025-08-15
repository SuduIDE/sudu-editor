package org.sudu.experiments.diff;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.EditorComponent;
import org.sudu.experiments.editor.EditorConst;
import org.sudu.experiments.editor.WindowScene;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.math.V2i;

public class FileDiff extends WindowScene {

  protected FileDiffWindow w;

  public FileDiff(SceneApi api) {
    this(api, EditorConst.DEFAULT_DISABLE_PARSER);
  }

  public FileDiff(SceneApi api, boolean disableParser) {
    super(api);
    var theme = EditorColorScheme.darkIdeaColorScheme();
    w = new FileDiffWindow(windowManager, theme, this::menuFonts, disableParser);
    w.processEsc = false;
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
  public void onResize(V2i newSize, float newDpr) {
    boolean init = windowManager.uiContext.dpr == 0;
    super.onResize(newSize, newDpr);
    if (init) {
      w.window.fullscreen();
    }
  }

}
