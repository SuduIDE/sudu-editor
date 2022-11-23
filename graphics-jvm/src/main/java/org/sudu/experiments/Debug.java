package org.sudu.experiments;

import java.util.Arrays;

public class Debug {

  public static void consoleInfo(String s0) {
    System.out.println(s0);
  }

  public static void consoleInfo(String s0, double n) {
    System.out.println(s0.concat(Double.toString(n)));
  }

  public static void consoleInfo(String s0, Object jsObject) {
    System.out.println(s0.concat(jsObject.toString()));
  }

  public static void consoleInfo(String s0, float[] array) {
    System.out.println(s0.concat(Arrays.toString(array)));
  }

  public static void consoleInfo(String s0, int[] array) {
    System.out.println(s0.concat(Arrays.toString(array)));
  }

  public static void consoleInfo(String s0, double s1, String s2, double s3) {
    System.out.println(s0 + s1 + s2 + s3);
  }

  public static void consoleInfo(String s0, double s1, String s2, String s3) {
    System.out.println(s0 + s1 + s2 + s3);
  }

  public static void consoleInfo(String s0, double s1, String s2, String s3, String s4) {
    System.out.println(s0 + s1 + s2 + s3 + s4);
  }
}
