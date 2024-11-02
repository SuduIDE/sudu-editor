package org.sudu.experiments.update;

import org.sudu.experiments.*;
import org.sudu.experiments.diff.folder.ItemFolderDiffModel;
import org.sudu.experiments.encoding.FileEncoding;
import org.sudu.experiments.encoding.TextDecoder;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsMemoryAccess;
import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.js.TextEncoder;
import org.sudu.experiments.js.node.NodeFileHandle;
import org.sudu.experiments.protocol.FrontendMessage;
import org.sudu.experiments.protocol.JsCast;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Int32Array;

public class DiffModelChannelUpdater {

  private RemoteCollector collector;
  private Channel channel;

  public static final int FRONTEND_MESSAGE = 0;
  public static final int OPEN_FILE = 1;
  public static final int APPLY_DIFF = 2;
  public static final int FILE_SAVE = 3;
  public static final int REFRESH = 4;
  public static final int APPLY_FILTERS = 5;
  public static final Int32Array FRONTEND_MESSAGE_ARRAY = JsMemoryAccess.bufferView(new int[]{FRONTEND_MESSAGE});
  public static final Int32Array OPEN_FILE_ARRAY = JsMemoryAccess.bufferView(new int[]{OPEN_FILE});
  public static final Int32Array APPLY_DIFF_ARRAY = JsMemoryAccess.bufferView(new int[]{APPLY_DIFF});
  public static final Int32Array FILE_SAVE_ARRAY = JsMemoryAccess.bufferView(new int[]{FILE_SAVE});
  public static final Int32Array REFRESH_ARRAY = JsMemoryAccess.bufferView(new int[]{REFRESH});
  public static final Int32Array APPLY_FILTERS_ARRAY = JsMemoryAccess.bufferView(new int[]{APPLY_FILTERS});

  public DiffModelChannelUpdater(
      ItemFolderDiffModel root,
      boolean scanFileContent,
      NodeWorkersPool executor, Channel channel
  ) {
    this.collector = new RemoteCollector(
        root,
        scanFileContent,
        executor
    );
    this.channel = channel;
    this.channel.setOnMessage(this::onMessage);
  }

  public void shutdown(Runnable onComplete) {
    LoggingJs.info("DiffModelChannelUpdater.dispose");
    collector.shutdown(onComplete);
    collector = null;
    channel = null;
  }

  public void beginCompare() {
    collector.setSendResult(channel::sendMessage);
    collector.setOnComplete(channel::sendMessage);
    collector.beginCompare();
  }

  public void onMessage(JsArray<JSObject> jsArray) {
    Int32Array intArray = jsArray.pop().cast();
    switch (intArray.get(0)) {
      case FRONTEND_MESSAGE -> onFrontendMessage(jsArray);
      case OPEN_FILE -> onOpenFile(jsArray);
      case APPLY_DIFF -> onApplyDiff(jsArray);
      case FILE_SAVE -> onFileSave(jsArray);
      case REFRESH -> onRefresh();
      case APPLY_FILTERS -> applyFilters(jsArray);
    }
  }

  private void onFrontendMessage(JsArray<JSObject> jsArray) {
    FrontendMessage message = FrontendMessage.deserialize(jsArray);
    collector.onMessageGot(message);
  }

  private void onOpenFile(JsArray<JSObject> jsArray) {
    JSString jsPath = jsArray.pop().cast();
    Int32Array key = jsArray.pop().cast();

    var fileHandle = new NodeFileHandle(jsPath);
    FileHandle.readTextFile(fileHandle,
        (source, encoding) -> {
          // todo: encoding
          var result = JsArray.create();
          result.push(JSString.valueOf(source));
          result.push(key);
          result.push(OPEN_FILE_ARRAY);
          channel.sendMessage(result);
        },
        error -> {
          var result = JsArray.create();
          System.err.println(error);
          result.push(key);
          result.push(OPEN_FILE_ARRAY);
          channel.sendMessage(result);
        }
    );
  }

  private void onApplyDiff(JsArray<JSObject> jsArray) {
    int[] path = JsCast.ints(jsArray, 0);
    boolean left = JsCast.ints(jsArray, 1)[0] == 0;
    collector.applyDiff(path, left);
  }

  private void onFileSave(JsArray<JSObject> jsArray) {
    System.out.println("DiffModelChannelUpdater.onFileSave");
    int[] path = JsCast.ints(jsArray, 0);
    boolean left = JsCast.ints(jsArray, 1)[0] == 0;
    String source = JsCast.string(jsArray, 2);
    // todo: add encoding support
    collector.fileSave(path, left, source, FileEncoding.utf8);
  }

  private void onRefresh() {
    collector.refresh();
  }

  private void applyFilters(JsArray<JSObject> jsArray) {
    System.out.println("DiffModelChannelUpdater.applyFilters");
    int[] filters = JsCast.ints(jsArray, 0);
    collector.applyFilters(filters);
  }

  public void onRemoteFileSave(boolean left, String fullPath) {
    collector.onRemoteFileSave(left, fullPath);
  }
}
