package org.sudu.experiments.diff;

import org.sudu.experiments.Channel;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.ThemeControl;
import org.sudu.experiments.editor.WindowScene;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.math.V2i;

public class RemoteFolderDiffScene extends WindowScene implements ThemeControl {

  EditorColorScheme theme = EditorColorScheme.darkIdeaColorScheme();
  RemoteFolderDiffWindow w;
  protected Channel channel;

  public RemoteFolderDiffScene(SceneApi api, Channel channel) {
    super(api, false);
    this.channel = channel;
  }

  public String[] menuFonts() {
    return Fonts.editorFonts(false);
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
      w = new RemoteFolderDiffWindow(theme, windowManager, this::menuFonts, channel);
      w.window.fullscreen();
    }
  }

  public void setReadonly(boolean flag) {}
}
