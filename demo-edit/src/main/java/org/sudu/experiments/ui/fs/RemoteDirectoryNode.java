package org.sudu.experiments.ui.fs;

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
    if (isOpened()) closeDir(); else openDir();
    handle.sendModel(this, isOpened());
    handle.updateView();
  }

  public void openDir() {
//    if (model() == null || model().children == null) return;
    doOpen();
    var opposite = handle.getOppositeDir(this);
    if (opposite != null) opposite.doOpen();
    if (children.length == 1 && children[0] instanceof RemoteDirectoryNode singleDir) singleDir.openDir();
  }

  public void closeDir() {
    doClose();
    var opposite = handle.getOppositeDir(this);
    if (opposite != null) opposite.doClose();
    handle.updateView();
  }

  public void doOpen() {
    handle.openDir(this);
    super.open();
  }

  public void doClose() {
    handle.closeDir(this);
    super.close();
  }

  public RemoteDirectoryNode getOppositeDir() {
    return handle.getOppositeDir(this);
  }

  public RemoteFileTreeNode findSubItem(String path, boolean isFolder) {
    return isFolder ? findSubDir(path) : findSubFile(path);
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

  // todo open isOpened dirs with empty children when updating model
  @Override
  public boolean isOpened() {
    return super.isOpened() && children != FileTreeNode.ch0;
  }

  @Override
  public boolean isClosed() {
    return super.isClosed() || children == FileTreeNode.ch0;
  }
}
