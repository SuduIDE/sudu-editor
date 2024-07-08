package org.sudu.experiments.update;

import org.sudu.experiments.Channel;
import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsMemoryAccess;
import org.sudu.experiments.worker.WorkerJobExecutor;
import org.teavm.jso.core.JSString;

import java.util.Arrays;

public class DiffModelChannelUpdater {

  public final RemoteFolderDiffModel leftRootAcc, rightRootAcc;
  public final DirectoryHandle leftDir, rightDir;
  private final WorkerJobExecutor executor;
  private final Channel channel;

  public DiffModelChannelUpdater(
      RemoteFolderDiffModel leftRoot, RemoteFolderDiffModel rightRoot,
      DirectoryHandle leftDir, DirectoryHandle rightDir,
      WorkerJobExecutor executor, Channel channel
  ) {
    this.leftRootAcc = leftRoot;
    this.rightRootAcc = rightRoot;
    this.leftDir = leftDir;
    this.rightDir = rightDir;
    this.executor = executor;
    this.channel = channel;
  }

  public void beginCompare() {
    var collector = new Collector(leftRootAcc, rightRootAcc, executor, this::onCompared);
    collector.compare(leftRootAcc, rightRootAcc, leftDir, rightDir);
  }

  public void onCompared(Object[] result) {
    var jsResult = JsArray.create(result.length);
    System.out.println("onCompared: " + Arrays.toString(result));
    int[] ints = (int[]) result[0];
    jsResult.set(0, JsMemoryAccess.bufferView(ints));
    for (int i = 1; i < result.length; i++) {
      String path = (String) result[i];
      jsResult.set(i, JSString.valueOf(path));
    }
    channel.sendMessage(jsResult);
  }
}
