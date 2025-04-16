package org.sudu.experiments.editor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.V2i;

class CompactViewRangeTest {

  @Test
  void testCompactViewRange() {
    var model1 = model1();

    t1(model1);

    var model2 = model2(model1);

    t1(model2);
    t2(model2);

  }

  static void t2(CompactViewRange[] model2) {
    {
      int line = 7;
      int i3 = CompactViewRange.binSearch(line, model2);
      Assertions.assertEquals(1, i3);
      Assertions.assertTrue(line < model2[i3].startLine);
      Assertions.assertTrue(model2[i3 - 1].endLine <= line);
    }
    {
      int line2 = 8;
      int i4 = CompactViewRange.binSearch(line2, model2);
      Assertions.assertEquals(1, i4);
      Assertions.assertEquals(line2, model2[i4].startLine);
    }
    {
      int line3 = 10;
      int i5 = CompactViewRange.binSearch(line3, model2);
      Assertions.assertEquals(1, i5);
      Assertions.assertTrue(
          model2[i5].startLine < line3 &&
              line3 < model2[i5].endLine);
    }
  }

  // [1..5)
  static CompactViewRange[] model1() {
    return new CompactViewRange[]{
        new CompactViewRange(1, 5, true)};
  }

  // [8..11)
  static CompactViewRange[] model2() {
    return model2(model1());
  }

  static CompactViewRange[] model2(CompactViewRange[] model1) {
    return ArrayOp.add(model1,
        new CompactViewRange(8, 11, false));
  }

  // [12..17)

  static CompactViewRange[] model3() {
    return model3(model2());
  }

  static CompactViewRange[] model3(CompactViewRange[] model3) {
    return ArrayOp.add(model3,
        new CompactViewRange(12, 17, true));
  }

  static void t1(CompactViewRange[] m) {
    int i0 = CompactViewRange.binSearch(-1, m);
    int i1 = CompactViewRange.binSearch(1, m);
    int i2 = CompactViewRange.binSearch(5, m);
    int i3 = CompactViewRange.binSearch(7, m);
    int i4 = CompactViewRange.binSearch(10, m);

    Assertions.assertEquals(0, i0);
    Assertions.assertEquals(0, i1);
    Assertions.assertEquals(0, i2);
    Assertions.assertEquals(1, i3);
    Assertions.assertEquals(1, i4);

    Assertions.assertTrue(true);
  }


  @Test
  void testCompactCodeView() {
    CompactCodeView v = new CompactCodeView(model3());
    V2i[] viewToDocVerifyTable = new V2i[]{
        // [1..5)  visible
        new V2i(0, 1),
        new V2i(1, 2),
        new V2i(2, 3),
        new V2i(3, 4),
        // [8..11)  invisible
        new V2i(4, -1),
        // [12..17)  visible
        new V2i(5, 12),
        new V2i(6, 13),
        new V2i(7, 14),
        new V2i(8, 15),
        new V2i(9, 16)
    };


    for (V2i pair : viewToDocVerifyTable) {
      int docLine = v.viewToDoc(pair.x);
      Assertions.assertEquals(docLine, pair.y);
    }
  }
}
