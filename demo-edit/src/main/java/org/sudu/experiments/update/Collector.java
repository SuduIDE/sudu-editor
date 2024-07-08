package org.sudu.experiments.update;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.diff.folder.RangeCtx;
import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.ui.fs.TreeS;
import org.sudu.experiments.worker.ArrayView;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.*;
import java.util.function.Consumer;

public class Collector {

  private final RangeCtx rangeCtx;
  private final RemoteFolderDiffModel leftAcc, rightAcc;
  private final WorkerJobExecutor executor;
  private final Consumer<Object[]> r;

  private static final int MAX_DEPTH = Integer.MAX_VALUE;
  private int inComparing = 0;

  public Collector(
      RemoteFolderDiffModel left,
      RemoteFolderDiffModel right,
      WorkerJobExecutor executor,
      Consumer<Object[]> r
  ) {
    this.leftAcc = left;
    this.rightAcc = right;
    this.rangeCtx = new RangeCtx();
    this.executor = executor;
    this.r = r;
  }

  public void beginCompare(FsItem leftItem, FsItem rightItem) {
    compare(leftAcc, rightAcc, leftItem, rightItem);
  }

  public void compare(
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

  public static void setChildren(RemoteFolderDiffModel parent, FsItem[] paths) {
    int len = paths.length;
    parent.children = new RemoteFolderDiffModel[paths.length];
    parent.childrenComparedCnt = 0;
    for (int i = 0; i < len; i++) {
      parent.children[i] = new RemoteFolderDiffModel(parent, paths[i].getName());
      parent.child(i).setIsFile(paths[i] instanceof FileHandle);
    }
    if (len == 0) parent.itemCompared();
  }

  public static void setChildren(FolderDiffModel parent, TreeS[] paths) {
    int len = paths.length;
    parent.children = new RemoteFolderDiffModel[paths.length];
    parent.childrenComparedCnt = 0;
    for (int i = 0; i < len; i++) {
      parent.children[i] = new RemoteFolderDiffModel(parent, paths[i].name);
      parent.child(i).setIsFile(!paths[i].isFolder);
    }
    if (len == 0) parent.itemCompared();
  }

  public void compareFolders(
      RemoteFolderDiffModel leftModel, RemoteFolderDiffModel rightModel,
      DirectoryHandle leftDir, DirectoryHandle rightDir
  ) {
    executor.sendToWorker(
        result -> onFoldersCompared(leftModel, rightModel, result),
        DiffUtils.CMP_FOLDERS,
        leftDir, rightDir
    );
  }

  private void onFoldersCompared(
      RemoteFolderDiffModel leftModel, RemoteFolderDiffModel rightModel,
      Object[] result
  ) {
    if (result.length == 0) return;
    int[] ints = ((ArrayView) result[0]).ints();
    int leftLen = ints[0], rightLen = ints[1];
    int[] leftDiff = Arrays.copyOfRange(ints, 2, 2 + leftLen);
    int[] rightDiff = Arrays.copyOfRange(ints, 2 + leftLen, 2 + leftLen + rightLen);
    FsItem[] leftItem = Arrays.copyOfRange(result, 1, 1 + leftLen, FsItem[].class);
    FsItem[] rightItem = Arrays.copyOfRange(result, 1 + leftLen, 1 + leftLen + rightLen, FsItem[].class);

    setChildren(leftModel, leftItem);
    setChildren(rightModel, rightItem);

    boolean changed = true;
    int lP = 0, rP = 0;
    while (changed) {
      changed = false;
      while (lP < leftLen && rP < rightLen &&
          leftDiff[lP] == DiffTypes.DEFAULT &&
          rightDiff[rP] == DiffTypes.DEFAULT
      ) {
        int id = rangeCtx.nextId();
        changed = true;
        leftModel.child(lP).rangeId = id;
        rightModel.child(rP).rangeId = id;
        compare(leftModel.child(lP), rightModel.child(rP), leftItem[lP], rightItem[rP]);
        lP++;
        rP++;
      }
      if (changed) continue;
      int id = rangeCtx.nextId();
      while (lP < leftLen && leftDiff[lP] == DiffTypes.DELETED) {
        changed = true;
        leftModel.child(lP).setDiffType(DiffTypes.DELETED);
        leftModel.child(lP).rangeId = id;
        if (!leftModel.child(lP).isFile()) {
          var readDto = new ReadDto(leftModel.child(lP), (DirectoryHandle) leftItem[lP], DiffTypes.DELETED, id);
          readFolder(readDto);
        } else {
          leftModel.child(lP).itemCompared();
        }
        lP++;
      }
      if (changed) {
        rangeCtx.markUp(leftModel, rightModel);
        continue;
      }
      while (rP < rightLen && rightDiff[rP] == DiffTypes.INSERTED) {
        changed = true;
        rightModel.child(rP).setDiffType(DiffTypes.INSERTED);
        rightModel.child(rP).rangeId = id;
        if (!rightModel.child(rP).isFile()) {
          var readDto = new ReadDto(rightModel.child(rP), (DirectoryHandle) rightItem[rP], DiffTypes.INSERTED, id);
          readFolder(readDto);
        } else {
          rightModel.child(rP).itemCompared();
        }
        rP++;
      }
      if (changed) rangeCtx.markUp(leftModel, rightModel);
    }
    onItemCompared();
  }

  public void compareFiles(
      RemoteFolderDiffModel leftModel,
      RemoteFolderDiffModel rightModel,
      FileHandle leftFile,
      FileHandle rightFile
  ) {
    executor.sendToWorker(
        result -> onFilesCompared(leftModel, rightModel, result),
        DiffUtils.CMP_FILES,
        leftFile, rightFile
    );
  }

  private void onFilesCompared(
      RemoteFolderDiffModel leftModel,
      RemoteFolderDiffModel rightModel,
      Object[] result
  ) {
    boolean equals = ((ArrayView) result[0]).ints()[0] == 1;
    if (!equals) {
      int rangeId = rangeCtx.nextId();
      leftModel.rangeId = rangeId;
      rightModel.rangeId = rangeId;
      rangeCtx.markUp(leftModel, rightModel);
    }
    leftModel.itemCompared();
    rightModel.itemCompared();
    onItemCompared();
  }

  private void readFolder(ReadDto readDto) {
    ++inComparing;
    executor.sendToWorker(
        result -> onFolderRead(readDto.model, result),
        DiffUtils.READ_FOLDER,
        readDto.dirHandle, new int[] {readDto.diffType, readDto.rangeId}
    );
  }

  private void onFolderRead(
      RemoteFolderDiffModel model,
      Object[] result
  ) {
    int[] ints = ((ArrayView) result[0]).ints();
    String[] paths = new String[result.length - 1];
    for (int i = 0; i < paths.length; i++) paths[i] = (String) result[i + 1];
    var updModel = RemoteFolderDiffModel.fromInts(ints, paths);
    model.update(updModel);
    onItemCompared();
  }

  private void onItemCompared() {
    if (--inComparing < 0) throw new IllegalStateException("inComparing cannot be negative");
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
