package org.sudu.experiments.ui.fs;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.math.ArrayOp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Consumer;

public class RereadFolderHandler {

  public DirectoryHandle handle;
  public Consumer<Object[]> r;

  public ArrayList<FsItem> items;
  public ArrayWriter writer;
  int readCnt = 0;

  LinkedList<TreeS> paths;

  public RereadFolderHandler(
      DirectoryHandle handle,
      ArrayList<FsItem> items,
      ArrayWriter writer,
      LinkedList<TreeS> paths,
      Consumer<Object[]> r
  ) {
    this.handle = handle;
    this.items = items;
    this.writer = writer;
    this.paths = paths;
    this.r = r;
  }

  public void beginRead() {
    read(handle);
  }

  public void read(DirectoryHandle handle) {
    ++readCnt;
    handle.read(new DiffReader(this::onRead));
  }

  public void onRead(TreeS[] children) {
    if (!paths.isEmpty()) {
      var path = paths.removeFirst();
      children = Arrays.stream(children)
          .filter(it -> (it.isFolder == path.isFolder) && it.name.equals(path.name))
          .toArray(TreeS[]::new);
      writer.write(-1);
    } else {
      writer.write(children.length);
    }
    for (var child: children) {
      items.add(child.item);
      if (child.isFolder) {
        read((DirectoryHandle) child.item);
      }
    }
    --readCnt;
    if (readCnt <= 0) {
      ArrayList<Object> result = new ArrayList<>();
      result.add(writer.getInts());
      result.addAll(items);
      ArrayOp.sendArrayList(result, r);
    }
  }
}
