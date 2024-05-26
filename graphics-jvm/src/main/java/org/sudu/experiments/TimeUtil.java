package org.sudu.experiments;

import java.util.function.DoubleSupplier;

public interface TimeUtil {

  double nsToS = 1. / 1_000_000_000.;

  static DoubleSupplier dt() {
    return new DoubleSupplier() {
      long t0 = System.nanoTime();
      @Override
      public double getAsDouble() {
        long t1 = System.nanoTime();
        long dT = t1 - t0;
        t0 = t1;
        return dT * nsToS;
      }

      @Override
      public String toString() {
        return toString3(getAsDouble());
      }
    };
  }

  static double now() {
    return System.nanoTime() * nsToS;
  }

  static String toString3(double t) {
    int t3 = (int) (t * 1000);
    int s = t3 / 1000, ms = t3 % 1000;
    char[] c4 = new char[4]; c4[0] = '.';
    for (int i = 0; i != 3; ms /= 10, ++i) {
      c4[3 - i] = (char) ('0' + (ms % 10));
    }
    return Integer.toString(s).concat(new String(c4));
  }
}
