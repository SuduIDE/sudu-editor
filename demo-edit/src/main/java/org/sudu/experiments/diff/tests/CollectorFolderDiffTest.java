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

  private void onComplete(int[] stats) {
    onComplete.run();
    dumpResult(stats);
    running = false;
  }

  private void dumpResult(int[] stats) {
    int foldersCompared = stats[0],
        filesCompared = stats[1],
        leftFiles = stats[2],
        leftFolders = stats[3],
        rightFiles = stats[4],
        rightFolders = stats[5],
        filesInserted = stats[6],
        filesDeleted = stats[7],
        filesEdited = stats[8];
    String r = ""
        + "leftModel:\n"
        + "  .compared = " + root.isCompared() + '\n'
        + "  .diffType = " + DiffTypes.name(root.getDiffType()) + '\n'
        + "rightModel:\n"
        + "  .compared = " + root.isCompared() + '\n'
        + "  .diffType = " + DiffTypes.name(root.getDiffType()) + '\n'
        + "Total folders compared: " + foldersCompared + '\n'
        + "  - total left = " + leftFolders + '\n'
        + "  - total right = " + rightFolders + '\n'
        + "Total files compared: " + filesCompared + '\n'
        + "  - total left = " + leftFiles + '\n'
        + "  - total right = " + rightFiles + '\n'
        + "  - inserted = " + filesInserted + '\n'
        + "  - deleted = " + filesDeleted + '\n'
        + "  - edited = " + filesEdited + '\n'
        + "time: " + time + "s\n";
    System.out.print(r);
  }
}
