package org.sudu.experiments.diff.folder;

import java.util.Objects;

public class TreeS {

  public String name;
  public boolean isFolder;

  public TreeS(String name, boolean isFolder) {
    this.name = name;
    this.isFolder = isFolder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TreeS treeS = (TreeS) o;
    return isFolder == treeS.isFolder && Objects.equals(name, treeS.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, isFolder);
  }
}
