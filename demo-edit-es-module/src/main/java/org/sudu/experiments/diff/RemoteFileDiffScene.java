package org.sudu.experiments.diff;

import org.sudu.experiments.Channel;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.EditorComponent;
import org.sudu.experiments.editor.WindowScene;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.math.V2i;

public class RemoteFileDiffScene extends WindowScene {
  final Channel channel;

  protected RemoteFileDiffWindow w;

  public RemoteFileDiffScene(SceneApi api, boolean disableParser, Channel channel) {
    super(api);
    this.channel = channel;
    var theme = EditorColorScheme.darkIdeaColorScheme();
    w = new RemoteFileDiffWindow(windowManager, theme, this::menuFonts, disableParser, channel);
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
