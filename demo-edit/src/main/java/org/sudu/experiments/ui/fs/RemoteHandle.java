package org.sudu.experiments.ui.fs;

import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.ui.FileTreeNode;

import java.util.Arrays;

public abstract class RemoteHandle {

  public void openDir(RemoteDirectoryNode node) {
    var model = node.model;
    int childLen = model.children.length;
    int folderPtr = 0, filePtr = 0;

    RemoteDirectoryNode[] folders = new RemoteDirectoryNode[1];
    RemoteFileNode[] files = new RemoteFileNode[1];

    for (int i = 0; i < childLen; i++) {
      var child = model.child(i);
      if (child.isFile()) {
        var file = new RemoteFileNode(child, this);
        files = ArrayOp.addAt(file, files, filePtr++);
      } else {
        var folder = new RemoteDirectoryNode(child, this);
        folders = ArrayOp.addAt(folder, folders, folderPtr++);
      }
    }

    folders = Arrays.copyOf(folders, folderPtr);
    files = Arrays.copyOf(files, filePtr);
    Arrays.sort(folders, FileTreeNode.cmp);
    Arrays.sort(files, FileTreeNode.cmp);

    var children = new RemoteFileTreeNode[childLen];
    System.arraycopy(folders, 0, children, 0, folderPtr);
    System.arraycopy(files, 0, children, folderPtr, filePtr);

    node.setChildren(children);
    node.folderCnt = folders.length;
    if (children.length == 1 && children[0] instanceof RemoteDirectoryNode singleDir) singleDir.openDir();
  }

  public void closeDir(RemoteDirectoryNode node) {
    node.setChildren(FileTreeNode.ch0);
    node.folderCnt = 0;
  }

  public abstract void updateView();
  public abstract void openFile(RemoteFileNode node);
  public abstract RemoteDirectoryNode getOppositeDir(RemoteDirectoryNode node);
  public abstract RemoteFileNode getOppositeFile(RemoteFileNode node);
}
