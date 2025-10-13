package org.sudu.experiments.ui.fs;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.diff.ItemKind;
import org.sudu.experiments.diff.folder.ItemFolderDiffModel;
import org.sudu.experiments.math.ArrayOp;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ReadFolderHandler {

  public final ItemFolderDiffModel rootModel;
  private final int diffType;
  private final int itemKind;
  private final boolean isExcluded;
  private final Consumer<Object[]> r;
  private int readCnt = 0;
  private int foldersRead, filesRead;

  public ReadFolderHandler(
      ItemFolderDiffModel rootModel,
      int diffType,
      int itemKind,
      boolean isExcluded,
      Consumer<Object[]> r
  ) {
    this.rootModel = rootModel;
    this.diffType = diffType;
    this.itemKind = itemKind;
    this.isExcluded = isExcluded;
    this.r = r;
  }

  public void beginRead() {
    rootModel.setDiffType(diffType);
    rootModel.setItemKind(itemKind);
    rootModel.setExcluded(isExcluded);
    read(rootModel);
  }

  public void read(ItemFolderDiffModel model) {
    ++readCnt;
    var dirHandle = (DirectoryHandle) model.item();
    Consumer<TreeS[]> onComplete = children -> onFolderRead(model, children);
    dirHandle.read(new DiffReader(onComplete, this::sendError));
  }

  public static void setChildren(ItemFolderDiffModel parent, TreeS[] paths) {
    int len = paths.length;
    parent.children = new ItemFolderDiffModel[paths.length];
    parent.childrenComparedCnt = 0;
    for (int i = 0; i < len; i++) {
      parent.children[i] = new ItemFolderDiffModel(parent, paths[i].name);
      int kind = paths[i].isFolder
          ? ItemKind.FOLDER
          : ItemKind.FILE;
      parent.child(i).posInParent = i;
      parent.child(i).setItemKind(kind);
    }
    if (len == 0) parent.itemCompared();
  }

  public void onFolderRead(
      ItemFolderDiffModel model,
      TreeS[] children
  ) {
    setChildren(model, children);
    for (int i = 0; i < children.length; i++) {
      var child = model.child(i);
      child.setDiffType(diffType);
      child.setExcluded(isExcluded);
      child.setItem(children[i].item);
      if (!child.isFile()) {
        foldersRead++;
        read(child);
      } else {
        filesRead++;
        child.itemCompared();
      }
    }
    --readCnt;
    if (readCnt <= 0) {
      ArrayList<Object> result = new ArrayList<>();
      ArrayList<String> paths = new ArrayList<>();
      ArrayList<FsItem> fsItems = new ArrayList<>();
      int[] ints = ItemFolderDiffModel.toInts(rootModel, paths, fsItems);
      result.add(ints);
      result.add(new int[] {paths.size(), fsItems.size(), foldersRead, filesRead});
      result.addAll(paths);
      result.addAll(fsItems);
      ArrayOp.sendArrayList(result, r);
    }
  }

  public void sendError(String error) {
    System.err.println("ReadFolderHandler: " + error);
    ArrayList<Object> result = new ArrayList<>();
    result.add(new int[]{-1});
    result.add(error);
    ArrayOp.sendArrayList(result, r);
  }
}
