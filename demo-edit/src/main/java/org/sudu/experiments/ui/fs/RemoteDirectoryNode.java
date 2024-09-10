package org.sudu.experiments.ui.fs;

import org.sudu.experiments.parser.common.Pair;
import org.sudu.experiments.ui.FileTreeNode;

public class RemoteDirectoryNode extends RemoteFileTreeNode {

  public int folderCnt;

  public RemoteDirectoryNode(
      String path,
      RemoteHandle handle,
      int depth
  ) {
    super(path, handle, depth);
    iconFolder();
    onClick = this::onClick;
    onClickArrow = onClick;
    close();
  }

  public void onClick() {
    Pair<RemoteFileTreeNode, RemoteFileTreeNode> result = isOpened()
        ? closeDir()
        : openDir();
    handle.sendModel();
    handle.updateView();
  }

  public Pair<RemoteFileTreeNode, RemoteFileTreeNode> openDir() {
    doOpen();
    var opposite = handle.getOppositeDir(this);
    if (opposite != null) opposite.doOpen();
    if (children.length == 1 && children[0] instanceof RemoteDirectoryNode singleDir) return singleDir.openDir();
    return Pair.of(this, opposite);
  }

  public Pair<RemoteFileTreeNode, RemoteFileTreeNode> closeDir() {
    doClose();
    var opposite = handle.getOppositeDir(this);
    if (opposite != null) opposite.doClose();
    handle.updateView();
    return Pair.of(this, opposite);
  }

  public void doOpen() {
    handle.openDir(this);
    super.open();
  }

  public void doClose() {
    handle.closeDir(this);
    super.close();
  }

  public void setChildren(FileTreeNode[] children) {
    this.children = children;
  }

  public RemoteDirectoryNode findSubDir(String path) {
    return (RemoteDirectoryNode) FileTreeNode.bs(children, 0, folderCnt, path);
  }

  public RemoteFileNode findSubFile(String path) {
    return (RemoteFileNode) FileTreeNode.bs(children, folderCnt, childrenLength(), path);
  }

  @Override
  protected void defaultIcon() {
    if (isOpened()) iconFolderOpened();
    else iconFolder();
  }
}
