package org.sudu.experiments.win32;

import org.sudu.experiments.CString;
import org.sudu.experiments.TimeUtil;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.XorShiftRandom;

public class CStringPerf {

  static int allLength;
  static double arraysTime, charAtTime, concatTime;

  public static void main(String[] args) {
    String[] strings = ArrayOp.add(
        StringGen.asciiStrings(10240, 20, 30),
        StringGen.strings(2560, 20, 30));

    char[][] result = new char[strings.length][];

    Runnable r1 = () -> it1_10x(strings, result, 100);
    Runnable r2 = () -> it2_10x(strings, result, 100);
    Runnable r3 = () -> it3_10x(strings, result, 100);

    XorShiftRandom r = new XorShiftRandom();

    while (true) {
      experiment(r1, r2, r3, r);

      System.out.println("toChar16CString time:" + TimeUtil.toString3(arraysTime));
      System.out.println("toChar16 charAt time:" + TimeUtil.toString3(charAtTime));
      System.out.println("toChar16 concat time:" + TimeUtil.toString3(concatTime));
    }
  }

  static void experiment(Runnable r1, Runnable r2, Runnable r3, XorShiftRandom r) {
    int f = r.nextInt(3);
    switch (f) {
      case 0 -> { r1.run(); r2.run(); r3.run(); }
      case 1 -> { r2.run(); r3.run(); r1.run(); }
      case 2 -> { r3.run(); r1.run(); r2.run(); }
    }
  }

  static void it1_10x(String[] strings, char[][] result, int N) {
    long t0 = System.nanoTime();
    for (int i = 0; i < N; i++) {
      iteration1(result, strings);
      addLength(result);
    }
    long t1 = System.nanoTime();
    arraysTime += TimeUtil.nsToS * (t1 - t0);
  }

  static void it3_10x(String[] strings, char[][] result, int N) {
    long t0 = System.nanoTime();
    for (int i = 0; i < N; i++) {
      iteration3(result, strings);
      addLength(result);
    }
    long t1 = System.nanoTime();
    concatTime += TimeUtil.nsToS * (t1 - t0);
  }

  static void it2_10x(String[] strings, char[][] result, int N) {
    long t0 = System.nanoTime();
    for (int i = 0; i < N; i++) {
      iteration2(result, strings);
      addLength(result);
    }
    long t1 = System.nanoTime();
    charAtTime += TimeUtil.nsToS * (t1 - t0);
  }


  static void addLength(char[][] result) {
    for (int i = 0; i < result.length; i += 37) {
      allLength += result[i].length;
    }
  }

  static void iteration1(char[][] result, String[] strings) {
    for (int i = 0, n = result.length; i < n; i++) {
      result[i] = CString.toChar16CString(strings[i]);
    }
  }

  static void iteration2(char[][] result, String[] strings) {
    for (int i = 0, n = result.length; i < n; i++) {
      result[i] = toChar16charAt(strings[i]);
    }
  }

  static void iteration3(char[][] result, String[] strings) {
    for (int i = 0, n = result.length; i < n; i++) {
      result[i] = toChar16concat(strings[i]);
    }
  }

  static char[] toChar16charAt(String x) {
    char[] charArray = new char[x.length() + 1];
    for (int i = 0, n = x.length(); i < n; i++) {
      charArray[i] = x.charAt(i);
    }
    return charArray;
  }

  static char[] toChar16concat(String x) {
    return x.concat("\0").toCharArray();
  }
}
