package org.sudu.experiments.diff.update;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.worker.ArrayView;
import org.sudu.experiments.worker.WorkerJobExecutor;

public class DiffModelUpdater {

  public final FolderDiffModel leftRootAcc, rightRootAcc;
  public final DirectoryHandle leftDir, rightDir;
  private final WorkerJobExecutor executor;
  private final Runnable updateInfo;

  public DiffModelUpdater(
      FolderDiffModel leftRoot, FolderDiffModel rightRoot,
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
    compare(leftRootAcc, rightRootAcc, leftDir, rightDir);
  }

  public void compare(
      FolderDiffModel leftModel, FolderDiffModel rightModel,
      DirectoryHandle leftDir, DirectoryHandle rightDir
  ) {
    executor.sendToWorker(
        results -> onCompared(leftModel, rightModel, results),
        Collector.COLLECT,
        leftDir, rightDir
    );
  }

  public void onCompared(
      FolderDiffModel leftModel, FolderDiffModel rightModel,
      Object[] result
  ) {
    int[] ints = ((ArrayView) result[0]).ints();
    var updateDto = UpdateDto.fromInts(ints, result);
    leftModel.update(updateDto.leftRoot);
    rightModel.update(updateDto.rightRoot);
    updateInfo.run();
  }
}
