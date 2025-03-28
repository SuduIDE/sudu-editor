package org.sudu.experiments.update;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.editor.worker.FileCompare;
import org.sudu.experiments.editor.worker.SizeScanner;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.diff.folder.ItemFolderDiffModel;
import org.sudu.experiments.editor.worker.ArgsCast;
import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.worker.ArrayView;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.*;
import java.util.function.Consumer;

// TODO Remove copypasta
public class Collector {

  protected int foldersCompared = 0, filesCompared = 0;
  protected int leftFiles = 0, leftFolders = 0;
  protected int rightFiles = 0, rightFolders = 0;
  protected int filesInserted = 0, filesDeleted = 0, filesEdited = 0;

  protected DiffModelUpdater.Listener onComplete;
  protected Consumer<String> onError = System.err::println;

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
    leftFolders++;
    rightFolders++;
    int[] ints = ((ArrayView) result[0]).ints();
    if (isErrorInts(ints)) {
      String error = (String) result[1];
      onError(error);
      return;
    }
    int commonLen = ints[0];
    int leftLen = ints[1];
    int rightLen = ints[2];

    int[] diffs = Arrays.copyOfRange(ints, 3, 3 + commonLen);
    int[] kinds = Arrays.copyOfRange(ints, 3 + commonLen, 3 + 2 * commonLen);
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
      int kind = kinds[mP];
      if (diffs[mP] == DiffTypes.DELETED) {
        edited = true;
        model.children[mP] = new FolderDiffModel(model);
        model.child(mP).posInParent = mP;
        model.child(mP).setItemKind(kind);
        model.child(mP).setDiffType(DiffTypes.DELETED);
        model.child(mP).markDown(DiffTypes.DELETED);
        read(model.child(mP), leftItem[lP]);
        mP++;
        lP++;
      } else if (diffs[mP] == DiffTypes.INSERTED) {
        edited = true;
        model.children[mP] = new FolderDiffModel(model);
        model.child(mP).posInParent = mP;
        model.child(mP).setItemKind(kind);
        model.child(mP).setDiffType(DiffTypes.INSERTED);
        model.child(mP).markDown(DiffTypes.INSERTED);
        read(model.child(mP), rightItem[rP]);
        mP++;
        rP++;
      } else {
        model.children[mP] = new FolderDiffModel(model);
        model.child(mP).posInParent = mP;
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
      FileCompare.asyncCompareFiles(executor, leftFile, rightFile,
          (equals, error) -> onFilesCompared(model, equals, error));
    } else {
      SizeScanner.scan(executor, leftFile, rightFile,
          (sizeL, sizeR, error) ->
              onFilesCompared(model, sizeL == sizeR, error));
    }
  }

  private void onFilesCompared(
      FolderDiffModel model,
      boolean equals, String error
  ) {
    if (error != null)
      System.err.println(error);
    leftFiles++;
    rightFiles++;
    filesCompared++;
    if (!equals) {
      filesEdited++;
      model.markUp(DiffTypes.EDITED);
    }
    onItemCompared(model.itemCompared());
  }

  private void read(FolderDiffModel model, FsItem handle) {
    if (handle instanceof DirectoryHandle dirHandle) readFolder(model, dirHandle);
    else {
      if (model.getDiffType() == DiffTypes.INSERTED) {
        filesInserted++;
        rightFiles++;
      } else if (model.getDiffType() == DiffTypes.DELETED) {
        filesDeleted++;
        leftFiles++;
      }
      filesCompared++;
      model.itemCompared();
    }
  }

  private void readFolder(FolderDiffModel model, DirectoryHandle dirHandle) {
    ++inComparing;
    executor.sendToWorker(
        result -> onFolderRead(model, result),
        DiffUtils.READ_FOLDER,
        dirHandle, new int[]{model.getDiffType(), model.getItemKind(), 0}
    );
  }

  private void onFolderRead(
      FolderDiffModel model,
      Object[] result
  ) {
    int[] ints = ArgsCast.intArray(result, 0);
    if (isErrorInts(ints)) {
      String error = ArgsCast.string(result, 1);
      onError(error);
    }
    int[] stats = ArgsCast.intArray(result, 1);
    String[] paths = new String[stats[0]];
    FsItem[] items = new FsItem[stats[1]];
    int foldersRead = stats[2];
    int filesRead = stats[3];
    foldersCompared += foldersRead;
    filesCompared += filesRead;
    if (model.getDiffType() == DiffTypes.INSERTED) {
      filesInserted += filesRead;
      rightFiles += filesRead;
      rightFolders += foldersRead;
    } else if (model.getDiffType() == DiffTypes.DELETED) {
      filesDeleted += filesRead;
      leftFiles += filesRead;
      leftFolders += foldersRead;
    }
    for (int i = 0; i < stats[0]; i++) paths[i] = (String) result[i + 2];
    for (int i = 0; i < stats[1]; i++) items[i] = (FsItem) result[stats[0] + i + 2];
    var updModel = ItemFolderDiffModel.fromInts(ints, paths, items);
    model.update(updModel);
    onItemCompared(false);
  }

  private void onItemCompared(boolean needUpdate) {
    if (--inComparing < 0) throw new IllegalStateException("inComparing cannot be negative");
    if (inComparing == 0) onComplete.onComplete(mkStatInts());
    else if (needUpdate) update.run();
  }

  public void setUpdate(Runnable update) {
    this.update = update;
  }

  public void setOnComplete(DiffModelUpdater.Listener onComplete) {
    this.onComplete = onComplete;
  }

  public void setOnError(Consumer<String> onError) {
    this.onError = onError;
  }

  private void onError(String error) {
    onError.accept(error);
  }

  private boolean isErrorInts(int[] ints) {
    return ints.length == 1 && ints[0] == -1;
  }

  public int[] mkStatInts() {
    return new int[] {
        foldersCompared, filesCompared,
        leftFiles, leftFolders,
        rightFiles, rightFolders,
        filesInserted, filesDeleted, filesEdited
    };
  }
}
