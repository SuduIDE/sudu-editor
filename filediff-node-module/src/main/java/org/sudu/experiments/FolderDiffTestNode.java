package org.sudu.experiments;

import org.sudu.experiments.diff.DiffModelBuilder;
import org.sudu.experiments.editor.worker.TestJobs;
import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.function.Consumer;

public class FolderDiffTestNode extends FolderDiffTestNode0 {

  private final boolean content;
  private int completed;

  public FolderDiffTestNode(
      DirectoryHandle dir1,
      DirectoryHandle dir2,
      WorkerJobExecutor executor,
      JsFunctions.Runnable onComplete,
      boolean content
  ) {
    super(dir1, dir2, executor, onComplete);
    this.content = content;
  }

  public void test() {
    var builder = new DiffModelBuilder(
        (_1, _2, _3) -> updateDiffInfo(), executor, content);
  }

  private void updateDiffInfo() {


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
