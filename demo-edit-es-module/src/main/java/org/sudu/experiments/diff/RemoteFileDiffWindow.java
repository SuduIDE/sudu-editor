package org.sudu.experiments.diff;

import org.sudu.experiments.Channel;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.protocol.JsCast;
import org.sudu.experiments.ui.window.WindowManager;
import org.sudu.experiments.update.FileDiffChannelUpdater;
import org.teavm.jso.JSObject;

import java.util.function.Supplier;

public class RemoteFileDiffWindow extends FileDiffWindow {

  private final Channel channel;

  public RemoteFileDiffWindow(
      WindowManager wm,
      EditorColorScheme theme,
      Supplier<String[]> fonts,
      Channel channel
  ) {
    super(wm, theme, fonts, (_wm) -> new RemoteFileDiffRootView(_wm, channel));
    this.channel = channel;
    this.channel.setOnMessage(this::onMessage);
  }

  private void onMessage(JsArray<JSObject> jsArray) {
    int type = JsCast.ints(jsArray.pop())[0];
    switch (type) {
      case FileDiffChannelUpdater.FILE_READ -> onFileOpen(jsArray);
    }
  }

  private void onFileOpen(JsArray<JSObject> jsArray) {
    String source = JsCast.string(jsArray, 0);
    String name = JsCast.string(jsArray, 1);
    boolean left = JsCast.ints(jsArray, 2)[0] == 1;
    open(source, name, left);
  }
}
