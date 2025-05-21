package org.sudu.experiments.editor;

import org.junit.jupiter.api.Test;
import org.sudu.experiments.math.V2i;

import static org.junit.jupiter.api.Assertions.*;

class CompactViewRangeTest {

  @Test
  void testCompactViewRange() {
    var model1 = model1();

    t_1_5(model1);

    var model2 = model2();

    t_1_5(model2);
    t2(model2);

  }

  static void t2(CompactViewRange[] model2) {
    {
      int line = 7;
      int i3 = CompactViewRange.binSearch(line, model2);
      assertEquals(1, i3);
      assertTrue(line < model2[i3].startLine);
      assertTrue(model2[i3 - 1].endLine <= line);
    }
    {
      int line2 = 8;
      int i4 = CompactViewRange.binSearch(line2, model2);
      assertEquals(1, i4);
      assertEquals(line2, model2[i4].startLine);
    }
    {
      int line3 = 10;
      int i5 = CompactViewRange.binSearch(line3, model2);
      assertEquals(1, i5);
      assertTrue(
          model2[i5].startLine < line3 &&
              line3 < model2[i5].endLine);
    }
  }

  static CompactViewRange[] model1() {
    return new CompactViewRange[]{
        _1_5_visible()
    };
  }

  static CompactViewRange[] model2() {
    return  new CompactViewRange[]{
        _1_5_visible(),
        _8_11_invisible()
    };
  }

  static CompactViewRange[] model3() {
    return  new CompactViewRange[]{
        _1_5_visible(),
        _8_11_invisible(),
        _12_17_visible()
    };
  }

  static CompactViewRange _1_5_visible() {
    return new CompactViewRange(1, 5, true);
  }

  static CompactViewRange _8_11_invisible() {
    return new CompactViewRange(8, 11, false);
  }

  static CompactViewRange _12_17_visible() {
    return new CompactViewRange(12, 17, true);
  }

  static void t_1_5(CompactViewRange[] m) { // [1..5)
    int i0 = CompactViewRange.binSearch(-1, m);
    int i1 = CompactViewRange.binSearch(1, m);
    int i2 = CompactViewRange.binSearch(5, m);
    int i3 = CompactViewRange.binSearch(7, m);
    int i4 = CompactViewRange.binSearch(10, m);

    assertEquals(0, i0);
    assertEquals(0, i1);
    assertEquals(1, i2);
    assertTrue(i3 >= 1);
    assertTrue(i4 >= 1);

    assertTrue(true);
  }

  @Test
  void testViewToDoc() {
    CompactCodeMapping v = new CompactCodeMapping(model3());
    V2i[] viewToDocVerifyTable = new V2i[]{
        // [1..5)  visible
        new V2i(0, 1),
        new V2i(1, 2),
        new V2i(2, 3),
        new V2i(3, 4),
        // [8..11)  invisible
        new V2i(4, CodeLineMapping.regionIndex(1)),
        // [12..17)  visible
        new V2i(5, 12),
        new V2i(6, 13),
        new V2i(7, 14),
        new V2i(8, 15),
        new V2i(9, 16),
        new V2i(10, CodeLineMapping.outOfRange),
        new V2i(11, CodeLineMapping.outOfRange)
    };

    for (V2i pair : viewToDocVerifyTable) {
      int docLine = v.viewToDoc(pair.x);
      if (pair.y != docLine)
        fail();
    }
  }

  @Test
  void testDocToView() {
    CompactCodeMapping v = new CompactCodeMapping(model3());

    V2i[] docToViewVerifyTable = new V2i[]{
        new V2i(0, CodeLineMapping.outOfRange),

        // [1..5)  visible  -> [0,1,2,3]
        new V2i(1, 0),
        new V2i(2, 1),
        new V2i(3, 2),
        new V2i(4, 3),

        new V2i(5, CodeLineMapping.outOfRange),
        new V2i(6, CodeLineMapping.outOfRange),
        new V2i(7, CodeLineMapping.outOfRange),

        // [8..11)  invisible -> [4]
        new V2i(8, CodeLineMapping.regionIndex(1)),
        new V2i(9, CodeLineMapping.regionIndex(1)),
        new V2i(10, CodeLineMapping.regionIndex(1)),

        // out of range
        new V2i(11, CodeLineMapping.outOfRange),

        // [12..17)  visible -> [5,6,7,8,9]
        new V2i(12, 5),
        new V2i(13, 6),
        new V2i(14, 7),
        new V2i(15, 8),
        new V2i(16, 9),

        // out of range
        new V2i(17, CodeLineMapping.outOfRange),
        new V2i(18, CodeLineMapping.outOfRange)
    };

    for (V2i pair : docToViewVerifyTable) {
      int viewLine = v.docToView(pair.x);
      if (pair.y != viewLine) {
        System.out.println("fail " + pair);
        fail();
      }
      if (viewLine >= 0) {
        int actualDoc = v.viewToDoc(viewLine);
        assertEquals(pair.x, actualDoc);
      }
    }
  }

  // visible(35, 37),
  @Test
  void testDocToView2() {
    CompactCodeMapping v = new CompactCodeMapping(DebugHelper.t1());
    int toView35 = v.docToView(35);
    int toView36 = v.docToView(36);
    assertEquals(toView36, 1 + toView35);
  }

  static CompactViewRange[] _0_51_v_51_69() {
    return new CompactViewRange[]{
        new CompactViewRange(0, 51, false),
        new CompactViewRange(51, 51, true),
        new CompactViewRange(51, 69, false),
    };
  }

  static CompactViewRange[] _0_51_51_69() {
    return new CompactViewRange[]{
        new CompactViewRange(0, 51, false),
        new CompactViewRange(51, 51, false),
        new CompactViewRange(51, 69, false),
    };
  }

  @Test
  void testViewToDocLinesZeroRange() {
    int[] viewToDocVerify = new int[]{ -2, -4 };

    CompactCodeMapping v1 = new CompactCodeMapping(_0_51_v_51_69());
    testTranslation(v1, viewToDocVerify);
    CompactCodeMapping v2 = new CompactCodeMapping(_0_51_51_69());
    testTranslation(v2, viewToDocVerify);
  }

  static void testTranslation(CompactCodeMapping v, int[] viewToDocVerify) {
    int[] viewToDocTable = new int[20];
    int viewBegin = 0;
    int viewEnd = 2;
    v.viewToDocLines(viewBegin, viewEnd, viewToDocTable);
    assertBeginWith(viewToDocTable, viewToDocVerify);
    for (int i = viewBegin; i < viewEnd; i++) {
      int actualDoc = v.viewToDoc(i);
      assertEquals(viewToDocVerify[i], actualDoc);
//      int actualView = v.docToView(actualDoc);
//      Assertions.assertEquals(i, actualView);
    }
  }

  static void assertBeginWith(int[] result, int[] checker) {
    assertTrue(result.length >= checker.length);
    for (int i = 0; i < checker.length; i++) {
      assertEquals(checker[i], result[i]);
    }
  }

  @Test
  void testInsert0() {
    {
      var r1 = insertTestData();

      CompactViewRange.insertLines(0, 1, r1);
      assertEquals(0, r1[0].startLine);
      assertEquals(2, r1[0].endLine);
      assertEquals(2, r1[1].startLine);
      assertEquals(2, r1[1].endLine);
      assertEquals(2, r1[2].startLine);
      assertEquals(3, r1[2].endLine);
    }

    {
      var r2 = insertTestData();

      CompactViewRange.insertLines(1, 2, r2);
      assertEquals(0, r2[0].startLine);
      assertEquals(1, r2[0].endLine);
      assertEquals(1, r2[1].startLine);
      assertEquals(1, r2[1].endLine);
      assertEquals(1, r2[2].startLine);
      assertEquals(4, r2[2].endLine);
    }

    {
      var r3 = insertTestData();

      CompactViewRange.insertLines(2, 2, r3);
      assertEquals(0, r3[0].startLine);
      assertEquals(1, r3[0].endLine);
      assertEquals(1, r3[1].startLine);
      assertEquals(1, r3[1].endLine);
      assertEquals(1, r3[2].startLine);
      assertEquals(4, r3[2].endLine);
    }
  }

  private static CompactViewRange[] insertTestData() {
    return new CompactViewRange[]{
        new CompactViewRange(0, 1, false),
        new CompactViewRange(1, 1, false),
        new CompactViewRange(1, 2, false),
    };
  }
}
