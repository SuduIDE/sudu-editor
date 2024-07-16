package org.sudu.experiments.ui.fs;

import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.ui.FileTreeNode;

public class RemoteFileTreeNode extends FileTreeNode {

  public RemoteHandle handle;

  public RemoteFileTreeNode(
      RemoteFolderDiffModel model,
      RemoteHandle handle
  ) {
    this(model.path, model.depth);
    this.handle = handle;
    defaultIcon();
  }

  private RemoteFileTreeNode(String v, int d) {
    super(v, d);
  }

  public RemoteFolderDiffModel model() {
    return handle.getModel();
  }

  @Override
  public String value() {
    return model().path;
  }

  @Override
  protected boolean needLineUpdate() {
    return true;
  }
}
