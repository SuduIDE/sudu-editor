package org.sudu.experiments.update;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.ItemKind;
import org.sudu.experiments.diff.SizeScanner;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.editor.worker.ArgsCast;
import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.worker.ArrayView;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.*;

public class Collector {

  private final FolderDiffModel leftAcc, rightAcc;
  private final WorkerJobExecutor executor;
  private final boolean scanFileContent;

  private Runnable onComplete;
  private Runnable update;

  private int inComparing = 0;

  public Collector(
      FolderDiffModel left,
      FolderDiffModel right,
      boolean scanFileContent,
      WorkerJobExecutor executor
  ) {
    this.leftAcc = left;
    this.rightAcc = right;
    this.executor = executor;
    this.scanFileContent = scanFileContent;
  }

  public void beginCompare(FsItem leftItem, FsItem rightItem) {
    compare(leftAcc, rightAcc, leftItem, rightItem);
  }

  public void compare(
      FolderDiffModel leftModel,
      FolderDiffModel rightModel,
      FsItem leftItem,
      FsItem rightItem
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

  public static void setChildren(FolderDiffModel parent, FsItem[] paths) {
    int len = paths.length;
    parent.children = new FolderDiffModel[paths.length];
    parent.childrenComparedCnt = 0;
    for (int i = 0; i < len; i++) {
      parent.children[i] = new FolderDiffModel(parent);
      int kind = paths[i] instanceof DirectoryHandle
          ? ItemKind.FOLDER
          : ItemKind.FILE;
      parent.child(i).setItemKind(kind);
    }
    if (len == 0) parent.itemCompared();
  }

  public void compareFolders(
      FolderDiffModel leftModel,
      FolderDiffModel rightModel,
      DirectoryHandle leftDir,
      DirectoryHandle rightDir
  ) {
    executor.sendToWorker(
        result -> onFoldersCompared(leftModel, rightModel, result),
        DiffUtils.CMP_FOLDERS,
        leftDir, rightDir
    );
  }

  private void onFoldersCompared(
      FolderDiffModel leftModel,
      FolderDiffModel rightModel,
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
    boolean needUpdate = false;
    int lP = 0, rP = 0;
    while (changed) {
      changed = false;
      while (lP < leftLen && rP < rightLen &&
          leftDiff[lP] == DiffTypes.DEFAULT &&
          rightDiff[rP] == DiffTypes.DEFAULT
      ) {
        changed = true;
        compare(leftModel.child(lP), rightModel.child(rP), leftItem[lP], rightItem[rP]);
        lP++;
        rP++;
      }
      if (changed) continue;
      while (lP < leftLen && leftDiff[lP] == DiffTypes.DELETED) {
        changed = true;
        leftModel.child(lP).markDown(DiffTypes.DELETED);
        leftModel.child(lP).itemCompared();
        lP++;
      }
      if (changed) {
        leftModel.markUp(DiffTypes.EDITED);
        rightModel.markUp(DiffTypes.EDITED);
        needUpdate = true;
        continue;
      }
      while (rP < rightLen && rightDiff[rP] == DiffTypes.INSERTED) {
        changed = true;
        rightModel.child(rP).itemCompared();
        rightModel.child(rP).markDown(DiffTypes.INSERTED);
        rP++;
      }
      if (changed) {
        leftModel.markUp(DiffTypes.EDITED);
        rightModel.markUp(DiffTypes.EDITED);
        needUpdate = true;
      }
    }
    onItemCompared(needUpdate);
  }

  public void compareFiles(
      FolderDiffModel leftModel,
      FolderDiffModel rightModel,
      FileHandle leftFile,
      FileHandle rightFile
  ) {
    if (scanFileContent) {
      executor.sendToWorker(
          result -> onFilesCompared(leftModel, rightModel, result),
          DiffUtils.CMP_FILES,
          leftFile, rightFile
      );
    } else {
      new SizeScanner(leftFile, rightFile) {
        @Override
        protected void onComplete(int sizeL, int sizeR) {
          onFilesCompared(leftModel, rightModel, sizeL == sizeR);
        }
      };
    }
  }

  private void onFilesCompared(
      FolderDiffModel leftModel,
      FolderDiffModel rightModel,
      Object[] result
  ) {
    boolean equals = ArgsCast.intArray(result, 0)[0] == 1;
    onFilesCompared(leftModel, rightModel, equals);
  }

  private void onFilesCompared(
      FolderDiffModel leftModel,
      FolderDiffModel rightModel,
      boolean equals
  ) {
    if (!equals) {
      leftModel.markUp(DiffTypes.EDITED);
      rightModel.markUp(DiffTypes.EDITED);
    }
    boolean needUpdate = false;
    needUpdate |= leftModel.itemCompared();
    needUpdate |= rightModel.itemCompared();
    onItemCompared(needUpdate);
  }

  private void onItemCompared(boolean needUpdate) {
    if (--inComparing < 0) throw new IllegalStateException("inComparing cannot be negative");
    if (inComparing == 0) onComplete.run();
    else if (needUpdate) update.run();
  }

  public void setUpdate(Runnable update) {
    this.update = update;
  }

  public void setOnComplete(Runnable onComplete) {
    this.onComplete = onComplete;
  }
}
