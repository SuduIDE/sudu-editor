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
    LineDiff[] leftLines = new LineDiff[left.length];
    LineDiff[] rightLines = new LineDiff[right.length];
    for (int i = 0; i < left.length; i++) leftLines[i] = new LineDiff(left[i].diffType);
    for (int i = 0; i < right.length; i++) rightLines[i] = new LineDiff(right[i].diffType);
    int ptr = 0;

    int i = 0;
    while (i < left.length) {
      int begin = i;
      while (i < left.length && left[begin].diffType == left[i].diffType) i++;
      int diffType = left[begin].diffType == DiffTypes.DEFAULT ? DiffTypes.DEFAULT : DiffTypes.FOLDER_ALIGN_DIFF_TYPE;
      var range = new DiffRange(begin, i - begin, begin, i - begin, diffType);
      ranges = ArrayOp.addAt(range, ranges, ptr++);
    }
    return new DiffInfo(leftLines, rightLines, Arrays.copyOf(ranges, ptr));
  }
}
