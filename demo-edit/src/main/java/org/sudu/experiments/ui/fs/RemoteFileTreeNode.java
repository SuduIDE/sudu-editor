package org.sudu.experiments.ui.fs;

import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.ui.FileTreeNode;

public class RemoteFileTreeNode extends FileTreeNode {

  public RemoteHandle handle;

  public RemoteFileTreeNode(
      String path,
      RemoteHandle handle,
      int depth
  ) {
    this(path, depth);
    this.handle = handle;
    defaultIcon();
  }

  private RemoteFileTreeNode(String v, int d) {
    super(v, d);
  }

  public RemoteFolderDiffModel model() {
    return handle.getModel();
  }

  public RemoteFileTreeNode child(int ind) {
    return (RemoteFileTreeNode) super.child(ind);
  }

  @Override
  public String value() {
    return model().path;
  }
}
