package org.sudu.experiments.diff.update;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.diff.folder.RangeCtx;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.ui.fs.FileDiffHandler;
import org.sudu.experiments.ui.fs.FileDiffUpdateHandler;
import org.sudu.experiments.ui.fs.FolderDiffHandler;
import org.sudu.experiments.ui.fs.FolderDiffUpdateHandler;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Collector {

  private final RangeCtx rangeCtx;
  private final FolderDiffModel leftAcc, rightAcc;
  private final Consumer<Object[]> r;

  public Collector(FolderDiffModel left, FolderDiffModel right, Consumer<Object[]> r) {
    this.leftAcc = left;
    this.rightAcc = right;
    this.rangeCtx = new RangeCtx();
    this.r = r;
  }

  public static final String COLLECT = "asyncCollector.collect";

  public static void collect(
      FsItem leftItem, FsItem rightItem,
      Consumer<Object[]> r
  ) {
    var leftAcc = new FolderDiffModel(null);
    var rightAcc = new FolderDiffModel(null);
    Collector collector = new Collector(leftAcc, rightAcc, r);
    collector.compare(leftAcc, rightAcc, leftItem, rightItem);
  }

  private void collect(
      FolderDiffModel leftModel, FolderDiffModel rightModel,
      FsItem leftItem, FsItem rightItem
  ) {
    compare(leftModel, rightModel, leftItem, rightItem);
  }

  private void compare(
      FolderDiffModel leftModel, FolderDiffModel rightModel,
      FsItem leftItem, FsItem rightItem
  ) {
    if (leftItem instanceof DirectoryHandle leftDir &&
        rightItem instanceof DirectoryHandle rightDir
    ) collectFolders(leftModel, rightModel, leftDir, rightDir);
    else if (leftItem instanceof FileHandle leftFile
        && rightItem instanceof FileHandle rightFile
    ) collectFiles(leftModel, rightModel, leftFile, rightFile);
    else throw new IllegalArgumentException();
  }

  public void collectFolders(
      FolderDiffModel leftModel, FolderDiffModel rightModel,
      DirectoryHandle leftDir, DirectoryHandle rightDir
  ) {
    FolderDiffHandler handler = new FolderDiffUpdateHandler(
        leftDir, rightDir,
        leftModel, rightModel,
        rangeCtx, this::collect,
        this::onItemCompared
    );
    handler.read();
  }

  public void collectFiles(
      FolderDiffModel leftModel, FolderDiffModel rightModel,
      FileHandle leftFile, FileHandle rightFile
  ) {
    FileDiffHandler handler = new FileDiffUpdateHandler(
        leftFile, rightFile,
        leftModel, rightModel,
        rangeCtx, this::onItemCompared
    );
    handler.beginCompare();
  }

  private void onItemCompared() {
    if (leftAcc.compared && rightAcc.compared) onFullyCompared();
  }

  private void onFullyCompared() {
    var leftInts = FolderDiffModel.toInts(leftAcc);
    var rightInts = FolderDiffModel.toInts(rightAcc);
    ArrayList<Object> result = new ArrayList<>();
    result.add(leftInts);
    result.add(rightInts);
    ArrayOp.sendArrayList(result, r);
  }
}
