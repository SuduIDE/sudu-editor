package org.sudu.experiments.ui.fs;

public class RemoteFileNode extends RemoteFileTreeNode {

  public RemoteFileNode(
      String model,
      RemoteHandle handle,
      int depth
  ) {
    super(model, handle, depth);
    onDblClick = this::openFile;
  }

  public void openFile() {
    handle.openFile(this);
  }

  @Override
  protected void defaultIcon() {
    iconFile();
  }
}
