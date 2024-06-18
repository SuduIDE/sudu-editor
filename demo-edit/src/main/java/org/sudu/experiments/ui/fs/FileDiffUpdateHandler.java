package org.sudu.experiments.ui.fs;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.diff.folder.RangeCtx;

public class FileDiffUpdateHandler extends FileDiffHandler {

  private final FolderDiffModel leftModel, rightModel;
  private final RangeCtx rangeCtx;
  private final Runnable onCompared;

  public FileDiffUpdateHandler(
      FileHandle left, FileHandle right,
      FolderDiffModel leftModel, FolderDiffModel rightModel,
      RangeCtx rangeCtx, Runnable onCompared
  ) {
    super(null, left, right);
    this.leftModel = leftModel;
    this.rightModel = rightModel;
    this.rangeCtx = rangeCtx;
    this.onCompared = onCompared;
  }

  @Override
  protected void onCompared(boolean equals) {
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
