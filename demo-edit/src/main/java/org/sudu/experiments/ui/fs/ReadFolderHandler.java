package org.sudu.experiments.ui.fs;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.math.ArrayOp;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ReadFolderHandler {

  public final RemoteFolderDiffModel rootModel;
  public final DirectoryHandle rootHandle;
  private final int diffType, rangeId;
  private final Consumer<Object[]> r;
  private int readCnt = 0;

  public ReadFolderHandler(
      RemoteFolderDiffModel rootModel,
      DirectoryHandle rootHandle,
      int diffType,
      int rangeId,
      Consumer<Object[]> r
  ) {
    this.rootModel = rootModel;
    this.rootHandle = rootHandle;
    this.diffType = diffType;
    this.rangeId = rangeId;
    this.r = r;
  }

  public void beginRead() {
    rootModel.setDiffType(diffType);
    rootModel.rangeId = rangeId;
    read(rootModel, rootHandle);
  }

  public void read(
      RemoteFolderDiffModel model,
      DirectoryHandle handle
  ) {
    ++readCnt;
    handle.read(new DiffReader(children -> onFolderRead(model, children)));
  }

  public void onFolderRead(
      RemoteFolderDiffModel model,
      TreeS[] children
  ) {
    FolderDiffUpdateHandler.setChildren(model, children);
    for (int i = 0; i < children.length; i++) {
      var child = model.child(i);
      child.setDiffType(diffType);
      child.rangeId = rangeId;
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
