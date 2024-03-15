package org.sudu.experiments.diff.folder;

import org.sudu.experiments.diff.DiffTypes;

import static org.sudu.experiments.diff.folder.PropTypes.*;

public class DiffStatus {

  public DiffStatus parent;
  public DiffStatus[] children;
  public int diffType = DiffTypes.DEFAULT;
  public int propagation = NO_PROP;
  public int rangeId;
  public int depth;

  public DiffStatus() {

  }

  public DiffStatus(DiffStatus parent) {
    this.parent = parent;
    if (parent != null) this.depth = parent.depth + 1;
  }

  public void markUp(int diffType, RangeCtx ctx) {
    this.diffType = diffType;
    this.propagation = PROP_UP;
    this.rangeId = ctx.nextId();
    if (parent != null) parent.markUp(diffType, ctx);
  }

  public void markDown(int diffType) {
    this.diffType = diffType;
    this.propagation = PROP_DOWN;
    if (parent != null) this.rangeId = parent.rangeId;
    if (children != null) for (var child: children) child.markDown(diffType);
  }
}
