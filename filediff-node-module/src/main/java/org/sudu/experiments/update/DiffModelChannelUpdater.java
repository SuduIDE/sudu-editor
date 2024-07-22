package org.sudu.experiments.update;

import org.sudu.experiments.Channel;
import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.JsMemoryAccess;
import org.sudu.experiments.protocol.FrontendMessage;
import org.sudu.experiments.worker.WorkerJobExecutor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public class DiffModelChannelUpdater {

  private final RemoteCollector collector;
  private final Channel channel;

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

  public void beginCompare() {
    collector.setSendResult(this::onCompared);
    collector.setOnComplete(this::onCompared);
    collector.beginCompare();
  }

  public void onCompared(Object[] result) {
    var jsResult = JsArray.create(result.length);
    int[] ints = (int[]) result[0];
    jsResult.set(0, JsMemoryAccess.bufferView(ints));
    for (int i = 1; i < result.length; i++) {
      String path = (String) result[i];
      jsResult.set(i, JSString.valueOf(path));
    }
    channel.sendMessage(jsResult);
  }

  public void onMessage(JsArray<JSObject> jsArray) {
    FrontendMessage message = FrontendMessage.deserialize(jsArray);
    collector.onMessageGot(message);
  }
}
