package org.sudu.experiments.ui.fs;

import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.ui.FileTreeNode;

import java.util.function.Supplier;

public abstract class RemoteHandle {

  public void closeDir(RemoteDirectoryNode node) {
    node.setChildren(FileTreeNode.ch0);
    node.folderCnt = 0;
  }

  public abstract void updateView();
  public abstract void openDir(RemoteDirectoryNode node);
  public abstract void openFile(RemoteFileNode node);
  public abstract void sendModel();
  public abstract RemoteFileTreeNode getNearestParent(RemoteFolderDiffModel node);
  public abstract RemoteDirectoryNode getOppositeDir(RemoteDirectoryNode node);
  public abstract RemoteFileNode getOppositeFile(RemoteFileNode node);
  public abstract Supplier<RemoteFolderDiffModel> getModelSupplier();
}
