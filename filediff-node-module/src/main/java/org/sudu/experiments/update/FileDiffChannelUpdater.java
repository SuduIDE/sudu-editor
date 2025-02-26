package org.sudu.experiments.update;

import org.sudu.experiments.Channel;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.editor.worker.FsWorkerJobs;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.TextEncoder;
import org.sudu.experiments.protocol.JsCast;
import org.sudu.experiments.worker.WorkerJobExecutor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Int32Array;

import static org.sudu.experiments.editor.worker.FsWorkerJobs.*;

public class FileDiffChannelUpdater {
  static final boolean debug = false;

  private FileHandle leftHandle, rightHandle;
  private WorkerJobExecutor executor;
  private final Channel channel;
  private final DiffModelChannelUpdater updater;

  public final static int FILE_READ = 0;
  public final static int FILE_SAVE = 1;
  public final static int SEND_DIFF = 2;
  public final static int SEND_INT_DIFF = 3;
  public final static Int32Array SEND_DIFF_MESSAGE = JsCast.jsInts(SEND_DIFF);
  public final static Int32Array SEND_INT_DIFF_MESSAGE = JsCast.jsInts(SEND_INT_DIFF);

  public FileDiffChannelUpdater(
      Channel channel,
      DiffModelChannelUpdater updater,
      WorkerJobExecutor executor
  ) {
    this.executor = executor;
    this.channel = channel;
    this.channel.setOnMessage(this::onMessage);
    this.updater = updater;
  }

  public void compareLeft(FileHandle leftHandle) {
    this.leftHandle = leftHandle;
    FsWorkerJobs.readTextFile(executor, leftHandle,
        (text, encoding) -> sendFileRead(true, text, encoding), this::onError);
  }

  public void compareRight(FileHandle rightHandle) {
    this.rightHandle = rightHandle;
    FsWorkerJobs.readTextFile(executor, rightHandle,
        (text, encoding) -> sendFileRead(false, text, encoding), this::onError);
  }

  private void onMessage(JsArray<JSObject> jsArray) {
    int type = JsCast.ints(jsArray.pop())[0];
    switch (type) {
      case FILE_READ -> onFileRead(jsArray);
//      case SEND_DIFF -> onSendDiff(jsArray);
      case FILE_SAVE -> onFileSave(jsArray);
//      case SEND_INT_DIFF -> onSendIntervalDiff(jsArray);
    }
  }

  private void onFileSave(JsArray<JSObject> jsArray) {
    var jsString = JsCast.jsString(jsArray, 0);
    var source = TextEncoder.toCharArray(jsString);
    String encoding = JsCast.string(jsArray, 1);
    boolean left = JsCast.ints(jsArray, 2)[0] == 1;
    if (left && leftHandle != null) {
      LoggingJs.debug("writeText: encoding = " + encoding + ", file = " + leftHandle);
      fileWriteText(executor, leftHandle, source, encoding,
          () -> onFileWrite(true, leftHandle.getFullPath()),
          this::onError);
    } else if (!left && rightHandle != null) {
      LoggingJs.debug("writeText: encoding = " + encoding + ", file = " + rightHandle);
      fileWriteText(executor, rightHandle, source, encoding,
          () -> onFileWrite(false, rightHandle.getFullPath()),
          this::onError);
    }
  }

  private void onFileRead(JsArray<JSObject> jsArray) {
    boolean left = JsCast.ints(jsArray)[0] == 1;
    FileHandle handle = left ? leftHandle : rightHandle;
    if (handle == null) {
      onError("onFileRead: handle is null");
    } else {
      FsWorkerJobs.readTextFile(executor, handle,
          (t, en) -> sendFileRead(left, t, en),
          this::onError);
    }
  }

  // todo finish writing in future
//  private void onSendDiff(JsArray<JSObject> jsArray) {
//    String src1 = JsCast.string(jsArray, 0);
//    String src2 = JsCast.string(jsArray, 1);
//    int[] intervals1 = JsCast.ints(jsArray, 2);
//    int[] intervals2 = JsCast.ints(jsArray, 3);
//    executor.sendToWorker((result) -> {
//      JsArray<JSObject> jsResult = JsArray.create();
//      int[] modelInts = ((ArrayView) result[0]).ints();
//      jsResult.set(0, JsCast.jsInts(modelInts));
//      jsResult.push(SEND_DIFF_MESSAGE);
//      channel.sendMessage(jsResult);
//    }, DiffUtils.FIND_DIFFS, src1.toCharArray(), intervals1, src2.toCharArray(), intervals2);
//  }
//
//  private void onSendIntervalDiff(JsArray<JSObject> jsArray) {
//
//  }
//

  private void onFileWrite(boolean left, String fullPath) {
    if (updater != null)
      updater.onRemoteFileSave(left, fullPath);
  }

  public void sendFileRead(boolean left, JSString source, JSString encoding) {
    if (debug) LoggingJs.debug(JsHelper.concat(
        "FileDiffChannelUpdater.postOpenFile: left=" + left +
            " length = " + source.getLength() +
            ", encoding=", encoding));
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, source);
    jsArray.set(1, encoding);
    jsArray.set(2, JSString.valueOf(name(left)));
    jsArray.set(3, JsCast.jsInts(left ? 1 : 0, havaHandle(left) ? 1 : 0));
    jsArray.set(4, JsCast.jsInts(FILE_READ));
    channel.sendMessage(jsArray);
  }

  public String name(boolean left) {
    var handle = left ? leftHandle : rightHandle;
    if (handle == null) return "";
    else return handle.getName();
  }

  public boolean havaHandle(boolean left) {
    return (left ? leftHandle : rightHandle) != null;
  }

  private void sendFileRead(boolean left, String source, String encoding) {
    sendFileRead(left, JSString.valueOf(source), JSString.valueOf(encoding));
  }

  private void onError(String error) {
    LoggingJs.log(LoggingJs.ERROR, "Can't read file: " + error);
  }
}
