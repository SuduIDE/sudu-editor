package org.sudu.experiments.ui.fs;

import org.sudu.experiments.FsItem;

import java.util.Objects;

public class TreeS {

  public final String name;
  public final boolean isFolder;
  public FsItem item;
  public int diffType;
  private final int hash;

  public TreeS(FsItem item, boolean isFolder) {
    this(item.getName(), isFolder);
    this.item = item;
  }

  public TreeS(String name, boolean isFolder) {
    this.name = name;
    this.isFolder = isFolder;
    this.hash = Objects.hash(name, isFolder);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || hashCode() != o.hashCode() || getClass() != o.getClass()) return false;
    TreeS treeS = (TreeS) o;
    return isFolder == treeS.isFolder && name.equals(treeS.name);
  }

  @Override
  public int hashCode() {
    return hash;
  }

  @Override
  public String toString() {
    return name;
  }
}
