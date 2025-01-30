package org.sudu.experiments.update;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.worker.WorkerJobExecutor;

public class DiffModelUpdater {

  public interface Listener {
    /* stat = {
         foldersCompared, filesCompared,
         leftFiles, leftFolders,
         rightFiles, rightFolders,
         inserted, deleted, edited
       } */
    void onComplete(int[] stat);
  }

  public final FolderDiffModel root;
  public final DirectoryHandle leftDir, rightDir;
  private final WorkerJobExecutor executor;
  private final Listener updateInfo;

  public DiffModelUpdater(
      FolderDiffModel root,
      DirectoryHandle leftDir,
      DirectoryHandle rightDir,
      WorkerJobExecutor executor,
      Listener updateInfo
  ) {
    this.root = root;
    this.leftDir = leftDir;
    this.rightDir = rightDir;
    this.executor = executor;
    this.updateInfo = updateInfo;
  }

  public void beginCompare() {
    Collector collector = new Collector(
        root,
        true,
        executor
    );
    collector.setUpdate(this::update);
    collector.setOnComplete(this::onComplete);
    collector.beginCompare(leftDir, rightDir);
  }

  private void update() {
    updateInfo.onComplete(new int[]{0, 0});
  }

  private void onComplete(int[] stat) {
    updateInfo.onComplete(stat);
  }
}
