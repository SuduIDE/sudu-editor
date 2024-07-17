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
    var merged = countDiffs();
    onCompared(merged);
  }

  protected void onCompared(TreeS[] merged) {
    ArrayWriter writer = new ArrayWriter();

    // Write diff types
    writer.write(merged.length);
    writer.write(leftChildren.length);
    writer.write(rightChildren.length);
    for (var child: merged) writer.write(child.diffType);

    // Write fs items
    var result = new ArrayList<>();
    result.add(writer.getInts());
    for (var child: leftChildren) result.add(child.item);
    for (var child: rightChildren) result.add(child.item);
    ArrayOp.sendArrayList(result, r);
  }

  protected TreeS[] countDiffs() {
    var result = DiffModel.countFolderCommon(leftChildren, rightChildren);
    int commonLen = result.first;
    int leftLen = leftChildren.length;
    int rightLen = rightChildren.length;
    var commons = result.second;

    BitSet leftCommon = commons[0], rightCommon = commons[1];
    int mergedLen = leftChildren.length + rightChildren.length - commonLen;
    TreeS[] merged = new TreeS[mergedLen];
    int mP = 0;
    int lP = 0, rP = 0;

    while (lP < leftLen && rP < rightLen) {
      if (!leftCommon.get(lP)) {
        merged[mP] = leftChildren[lP++];
        merged[mP++].diffType = DiffTypes.DELETED;
      } else if (!rightCommon.get(rP)) {
        merged[mP] = rightChildren[rP++];
        merged[mP++].diffType = DiffTypes.INSERTED;
      } else {
        merged[mP++] = leftChildren[lP];
        lP++; rP++;
      }
    }
    while (lP < leftLen) {
      merged[mP] = leftChildren[lP++];
      merged[mP++].diffType = DiffTypes.DELETED;
    }
    while (rP < rightLen) {
      merged[mP] = rightChildren[rP++];
      merged[mP++].diffType = DiffTypes.INSERTED;
    }
    return merged;
  }
}