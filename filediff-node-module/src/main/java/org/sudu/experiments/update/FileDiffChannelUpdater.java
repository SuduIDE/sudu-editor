package org.sudu.experiments.update;

import org.sudu.experiments.Channel;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.protocol.JsCast;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public class FileDiffChannelUpdater {

  private final FileHandle leftHandle, rightHandle;
  private final Channel channel;

  public FileDiffChannelUpdater(
      FileHandle leftHandle,
      FileHandle rightHandle,
      Channel channel
  ) {
    this.leftHandle = leftHandle;
    this.rightHandle = rightHandle;
    this.channel = channel;
  }

  public void beginCompare() {
    leftHandle.readAsText((str) -> sendMessage(true, str), this::onError);
    rightHandle.readAsText((str) -> sendMessage(false, str), this::onError);
  }

  private void sendMessage(boolean left, String source) {
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, JSString.valueOf(source));
    jsArray.set(1, JSString.valueOf(left ? this.leftHandle.getName() : this.rightHandle.getName()));
    jsArray.set(2, JsCast.jsInts(left ? 1 : 0));
    channel.sendMessage(jsArray);
  }

  private void onError(String error) {
    LoggingJs.log(LoggingJs.ERROR, "Can't read file: " + error);
  }
}
