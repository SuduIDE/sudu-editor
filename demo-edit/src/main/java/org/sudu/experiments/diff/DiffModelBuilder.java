package org.sudu.experiments.diff;

import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffRange;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.ui.TreeNode;

import java.util.Arrays;

public class DiffModelBuilder {

  // left.length == right.length
  public static DiffInfo getDiffInfo(TreeNode[] left, TreeNode[] right) {
    DiffRange[] ranges = new DiffRange[1];
    int ptr = 0;

    int i = 0;
    while (i < left.length) {
      int diffType = left[i].diffType;
      int begin = i;
      while (i < left.length && diffType == left[i].diffType) i++;
      var range = new DiffRange(begin, i - begin, begin, i - begin, diffType);
      ranges = ArrayOp.addAt(range, ranges, ptr++);
    }
    return new DiffInfo(null, null, Arrays.copyOf(ranges, ptr));
  }
}
