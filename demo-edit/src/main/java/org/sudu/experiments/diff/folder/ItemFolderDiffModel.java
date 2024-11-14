package org.sudu.experiments.diff.folder;

import org.sudu.experiments.FsItem;
import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.DiffTypes;

import java.util.Deque;
import java.util.List;

public class ItemFolderDiffModel extends RemoteFolderDiffModel {

  public final FsItem[] items = new FsItem[] {null, null};

  public ItemFolderDiffModel(FolderDiffModel parent, String path) {
    super(parent, path);
  }

  @Override
  public ItemFolderDiffModel child(int i) {
    return (ItemFolderDiffModel) super.child(i);
  }

  @Override
  public ItemFolderDiffModel parent() {
    return (ItemFolderDiffModel) parent;
  }

  public ItemFolderDiffModel findNeedUpdate(Deque<ItemFolderDiffModel> paths) {
    if (parent == null) return this;
    int diffType = getDiffType();
    if ((diffType == DiffTypes.DEFAULT || diffType == DiffTypes.EDITED) && countItems() < 2) {
      paths.addFirst(this);
      return parent().findNeedUpdate(paths);
    }
    return this;
  }

  public void setItems(FsItem left, FsItem right) {
    items[0] = left;
    items[1] = right;
  }

  public void setItem(FsItem item) {
    if (isLeftOnly()) items[0] = item;
    else if (isRightOnly()) items[1] = item;
  }

  public void setItem(boolean left, FsItem item) {
    items[left ? 0 : 1] = item;
  }

  public FsItem item() {
    if (isLeftOnly()) return items[0];
    if (isRightOnly()) return items[1];
    return null;
  }

  public FsItem item(boolean left) {
    return left ? left() : right();
  }

  public FsItem left() {
    return items[0];
  }

  public FsItem right() {
    return items[1];
  }

  private int countItems() {
    return (left() == null ? 0 : 1) + (right() == null ? 0 : 1);
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
    writer.write(model.posInParent);

    writer.write(pathList.size());
    pathList.add(model.path);

    for (var fsItem: model.items) {
      if (fsItem != null) {
        writer.write(fsList.size());
        fsList.add(fsItem);
      } else writer.write(-1);
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
    model.posInParent = reader.next();

    int pathInd = reader.next();
    model.path = paths[pathInd];

    for (int i = 0; i < 2; i++) {
      int itemInd = reader.next();
      if (itemInd == -1) continue;
      model.items[i] = items[itemInd];
    }

    int childrenLen = reader.next();
    if (childrenLen != -1) {
      var children = new ItemFolderDiffModel[childrenLen];
      for (int i = 0; i < childrenLen; i++) {
        children[i] = fromInts(reader, paths, items, model);
      }
      model.children = children;
    }
    return model;
  }

  public void updateFsItems(
      boolean left,
      ArrayReader reader,
      Deque<FsItem> items,
      Deque<ItemFolderDiffModel> paths
  ) {
    if (isFile()) return;
    if (items.isEmpty()) return;
    int len = reader.next();
    var item = items.removeFirst();
    if (len == -1) {
      var child = paths.removeFirst();
      child.setItem(left, item);
      child.updateFsItems(left, reader, items, paths);
    } else {
      int side = left ? FolderDiffSide.LEFT : FolderDiffSide.RIGHT;
      int mP = 0;
      for (int i = 0; i < len; i++) {
        mP = nextInd(mP, side);
        var child = child(mP);
        child.setItem(left, item);
        child.updateFsItems(left, reader, items, paths);
        mP++;
      }
    }
  }
}
