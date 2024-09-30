package org.sudu.experiments.update;

import org.sudu.experiments.Channel;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.protocol.JsCast;
import org.sudu.experiments.worker.ArrayView;
import org.sudu.experiments.worker.WorkerJobExecutor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Int32Array;

import java.util.Arrays;

public class FileDiffChannelUpdater {

  private FileHandle leftHandle, rightHandle;
  private WorkerJobExecutor executor;
  private final Channel channel;

  public final static int FILE_READ = 0;
  public final static int FILE_SAVE = 1;
  public final static int SEND_DIFF = 2;
  public final static int SEND_INT_DIFF = 3;
  public final static Int32Array FILE_READ_MESSAGE = JsCast.jsInts(FILE_READ);
  public final static Int32Array FILE_SAVE_MESSAGE = JsCast.jsInts(FILE_SAVE);
  public final static Int32Array SEND_DIFF_MESSAGE = JsCast.jsInts(SEND_DIFF);
  public final static Int32Array SEND_INT_DIFF_MESSAGE = JsCast.jsInts(SEND_INT_DIFF);

  public FileDiffChannelUpdater(Channel channel, WorkerJobExecutor executor) {
    this.executor = executor;
    this.channel = channel;
    this.channel.setOnMessage(this::onMessage);
  }

  public void beginCompare(FileHandle leftHandle, FileHandle rightHandle) {
    this.leftHandle = leftHandle;
    this.rightHandle = rightHandle;
    leftHandle.readAsText((str) -> sendFileRead(true, str), this::onError);
    rightHandle.readAsText((str) -> sendFileRead(false, str), this::onError);
  }

  private void onMessage(JsArray<JSObject> jsArray) {
    int type = JsCast.ints(jsArray.pop())[0];
    switch (type) {
      case SEND_DIFF -> onSendDiff(jsArray);
      case FILE_SAVE -> onFileSave(jsArray);
      case SEND_INT_DIFF -> onSendIntervalDiff(jsArray);
    }
  }

  private void onFileSave(JsArray<JSObject> jsArray) {
    String source = JsCast.string(jsArray, 0);
    boolean left = JsCast.ints(jsArray, 1)[0] == 1;
    if (left) {
      leftHandle.writeText(source, () -> {/* todo */}, this::onError);
    } else {
      rightHandle.writeText(source, () -> {/* todo */}, this::onError);
    }
  }

  // todo finish writing in future
  private void onSendDiff(JsArray<JSObject> jsArray) {
    String src1 = JsCast.string(jsArray, 0);
    String src2 = JsCast.string(jsArray, 1);
    int[] intervals1 = JsCast.ints(jsArray, 2);
    int[] intervals2 = JsCast.ints(jsArray, 3);
    executor.sendToWorker((result) -> {
      JsArray<JSObject> jsResult = JsArray.create();
      int[] modelInts = ((ArrayView) result[0]).ints();
      jsResult.set(0, JsCast.jsInts(modelInts));
      jsResult.push(SEND_DIFF_MESSAGE);
      channel.sendMessage(jsResult);
    }, DiffUtils.FIND_DIFFS, src1.toCharArray(), intervals1, src2.toCharArray(), intervals2);
  }

  private void onSendIntervalDiff(JsArray<JSObject> jsArray) {

  }

  public void sendFileRead(boolean left, JSString source) {
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, source);
    jsArray.set(1, JSString.valueOf(left ? leftHandle.getName() : rightHandle.getName()));
    jsArray.set(2, JsCast.jsInts(left ? 1 : 0));
    jsArray.push(FILE_READ_MESSAGE);
    channel.sendMessage(jsArray);
  }

  private void sendFileRead(boolean left, String source) {
    sendFileRead(left, JSString.valueOf(source));
  }

  private void onError(String error) {
    LoggingJs.log(LoggingJs.ERROR, "Can't read file: " + error);
  }
}
