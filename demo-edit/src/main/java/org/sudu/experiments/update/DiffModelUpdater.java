package org.sudu.experiments.update;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.worker.WorkerJobExecutor;

public class DiffModelUpdater {

  public interface Listener {
    void onComplete(int foldersCompared, int filesCompared);
  }

  public final RemoteFolderDiffModel leftRootAcc, rightRootAcc;
  public final DirectoryHandle leftDir, rightDir;
  private final WorkerJobExecutor executor;
  private final Listener updateInfo;

  public DiffModelUpdater(
      RemoteFolderDiffModel leftRoot, RemoteFolderDiffModel rightRoot,
      DirectoryHandle leftDir, DirectoryHandle rightDir,
      WorkerJobExecutor executor, Listener updateInfo
  ) {
    this.leftRootAcc = leftRoot;
    this.rightRootAcc = rightRoot;
    this.leftDir = leftDir;
    this.rightDir = rightDir;
    this.executor = executor;
    this.updateInfo = updateInfo;
  }

  public void beginCompare() {
    Collector collector = new Collector.Collector1(
        leftRootAcc, rightRootAcc,
        true,
        executor
    );
    collector.setUpdate(this::update);
    collector.setOnComplete(this::onComplete);
    collector.beginCompare(leftDir, rightDir);
  }

  private void update() {
    updateInfo.onComplete(0,0);
  }

  private void onComplete(Object[] data) {
    int[] stats = (int[]) data[0];
    updateInfo.onComplete(stats[0], stats[1]);
  }
}
