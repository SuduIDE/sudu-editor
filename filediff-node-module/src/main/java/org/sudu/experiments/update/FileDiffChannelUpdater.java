package org.sudu.experiments.update;

import org.sudu.experiments.Channel;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.protocol.JsCast;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Int32Array;

public class FileDiffChannelUpdater {

  private FileHandle leftHandle, rightHandle;
  private final Channel channel;

  public final static int FILE_READ = 0;
  public final static int SEND_DIFF = 1;
  public final static int SEND_INT_DIFF = 2;
  public final static Int32Array FILE_READ_MESSAGE = JsCast.jsInts(FILE_READ);
  public final static Int32Array SEND_DIFF_MESSAGE = JsCast.jsInts(SEND_DIFF);
  public final static Int32Array SEND_INT_DIFF_MESSAGE = JsCast.jsInts(SEND_INT_DIFF);

  public FileDiffChannelUpdater(Channel channel) {
    this.channel = channel;
  }

  public void beginCompare(FileHandle leftHandle, FileHandle rightHandle) {
    this.leftHandle = leftHandle;
    this.rightHandle = rightHandle;
    leftHandle.readAsText((str) -> sendMessage(true, str), this::onError);
    rightHandle.readAsText((str) -> sendMessage(false, str), this::onError);
  }

  public void sendMessage(boolean left, JSString source) {
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, source);
    jsArray.set(1, JSString.valueOf(left ? leftHandle.getName() : rightHandle.getName()));
    jsArray.set(2, JsCast.jsInts(left ? 1 : 0));
    jsArray.push(FILE_READ_MESSAGE);
    channel.sendMessage(jsArray);
  }

  private void sendMessage(boolean left, String source) {
    sendMessage(left, JSString.valueOf(source));
  }

  private void onError(String error) {
    LoggingJs.log(LoggingJs.ERROR, "Can't read file: " + error);
  }
}
