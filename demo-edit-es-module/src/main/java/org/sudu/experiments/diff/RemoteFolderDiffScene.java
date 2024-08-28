package org.sudu.experiments.diff;

import org.sudu.experiments.Channel;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.ThemeControl;
import org.sudu.experiments.editor.WindowScene;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.math.V2i;

public class RemoteFolderDiffScene extends WindowScene implements ThemeControl {

  RemoteFolderDiffWindow w;

  public RemoteFolderDiffScene(SceneApi api, Channel channel) {
    super(api, false);
    w = new RemoteFolderDiffWindow(
        EditorColorScheme.darkIdeaColorScheme(),
        windowManager, this::menuFonts, channel);
  }

  public String[] menuFonts() {
    return Fonts.editorFonts(false);
  }

  @Override
  public void applyTheme(EditorColorScheme t) {
    w.applyTheme(t);
  }

  @Override
  public void onResize(V2i newSize, float newDpr) {
    boolean init = windowManager.uiContext.dpr == 0;
    super.onResize(newSize, newDpr);
    if (init) {
      w.window.fullscreen();
    }
  }

  public void setReadonly(boolean flag) {}
}
