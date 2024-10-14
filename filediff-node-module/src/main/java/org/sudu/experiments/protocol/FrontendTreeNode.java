package org.sudu.experiments.protocol;

import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.folder.FolderDiffModel;

import java.util.Arrays;
import java.util.Objects;

public class FrontendTreeNode {

  public String name;
  public FrontendTreeNode[] children;

  FrontendTreeNode findNode(int[] path) {
    return findNode(path, 0);
  }

  private FrontendTreeNode findNode(int[] path, int ind) {
    if (ind == path.length) return this;
    if (children == null || path[ind] >= children.length) return null;
    return children[path[ind]].findNode(path, ind + 1);
  }

  public void collectPath(int[] path, ArrayWriter pathWriter, FolderDiffModel model, int side) {
    collectPath(path, pathWriter, model, side, 0);
  }

  private void collectPath(int[] path, ArrayWriter pathWriter, FolderDiffModel model, int side, int ind) {
    if (ind == path.length) return;
    if (children == null || path[ind] >= children.length) {
      pathWriter.clear();
      return;
    }

    var node = children[path[ind]];
    int nodeInd = 0;
    for (int i = 0; i < children.length; i++) {
      var nodeChild = children[i];
      var modelChild = model.child(i);
      if (nodeChild == node) break;
      if (modelChild.matchSide(side)) nodeInd++;
    }
    pathWriter.write(nodeInd);
    node.collectPath(path, pathWriter, model.child(path[ind]), side, ind + 1);
  }

  public void deleteItem(FrontendTreeNode node) {
    FrontendTreeNode[] newChildren = new FrontendTreeNode[children.length - 1];
    int i = 0;
    for (var child: children) {
      if (child == node) continue;
      newChildren[i++] = child;
    }
    this.children = newChildren;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FrontendTreeNode node = (FrontendTreeNode) o;
    return Objects.equals(name, node.name) && Objects.deepEquals(children, node.children);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, Arrays.hashCode(children));
  }

  @Override
  public String toString() {
    return
        "{\"name\":\"" + name + '\"' +
        ", \"children\":" + Arrays.toString(children) + "}";
  }
}
