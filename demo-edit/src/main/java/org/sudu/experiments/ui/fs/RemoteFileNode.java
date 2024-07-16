package org.sudu.experiments.ui.fs;

import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;

public class RemoteFileNode extends RemoteFileTreeNode {

  public RemoteFileNode(
      RemoteFolderDiffModel model,
      RemoteHandle handle,
      int depth
  ) {
    super(model, handle, depth);
    onClick = () -> handle.openFile(this);
  }

  @Override
  protected void defaultIcon() {
    iconFile();
  }
}
