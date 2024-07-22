package org.sudu.experiments.diff.tests;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.ui.fs.DirectoryNode;
import org.sudu.experiments.update.Collector;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.function.DoubleSupplier;

public class CollectorFolderDiffTest extends DirectoryTest {

  final DoubleSupplier time;

  private final boolean content;

  boolean running = true;
  DirectoryNode leftRoot, rightRoot;
  FolderDiffModel root;

  public CollectorFolderDiffTest(
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
    root = new FolderDiffModel(null);
    var collector = new Collector(
        root,
        content,
        executor
    );
    collector.setUpdate(() -> {});
    collector.setOnComplete(this::onComplete);
    collector.beginCompare(dir1, dir2);
  }

  public boolean running() { return running; }

  private void onComplete() {
    onComplete.run();
    dumpResult();
    running = false;
  }

  private void dumpResult() {
    String r = ""
        + "leftModel:\n"
        + "  .compared = " + root.isCompared() + '\n'
        + "  .diffType = " + DiffTypes.name(root.getDiffType()) + '\n'
        + "rightModel:\n"
        + "  .compared = " + root.isCompared() + '\n'
        + "  .diffType = " + DiffTypes.name(root.getDiffType()) + '\n'
        + "time: " + time + "s\n";
    System.out.print(r);
  }
}
