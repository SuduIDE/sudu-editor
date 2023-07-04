package org.sudu.experiments.math;

public class Numbers {
  // this function compute integer division rounded: [x / y + 0.5] = [(x + y * 0.5) / y]
  public static int iDivRound(int numerator, int denominator) {
    return (numerator + denominator / 2) / denominator;
  }

  // multiply without overflow
  public static int iDivRound(int a, int b, int denominator) {
    if (a < Integer.MAX_VALUE / b) return iDivRound(a * b, denominator);
    return (int) (.5 + ((double) b * a) / denominator);
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
}
