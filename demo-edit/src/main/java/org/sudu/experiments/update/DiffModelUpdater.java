package org.sudu.experiments.update;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.worker.WorkerJobExecutor;

public class DiffModelUpdater {

  public final FolderDiffModel leftRootAcc, rightRootAcc;
  public final DirectoryHandle leftDir, rightDir;
  private final WorkerJobExecutor executor;
  private final Runnable updateInfo;

  public DiffModelUpdater(
      FolderDiffModel leftRoot,
      FolderDiffModel rightRoot,
      DirectoryHandle leftDir,
      DirectoryHandle rightDir,
      WorkerJobExecutor executor,
      Runnable updateInfo
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
        leftRootAcc, rightRootAcc,
        true,
        executor
    );
    collector.setUpdate(updateInfo);
    collector.setOnComplete(updateInfo);
    collector.beginCompare(leftDir, rightDir);
  }
}
