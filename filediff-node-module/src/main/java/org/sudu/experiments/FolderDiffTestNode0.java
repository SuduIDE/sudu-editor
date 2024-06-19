package org.sudu.experiments;

import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.worker.WorkerJobExecutor;

public class FolderDiffTestNode0 {

  final DirectoryHandle dir1;
  final DirectoryHandle dir2;
  final WorkerJobExecutor executor;
  final JsFunctions.Runnable onComplete;

  public FolderDiffTestNode0(
      DirectoryHandle dir1,
      DirectoryHandle dir2,
      WorkerJobExecutor executor,
      JsFunctions.Runnable onComplete
  ) {
    this.dir1 = dir1;
    this.dir2 = dir2;
    this.executor = executor;
    this.onComplete = onComplete;
  }

}
