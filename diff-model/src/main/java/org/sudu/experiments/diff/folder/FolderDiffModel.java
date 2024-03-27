package org.sudu.experiments.diff.folder;

import static org.sudu.experiments.diff.folder.PropTypes.*;

public class FolderDiffModel {

  FolderDiffModel parent;
  public FolderDiffModel[] children;
  public DiffStatus status;

  public FolderDiffModel(FolderDiffModel parent) {
    this.parent = parent;
    this.status = new DiffStatus();
    if (parent != null) status.depth = parent.status.depth + 1;
  }

  public void setChildren(int len) {
    children = new FolderDiffModel[len];
    for (int i = 0; i < len; i++) children[i] = new FolderDiffModel(this);
  }

  public FolderDiffModel child(int i) {
    return children[i];
  }

  public DiffStatus childStatus(int i) {
    return children[i].status;
  }

  public void markUp(int diffType, RangeCtx ctx) {
    status.diffType = diffType;
    status.propagation = PROP_UP;
    status.rangeId = ctx.nextId();
    if (parent != null) parent.markUp(diffType, ctx);
  }

  public void markDown(int diffType) {
    status.diffType = diffType;
    status.propagation = PROP_DOWN;
    if (parent != null) status.rangeId = parent.status.rangeId;
    if (children != null) for (var child: children) child.markDown(diffType);
  }
}
