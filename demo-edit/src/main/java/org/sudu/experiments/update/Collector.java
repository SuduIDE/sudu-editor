package org.sudu.experiments.update;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.diff.folder.RangeCtx;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.ui.fs.FileDiffHandler;
import org.sudu.experiments.ui.fs.FileDiffUpdateHandler;
import org.sudu.experiments.ui.fs.FolderDiffHandler;
import org.sudu.experiments.ui.fs.FolderDiffUpdateHandler;

import java.util.*;
import java.util.function.Consumer;

public class Collector {

  private final RangeCtx rangeCtx;
  private final RemoteFolderDiffModel leftAcc, rightAcc;
  private final Consumer<Object[]> r;

  private static final int MAX_DEPTH = Integer.MAX_VALUE;
  private int inComparing = 0;

  public Collector(
      RemoteFolderDiffModel left,
      RemoteFolderDiffModel right,
      Consumer<Object[]> r
  ) {
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
    var leftAcc = new RemoteFolderDiffModel(null, leftItem.getName());
    var rightAcc = new RemoteFolderDiffModel(null, rightItem.getName());
    Collector collector = new Collector(leftAcc, rightAcc, r);
    collector.compare(leftAcc, rightAcc, leftItem, rightItem);
  }

  private void collect(CollectDto dto) {
    if (!depthCheck(dto.leftModel, dto.rightModel))
      compare(dto.leftModel, dto.rightModel, dto.leftItem, dto.rightItem);
  }

  private void compare(
      RemoteFolderDiffModel leftModel, RemoteFolderDiffModel rightModel,
      FsItem leftItem, FsItem rightItem
  ) {
    ++inComparing;
    if (leftItem instanceof DirectoryHandle leftDir &&
        rightItem instanceof DirectoryHandle rightDir
    ) compareFolders(leftModel, rightModel, leftDir, rightDir);
    else if (leftItem instanceof FileHandle leftFile
        && rightItem instanceof FileHandle rightFile
    ) compareFiles(leftModel, rightModel, leftFile, rightFile);
    else throw new IllegalArgumentException();
  }

  public void compareFolders(
      RemoteFolderDiffModel leftModel, RemoteFolderDiffModel rightModel,
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

  public void compareFiles(
      RemoteFolderDiffModel leftModel,
      RemoteFolderDiffModel rightModel,
      FileHandle leftFile,
      FileHandle rightFile
  ) {
    FileDiffHandler handler = new FileDiffUpdateHandler(
        leftFile, rightFile,
        leftModel, rightModel,
        rangeCtx, this::onItemCompared
    );
    handler.beginCompare();
  }

  private void onItemCompared() {
    if (--inComparing < 0) throw new IllegalStateException();
    if (leftAcc.compared && rightAcc.compared) {
      if (inComparing != 0) throw new IllegalStateException();
      else onFullyCompared();
    }
    if (inComparing == 0) onFullyCompared();
  }

  private void onFullyCompared() {
    ArrayList<Object> result = new ArrayList<>();
    result.add(null);
    var ints = UpdateDto.toInts(leftAcc, rightAcc, result);
    result.set(0, ints);
    ArrayOp.sendArrayList(result, r);
  }

  private boolean depthCheck(RemoteFolderDiffModel left, RemoteFolderDiffModel right) {
    if (left.depth != right.depth || left.depth > MAX_DEPTH) throw new IllegalStateException();
    return left.depth == MAX_DEPTH;
  }
}
