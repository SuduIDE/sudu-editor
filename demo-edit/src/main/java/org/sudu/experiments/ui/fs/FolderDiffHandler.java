package org.sudu.experiments.ui.fs;

import org.sudu.experiments.DirectoryHandle;
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
    left.read(new DiffReader(this::sendLeft));
    right.read(new DiffReader(this::sendRight));
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
}