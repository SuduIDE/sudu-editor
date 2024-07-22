package org.sudu.experiments.diff.folder;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.DiffTypes;

import java.util.List;

import static org.sudu.experiments.diff.folder.PropTypes.*;

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

  public static final RemoteFolderDiffModel REMOTE_DEFAULT = getDefault();

  private static RemoteFolderDiffModel getDefault() {
    var model = new RemoteFolderDiffModel(null, "");
    model.setPropagation(PROP_DOWN);
    model.setDiffType(DiffTypes.DEFAULT);
    model.setCompared(true);
    return model;
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
      for (int i = 0; i < childrenLen; i++) children[i] = fromInts(reader, paths, model);
      model.children = children;
    }
    return model;
  }

//  @Override
//  public String value() {
//    return path + " " + super.value();
//  }

  @Override
  public String infoString() {
    return "RemoteFolderDiffModel{" +
        "path='" + path +
        "', parent=" + parent +
        ", childrenComparedCnt=" + childrenComparedCnt +
        ", children.length=" + (children != null ? children.length : 0) +
        ", compared=" + isCompared() +
        ", propagation=" + getPropagation() +
        ", diffType=" + getDiffType() +
        "}";
  }
}
