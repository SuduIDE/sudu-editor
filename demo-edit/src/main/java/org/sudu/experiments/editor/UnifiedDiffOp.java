package org.sudu.experiments.editor;

import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.editor.worker.diff.DiffRange;
import org.sudu.experiments.math.ArrayOp;

import java.util.Arrays;

interface UnifiedDiffOp {
  static int unifiedSize(DiffRange[] ranges) {
    int size = 0;
    for (DiffRange range : ranges) {
      switch (range.type) {
        case DiffTypes.DEFAULT, DiffTypes.INSERTED -> size += range.lenR;
        case DiffTypes.DELETED -> size += range.lenL;
        case DiffTypes.EDITED -> size += range.lenL + range.lenR;
      }
    }
    return size;
  }

  static void buildDocIndex(DiffRange[] ranges, int[] lines, boolean[] index) {
    int pos = 0;
    for (DiffRange range : ranges) {
      switch (range.type) {
        case DiffTypes.DEFAULT, DiffTypes.INSERTED ->
            pos = addRangeR(range, pos, lines, index);
        case DiffTypes.DELETED ->
            pos = addRangeL(range, pos, lines, index);
        case DiffTypes.EDITED -> {
          pos = addRangeL(range, pos, lines, index);
          pos = addRangeR(range, pos, lines, index);
        }
      }
    }
  }

  static int addRangeL(DiffRange r, int pos, int[] lines, boolean[] index) {
    ArrayOp.fillSequence(lines, pos, pos + r.lenL, r.fromL);
    Arrays.fill(index, pos, pos + r.lenL, false);
    pos += r.lenL;
    return pos;
  }

  static int addRangeR(DiffRange r, int pos, int[] lines, boolean[] index) {
    ArrayOp.fillSequence(lines, pos, pos + r.lenR, r.fromR);
    Arrays.fill(index, pos, pos + r.lenR, true);
    pos += r.lenR;
    return pos;
  }
}
