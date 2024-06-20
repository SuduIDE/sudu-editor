package org.sudu.experiments;

import org.sudu.experiments.text.TextFormat;

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
        return TextFormat.toString3(getAsDouble());
      }
    };
  }

  static double now() {
    return System.nanoTime() * nsToS;
  }

}
