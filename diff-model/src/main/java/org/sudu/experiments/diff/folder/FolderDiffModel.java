package org.sudu.experiments.diff.folder;

import org.sudu.experiments.diff.DiffTypes;

import static org.sudu.experiments.diff.folder.PropTypes.*;

public class FolderDiffModel {

  FolderDiffModel parent;
  public FolderDiffModel[] children;
  int childrenComparedCnt;
  public boolean compared;
  public int propagation = NO_PROP;
  public int diffType = DiffTypes.DEFAULT;
  public int rangeId;

  public FolderDiffModel(FolderDiffModel parent) {
    this.parent = parent;
  }

  public void setChildren(int len) {
    children = new FolderDiffModel[len];
    this.childrenComparedCnt = 0;
    for (int i = 0; i < len; i++) children[i] = new FolderDiffModel(this);
    if (len == 0) {
      compared = true;
      if (parent != null) parent.childCompared();
    }
  }

  public void itemCompared() {
    this.compared = true;
    if (parent == null) throw new IllegalStateException("File must have a parent");
    parent.childCompared();
  }

  public void childCompared() {
    childrenComparedCnt++;
    if (!isFullyCompared()) return;
    compared = true;
    if (parent != null) parent.childCompared();
  }

  public boolean isFullyCompared() {
    return children.length == childrenComparedCnt;
  }

  public boolean isFile() {
    return children == null;
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

  public static final FolderDiffModel DEFAULT = getDefault();

  private static FolderDiffModel getDefault() {
    var model = new FolderDiffModel(null);
    model.propagation = PROP_DOWN;
    model.diffType = DiffTypes.DEFAULT;
    model.compared = true;
    return model;
  }
}
