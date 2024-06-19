package org.sudu.experiments.diff.tests;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.worker.WorkerJobExecutor;

public class DirectoryTest {

  final DirectoryHandle dir1;
  final DirectoryHandle dir2;
  final WorkerJobExecutor executor;

  final Runnable onComplete;

  public DirectoryTest(
      DirectoryHandle dir1,
      DirectoryHandle dir2,
      WorkerJobExecutor executor,
      Runnable onComplete
  ) {
    this.dir1 = dir1;
    this.dir2 = dir2;
    this.executor = executor;
    this.onComplete = onComplete;
  }

}
