package org.sudu.experiments.ui.fs;

import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.ui.FileTreeNode;

public class RemoteDirectoryNode extends RemoteFileTreeNode {

  public final RemoteFolderDiffModel model;
  public final RemoteDirectoryHandle handle;
  public int folderCnt;

  public RemoteDirectoryNode(
      RemoteFolderDiffModel model,
      RemoteDirectoryHandle handle
  ) {
    super(model);
    this.model = model;
    this.handle = handle;
    onClick = this::onClick;
  }

  public void onClick() {
    if (isOpened()) closeDir();
    else openDir();
  }

  public void openDir() {
    doOpen();
    var opposite = handle.getOpposite(this);
    if (opposite != null) opposite.doOpen();
  }

  public void closeDir() {
    doClose();
    var opposite = handle.getOpposite(this);
    if (opposite != null) opposite.doClose();
  }

  public void doOpen() {
    handle.open(this);
    handle.updateView();
    super.iconFolderOpened();
  }

  public void doClose() {
    handle.close(this);
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
    return (RemoteDirectoryNode) FileTreeNode.bs(children, folderCnt, path);
  }
}
