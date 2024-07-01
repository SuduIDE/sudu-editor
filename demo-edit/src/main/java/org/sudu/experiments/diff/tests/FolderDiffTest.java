package org.sudu.experiments.diff.tests;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.diff.DiffModelBuilder;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.ui.fs.DirectoryNode;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.function.DoubleSupplier;

public class FolderDiffTest extends DirectoryTest {

  final DoubleSupplier time;

  private final boolean content;

  boolean running = true;
  DirectoryNode leftRoot, rightRoot;
  FolderDiffModel leftModel = new FolderDiffModel(null);
  FolderDiffModel rightModel = new FolderDiffModel(null);
  int updateDiffInfoCounter;

  public FolderDiffTest(
      DirectoryHandle dir1,
      DirectoryHandle dir2,
      boolean content,
      WorkerJobExecutor executor,
      DoubleSupplier time,
      Runnable onComplete
  ) {
    super(dir1, dir2, executor, onComplete);
    this.content = content;
    this.time = time;
  }

  public void scan() {
    leftRoot = new DirectoryNode(dir1, null);
    rightRoot = new DirectoryNode(dir2, null);
    var builder = new DiffModelBuilder(
        (_1, _2, _3) -> updateDiffInfo(), executor, content);
    builder.compareRoots(
        leftRoot, rightRoot,
        leftModel, rightModel);
  }

  public boolean running() { return running; }

  private void updateDiffInfo() {
    ++updateDiffInfoCounter;
    if (updateDiffInfoCounter % 1000 == 0) {
      System.out.println("updateDiffInfoCounter = " + updateDiffInfoCounter);
    }
    if (running) {
      if (leftModel.compared && rightModel.compared) {
        System.out.println("Finished" + (content ? " scan with content: " : ": "));
        dumpResult();
        running = false;
        onComplete.run();
      }
    } else {
      System.err.println("updateDiffInfo after Finished, updateDiffInfoCounter = " + updateDiffInfoCounter);
    }
  }

  private void dumpResult() {
    String r = "" +
        "updateDiffInfo #calls = " + updateDiffInfoCounter + '\n'
        + "leftModel:\n"
        + "  .compared = " + leftModel.compared + '\n'
        + "  .diffType = " + DiffTypes.name(leftModel.diffType) + '\n'
        + "rightModel:\n"
        + "  .compared = " + rightModel.compared + '\n'
        + "  .diffType = " + DiffTypes.name(rightModel.diffType) + '\n'
        + "time: " + time + "s\n";
    System.out.print(r);
  }
}
