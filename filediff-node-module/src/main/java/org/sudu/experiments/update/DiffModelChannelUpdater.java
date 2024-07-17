package org.sudu.experiments.update;

import org.sudu.experiments.Channel;
import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsMemoryAccess;
import org.sudu.experiments.worker.WorkerJobExecutor;
import org.teavm.jso.core.JSString;

public class DiffModelChannelUpdater {

  public final RemoteFolderDiffModel root;
  public final DirectoryHandle leftDir, rightDir;
  private final WorkerJobExecutor executor;
  private final Channel channel;
  private final boolean scanFileContent;

  public DiffModelChannelUpdater(
      RemoteFolderDiffModel root,
      DirectoryHandle leftDir, DirectoryHandle rightDir,
      boolean scanFileContent,
      WorkerJobExecutor executor, Channel channel
  ) {
    this.root = root;
    this.leftDir = leftDir;
    this.rightDir = rightDir;
    this.scanFileContent = scanFileContent;
    this.executor = executor;
    this.channel = channel;
//    this.channel.setOnMessage(jsArray -> System.out.println(jsArray.toString()));
  }

  public void beginCompare() {
    var collector = new RemoteCollector(
        root,
        leftDir.getName(), rightDir.getName(),
        scanFileContent, executor
    );
    collector.setSendResult(this::onCompared);
    collector.setOnComplete(this::onCompared);
    collector.beginCompare(leftDir, rightDir);
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
}
