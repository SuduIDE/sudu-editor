package org.sudu.experiments.editor;

import org.junit.jupiter.api.Test;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.editor.worker.diff.DiffRange;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlignedDiffMappingTest {

  @Test
  void defaultRangesStayOnTheSameViewLines() {
    DiffRange[] ranges = {
        new DiffRange(0, 3, 0, 3, DiffTypes.DEFAULT),
        new DiffRange(3, 2, 3, 2, DiffTypes.DEFAULT)
    };
    var left = new AlignedDiffMapping(ranges, true);
    var right = new AlignedDiffMapping(ranges, false);

    assertEquals(5, left.length());
    assertEquals(5, right.length());
    for (int i = 0; i < 5; i++) {
      assertEquals(i, left.viewToDoc(i));
      assertEquals(i, right.viewToDoc(i));
      assertEquals(i, left.docToView(i));
      assertEquals(i, right.docToView(i));
    }
  }

  @Test
  void editedRangePadsTheShorterSide() {
    // 3 lines edited to 1: left 3 edited, right 1 edited + 2 blank
    DiffRange[] ranges = {
        new DiffRange(0, 2, 0, 2, DiffTypes.DEFAULT),
        new DiffRange(2, 3, 2, 1, DiffTypes.EDITED),
        new DiffRange(5, 2, 3, 2, DiffTypes.DEFAULT)
    };
    var left = new AlignedDiffMapping(ranges, true);
    var right = new AlignedDiffMapping(ranges, false);

    assertEquals(7, left.length());
    assertEquals(7, right.length());

    assertEquals(2, left.viewToDoc(2));
    assertEquals(3, left.viewToDoc(3));
    assertEquals(4, left.viewToDoc(4));
    assertEquals(CodeLineMapping.outOfRange, right.viewToDoc(3));
    assertEquals(CodeLineMapping.outOfRange, right.viewToDoc(4));
    assertEquals(2, right.viewToDoc(2));

    assertEquals(5, left.viewToDoc(5));
    assertEquals(3, right.viewToDoc(5));
    assertEquals(5, left.docToView(5));
    assertEquals(5, right.docToView(3));

    assertEquals(DiffTypes.EDITED, right.viewLineDiffType(3));
    assertEquals(DiffTypes.EDITED, right.viewLineDiffType(4));
    assertEquals(DiffTypes.DEFAULT, left.viewLineDiffType(5));
  }

  @Test
  void deletedRangeFillsBlanksOnTheRight() {
    DiffRange[] ranges = {
        new DiffRange(0, 1, 0, 1, DiffTypes.DEFAULT),
        new DiffRange(1, 3, 1, 0, DiffTypes.DELETED),
        new DiffRange(4, 1, 1, 1, DiffTypes.DEFAULT)
    };
    var left = new AlignedDiffMapping(ranges, true);
    var right = new AlignedDiffMapping(ranges, false);

    assertEquals(5, left.length());
    assertEquals(5, right.length());

    assertEquals(1, left.viewToDoc(1));
    assertEquals(2, left.viewToDoc(2));
    assertEquals(3, left.viewToDoc(3));
    assertEquals(CodeLineMapping.outOfRange, right.viewToDoc(1));
    assertEquals(CodeLineMapping.outOfRange, right.viewToDoc(2));
    assertEquals(CodeLineMapping.outOfRange, right.viewToDoc(3));
    assertEquals(4, left.viewToDoc(4));
    assertEquals(1, right.viewToDoc(4));
    assertEquals(DiffTypes.DELETED, right.viewLineDiffType(2));
  }

  @Test
  void insertedRangeFillsBlanksOnTheLeft() {
    DiffRange[] ranges = {
        new DiffRange(0, 1, 0, 1, DiffTypes.DEFAULT),
        new DiffRange(1, 0, 1, 2, DiffTypes.INSERTED),
        new DiffRange(1, 1, 3, 1, DiffTypes.DEFAULT)
    };
    var left = new AlignedDiffMapping(ranges, true);
    var right = new AlignedDiffMapping(ranges, false);

    assertEquals(4, left.length());
    assertEquals(4, right.length());

    assertEquals(CodeLineMapping.outOfRange, left.viewToDoc(1));
    assertEquals(CodeLineMapping.outOfRange, left.viewToDoc(2));
    assertEquals(1, right.viewToDoc(1));
    assertEquals(2, right.viewToDoc(2));
    assertEquals(1, left.viewToDoc(3));
    assertEquals(3, right.viewToDoc(3));
    assertEquals(3, left.docToView(1));
    assertEquals(DiffTypes.INSERTED, left.viewLineDiffType(1));
  }

  @Test
  void laterDefaultRangesStayAlignedAfterMixedHunks() {
    DiffRange[] ranges = {
        new DiffRange(0, 2, 0, 2, DiffTypes.DEFAULT),
        new DiffRange(2, 3, 2, 1, DiffTypes.EDITED),
        new DiffRange(5, 1, 3, 1, DiffTypes.DEFAULT),
        new DiffRange(6, 0, 4, 2, DiffTypes.INSERTED),
        new DiffRange(6, 2, 6, 2, DiffTypes.DEFAULT)
    };
    var left = new AlignedDiffMapping(ranges, true);
    var right = new AlignedDiffMapping(ranges, false);

    assertEquals(10, left.length());
    assertEquals(10, right.length());

    assertEquals(5, left.viewToDoc(5));
    assertEquals(3, right.viewToDoc(5));
    assertEquals(5, left.docToView(5));
    assertEquals(5, right.docToView(3));

    assertEquals(CodeLineMapping.outOfRange, left.viewToDoc(6));
    assertEquals(CodeLineMapping.outOfRange, left.viewToDoc(7));
    assertEquals(4, right.viewToDoc(6));
    assertEquals(5, right.viewToDoc(7));

    assertEquals(6, left.viewToDoc(8));
    assertEquals(6, right.viewToDoc(8));
    assertEquals(8, left.docToView(6));
    assertEquals(8, right.docToView(6));
  }

  @Test
  void leadingInsertPadsTheLeftFromTheFirstLine() {
    DiffRange[] ranges = {
        new DiffRange(0, 0, 0, 2, DiffTypes.INSERTED),
        new DiffRange(0, 3, 2, 3, DiffTypes.DEFAULT)
    };
    var left = new AlignedDiffMapping(ranges, true);
    var right = new AlignedDiffMapping(ranges, false);

    assertEquals(5, left.length());
    assertEquals(5, right.length());
    assertEquals(CodeLineMapping.outOfRange, left.viewToDoc(0));
    assertEquals(CodeLineMapping.outOfRange, left.viewToDoc(1));
    assertEquals(0, right.viewToDoc(0));
    assertEquals(0, left.viewToDoc(2));
    assertEquals(2, left.docToView(0));
    assertEquals(2, right.docToView(2));
  }
}
