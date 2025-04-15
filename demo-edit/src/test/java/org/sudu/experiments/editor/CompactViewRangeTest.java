package org.sudu.experiments.editor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sudu.experiments.math.ArrayOp;

class CompactViewRangeTest {

  @Test
  void testCompactViewRange() {
    var model1 = new CompactViewRange[] {
        new CompactViewRange(1, 5, true)};

    t1(model1);

    var model2 = ArrayOp.add(model1,
      new CompactViewRange(8, 11, true));

    int i3 = CompactViewRange.binSearch(7, model2);
    Assertions.assertEquals(1, i3);
    Assertions.assertTrue(7 < model2[i3].startLine);

    t1(model2);
  }

  private static void t1(CompactViewRange[] m) {
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

}
