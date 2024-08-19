package org.sudu.experiments.diff;

import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffRange;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.ui.TreeNode;

import java.util.Arrays;

public class DiffModelBuilder {

  public static DiffInfo getDiffInfo(TreeNode[] left, TreeNode[] right) {
    DiffRange[] ranges = new DiffRange[1];
    int ptr = 0;
    int lP = 0, rP = 0;
    while (lP < left.length && rP < right.length) {
      boolean changed = false;
      int diffType = left[lP].diffType;
      int leftDiff;
      int lenL = 0, lenR = 0;
      while (lP < left.length && rP < right.length
          && (leftDiff = left[lP].diffType) == right[rP].diffType
          && leftDiff == diffType) {
        changed = true;
        lP++;
        lenL++;
        rP++;
        lenR++;
      }
      if (changed) {
        var range = new DiffRange(lP - lenL, lenL, rP - lenR, lenR, diffType);
        ranges = ArrayOp.addAt(range, ranges, ptr++);
        continue;
      }
      boolean leftDepth = left[lP].depth > right[rP].depth;
      if (leftDepth) {
        DiffRange range = handleDeleted(left, lP, rP);
        if (range != null) {
          lP += range.lenL;
          ranges = ArrayOp.addAt(range, ranges, ptr++);
          continue;
        }
        range = handleInserted(right, rP, lP);
        if (range != null) {
          rP += range.lenR;
          ranges = ArrayOp.addAt(range, ranges, ptr++);
          continue;
        }
      } else {
        DiffRange range = handleInserted(right, rP, lP);
        if (range != null) {
          rP += range.lenR;
          ranges = ArrayOp.addAt(range, ranges, ptr++);
          continue;
        }
        range = handleDeleted(left, lP, rP);
        if (range != null) {
          lP += range.lenL;
          ranges = ArrayOp.addAt(range, ranges, ptr++);
          continue;
        }
      }
      if (left[lP].diffType == DiffTypes.DEFAULT || left[lP].diffType == DiffTypes.EDITED) {
        var range = new DiffRange(lP, 1, rP, 0, left[lP].diffType);
        ranges = ArrayOp.addAt(range, ranges, ptr++);
        lP++;
      } else if (right[rP].diffType == DiffTypes.DEFAULT || right[rP].diffType == DiffTypes.EDITED) {
        var range = new DiffRange(lP, 0, rP, 1, right[rP].diffType);
        ranges = ArrayOp.addAt(range, ranges, ptr++);
        rP++;
      } else {
        throw new IllegalStateException();
      }
    }
    if (lP < left.length) {
      var range = handleDeleted(left, lP, rP);
      if (range != null) ranges = ArrayOp.addAt(range, ranges, ptr++);
    }
    if (rP < right.length) {
      var range = handleInserted(right, rP, lP);
      if (range != null) ranges = ArrayOp.addAt(range, ranges, ptr++);
    }
    while (lP < left.length) {
      var range = new DiffRange(lP, 1, rP, 0, left[lP].diffType);
      ranges = ArrayOp.addAt(range, ranges, ptr++);
      lP++;
    }
    while (rP < right.length) {
      var range = new DiffRange(lP, 0, rP, 1, right[rP].diffType);
      ranges = ArrayOp.addAt(range, ranges, ptr++);
      rP++;
    }
    return new DiffInfo(null, null, Arrays.copyOf(ranges, ptr));
  }

  static DiffRange handleDeleted(TreeNode[] left, int lP, int rP) {
    if (left[lP].diffType == DiffTypes.DELETED) {
      int len = 0;
      while (lP < left.length && left[lP].diffType == DiffTypes.DELETED) {
        lP++;
        len++;
      }
      return new DiffRange(lP - len, len, rP, 0, DiffTypes.DELETED);
    }
    return null;
  }

  static DiffRange handleInserted(TreeNode[] right, int rP, int lP) {
    if (right[rP].diffType == DiffTypes.INSERTED) {
      int len = 0;
      while (rP < right.length && right[rP].diffType == DiffTypes.INSERTED) {
        rP++;
        len++;
      }
      return new DiffRange(lP, 0, rP - len, len, DiffTypes.INSERTED);
    }
    return null;
  }
}
