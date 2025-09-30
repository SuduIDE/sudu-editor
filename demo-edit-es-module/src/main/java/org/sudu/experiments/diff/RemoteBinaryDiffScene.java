package org.sudu.experiments.diff;

import org.sudu.experiments.Channel;
import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.WindowScene;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.protocol.JsCast;
import org.sudu.experiments.protocol.RemoteDataSource;
import org.sudu.experiments.update.FileDiffChannelUpdater;
import org.teavm.jso.JSObject;

public class RemoteBinaryDiffScene extends WindowScene {

  protected Channel channel;
  protected BinaryDiffWindow w;
  protected RemoteDataSource leftData, rightData;

  public RemoteBinaryDiffScene(SceneApi api, Channel channel) {
    super(api);
    var theme = EditorColorScheme.darkIdeaColorScheme();
    this.channel = channel;
    w = new BinaryDiffWindow(windowManager, theme, this::menuFonts);
    leftData = new RemoteDataSource(channel, true);
    rightData = new RemoteDataSource(channel, false);
    w.rootView.setData(leftData, true);
    w.rootView.setData(rightData, false);
    w.setOnRefresh(this::onRefresh);
    channel.setOnMessage(this::onMessage);
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

  private void onMessage(JsArray<JSObject> m) {
    int type = JsCast.ints(m.pop())[0];
    LoggingJs.info("RemoteDataSource.onMessage got " + type);
    switch (type) {
      case FileDiffChannelUpdater.FETCH -> onFetched(m);
      case FileDiffChannelUpdater.FETCH_SIZE -> onSizeFetched(m);
    }
  }

  private void onFetched(JsArray<JSObject> jsArray) {
    boolean left = JsCast.ints(jsArray, 0)[0] == 1;
    double address = JsCast.doubles(jsArray, 1)[0];
    byte[] bytes = JsCast.bytes(jsArray, 2);
    if (left) leftData.fetchHandler.onData(address, bytes);
    else rightData.fetchHandler.onData(address, bytes);
  }

  private void onSizeFetched(JsArray<JSObject> jsArray) {
    boolean left = JsCast.ints(jsArray, 0)[0] == 1;
    String filename = JsCast.string(jsArray, 1);
    double size = JsCast.doubles(jsArray, 2)[0];
    if (left) leftData.sizeHandler.accept(size);
    else rightData.sizeHandler.accept(size);
  }

  private void onRefresh() {
    leftData = new RemoteDataSource(channel, true);
    rightData = new RemoteDataSource(channel, false);
    w.rootView.setData(leftData, true);
    w.rootView.setData(rightData, false);
  }
}
