package org.sudu.experiments.update;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.worker.WorkerJobExecutor;

public class DiffModelUpdater {

  public final RemoteFolderDiffModel leftRootAcc, rightRootAcc;
  public final DirectoryHandle leftDir, rightDir;
  private final WorkerJobExecutor executor;
  private final Runnable updateInfo;

  public DiffModelUpdater(
      RemoteFolderDiffModel leftRoot, RemoteFolderDiffModel rightRoot,
      DirectoryHandle leftDir, DirectoryHandle rightDir,
      WorkerJobExecutor executor, Runnable updateInfo
  ) {
    this.leftRootAcc = leftRoot;
    this.rightRootAcc = rightRoot;
    this.leftDir = leftDir;
    this.rightDir = rightDir;
    this.executor = executor;
    this.updateInfo = updateInfo;
  }

  public void beginCompare() {
    Collector collector = new Collector(
        leftRootAcc,
        rightRootAcc,
        executor,
        this::onCompared
    );
    collector.beginCompare(leftDir, rightDir);
  }

  public void onCompared(Object[] result) {
    int[] ints = (int[]) result[0];
    var updateDto = UpdateDto.fromInts(ints, result);
    leftRootAcc.update(updateDto.leftRoot);
    rightRootAcc.update(updateDto.rightRoot);
    updateInfo.run();
  }
}
