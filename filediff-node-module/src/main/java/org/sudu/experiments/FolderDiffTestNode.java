package org.sudu.experiments;

import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.js.node.NodeDirectoryHandle;
import org.sudu.experiments.worker.WorkerJobExecutor;

public class FolderDiffTestNode {

  private final NodeDirectoryHandle dir1;
  private final NodeDirectoryHandle dir2;
  private final WorkerJobExecutor executor;
  private final JsFunctions.Runnable onComplete;

  public FolderDiffTestNode(
      NodeDirectoryHandle dir1,
      NodeDirectoryHandle dir2,
      WorkerJobExecutor executor,
      JsFunctions.Runnable onComplete
  ) {
    this.dir1 = dir1;
    this.dir2 = dir2;
    this.executor = executor;
    this.onComplete = onComplete;
  }

  public void scan() {
//    executor.sendToWorker(
//        result -> {},
//
//    );
    onComplete.f();
  }

  private void dirResult(Object[] objects) {
    for (int i = 0; i < objects.length; i++) {
      System.out.println(
          "objects[" + i + "] = " +
              objects[i].getClass().getSimpleName() + ": " +
              objects[i]);
    }
  }
}
