package org.sudu.experiments.diff.folder;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.DiffTypes;

import java.util.ArrayList;

import static org.sudu.experiments.diff.folder.PropTypes.*;

public class RemoteFolderDiffModel extends FolderDiffModel {

  public String path;

  public RemoteFolderDiffModel(RemoteFolderDiffModel parent, String path) {
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
    model.propagation = PROP_DOWN;
    model.diffType = DiffTypes.DEFAULT;
    model.compared = true;
    return model;
  }

  public static int[] toInts(
      RemoteFolderDiffModel model,
      ArrayList<String> pathList
  ) {
    ArrayWriter writer = new ArrayWriter();
    writeInts(model, pathList, writer);
    return writer.getInts();
  }

  public static void writeInts(
      RemoteFolderDiffModel model,
      ArrayList<String> pathList,
      ArrayWriter writer
  ) {
    writer.write(model.propagation);
    writer.write(model.diffType);
    writer.write(model.rangeId);
    writer.write(model.childrenComparedCnt);
    writer.write(model.compared ? 1 : 0);
    writer.write(model.isFile ? 1 : 0);

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
    model.propagation = reader.next();
    model.diffType = reader.next();
    model.rangeId = reader.next();
    model.childrenComparedCnt = reader.next();
    model.compared = reader.next() == 1;
    model.isFile = reader.next() == 1;

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
}
