package org.sudu.experiments.diff.tests;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.editor.worker.TestJobs;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.function.Consumer;

public class FolderScanTest extends DirectoryTest {

  private int completed;

  public FolderScanTest(
      DirectoryHandle dir1,
      DirectoryHandle dir2,
      WorkerJobExecutor executor,
      Runnable onComplete
  ) {
    super(dir1, dir2, executor, onComplete);
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
      onComplete.run();
    }
  }
}
