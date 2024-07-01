package org.sudu.experiments.ui.fs;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.folder.RangeCtx;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.update.CollectDto;

import java.util.function.Consumer;

public class FolderDiffUpdateHandler extends FolderDiffHandler {

  private final RemoteFolderDiffModel leftModel, rightModel;
  private final Consumer<CollectDto> collect;
  private final RangeCtx rangeCtx;
  private final Runnable onCompared;

  public FolderDiffUpdateHandler(
      DirectoryHandle left, DirectoryHandle right,
      RemoteFolderDiffModel leftModel, RemoteFolderDiffModel rightModel,
      RangeCtx rangeCtx, Consumer<CollectDto> collect,
      Runnable onCompared
  ) {
    super(left, right, null);
    this.leftModel = leftModel;
    this.rightModel = rightModel;
    this.collect = collect;
    this.rangeCtx = rangeCtx;
    this.onCompared = onCompared;
  }

  static void setChildren(RemoteFolderDiffModel parent, TreeS[] paths) {
    int len = paths.length;
    parent.children = new RemoteFolderDiffModel[paths.length];
    parent.childrenComparedCnt = 0;
    for (int i = 0; i < len; i++) {
      parent.children[i] = new RemoteFolderDiffModel(parent, paths[i].name);
      parent.child(i).setIsFile(!paths[i].isFolder);
    }
    if (len == 0) {
      parent.setCompared(true);
      if (parent.parent != null) parent.childCompared();
    }
  }

  @Override
  protected void onCompared() {
    int leftLen = leftChildren.length, rightLen = rightChildren.length;
    setChildren(leftModel, leftChildren);
    setChildren(rightModel, rightChildren);

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
        collect.accept(new CollectDto(
            leftModel.child(lP), rightModel.child(rP),
            leftItem(lP), rightItem(rP))
        );
        lP++;
        rP++;
      }
      if (changed) continue;
      int id = rangeCtx.nextId();
      while (lP < leftLen && leftDiff(lP) == DiffTypes.DELETED) {
        changed = true;
        leftModel.child(lP).setDiffType(DiffTypes.DELETED);
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
        rightModel.child(rP).setDiffType(DiffTypes.INSERTED);
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
