package org.sudu.experiments.update;

import org.sudu.experiments.Channel;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.encoding.JsTextFileReader;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.protocol.JsCast;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public class FileEditChannelUpdater {
  static final boolean debug = false;

  private FileHandle handle;
  private final Channel channel;

  public FileEditChannelUpdater(Channel channel) {
    this.channel = channel;
    this.channel.setOnMessage(this::onMessage);
  }

  public void setFile(FileHandle fileHandle) {
    this.handle = fileHandle;
    JsTextFileReader.read(handle, this::sendMessage, this::onReadError);
  }

  public void onMessage(JsArray<JSObject> jsArray) {
    String source = JsCast.string(jsArray, 0);
    String encoding = JsCast.string(jsArray, 1);
    handle.writeText(source, encoding, () -> onWriteComplete(source), this::onWriteError);
  }

  public void sendMessage(JSString source, JSString encoding) {
    if (debug) LoggingJs.debug(JsHelper.concat(
        "FileEditChannelUpdater.sendMessage, length = " + source.getLength()
            + ", encoding = ", encoding));
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, source);
    jsArray.set(1, encoding);
    jsArray.set(2, JSString.valueOf(handle.getName()));
    channel.sendMessage(jsArray);
  }

  private void onWriteComplete(String source) {
    if (!debug) return;
    String info = "Write complete" +
        ", Source length = " + source.length() +
        ", handle " + handle;
    LoggingJs.debug(info);
    handle.getSize(this::onSizeGet);
  }

  private void onSizeGet(int size) {
    LoggingJs.debug("Size: " + size + ", handle: " + handle);
  }

  private void onReadError(String error) {
    LoggingJs.log(LoggingJs.ERROR, "Can't read file: " + error);
  }

  private void onWriteError(String error) {
    LoggingJs.log(LoggingJs.ERROR, "Can't write file: " + error);
  }
}
