package org.sudu.experiments.diff;

import org.sudu.experiments.Channel;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.math.V2i;

public class RemoteFolderDiffScene extends FolderDiffScene {

  protected Channel channel;

  public RemoteFolderDiffScene(SceneApi api, Channel channel) {
    super(api);
    this.channel = channel;
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
}
