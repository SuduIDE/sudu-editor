package org.sudu.experiments.ui.fs;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.diff.ItemKind;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.math.ArrayOp;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ReadFolderHandler {

  public final RemoteFolderDiffModel rootModel;
  public final DirectoryHandle rootHandle;
  private final int diffType;
  private final int itemKind;
  private final Consumer<Object[]> r;
  private int readCnt = 0;

  public ReadFolderHandler(
      RemoteFolderDiffModel rootModel,
      DirectoryHandle rootHandle,
      int diffType,
      int itemKind,
      Consumer<Object[]> r
  ) {
    this.rootModel = rootModel;
    this.rootHandle = rootHandle;
    this.diffType = diffType;
    this.itemKind = itemKind;
    this.r = r;
  }

  public void beginRead() {
    rootModel.setDiffType(diffType);
    rootModel.setItemKind(itemKind);
    read(rootModel, rootHandle);
  }

  public void read(
      RemoteFolderDiffModel model,
      DirectoryHandle handle
  ) {
    ++readCnt;
    handle.read(new DiffReader(children -> onFolderRead(model, children)));
  }

  public static void setChildren(RemoteFolderDiffModel parent, TreeS[] paths) {
    int len = paths.length;
    parent.children = new RemoteFolderDiffModel[paths.length];
    parent.childrenComparedCnt = 0;
    for (int i = 0; i < len; i++) {
      parent.children[i] = new RemoteFolderDiffModel(parent, paths[i].name);
      int kind = paths[i].isFolder
          ? ItemKind.FOLDER
          : ItemKind.FILE;
      parent.child(i).posInParent = i;
      parent.child(i).setItemKind(kind);
    }
    if (len == 0) parent.itemCompared();
  }

  public void onFolderRead(
      RemoteFolderDiffModel model,
      TreeS[] children
  ) {
    setChildren(model, children);
    for (int i = 0; i < children.length; i++) {
      var child = model.child(i);
      child.setDiffType(diffType);
      if (!child.isFile()) {
        read(child, (DirectoryHandle) children[i].item);
      } else child.itemCompared();
    }
    --readCnt;
    if (readCnt <= 0) {
      ArrayList<Object> result = new ArrayList<>();
      ArrayList<String> paths = new ArrayList<>();
      int[] ints = RemoteFolderDiffModel.toInts(rootModel, paths);
      result.add(ints);
      result.addAll(paths);
      ArrayOp.sendArrayList(result, r);
    }
  }
}
