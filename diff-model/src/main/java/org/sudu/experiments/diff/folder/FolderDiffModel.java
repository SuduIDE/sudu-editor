package org.sudu.experiments.diff.folder;

import org.sudu.experiments.diff.DiffTypes;

import static org.sudu.experiments.diff.folder.PropTypes.*;

public class FolderDiffModel {

  FolderDiffModel parent;
  public FolderDiffModel[] children;
  public int propagation = NO_PROP;
  public int diffType = DiffTypes.DEFAULT;
  public int rangeId;

  public FolderDiffModel(FolderDiffModel parent) {
    this.parent = parent;
  }

  public void setChildren(int len) {
    children = new FolderDiffModel[len];
    for (int i = 0; i < len; i++) children[i] = new FolderDiffModel(this);
  }

  public FolderDiffModel child(int i) {
    return children[i];
  }

  public void markUp(int diffType, RangeCtx ctx) {
    propagation = PROP_UP;
    this.diffType = diffType;
    rangeId = ctx.nextId();
    if (parent != null) parent.markUp(diffType, ctx);
  }

  public void markDown(int diffType) {
    propagation = PROP_DOWN;
    this.diffType = diffType;
    if (parent != null) rangeId = parent.rangeId;
    if (children != null) for (var child: children) child.markDown(diffType);
  }
}
