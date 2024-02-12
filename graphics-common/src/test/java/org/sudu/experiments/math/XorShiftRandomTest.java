package org.sudu.experiments.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class XorShiftRandomTest {
  record R(int sA, int sB, int i) {}

  @Test @Disabled
  void testNextInt() {
    XorShiftRandom rrr = new XorShiftRandom((int) (Math.random() * 0x1000000), 0);

    int max = 17;
    int[] stats = new int[max];
    for (int i = 0; i < 1_000_000_000; i++) {
      stats[rrr.nextInt(max)]++;
    }
    System.out.println(Arrays.toString(stats));
  }

  @Test @Disabled
  void testPoissonNumber() {
    XorShiftRandom rrr = new XorShiftRandom((int) (Math.random() * 0x1000000), 0);

    iterPoisson(rrr, 1);
    iterPoisson(rrr, 17);
  }

  private static void iterPoisson(XorShiftRandom rrr, double frequency) {
    int sum = 0, N = 1_000_000;
    for (int i = 0; i < N; i++) {
      sum += rrr.poissonNumber(frequency);
    }
    System.out.println("frequency = " + frequency);
    System.out.println("sum/N = " + (double)sum / N);
  }

  @Test @Disabled
  void testSh() {
    Integer[] base = new Integer[20];
    for (int i = 0; i < base.length; i++) {
      base[i] = i;
    }

    XorShiftRandom r = new XorShiftRandom();

    for (int j = 0; j < 100; j++) {
      Integer[] copy = Arrays.copyOf(base, base.length);
      r.shuffle(copy);
      System.out.println("[" + j + "] = " + Arrays.toString(copy));
      Arrays.sort(copy);
      Assertions.assertArrayEquals(copy, base);
    }
  }

  @Test @Disabled
  void testZeroes() {
    XorShiftRandom rrr = new XorShiftRandom((int) (Math.random() * 0x1000000) + 1, 0);

    R[] r = new R[100];

    for (int y = 0, p = 0, pp = 0, i = 0; y < 10; y++) {
      long t = System.nanoTime();
      do {
        if (rrr.next() == 0) {
          r[p++] = new R(rrr.seedA, rrr.seedB, i);
        }
      } while (++i != 0);

      t = System.nanoTime() - t;
      System.out.print(" * 4Gb done, t = " + (t + 500_000) / 1_000_000 + "ms");
      if (pp != p) System.out.println();
      for (int j = pp; j < p; j++) {
        R s = r[j];
        System.out.println("[" + j + "]found 0: seedA = " + s.sA + ", seedB = " + s.sB + ", I = " + s.i);
      }
      pp = p;
    }
  }

  public static void main(String[] args) {
    XorShiftRandom rrr = new XorShiftRandom((int) (Math.random() * 0x1000000), 0);
    System.out.println("int");
    if (1>0) for (int i = 0; i < 10000; i++) {
      System.out.println(rrr.next());
    }
    System.out.println("float");
    if (1>0)  for (int i = 0; i < 10000; i++) {
      float nextFloat = rrr.nextFloat();
      System.out.println(
          "0b" + Integer.toBinaryString(Float.floatToRawIntBits(nextFloat)) +
          ", " + Float.toHexString(nextFloat)
              + " = " + nextFloat);
    }
    System.out.println("double");
    if (1>0) for (int i = 0; i < 10000; i++) {
      System.out.println(rrr.nextDouble());
    }
  }

  @Test
  void fillTest() {
    fillTest(1);
    fillTest(3);
    fillTest(5);
  }

  private void fillTest(int n) {
    byte[] data = new byte[n];
    new XorShiftRandom().fill(data);
//    System.out.println("data = " + Arrays.toString(data));
  }
}
