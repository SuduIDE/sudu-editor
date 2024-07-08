package org.sudu.experiments.diff.tests;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.ui.fs.DirectoryNode;
import org.sudu.experiments.update.Collector;
import org.sudu.experiments.update.UpdateDto;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.function.DoubleSupplier;

public class CollectorFolderDiffTest extends DirectoryTest {

  final DoubleSupplier time;

  private final boolean content;

  boolean running = true;
  DirectoryNode leftRoot, rightRoot;
  RemoteFolderDiffModel leftModel;
  RemoteFolderDiffModel rightModel;

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
    leftModel = new RemoteFolderDiffModel(null, dir1.getName());
    rightModel = new RemoteFolderDiffModel(null, dir2.getName());
    var collector = new Collector(leftModel, rightModel, executor, this::onComplete);
    collector.beginCompare(dir1, dir2);
  }

  public boolean running() { return running; }

  private void onComplete(Object[] result) {
    var updDto = UpdateDto.fromInts((int[]) result[0], result);
    System.out.println(updDto.leftRoot.infoString());
    System.out.println(updDto.rightRoot.infoString());
    onComplete.run();
    dumpResult();
    running = false;
  }

  private void dumpResult() {
    String r = ""
        + "leftModel:\n"
        + "  .compared = " + leftModel.isCompared() + '\n'
        + "  .diffType = " + DiffTypes.name(leftModel.getDiffType()) + '\n'
        + "rightModel:\n"
        + "  .compared = " + rightModel.isCompared() + '\n'
        + "  .diffType = " + DiffTypes.name(rightModel.getDiffType()) + '\n'
        + "time: " + time + "s\n";
    System.out.print(r);
  }
}
