package org.sudu.experiments.diff.folder;

import org.sudu.experiments.diff.DiffTypes;

import static org.sudu.experiments.diff.folder.PropTypes.*;

public class DiffStatus {

  public DiffStatus parent;
  public DiffStatus[] children;
  public int diffType = DiffTypes.DEFAULT;
  public int propagation = NO_PROP;

  public DiffStatus() {

  }

  public DiffStatus(DiffStatus parent) {
    this.parent = parent;
  }

  public void markUp(int diffType) {
    this.diffType = diffType;
    this.propagation = PROP_UP;
    if (parent != null) parent.markUp(diffType);
  }
}
