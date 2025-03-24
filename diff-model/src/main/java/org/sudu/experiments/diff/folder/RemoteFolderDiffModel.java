package org.sudu.experiments.diff.folder;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.ItemKind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class RemoteFolderDiffModel extends FolderDiffModel {

  public String path;

  public RemoteFolderDiffModel(FolderDiffModel parent, String path) {
    super(parent);
    this.path = path;
  }

  public void update(RemoteFolderDiffModel newModel) {
    super.update(newModel);
    this.path = newModel.path;
  }

  @Override
  public RemoteFolderDiffModel child(int i) {
    return (RemoteFolderDiffModel) children[i];
  }

  public RemoteFolderDiffModel parent() {
    return (RemoteFolderDiffModel) parent;
  }

  public String getFullPath(String root) {
    StringBuilder sb = new StringBuilder(root);
    collectPath(sb);
    return sb.toString();
  }

  public void collectPathFromRoot(StringBuilder sb) {
    collectPath(sb);
  }

  private void collectPath(StringBuilder sb) {
    if (parent != null)
      parent().collectPathFromRoot(sb);
    if (!sb.isEmpty() && !path.isEmpty())
      sb.append('/');
    sb.append(path);
  }

  public RemoteFolderDiffModel getByPath(String[] path, int ind, boolean left) {
    if (ind == path.length) return this;
    boolean isFile = ind + 1 == path.length;
    for (int i = 0; i < children.length; i++) {
      var child = child(i);
      if ((left && !child.isLeft()) || (!left && !child.isRight())) continue;
      if (isFile != child.isFile()) continue;
      if (child.path.equals(path[ind])) return child.getByPath(path, ind + 1, left);
    }
    return null;
  }

  public RemoteFolderDiffModel applyFilter(BitSet filterSet, RemoteFolderDiffModel parent) {
    int diffType = getDiffType();
    boolean matchFilter = filterSet.get(diffType);
    boolean emptyChildren = children == null || children.length == 0;
    if (!isFile()) matchFilter &= emptyChildren || !(diffType == DiffTypes.DEFAULT || diffType == DiffTypes.EDITED);
    if (isFile() || emptyChildren) return matchFilter ? this : null;
    List<RemoteFolderDiffModel> filteredChildren = new ArrayList<>();
    var filteredNode = new RemoteFolderDiffModel(parent, path);
    for (int i = 0; i < children.length; i++) {
      var child = child(i);
      var filteredChild = child.applyFilter(filterSet, filteredNode);
      if (filteredChild == null) continue;
      filteredChildren.add(filteredChild);
    }
    if (matchFilter || !filteredChildren.isEmpty()) {
      filteredNode.flags = flags;
      filteredNode.childrenComparedCnt = childrenComparedCnt;
      filteredNode.children = filteredChildren.toArray(RemoteFolderDiffModel[]::new);
      filteredNode.posInParent = posInParent;
      return filteredNode;
    }
    return null;
  }

  public static int[] toInts(
      RemoteFolderDiffModel model,
      List<String> pathList
  ) {
    ArrayWriter writer = new ArrayWriter();
    writeInts(model, pathList, writer);
    return writer.getInts();
  }

  public static void writeInts(
      RemoteFolderDiffModel model,
      List<String> pathList,
      ArrayWriter writer
  ) {
    writer.write(model.flags);
    writer.write(model.childrenComparedCnt);
    writer.write(model.posInParent);

    writer.write(pathList.size());
    pathList.add(model.path);

    if (model.children == null) writer.write(-1);
    else {
      writer.write(model.children.length);
      for (var child : model.children) writeInts((RemoteFolderDiffModel) child, pathList, writer);
    }
  }

  public static RemoteFolderDiffModel fromInts(int[] ints, String[] paths) {
    return fromInts(new ArrayReader(ints), paths, null);
  }

  public static RemoteFolderDiffModel fromInts(
      ArrayReader reader,
      String[] paths,
      RemoteFolderDiffModel parent
  ) {
    RemoteFolderDiffModel model = new RemoteFolderDiffModel(parent, null);
    model.flags = reader.next();
    model.childrenComparedCnt = reader.next();
    model.posInParent = reader.next();

    int pathInd = reader.next();
    model.path = paths[pathInd];

    int childrenLen = reader.next();
    if (childrenLen != -1) {
      var children = new RemoteFolderDiffModel[childrenLen];
      for (int i = 0; i < childrenLen; i++) {
        children[i] = fromInts(reader, paths, model);
      }
      model.children = children;
    }
    return model;
  }

  @Override
  public String toString() {
    return "{" +
        "\"path\":\"" + path + '\"' +
        ", \"children\":" + (children == null ? "null" : "FolderDiffModel[" + children.length + "]") +
        ", \"childrenComparedCnt\":" + childrenComparedCnt +
        ", \"compared\":" + isCompared() +
        ", \"propagation\":\"" + PropTypes.name(getPropagation()) + "\"" +
        ", \"diffType\":\"" + DiffTypes.name(getDiffType()) + "\"" +
        ", \"itemKind\":\"" + ItemKind.name(getItemKind()) + "\"" +
        "}";
  }

  public String recToString() {
    return "{" +
        "\"path\":\"" + path + '\"' +
        ", \"children\":" + Arrays.toString(children) +
        ", \"childrenComparedCnt\":" + childrenComparedCnt +
        ", \"compared\":" + isCompared() +
        ", \"propagation\":\"" + PropTypes.name(getPropagation()) + "\"" +
        ", \"diffType\":\"" + DiffTypes.name(getDiffType()) + "\"" +
        ", \"itemKind\":\"" + ItemKind.name(getItemKind()) + "\"" +
        "}";
  }

  public String describeError(int[] path) {
    return describeError(path, 0);
  }

  String describeError(int[] path, int index) {
    if (index == path.length) return "";
    if (children == null)
      return "no children but requested children[" + path[index] + "]";
    if (path[index] >= children.length)
      return "path[index](" + path[index] +
          ") > children.length(" + children.length + ')';
    return child(path[index]).path + '/' +
        describeError(path, index + 1);
  }
}
