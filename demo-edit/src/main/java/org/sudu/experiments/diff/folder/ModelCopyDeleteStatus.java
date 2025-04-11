package org.sudu.experiments.diff.folder;

import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.IdentityHashMap;
import java.util.function.Consumer;

public class ModelCopyDeleteStatus {

  int inWork, inTraverse;
  final WorkerJobExecutor executor;
  final Runnable onComplete;
  final Consumer<String> onError;

  final IdentityHashMap<ItemFolderDiffModel, Integer> markedForDelete;

  public ModelCopyDeleteStatus(
      WorkerJobExecutor executor,
      Runnable onComplete,
      Consumer<String> onError
  ) {
    this.executor = executor;
    this.onComplete = onComplete;
    this.onError = onError;
    markedForDelete = new IdentityHashMap<>();
  }

  public void onTraversed() {
    inTraverse--;
    onComplete();
  }

  public void onCopied() {
    inWork--;
    onComplete();
  }

  public void onFileDeleted(ItemFolderDiffModel file) {
    inWork--;
    onChildDeleted(file);
    onComplete();
  }

  public void onDirDeleted(ItemFolderDiffModel dir) {
    removeMarked(dir);
    onChildDeleted(dir);
    onComplete();
  }

  public void onChildDeleted(ItemFolderDiffModel model) {
    model.deleteItem();
    var parent = model.parent();
    if (!marked(parent)) return;
    int count = markedForDelete.get(parent) - 1;
    markedForDelete.put(parent, count);
    if (count == 0) parent.removeEmptyFolder(this);
  }

  public void onComplete() {
    if (inWork == 0 && inTraverse == 0 && markedForDelete.isEmpty()) onComplete.run();
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
    onError.accept(error);
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
}
