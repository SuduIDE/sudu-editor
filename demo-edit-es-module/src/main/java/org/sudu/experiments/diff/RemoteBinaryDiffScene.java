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
import org.teavm.jso.core.JSString;

public class RemoteBinaryDiffScene extends WindowScene {

  protected Channel channel;
  protected RemoteBinaryDiffWindow w;
  protected RemoteDataSource leftData, rightData;

  public RemoteBinaryDiffScene(SceneApi api, Channel channel) {
    super(api);
    var theme = EditorColorScheme.darkIdeaColorScheme();
    this.channel = channel;
    w = new RemoteBinaryDiffWindow(windowManager, theme, this::menuFonts, channel);
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
      case FileDiffChannelUpdater.BIN_FETCH -> onFetched(m);
      case FileDiffChannelUpdater.BIN_FETCH_SIZE -> onSizeFetched(m);
      case FileDiffChannelUpdater.BIN_NAVIGATE -> onNavigate(m);
      case FileDiffChannelUpdater.BIN_CAN_NAVIGATE -> onCanNavigate(m);
      case FileDiffChannelUpdater.NOTIFY -> onNotification(m);
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

  private void onNavigate(JsArray<JSObject> m) {
    double line = JsCast.doubles(m, 0)[0];
    if (line >= 0) w.rootView.navigateToLine((int) (line / w.rootView.bytesPerLine));
  }

  private void onCanNavigate(JsArray<JSObject> m) {

  }

  private void onNotification(JsArray<JSObject> m) {
    JSString notification = JsCast.jsString(m, 0);
    w.onNotification(notification);
  }

  private void onRefresh() {
    leftData = new RemoteDataSource(channel, true);
    rightData = new RemoteDataSource(channel, false);
    w.rootView.setData(leftData, true);
    w.rootView.setData(rightData, false);
  }
}
