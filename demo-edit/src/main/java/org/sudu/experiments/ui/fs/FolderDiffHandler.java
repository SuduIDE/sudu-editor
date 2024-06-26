package org.sudu.experiments.ui.fs;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.DiffModel;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.math.ArrayOp;

import java.util.*;
import java.util.function.Consumer;

public class FolderDiffHandler {

  DirectoryHandle left, right;
  TreeS[] leftChildren, rightChildren;
  Consumer<Object[]> r;

  public FolderDiffHandler(DirectoryHandle left, DirectoryHandle right, Consumer<Object[]> r) {
    this.left = left;
    this.right = right;
    this.r = r;
  }

  public void read() {
    left.read(new DiffReader(true));
    right.read(new DiffReader(false));
  }

  private void sendLeft(TreeS[] leftChildren) {
    this.leftChildren = leftChildren;
    if (rightChildren != null) compare();
  }

  private void sendRight(TreeS[] rightChildren) {
    this.rightChildren = rightChildren;
    if (this.leftChildren != null) compare();
  }

  private void compare() {
    countDiffs();
    onCompared();
  }

  protected void onCompared() {
    ArrayWriter writer = new ArrayWriter();
    writer.write(leftChildren.length);
    writer.write(rightChildren.length);
    for (var child: leftChildren) writer.write(child.diffType);
    for (var child: rightChildren) writer.write(child.diffType);
    var result = new ArrayList<>();
    result.add(writer.getInts());
    for (var child: leftChildren) result.add(child.item);
    for (var child: rightChildren) result.add(child.item);
    ArrayOp.sendArrayList(result, r);
  }

  protected void countDiffs() {
    var commons = DiffModel.countFolderCommon(leftChildren, rightChildren);
    BitSet leftCommon = commons[0], rightCommon = commons[1];

    for (int i = 0; i < leftChildren.length; i++) {
      if (!leftCommon.get(i)) leftChildren[i].diffType = DiffTypes.DELETED;
    }
    for (int i = 0; i < rightChildren.length; i++) {
      if (!rightCommon.get(i)) rightChildren[i].diffType = DiffTypes.INSERTED;
    }
  }

  public class DiffReader implements DirectoryHandle.Reader {

    boolean left;
    TreeS[] dirs = new TreeS[1], files = new TreeS[1];
    int dP = 0, fP = 0;

    public DiffReader(boolean left) {
      this.left = left;
    }

    @Override
    public void onDirectory(DirectoryHandle dir) {
      var d = new TreeS(dir, true);
      dirs = ArrayOp.addAt(d, dirs, dP++);
    }

    @Override
    public void onFile(FileHandle file) {
      var f = new TreeS(file, false);
      files = ArrayOp.addAt(f, files, fP++);
    }

    @Override
    public void onComplete() {
      Arrays.sort(dirs, 0, dP, Comparator.comparing(a -> a.name));
      Arrays.sort(files, 0, fP, Comparator.comparing(a -> a.name));
      TreeS[] children = ArrayOp.add(dirs, dP, files, fP, new TreeS[fP + dP]);
      if (left) sendLeft(children);
      else sendRight(children);
    }
  }
}