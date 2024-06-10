package org.sudu.experiments.ui.fs;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.ui.FileTreeNode;

public class FileNode extends FileTreeNode {
  public final FileHandle file;

  public FileNode(String v, int d, FileHandle file) {
    super(v, d);
    this.file = file;
  }

  @Override
  public String toString() {
    return file.toString();
  }

  @Override
  protected void defaultIcon() {
    this.iconFile();
  }
}
