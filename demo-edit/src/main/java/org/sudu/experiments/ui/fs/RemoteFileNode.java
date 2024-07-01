package org.sudu.experiments.ui.fs;

import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;

public class RemoteFileNode extends RemoteFileTreeNode {

  public RemoteFileNode(
      RemoteFolderDiffModel model,
      RemoteHandle handle
  ) {
    super(model, handle);
    iconFile();
    onClick = () -> handle.openFile(this);
  }

  @Override
  public String toString() {
    return "-" + name();
  }
}
