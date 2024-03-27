package org.sudu.experiments.diff.folder;

import org.sudu.experiments.diff.DiffTypes;

import static org.sudu.experiments.diff.folder.PropTypes.*;

public class DiffStatus {

  public int diffType = DiffTypes.DEFAULT;
  public int propagation = NO_PROP;
  public int rangeId;
  public int depth;

  public DiffStatus() {

  }

}
