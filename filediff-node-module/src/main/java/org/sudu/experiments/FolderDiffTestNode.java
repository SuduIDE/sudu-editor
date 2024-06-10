package org.sudu.experiments;

import org.sudu.experiments.editor.worker.TestJobs;
import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.js.node.NodeDirectoryHandle;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.function.Consumer;

public class FolderDiffTestNode {

  private final NodeDirectoryHandle dir1;
  private final NodeDirectoryHandle dir2;
  private final WorkerJobExecutor executor;
  private final JsFunctions.Runnable onComplete;
  private int completed;

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
    Consumer<Object[]> handler = this::dirResult;
    executor.sendToWorker(handler, TestJobs.asyncWithDir, dir1);
    executor.sendToWorker(handler, TestJobs.asyncWithDir, dir2);
  }

  private void dirResult(Object[] objects) {
    for (int i = 0; i < objects.length; i++) {
      System.out.println(
          "objects[" + i + "] = " +
              objects[i].getClass().getSimpleName() + ": " +
              objects[i]);
    }
    if (++completed == 2) {
      onComplete.f();
    }
  }
}
