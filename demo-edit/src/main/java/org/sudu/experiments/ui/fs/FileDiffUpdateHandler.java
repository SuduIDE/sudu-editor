package org.sudu.experiments.ui.fs;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.diff.folder.RangeCtx;

public class FileDiffUpdateHandler {

  private final FolderDiffModel leftModel, rightModel;
  private final RangeCtx rangeCtx;
  private final Runnable onCompared;

  public FileDiffUpdateHandler(
      FileHandle left, FileHandle right,
      FolderDiffModel leftModel, FolderDiffModel rightModel,
      RangeCtx rangeCtx, Runnable onCompared
  ) {
    this.leftModel = leftModel;
    this.rightModel = rightModel;
    this.rangeCtx = rangeCtx;
    this.onCompared = onCompared;
    new FileCompare(this::onCompared, left, right);
  }

  void onCompared(boolean equals) {
    leftModel.itemCompared();
    rightModel.itemCompared();
    if (!equals) {
      int rangeId = rangeCtx.nextId();
      leftModel.rangeId = rangeId;
      rightModel.rangeId = rangeId;
      rangeCtx.markUp(leftModel, rightModel);
    }
    onCompared.run();
  }
}
