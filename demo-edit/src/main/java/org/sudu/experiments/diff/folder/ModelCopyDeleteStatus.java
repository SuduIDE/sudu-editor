package org.sudu.experiments.diff.folder;

import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.IdentityHashMap;
import java.util.function.Consumer;
import java.util.function.BiConsumer;

public class ModelCopyDeleteStatus {

  int inWork, inTraverse;
  final WorkerJobExecutor executor;
  final Consumer<String> onError;
  final Consumer<int[]> sendStatus;
  final BiConsumer<ItemFolderDiffModel, Runnable> readFolder;
  final BiConsumer<ItemFolderDiffModel, Runnable> compareFolders;
  Runnable onComplete;
  public int insertedFiles, rewroteFiles;
  public int copiedDirs;
  public int deletedFiles, deletedDirs;

  final boolean syncOrphans;  // true -> remove orphan items on copy
  final boolean syncExcluded; // true -> copy excluded items (for files only now, wip for folders)

  final IdentityHashMap<ItemFolderDiffModel, Integer> markedForDelete;

  long lastSendStatusTime = System.currentTimeMillis();

  final long SEND_STATUS_MS = 2000;

  public ModelCopyDeleteStatus(
      WorkerJobExecutor executor,
      Consumer<int[]> sendStatus,
      Consumer<String> onError,
      BiConsumer<ItemFolderDiffModel, Runnable> readFolder,
      BiConsumer<ItemFolderDiffModel, Runnable> compareFolders,
      boolean syncOrphans,
      boolean syncExcluded
  ) {
    this.executor = executor;
    this.sendStatus = sendStatus;
    this.onError = onError;
    this.readFolder = readFolder;
    this.compareFolders = compareFolders;
    markedForDelete = new IdentityHashMap<>();
    this.syncOrphans = syncOrphans;
    this.syncExcluded = syncExcluded;
  }

  public void onTraversed() {
    inTraverse--;
    onComplete();
  }

  public void onFileCopied(boolean inserted) {
    inWork--;
    if (inserted) insertedFiles++;
    else rewroteFiles++;
    onComplete();
  }

  public void onDirCopied() {
    inWork--;
    copiedDirs++;
    onComplete();
  }

  public void onFileDeleted(ItemFolderDiffModel file) {
    inWork--;
    deletedFiles++;
    onChildDeleted(file);
    onComplete();
  }

  public void onDirDeleted(ItemFolderDiffModel dir) {
    deletedDirs++;
    removeMarked(dir);
    onChildDeleted(dir);
    onComplete();
  }

  public void onChildDeleted(ItemFolderDiffModel model) {
    model.deleteItem();
    var parent = model.parent();
    if (parent == null) return;
    parent.updateItemOnDelete();
    if (!marked(parent)) return;
    int count = markedForDelete.get(parent) - 1;
    markedForDelete.put(parent, count);
    if (count == 0) parent.removeEmptyFolder(this);
  }

  public void onComplete() {
    if (inWork == 0 && inTraverse == 0 && markedForDelete.isEmpty()) onComplete.run();
    else {
      long currentTime = System.currentTimeMillis();
      if (currentTime - lastSendStatusTime > SEND_STATUS_MS) {
        lastSendStatusTime = currentTime;
        sendStatus.accept(new int[]{copiedDirs, copiedFiles(), deletedDirs, deletedFiles});
      }
    }
  }

  public void onFolderDeleteError(ItemFolderDiffModel folder, String error) {
    onError.accept(error);
    unmarkParentsForDelete(folder);
    onComplete();
  }

  public void onFileDeleteError(ItemFolderDiffModel file, String error) {
    inWork--;
    onError.accept(error);
    unmarkParentsForDelete(file.parent());
    onComplete();
  }

  public void onCopyError(String error) {
    inWork--;
    onError.accept("onCopyError: " + error);
    onComplete();
  }

  private void unmarkParentsForDelete(ItemFolderDiffModel folder) {
    if (marked(folder)) {
      removeMarked(folder);
      if (folder.parent() != null) unmarkParentsForDelete(folder.parent());
    }
  }

  public void markForDelete(ItemFolderDiffModel model) {
    markedForDelete.put(model, model.children.length);
  }

  public void removeMarked(ItemFolderDiffModel model) {
    markedForDelete.remove(model);
  }

  public boolean marked(ItemFolderDiffModel model) {
    return markedForDelete.containsKey(model);
  }

  public void setOnComplete(Runnable onComplete) {
    this.onComplete = onComplete;
  }

  public int copiedFiles() {
    return insertedFiles + rewroteFiles;
  }
}
