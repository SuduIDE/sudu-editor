package org.sudu.experiments.diff;

public class FolderDiffSelection {
  public String path;
  public boolean isLeft;
  public boolean isFolder;
  public boolean isOrphan;

  public FolderDiffSelection(String path, boolean isLeft, boolean isFolder, boolean isOrphan) {
    this.path = path;
    this.isLeft = isLeft;
    this.isFolder = isFolder;
    this.isOrphan = isOrphan;
  }

  @Override
  public String toString() {
    return "{" +
        "\"path\": \"" + path + "\"" +
        ", \"isLeft\": " + isLeft +
        ", \"isFolder\": " + isFolder +
        ", \"isOrphan\": " + isOrphan +
        "}";
  }
}
