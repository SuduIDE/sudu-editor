package org.sudu.experiments.update;

import org.sudu.experiments.Channel;
import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.protocol.FrontendMessage;
import org.sudu.experiments.worker.WorkerJobExecutor;
import org.teavm.jso.JSObject;

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
    collector.setSendResult(channel::sendMessage);
    collector.setOnComplete(channel::sendMessage);
    collector.beginCompare();
  }

  public void onMessage(JsArray<JSObject> jsArray) {
    FrontendMessage message = FrontendMessage.deserialize(jsArray);
    collector.onMessageGot(message);
  }
}
