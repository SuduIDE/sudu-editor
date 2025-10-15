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

  static void buildDocIndex(
      DiffRange[] ranges,
      int[] lines, boolean[] index,
      int[] lLn, int[] rLn
  ) {
    int pos = 0;
    for (DiffRange range : ranges) {
      switch (range.type) {
        case DiffTypes.DEFAULT ->
            pos = addDefaultRange(range, lines, index, lLn, rLn, pos);
        case DiffTypes.DELETED ->
            pos = addLeftRange(range, lines, index, lLn, rLn, pos);
        case DiffTypes.INSERTED ->
            pos = addRightRange(range, lines, index, lLn, rLn, pos);
        case DiffTypes.EDITED -> {
          pos = addLeftRange(range, lines, index, lLn, rLn, pos);
          pos = addRightRange(range, lines, index, lLn, rLn, pos);
        }
      }
    }
  }

  private static int addDefaultRange(
      DiffRange range, int[] lines, boolean[] index,
      int[] lLn, int[] rLn, int pos
  ) {
    int end = pos + range.lenR;
    Arrays.fill(index, pos, end, true);
    ArrayOp.fillSequence(lines, pos, end, range.fromR);
    ArrayOp.fillSequence(rLn, pos, end, range.fromR);
    ArrayOp.fillSequence(lLn, pos, end, range.fromL);
    pos = end;
    return pos;
  }

  static int addRightRange(
      DiffRange range, int[] lines, boolean[] index,
      int[] lLn, int[] rLn, int pos
  ) {
    int end = pos + range.lenR;
    Arrays.fill(index, pos, end, true);
    ArrayOp.fillSequence(lines, pos, end, range.fromR);
    ArrayOp.fillSequence(rLn, pos, end, range.fromR);
    Arrays.fill(lLn, pos, end, -1);
    return end;
  }

  static int addLeftRange(
      DiffRange range, int[] lines, boolean[] index,
      int[] lLn, int[] rLn, int pos
  ) {
    int end = pos + range.lenL;
    Arrays.fill(index, pos, end, false);
    ArrayOp.fillSequence(lines, pos, end, range.fromL);
    ArrayOp.fillSequence(lLn, pos, end, range.fromL);
    Arrays.fill(rLn, pos, end, -1);
    return end;
  }
}
