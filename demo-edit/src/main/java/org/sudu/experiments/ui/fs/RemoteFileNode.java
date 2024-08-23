package org.sudu.experiments.ui.fs;

public class RemoteFileNode extends RemoteFileTreeNode {

  public RemoteFileNode(
      String model,
      RemoteHandle handle,
      int depth
  ) {
    super(model, handle, depth);
    onDblClick = () -> handle.openFile(this);
  }

  @Override
  protected void defaultIcon() {
    iconFile();
  }

  @Override
  public boolean isClosed() {
    return true;
  }

  @Override
  public boolean isOpened() {
    return false;
  }
}
