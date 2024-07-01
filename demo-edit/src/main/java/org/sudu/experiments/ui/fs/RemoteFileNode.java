package org.sudu.experiments.ui.fs;

import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;

public class RemoteFileNode extends RemoteFileTreeNode {

  RemoteFolderDiffModel model;

  public RemoteFileNode(RemoteFolderDiffModel model) {
    super(model);
    this.model = model;
    onClick = () -> System.out.println("Want to read file " + model.path);
  }
}
