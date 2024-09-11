package org.sudu.experiments.ui.fs;

import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.ui.FileTreeNode;

public class RemoteFileTreeNode extends FileTreeNode {

  protected RemoteHandle handle;
  public int posInParent = -1;

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

  public String getFullPath(String root) {
    return model().getFullPath(root);
  }

  public String getRelativePath() {
    StringBuilder sb = new StringBuilder();
    model().collectPathFromRoot(sb);
    return sb.toString();
  }

  public RemoteFileTreeNode[] getChildren() {
    return (RemoteFileTreeNode[]) children;
  }

  public void setChildren(FileTreeNode[] children) {
    this.children = children;
  }
}
