package org.sudu.experiments.update;

import org.sudu.experiments.Channel;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.js.JsArray;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public class FileEditChannelUpdater {

  private FileHandle handle;
  private final Channel channel;

  public FileEditChannelUpdater(Channel channel) {
    this.channel = channel;
  }

  public void beginCompare(FileHandle fileHandle) {
    this.handle = fileHandle;
    FileHandle.readTextFile(
      handle, this::sendMessage, this::onError);
  }

  public void sendMessage(JSString source, JSString encoding) {
    // todo add encoding support
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, source);
    jsArray.set(1, JSString.valueOf(handle.getName()));
    channel.sendMessage(jsArray);
  }

  private void sendMessage(String source, String encoding) {
    sendMessage(JSString.valueOf(source), JSString.valueOf(encoding));
  }

  private void onError(String error) {
    LoggingJs.log(LoggingJs.ERROR, "Can't read file: " + error);
  }
}
