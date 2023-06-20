package org.sudu.experiments.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ArrayWriterTest {
  @Test
  void fixedTest() {
    ArrayWriter aw = new ArrayWriter(15);
    test15(write15(aw));
  }

  @Test
  void expandTest() {
    ArrayWriter aw = new ArrayWriter();
    test15(write15(aw));
  }

  private void test15(int[] ints) {
    Assertions.assertEquals(ints.length, 15);
    for (int i = 0; i < ints.length; i++) {
      Assertions.assertEquals(ints[i], i + 1);
    }
  }

  private int[] write15(ArrayWriter aw) {
    aw.write(1);
    aw.write(2,3);
    aw.write(4,5,6);
    aw.write(7,8,9, 10);
    aw.write(11,12,13,14,15);
    return aw.getInts();
  }
}