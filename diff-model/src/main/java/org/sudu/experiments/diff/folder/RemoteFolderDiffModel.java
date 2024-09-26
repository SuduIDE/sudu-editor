package org.sudu.experiments.diff.folder;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.ItemKind;

import java.util.Arrays;
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
    StringBuilder sb = new StringBuilder(root).append("/");
    collectPathFromRoot(sb);
    return sb.toString();
  }

  public void collectPathFromRoot(StringBuilder sb) {
    if (parent != null) {
      parent().collectPathFromRoot(sb);
      if (parent.parent != null) sb.append("/");  // because root path is empty
    }
    sb.append(path);
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

    int pathInd = reader.next();
    model.path = paths[pathInd];

    int childrenLen = reader.next();
    if (childrenLen != -1) {
      var children = new RemoteFolderDiffModel[childrenLen];
      for (int i = 0; i < childrenLen; i++) {
        children[i] = fromInts(reader, paths, model);
        children[i].posInParent = i;
      }
      model.children = children;
    }
    return model;
  }

  @Override
  public String toString() {
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
}
