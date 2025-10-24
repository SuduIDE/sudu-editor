package org.sudu.experiments.update;

import org.sudu.experiments.Channel;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.editor.worker.FileCompare;
import org.sudu.experiments.editor.worker.FsWorkerJobs;
import org.sudu.experiments.editor.worker.NextDiffTask;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.TextDecoder;
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
  private final WorkerJobExecutor executor;
  private final Channel channel;
  private final DiffModelChannelUpdater updater;

  public final static int FILE_READ = 0;
  public final static int FILE_SAVE = 1;
  public final static int BIN_FETCH = 2;
  public final static int BIN_FETCH_SIZE = 3;
  public final static int ERROR = 4;
  public final static int BIN_NAVIGATE = 5;
  public final static int BIN_CAN_NAVIGATE = 6;
  public final static int NOTIFY = 7;
  public final static Int32Array FILE_READ_ARRAY = JsCast.jsInts(FILE_READ);
  public final static Int32Array FILE_SAVE_ARRAY = JsCast.jsInts(FILE_SAVE);
  public final static Int32Array FETCH_ARRAY = JsCast.jsInts(BIN_FETCH);
  public final static Int32Array FETCH_SIZE_ARRAY = JsCast.jsInts(BIN_FETCH_SIZE);
  public final static Int32Array ERROR_ARRAY = JsCast.jsInts(ERROR);
  public final static Int32Array BIN_NAVIGATE_ARRAY = JsCast.jsInts(BIN_NAVIGATE);
  public final static Int32Array BIN_CAN_NAVIGATE_ARRAY = JsCast.jsInts(BIN_CAN_NAVIGATE);
  public final static Int32Array NOTIFY_ARRAY = JsCast.jsInts(NOTIFY);

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
    LoggingJs.info("FileDiffChannelUpdater.onMessage: type = " + name(type));
    switch (type) {
      case FILE_READ -> onFileRead(jsArray);
      case FILE_SAVE -> onFileSave(jsArray);
      case BIN_FETCH -> fetch(jsArray);
      case BIN_FETCH_SIZE -> fetchSize(jsArray);
      case BIN_NAVIGATE -> navigate(jsArray);
      case BIN_CAN_NAVIGATE -> canNavigate(jsArray);
    }
  }

  private String name(int type) {
    return switch (type) {
      case FILE_READ -> "FILE_READ";
      case FILE_SAVE -> "FILE_SAVE";
      case BIN_FETCH -> "BIN_FETCH";
      case BIN_FETCH_SIZE -> "BIN_FETCH_SIZE";
      case BIN_NAVIGATE -> "BIN_NAVIGATE";
      case BIN_CAN_NAVIGATE -> "BIN_CAN_NAVIGATE";
      default -> "UNKNOWN";
    };
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

  private void onFileWrite(boolean left, String fullPath) {
    if (updater != null)
      updater.onRemoteFileSave(left, fullPath);
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, JsCast.jsInts(left ? 1 : 0));
    jsArray.push(FILE_SAVE_ARRAY);
    channel.sendMessage(jsArray);
  }

  public void sendFileRead(boolean left, JSString source, JSString encoding) {
    sendFileRead(left, source, encoding, JSString.valueOf(name(left)));
  }

  public void sendFileRead(boolean left, JSString source, JSString encoding, JSString filename) {
    if (debug) LoggingJs.debug(JsHelper.concat(
        "FileDiffChannelUpdater.postOpenFile: left=" + left +
            " length = " + source.getLength() +
            ", encoding=", encoding));
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, source);
    jsArray.set(1, encoding);
    jsArray.set(2, filename);
    jsArray.set(3, JsCast.jsInts(left ? 1 : 0, haveHandle(left) ? 1 : 0));
    jsArray.set(4, JsCast.jsInts(FILE_READ));
    channel.sendMessage(jsArray);
  }

  public void fetch(JsArray<JSObject> jsArray) {
    int[] ints = JsCast.ints(jsArray, 0);
    boolean left = ints[0] == 1;
    int chinkSize = ints[1];
    double address = JsCast.doubles(jsArray, 1)[0];
    fetch(left, address, chinkSize);
  }

  public void fetchSize(JsArray<JSObject> jsArray) {
    boolean left = JsCast.ints(jsArray, 0)[0] == 1;
    var handle = left ? leftHandle : rightHandle;
    FsWorkerJobs.asyncStats(executor, handle, sz -> sendFetchSize(left, sz.size), this::onError);
  }

  public void navigate(JsArray<JSObject> jsArray) {
    int[] ints = JsCast.ints(jsArray, 0);
    int bytesPerLine = ints[0];
    double address = JsCast.doubles(jsArray, 1)[0];
    boolean skipDiff = ints[1] == 1, findNext = ints[2] == 1;
    LoggingJs.info("FileDiffChannelUpdater.navigate: bytesPerLine = " + bytesPerLine
        + ", address = " + address
        + ", skipDiff = " + skipDiff
        + ", findNext = " + findNext
    );
    new NextDiffTask(executor,
        leftHandle, rightHandle,
        address, bytesPerLine,
        skipDiff, findNext,
        this::sendNotification,
        this::onNavigate
    );
  }

  public void canNavigate(JsArray<JSObject> jsArray) {
  }

  public void sendNotification(String msg) {
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, JSString.valueOf(msg));
    jsArray.push(NOTIFY_ARRAY);
    channel.sendMessage(jsArray);
  }

  public void onNavigate(double lSz, double rSz, double dPos, String err) {
    LoggingJs.info("onNavigate: lSz = " + lSz + ", rSz = " + rSz + ", dPos = " + dPos + ", err = " + err);
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, JsCast.jsNumbers(dPos));
    jsArray.push(BIN_NAVIGATE_ARRAY);
    channel.sendMessage(jsArray);
  }

  public void fetch(boolean left, double address, int chinkSize) {
    FsWorkerJobs.readBinFile(
        executor,
        (left ? leftHandle : rightHandle),
        address, chinkSize,
        bytes -> sendFetch(left, address, bytes),
        err -> sendError(address, err)
    );
  }

  public void fetchSizeLeft(FileHandle handle) {
    this.leftHandle = handle;
    FsWorkerJobs.asyncStats(executor, handle, sz -> sendFetchSize(true, sz.size), this::onError);
  }

  public void fetchSizeRight(FileHandle handle) {
    this.rightHandle = handle;
    FsWorkerJobs.asyncStats(executor, handle, sz -> sendFetchSize(false, sz.size), this::onError);
  }

  public String name(boolean left) {
    var handle = left ? leftHandle : rightHandle;
    if (handle == null) return "";
    else return handle.getFullPath();
  }

  public boolean haveHandle(boolean left) {
    return (left ? leftHandle : rightHandle) != null;
  }

  private void sendFileRead(boolean left, char[] source, String encoding) {
    sendFileRead(left, TextDecoder.decodeUTF16(source), JSString.valueOf(encoding));
  }

  private void sendFetch(boolean left, double address, byte[] bytes) {
    LoggingJs.debug("sendFetch: left = " + left + ", pos = " + address);
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, JsCast.jsInts(left ? 1 : 0));
    jsArray.set(1, JsCast.jsNumbers(address));
    jsArray.set(2, JsCast.jsBytes(bytes));
    jsArray.push(FETCH_ARRAY);
    channel.sendMessage(jsArray);
  }

  private void sendFetchSize(boolean left, double sz) {
    LoggingJs.debug("sendFetchSize: left = " + left + ", size = " + sz);
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, JsCast.jsInts(left ? 1 : 0));
    jsArray.set(1, JSString.valueOf(name(left)));
    jsArray.set(2, JsCast.jsNumbers(sz));
    jsArray.push(FETCH_SIZE_ARRAY);
    channel.sendMessage(jsArray);
  }

  private void sendError(double address, String error) {
    LoggingJs.debug("sendError: address = " + address + ", error = " + error);
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, JsCast.jsNumbers(address));
    jsArray.set(1, JSString.valueOf(error));
    jsArray.push(ERROR_ARRAY);
    channel.sendMessage(jsArray);
  }

  private void onError(String error) {
    LoggingJs.log(LoggingJs.ERROR, "Can't read file: " + error);
  }
}
