package org.sudu.experiments.diff;

import org.sudu.experiments.Channel;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.WindowScene;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.protocol.RemoteDataSource;

public class RemoteBinaryDiffScene extends WindowScene {

  private final Channel channel;
  protected BinaryDiffWindow w;

  public RemoteBinaryDiffScene(SceneApi api, Channel channel) {
    super(api);
    this.channel = channel;
    var theme = EditorColorScheme.darkIdeaColorScheme();
    w = new BinaryDiffWindow(windowManager, theme, this::menuFonts);
    w.rootView.setData(new RemoteDataSource(channel, true, d -> {}, new RemoteResult()), () -> {}, true);
    w.rootView.setData(new RemoteDataSource(channel, false, d -> {}, new RemoteResult()), () -> {}, false);
  }

  public String[] menuFonts() {
    return Fonts.editorFonts(true);
  }

  static class RemoteResult implements BinDataCache.DataSource.Result {

    @Override
    public void onData(double address, byte[] data) {

    }

    @Override
    public void onError(double address, String e) {

    }
  }
}
