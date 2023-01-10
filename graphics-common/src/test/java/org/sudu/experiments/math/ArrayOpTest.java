package org.sudu.experiments.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArrayOpTest {
  @Test
  void writeInt() {
    XorShiftRandom r = new XorShiftRandom();

    byte[] data = new byte[17];
    int value0 = r.nextInt(0x10000);

    int pos0 = 2;
    int pos1 = ArrayOp.writeInt16Le(data, pos0, value0);
    Assertions.assertEquals(pos1, pos0 + 2);

    int value1 = ArrayOp.readInt16Le(data, pos0);
    Assertions.assertEquals(value0, value1);

    int pos2 = ArrayOp.writeInt32Le(data, pos1, value0);
    Assertions.assertEquals(pos2, pos1 + 4);

    int value2 = ArrayOp.readInt32Le(data, pos1);
    Assertions.assertEquals(value0, value2);

  }

  @Test
  void deleteTest() {
    String[] from = new String[] { "A", "B", "C", "D", "E"};
    String[] r1 = ArrayOp.remove(from, 0, 3, new String[2]);
    String[] r2 = ArrayOp.remove(from, 1, 4, new String[2]);
    String[] r3 = ArrayOp.remove(from, 2, 5, new String[2]);
    Assertions.assertArrayEquals(new String[] {"D", "E"}, r1);
    Assertions.assertArrayEquals(new String[] {"A", "E"}, r2);
    Assertions.assertArrayEquals(new String[] {"A", "B"}, r3);
  }
}
