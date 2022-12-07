package org.sudu.experiments;

import org.sudu.experiments.win32.Helper;
import org.sudu.experiments.win32.Win32;

import java.util.Arrays;
import java.util.Objects;

public class CStringTest {
  static final double ns_to_s = 1. / 1_000_000_000.;

  public static void main(String[] args) {
    Helper.loadDlls();

    testArrayRegion();

    long pCommandLineA = Win32.GetCommandLineA();
    long pCommandLineW = Win32.GetCommandLineW();
    String commandLineA = CString.fromNativeString(pCommandLineA);
    String commandLineW = CString.fromNativeString16(pCommandLineW);

    System.out.println("commandLineA = " + commandLineA);
    System.out.println("commandLineW = " + commandLineW);

    System.out.println("equals = " + Objects.equals(commandLineA, commandLineW));

    char[] dataW = new char[7];
    byte[] dataA = new byte[7];

    for (int i = 3; i >= 0; i--) {
      System.out.println("range test: " + i + ", n = " + (dataA.length - i * 2));
      CString.setByteArrayRegion(dataA, i, dataA.length - i * 2, pCommandLineA);
      CString.setCharArrayRegion(dataW, i, dataW.length - i * 2, pCommandLineW);
      System.out.println(" dataA = " + Arrays.toString(dataA));
      System.out.println(" dataW = " + Arrays.toString(dataW));
    }

    int[] intArray = new int[1];

    for (int i = 10; i < 1_000_000_000; i *= 10) {
      iterate(i, intArray);
    }
  }

  static void iterate(int N, int[] intArray) {
    long now1 = System.nanoTime();

    for (int j = 0; j < N; j++) {
      CString.getSetPrimitiveArrayCriticalTest(intArray, j);
    }

    long now2 = System.nanoTime();

    for (int j = 0; j < N; j++) {
      CString.setIntArrayRegionTest(intArray, j);
    }

    long now3 = System.nanoTime();

    System.out.println("N = " + N);
    System.out.println("getSetPrimitiveArrayCriticalTest time " + TimeUtil.toString3(ns_to_s * (now2 - now1)));
    System.out.println("setIntArrayRegionTest time " + TimeUtil.toString3(ns_to_s * (now3 - now2)));
  }

  static void testArrayRegion() {
    String s = "тестируем регионы";
    {
      char[] chars = CString.toChar16CString(s);
      char[] chars1 = new char[chars.length];

      for (int i = 3; i >= 0; i--) {
        long dataChars = CString.operatorNew(chars.length * 2L);
        CString.getCharArrayRegion(chars, i, chars.length, dataChars);
        CString.setCharArrayRegion(chars1, i, chars1.length, dataChars);
        CString.operatorDelete(dataChars);
      }

      if (!Arrays.equals(chars, chars1)) throw new RuntimeException();
    }
    {
      byte[] utf8 = CString.toUtf8CString(s);
      byte[] utf8a = new byte[utf8.length];

      long dataBytes = CString.operatorNew(utf8.length);
      CString.getByteArrayRegion(utf8, 0, utf8.length, dataBytes);
      CString.setByteArrayRegion(utf8a, 0, utf8a.length, dataBytes);
      CString.operatorDelete(dataBytes);

      if (!Arrays.equals(utf8, utf8a)) throw new RuntimeException();
    }
  }
}
