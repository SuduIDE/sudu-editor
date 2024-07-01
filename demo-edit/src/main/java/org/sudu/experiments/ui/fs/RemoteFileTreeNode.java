package org.sudu.experiments.ui.fs;

import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.ui.FileTreeNode;

public class RemoteFileTreeNode extends FileTreeNode {

  public RemoteFileTreeNode(RemoteFolderDiffModel model) {
    this(model.path, model.depth);
  }

  public RemoteFileTreeNode(String v, int d) {
    super(v, d);
  }
}
