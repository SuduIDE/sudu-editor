package org.sudu.experiments.diff.folder;

import java.util.function.Consumer;

public class ModelCopyStatus {

  int inCopy, inTraverse;
  final Runnable onComplete;
  final Consumer<String> onError;

  public ModelCopyStatus(Runnable onComplete, Consumer<String> onError) {
    this.onComplete = onComplete;
    this.onError = onError;
  }

  public void onTraversed() {
    inTraverse--;
    onComplete();
  }

  public void onCopied() {
    inCopy--;
    onComplete();
  }

  public void onCopiedFile(int bytes) {
    onCopied();
  }

  public void onComplete() {
    if (inCopy == 0 && inTraverse == 0) onComplete.run();
  }

  public void onError(String error) {
    onError.accept(error);
  }
}
