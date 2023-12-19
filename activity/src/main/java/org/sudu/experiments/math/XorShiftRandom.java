package org.sudu.experiments.math;

// XorShift RGN with 64 bit state
// Paper: PDF "Xorshift RNGs" by George Marsaglia, 2003

public class XorShiftRandom {
  int seedA, seedB;

  public XorShiftRandom() {
    this(roll_7_1_9((int) System.nanoTime()), ((int) System.nanoTime()) ^ 0xDeadBeef);
  }

  public XorShiftRandom(int seedA, int seedB) {
    this.seedA = seedA;
    this.seedB = seedB;
    for (int i = 0; i < 19; i++) next();
  }

  public int next() {
    int v = roll_2_7_3(seedA, seedB);
    seedA = seedB;
    seedB = v;
    return v;
  }

  static int roll_2_7_3(int a, int b) {
    int t = a ^ (a << 2);
    int k = t ^ (t >>> 7);
    return (b ^ (b >>> 3)) ^ k;
  }

  public static int roll_7_1_9(int x) {
    x ^= x << 7;
    x ^= x >>> 1;
    x ^= x << 9;
    return x;
  }

  public static double intToDouble01(int next) {
    double bits32 = 0x1p-32 * next;
    return bits32 < 0 ? bits32 + 1 : bits32;
  }

  // return [0 .. limit)
  public int nextInt(int limit) {
    return (int) (nextFloat() * limit);
  }

  // returns [0 .. 1) with 24 bits of randomness
  public float nextFloat() {
    int bits24 = next() & 0xFFFFFF;
    return 0x1p-24f * bits24;
  }

  // returns [0 .. 1) with 32 random mantissa bits
  public final double nextDouble() {
    return intToDouble01(next());
  }

  public <T> void shuffle(T[] array) {
    for (int i = array.length - 1; i > 0; i--) {
      int r = nextInt(i + 1);
      if (r != i) {
        T a = array[i];
        T b = array[r];
        array[r] = a;
        array[i] = b;
      }
    }
  }

  public void fill(byte[] data) {
    int n = (data.length / 4) * 4;
    for (int i = 0; i < n; i+=4) {
      int next = next();
      data[i] = (byte) next;
      data[i + 1] = (byte) (next >>> 8);
      data[i + 2] = (byte) (next >>> 16);
      data[i + 3] = (byte) (next >>> 24);
    }
    if (n < data.length) {
      for (int next = next(); n < data.length; n++) {
        data[n] = (byte) next;
        next >>>= 8;
      }
    }
  }
}
