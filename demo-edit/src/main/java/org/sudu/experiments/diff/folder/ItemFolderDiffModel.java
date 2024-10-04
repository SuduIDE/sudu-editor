package org.sudu.experiments.diff.folder;

import org.sudu.experiments.FsItem;
import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.arrays.ArrayWriter;

import java.util.List;

public class ItemFolderDiffModel extends RemoteFolderDiffModel {

  public FsItem[] items;

  public ItemFolderDiffModel(FolderDiffModel parent, String path) {
    super(parent, path);
  }

  @Override
  public ItemFolderDiffModel child(int i) {
    return (ItemFolderDiffModel) super.child(i);
  }

  public FsItem item() {
    return left();
  }

  public FsItem item(boolean left) {
    return items.length < 2 || left ? left() : right();
  }

  public FsItem left() {
    return items[0];
  }

  public FsItem right() {
    return items[1];
  }

  public static int[] toInts(
      ItemFolderDiffModel model,
      List<String> pathList,
      List<FsItem> fsList
  ) {
    ArrayWriter writer = new ArrayWriter();
    writeInts(model, pathList, fsList, writer);
    return writer.getInts();
  }

  public static void writeInts(
      ItemFolderDiffModel model,
      List<String> pathList,
      List<FsItem> fsList,
      ArrayWriter writer
  ) {
    writer.write(model.flags);
    writer.write(model.childrenComparedCnt);

    writer.write(pathList.size());
    pathList.add(model.path);

    if (model.items == null) writer.write(-1);
    else {
      writer.write(model.items.length);
      for (var fsItem: model.items) {
        writer.write(fsList.size());
        fsList.add(fsItem);
      }
    }

    if (model.children == null) writer.write(-1);
    else {
      writer.write(model.children.length);
      for (var child : model.children) writeInts((ItemFolderDiffModel) child, pathList, fsList, writer);
    }
  }

  public static ItemFolderDiffModel fromInts(int[] ints, String[] paths, FsItem[] items) {
    return fromInts(new ArrayReader(ints), paths, items, null);
  }

  public static ItemFolderDiffModel fromInts(
      ArrayReader reader,
      String[] paths,
      FsItem[] items,
      ItemFolderDiffModel parent
  ) {
    ItemFolderDiffModel model = new ItemFolderDiffModel(parent, null);
    model.flags = reader.next();
    model.childrenComparedCnt = reader.next();

    int pathInd = reader.next();
    model.path = paths[pathInd];

    var itemsLen = reader.next();
    if (itemsLen != -1) {
      model.items = new FsItem[itemsLen];
      for (int i = 0; i < itemsLen; i++) {
        int itemInd = reader.next();
        model.items[i] = items[itemInd];
      }
    }

    int childrenLen = reader.next();
    if (childrenLen != -1) {
      var children = new ItemFolderDiffModel[childrenLen];
      for (int i = 0; i < childrenLen; i++) {
        children[i] = fromInts(reader, paths, items, model);
        children[i].posInParent = i;
      }
      model.children = children;
    }
    return model;
  }
}
