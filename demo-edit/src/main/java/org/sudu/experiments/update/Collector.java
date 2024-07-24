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

// TODO Remove copypasta
public class Collector {

  protected int foldersCompared = 0, filesCompared = 0;
  protected DiffModelUpdater.Listener onComplete;

  private final FolderDiffModel root;
  private final WorkerJobExecutor executor;
  private final boolean scanFileContent;

  private Runnable update;

  private int inComparing = 0;

  public Collector(
      FolderDiffModel root,
      boolean scanFileContent,
      WorkerJobExecutor executor
  ) {
    this.root = root;
    this.executor = executor;
    this.scanFileContent = scanFileContent;
  }

  public void beginCompare(FsItem leftItem, FsItem rightItem) {
    compare(root, leftItem, rightItem);
  }

  public void compare(
      FolderDiffModel root,
      FsItem leftItem,
      FsItem rightItem
  ) {
    ++inComparing;
    if (leftItem instanceof DirectoryHandle leftDir &&
        rightItem instanceof DirectoryHandle rightDir
    ) compareFolders(root, leftDir, rightDir);
    else if (leftItem instanceof FileHandle leftFile
        && rightItem instanceof FileHandle rightFile
    ) compareFiles(root, leftFile, rightFile);
    else throw new IllegalArgumentException();
  }

  public void compareFolders(
      FolderDiffModel root,
      DirectoryHandle leftDir,
      DirectoryHandle rightDir
  ) {
    executor.sendToWorker(
        result -> onFoldersCompared(root, result),
        DiffUtils.CMP_FOLDERS,
        leftDir, rightDir
    );
  }

  private void onFoldersCompared(
      FolderDiffModel model,
      Object[] result
  ) {
    foldersCompared++;
    int[] ints = ((ArrayView) result[0]).ints();

    int commonLen = ints[0];
    int leftLen = ints[1];
    int rightLen = ints[2];

    int[] diffs = Arrays.copyOfRange(ints, 3, 3 + commonLen);
    FsItem[] leftItem = Arrays.copyOfRange(result, 1, 1 + leftLen, FsItem[].class);
    FsItem[] rightItem = Arrays.copyOfRange(result, 1 + leftLen, 1 + leftLen + rightLen, FsItem[].class);

    int len = diffs.length;
    model.children = new FolderDiffModel[len];
    model.childrenComparedCnt = 0;

    int lP = 0, rP = 0;
    int mP = 0;
    boolean edited = false;
    boolean needUpdate = false;

    while (mP < len) {
      if (diffs[mP] == DiffTypes.DELETED) {
        edited = true;
        model.children[mP] = new FolderDiffModel(model);
        int kind = leftItem[lP] instanceof DirectoryHandle
            ? ItemKind.FOLDER
            : ItemKind.FILE;
        model.child(mP).setItemKind(kind);
        model.child(mP).setDiffType(DiffTypes.DELETED);
        model.child(mP).markDown(DiffTypes.DELETED);
        model.child(mP).itemCompared();
        mP++;
        lP++;
      } else if (diffs[mP] == DiffTypes.INSERTED) {
        edited = true;
        model.children[mP] = new FolderDiffModel(model);
        int kind = rightItem[rP] instanceof DirectoryHandle
            ? ItemKind.FOLDER
            : ItemKind.FILE;
        model.child(mP).setItemKind(kind);
        model.child(mP).setDiffType(DiffTypes.INSERTED);
        model.child(mP).markDown(DiffTypes.INSERTED);
        model.child(mP).itemCompared();
        mP++;
        rP++;
      } else {
        model.children[mP] = new FolderDiffModel(model);
        int kind = leftItem[lP] instanceof DirectoryHandle
            ? ItemKind.FOLDER
            : ItemKind.FILE;
        model.child(mP).setItemKind(kind);
        compare(model.child(mP), leftItem[lP], rightItem[rP]);
        mP++;
        lP++;
        rP++;
      }
    }
    if (len == 0) model.itemCompared();
    if (edited) {
      model.markUp(DiffTypes.EDITED);
      needUpdate = model.getDiffType() == DiffTypes.DEFAULT;
    }
    onItemCompared(needUpdate);
  }

  public void compareFiles(
      FolderDiffModel model,
      FileHandle leftFile,
      FileHandle rightFile
  ) {
    if (scanFileContent) {
      executor.sendToWorker(
          result -> onFilesCompared(model, result),
          DiffUtils.CMP_FILES,
          leftFile, rightFile
      );
    } else {
      new SizeScanner(leftFile, rightFile) {
        @Override
        protected void onComplete(int sizeL, int sizeR) {
          onFilesCompared(model, sizeL == sizeR);
        }
      };
    }
  }

  private void onFilesCompared(
      FolderDiffModel model,
      Object[] result
  ) {
    filesCompared++;
    boolean equals = ArgsCast.intArray(result, 0)[0] == 1;
    onFilesCompared(model, equals);
  }

  private void onFilesCompared(
      FolderDiffModel model,
      boolean equals
  ) {
    if (!equals) model.markUp(DiffTypes.EDITED);
    onItemCompared(model.itemCompared());
  }

  private void onItemCompared(boolean needUpdate) {
    if (--inComparing < 0) throw new IllegalStateException("inComparing cannot be negative");
    if (inComparing == 0) onComplete.onComplete(foldersCompared, filesCompared);
    else if (needUpdate) update.run();
  }

  public void setUpdate(Runnable update) {
    this.update = update;
  }

  public void setOnComplete(DiffModelUpdater.Listener onComplete) {
    this.onComplete = onComplete;
  }
}
