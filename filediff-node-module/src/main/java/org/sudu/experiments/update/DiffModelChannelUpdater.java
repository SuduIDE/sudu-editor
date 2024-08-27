package org.sudu.experiments.update;

import org.sudu.experiments.Channel;
import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.Disposable;
import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsMemoryAccess;
import org.sudu.experiments.js.node.NodeFileHandle;
import org.sudu.experiments.protocol.FrontendMessage;
import org.sudu.experiments.worker.WorkerJobExecutor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Int32Array;

public class DiffModelChannelUpdater implements Disposable {

  private final RemoteCollector collector;
  private final Channel channel;

  public static final int FRONTEND_MESSAGE = 0;
  public static final int OPEN_FILE = 1;
  public static final Int32Array FRONTEND_MESSAGE_ARRAY = JsMemoryAccess.bufferView(new int[]{FRONTEND_MESSAGE});
  public static final Int32Array OPEN_FILE_ARRAY = JsMemoryAccess.bufferView(new int[]{OPEN_FILE});

  public DiffModelChannelUpdater(
      RemoteFolderDiffModel root,
      DirectoryHandle leftDir, DirectoryHandle rightDir,
      boolean scanFileContent,
      WorkerJobExecutor executor, Channel channel
  ) {
    this.collector = new RemoteCollector(
        root,
        leftDir, rightDir,
        scanFileContent,
        executor
    );
    this.channel = channel;
    this.channel.setOnMessage(this::onMessage);
  }

  @Override
  public void dispose() {
    LoggingJs.Static.logger.log(LoggingJs.INFO,
        JSString.valueOf("DiffModelChannelUpdater.dispose"));
    // todo: shutdown all activity
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
    fileHandle.readAsText(
        source -> {
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
}
