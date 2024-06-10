package org.sudu.experiments.ui.fs;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.diff.folder.RangeCtx;
import org.sudu.experiments.diff.update.CollectConsumer;

public class FolderDiffUpdateHandler extends FolderDiffHandler {

  private final FolderDiffModel leftModel, rightModel;
  private final CollectConsumer collect;
  private final RangeCtx rangeCtx;
  private final Runnable onCompared;

  public FolderDiffUpdateHandler(
      DirectoryHandle left, DirectoryHandle right,
      FolderDiffModel leftModel, FolderDiffModel rightModel,
      RangeCtx rangeCtx, CollectConsumer collect,
      Runnable onCompared
  ) {
    super(left, right, null);
    this.leftModel = leftModel;
    this.rightModel = rightModel;
    this.collect = collect;
    this.rangeCtx = rangeCtx;
    this.onCompared = onCompared;
  }

  @Override
  protected void onCompared() {
    int leftLen = leftChildren.length, rightLen = rightChildren.length;
    leftModel.setChildren(leftLen);
    rightModel.setChildren(rightLen);

    boolean changed = true;
    int lP = 0, rP = 0;
    while (changed) {
      changed = false;
      while (lP < leftLen && rP < rightLen &&
          leftDiff(lP) == DiffTypes.DEFAULT &&
          rightDiff(rP) == DiffTypes.DEFAULT
      ) {
        int id = rangeCtx.nextId();
        changed = true;
        leftModel.child(lP).rangeId = id;
        rightModel.child(rP).rangeId = id;
        collect.accept(
            leftModel.child(lP), rightModel.child(rP),
            leftItem(lP), rightItem(rP)
        );
        lP++;
        rP++;
      }
      if (changed) continue;
      int id = rangeCtx.nextId();
      while (lP < leftLen && leftDiff(lP) == DiffTypes.DELETED) {
        changed = true;
        leftModel.child(lP).diffType = DiffTypes.DELETED;
        leftModel.child(lP).rangeId = id;
        leftModel.child(lP).markDown(DiffTypes.DELETED);
        leftModel.child(lP).itemCompared();
        lP++;
      }
      if (changed) {
        rangeCtx.markUp(leftModel, rightModel);
        continue;
      }
      while (rP < rightLen && rightDiff(rP) == DiffTypes.INSERTED) {
        changed = true;
        rightModel.child(rP).diffType = DiffTypes.INSERTED;
        rightModel.child(rP).rangeId = id;
        rightModel.child(rP).markDown(DiffTypes.INSERTED);
        rightModel.child(rP).itemCompared();
        rP++;
      }
      if (changed) rangeCtx.markUp(leftModel, rightModel);
    }
    onCompared.run();
  }

  private int leftDiff(int lP) {
    return leftChildren[lP].diffType;
  }

  private FsItem leftItem(int lP) {
    return leftChildren[lP].item;
  }

  private int rightDiff(int rP) {
    return rightChildren[rP].diffType;
  }

  private FsItem rightItem(int rP) {
    return rightChildren[rP].item;
  }
}
