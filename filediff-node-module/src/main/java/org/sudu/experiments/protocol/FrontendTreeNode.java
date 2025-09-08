package org.sudu.experiments.protocol;

import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;

import java.util.*;

public class FrontendTreeNode {

  public String name;
  public boolean isFile;
  public FrontendTreeNode[] children;

  public FrontendTreeNode() {
  }

  public FrontendTreeNode(RemoteFolderDiffModel model) {
    this(model.path, model.isFile());
  }

  public FrontendTreeNode(String name, boolean isFile) {
    this.name = name;
    this.isFile = isFile;
  }

  FrontendTreeNode findNode(int[] path) {
    return findNode(path, 0);
  }

  private FrontendTreeNode findNode(int[] path, int ind) {
    if (ind == path.length) return this;
    if (children == null || path[ind] >= children.length) return null;
    return children[path[ind]].findNode(path, ind + 1);
  }

  public FrontendTreeNode findNode(Deque<String> path) {
    if (path.isEmpty()) return this;
    String name = path.removeFirst();
    var child = child(-1, name, false);
    if (child == null) return null;
    return child.findNode(path);
  }

  public void openPath(RemoteFolderDiffModel model, int[] path, int ind) {
    if (ind == path.length || model.children == null) return;
    int childInd = path[ind];
    if (children == null) children = mkChildrenWithModel(model);
    var childModel = model.child(childInd);
    child(childModel.path, childModel.isFile()).openPath(childModel, path, ind + 1);
  }

  public void updateDeepWithModel(RemoteFolderDiffModel model) {
    if (children == null) return;
    FrontendTreeNode[] newChildren = new FrontendTreeNode[model.children.length];
    for (int i = 0; i < model.children.length; i++) {
      var modelChild = model.child(i);
      var nodeChild = child(i, modelChild.path, modelChild.isFile());
      if (nodeChild == null) nodeChild = new FrontendTreeNode(modelChild);
      nodeChild.updateDeepWithModel(modelChild);
      newChildren[i] = nodeChild;
    }
    children = newChildren;
  }

  public void updateWithModel(RemoteFolderDiffModel model) {
    if (children == null) return;
    FrontendTreeNode[] newChildren = new FrontendTreeNode[model.children.length];
    for (int i = 0; i < model.children.length; i++) {
      var modelChild = model.child(i);
      var nodeChild = child(i, modelChild.path, modelChild.isFile());
      if (nodeChild == null) nodeChild = new FrontendTreeNode(modelChild);
      newChildren[i] = nodeChild;
    }
    children = newChildren;
  }

  public static FrontendTreeNode[] mkChildrenWithModel(RemoteFolderDiffModel model) {
    FrontendTreeNode[] children = new FrontendTreeNode[model.children.length];
    for (int i = 0; i < children.length; i++) {
      children[i] = new FrontendTreeNode(model.child(i));
    }
    return children;
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

  public FrontendTreeNode child(String path, boolean isFile) {
    for (var child: children) {
      if (child.match(path, isFile)) return child;
    }
    return null;
  }

  public FrontendTreeNode child(int i, String path, boolean isFile) {
    if (0 <= i && i < children.length) {
      var child = children[i];
      if (child.match(path, isFile)) return child;
    }
    return child(path, isFile);
  }

  public boolean match(RemoteFolderDiffModel model) {
    return match(model.path, model.isFile());
  }

  public boolean match(String name, boolean isFile) {
    return this.isFile == isFile && this.name.equals(name);
  }

  public boolean isOpened() {
    return children != null;
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
