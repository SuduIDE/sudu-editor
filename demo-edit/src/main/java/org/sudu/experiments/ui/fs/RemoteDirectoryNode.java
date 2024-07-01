package org.sudu.experiments.ui.fs;

import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.ui.FileTreeNode;

public class RemoteDirectoryNode extends RemoteFileTreeNode {

  public int folderCnt;

  public RemoteDirectoryNode(
      RemoteFolderDiffModel model,
      RemoteHandle handle
  ) {
    super(model, handle);
    iconFolder();
    onClick = this::onClick;
  }

  public void onClick() {
    if (isOpened()) closeDir();
    else openDir();
  }

  public void openDir() {
    doOpen();
    var opposite = handle.getOppositeDir(this);
    if (opposite != null) opposite.doOpen();
  }

  public void closeDir() {
    doClose();
    var opposite = handle.getOppositeDir(this);
    if (opposite != null) opposite.doClose();
  }

  public void doOpen() {
    handle.openDir(this);
    handle.updateView();
    super.iconFolderOpened();
  }

  public void doClose() {
    handle.closeDir(this);
    handle.updateView();
    super.iconFolder();
  }

  public boolean isOpened() {
    return children != FileTreeNode.ch0;
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
  public String toString() {
    return ">" + name();
  }
}
