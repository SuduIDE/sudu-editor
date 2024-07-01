package org.sudu.experiments.ui.fs;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.diff.folder.RangeCtx;

public class FileDiffUpdateHandler {

  private final RemoteFolderDiffModel leftModel, rightModel;
  private final RangeCtx rangeCtx;
  private final Runnable onCompared;

  public FileDiffUpdateHandler(
      FileHandle left, FileHandle right,
      RemoteFolderDiffModel leftModel,
      RemoteFolderDiffModel rightModel,
      RangeCtx rangeCtx, Runnable onCompared
  ) {
    this.leftModel = leftModel;
    this.rightModel = rightModel;
    this.rangeCtx = rangeCtx;
    this.onCompared = onCompared;
    FileCompare.compare(this::onCompared, left, right);
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
