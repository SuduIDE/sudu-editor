package org.sudu.experiments.win32;

public class TestHelper {
  static native int invokeCritical(int index, int[] a);
  static native int invokeStandard(int index, int[] a);
}
