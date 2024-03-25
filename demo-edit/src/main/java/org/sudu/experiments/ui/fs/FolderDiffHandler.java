package org.sudu.experiments.ui.fs;

import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.Diff;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.LCS;
import org.sudu.experiments.math.ArrayOp;

import java.util.ArrayList;
import java.util.function.Consumer;

public class FolderDiffHandler {

  TreeS[] leftChildren, rightChildren;
  Consumer<Object[]> r;

  public FolderDiffHandler(Consumer<Object[]> r) {
    this.r = r;
  }

  public void sendLeft(TreeS[] leftChildren) {
    this.leftChildren = leftChildren;
    if (rightChildren != null) compare();
  }

  public void sendRight(TreeS[] rightChildren) {
    this.rightChildren = rightChildren;
    if (this.leftChildren != null) compare();
  }

  public void compare() {
    var leftC = leftChildren;
    var rightC = rightChildren;

    var lcs = new LCS<>(leftC, rightC);
    lcs.countAll();
    for (var range : lcs.ranges) {
      if (range instanceof Diff<TreeS> diff) {
        for (int i = 0; i < diff.lengthL(); i++)
          leftChildren[diff.fromL + i].diffType = DiffTypes.DELETED;
        for (int i = 0; i < diff.lengthR(); i++)
          rightChildren[diff.fromR + i].diffType = DiffTypes.INSERTED;
      }
    }

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
}