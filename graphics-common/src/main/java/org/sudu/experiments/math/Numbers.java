package org.sudu.experiments.math;

public class Numbers {
  // this function compute integer division rounded: [x / y + 0.5] = [(x + y * 0.5) / y]
  public static int iDivRound(int numerator, int denominator) {
    return (numerator + denominator / 2) / denominator;
  }

  public static int divRound(int a, int b, int denominator) {
    return (int) (.5 + ((double) a * b) / denominator);
  }

  public static int iDivRoundUp(int numerator, int denominator) {
    return (numerator + denominator - 1) / denominator;
  }

  public static int iRnd(double x) {
    return (int) (x + .5f);
  }

  public static int iRnd(float x) {
    return (int) (x + .5f);
  }

  public static int clamp(int min, int value, int max) {
    return Math.max(min, Math.min(value, max));
  }

  public static double log(double x, double base) {
    return Math.log(x) / Math.log(base);
  }

  public static int numDecimalDigits(int x) {
    int d = 1, n = 10;
    while (d < 10 && x >= n) {
      d++;
      n *= 10;
    }
    return d;
  }
}
