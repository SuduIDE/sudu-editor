package org.sudu.experiments;

import org.junit.jupiter.api.Assertions;
import org.sudu.experiments.win32.Helper;
import org.sudu.experiments.win32.Win32;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CStringTest {
  public static void main(String[] args) {
    Helper.loadDlls();

    testArrayRegion();
    cmdLineTest();
    performanceCmpTest();
  }

  static void cmdLineTest() {
    String commandLineA = CString.fromNativeString(Win32.GetCommandLineA());
    String commandLineW = CString.fromNativeString16(Win32.GetCommandLineW());

    System.out.println("commandLineA = " + commandLineA);
    System.out.println("commandLineW = " + commandLineW);
    if (!Objects.equals(commandLineA, commandLineW)) {
      System.out.println("commandLineA != commandLineW");
    }
  }

  static void performanceCmpTest() {
    int[] intArray = new int[1];
    float[] floats = new float[9];

    for (int i = 10; i < 1_000_000_000; i *= 10) {
      iterateSingleInt(i, intArray);
      iterateFloats(i, floats);
    }
  }

  static void iterateSingleInt(int N, int[] intArray) {
    long now1 = System.nanoTime();

    for (int j = 0; j < N; j++) {
      CString.getSetPrimitiveArrayCriticalTest(intArray, j);
    }

    long now2 = System.nanoTime();

    for (int j = 0; j < N; j++) {
      CString.setIntArrayRegionTest(intArray, j);
    }

    long now3 = System.nanoTime();

    report("int[1]: N = " + N,
        "\t{Get,Set}PrimitiveArrayCriticalTest time ",
        "\tSetIntArrayRegion time ",
        now1, now2, now3);
  }

  static void iterateFloats(int N, float[] floats) {
    long now1 = System.nanoTime();

    for (int j = 0; j < N; j++) {
      CString.getSetPrimitiveArrayCriticalTest(floats, floats.length);
    }

    long now2 = System.nanoTime();

    for (int j = 0; j < N; j++) {
      CString.setFloatArrayRegionTest9(floats, floats.length);
    }

    long now3 = System.nanoTime();

    report("float[" + floats.length + "]: N = " + N,
        "\t{Get,Set}PrimitiveArrayCriticalTest time ",
        "\tsetFloatArrayRegion time ", now1, now2, now3);
  }

  static void report(String N, String x, String y, long t1, long t2, long t3) {
    System.out.println(N);
    System.out.println(x + TimeUtil.toString3(TimeUtil.nsToS * (t2 - t1)));
    System.out.println(y + TimeUtil.toString3(TimeUtil.nsToS * (t3 - t2)));
  }

  static void testArrayRegion() {
    String s = "тестируем регионы";

    charArrayRegionTest(s);
    byteArrayRegionTest(s);
  }

  static void charArrayRegionTest(String s) {
    char[] source = CString.toChar16CString(s);
    long dataChars = CString.operatorNew(source.length * 2L);

    for (int a = 3; a >= 0; a--) {
      char[] copy = new char[source.length];
      int b = source.length - a;
      CString.getCharArrayRegion(source, a, b - a, dataChars);
      CString.setCharArrayRegion(copy, a, b - a, dataChars);
      for (int j = 0; j < a; j++) assertEquals(0, copy[j]);
      for (int j = a; j < b; j++) assertEquals(copy[j], source[j]);
      for (int j = b; j < copy.length; j++) assertEquals(0, copy[j]);
    }

    char[] copy = new char[source.length];
    CString.getCharArrayRegion(source, 0, source.length, dataChars);
    CString.setCharArrayRegion(copy, 0, source.length, dataChars);

    CString.operatorDelete(dataChars);
    Assertions.assertArrayEquals(source, copy);
  }

  static void byteArrayRegionTest(String s) {
    byte[] source = CString.toUtf8CString(s);
    long dataBytes = CString.operatorNew(source.length);

    for (int a = 3; a >= 0; a--) {
      byte[] copy = new byte[source.length];

      int b = source.length - a;
      CString.getByteArrayRegion(source, a, b - a, dataBytes);
      CString.setByteArrayRegion(copy, a, b - a, dataBytes);
      for (int j = 0; j < a; j++) assertEquals(0, copy[j]);
      for (int j = a; j < b; j++) assertEquals(copy[j], source[j]);
      for (int j = b; j < copy.length; j++) assertEquals(0, copy[j]);
    }

    byte[] copy = new byte[source.length];

    CString.getByteArrayRegion(source, 0, source.length, dataBytes);
    CString.setByteArrayRegion(copy, 0, copy.length, dataBytes);
    CString.operatorDelete(dataBytes);

    Assertions.assertArrayEquals(source, copy);
  }
}
