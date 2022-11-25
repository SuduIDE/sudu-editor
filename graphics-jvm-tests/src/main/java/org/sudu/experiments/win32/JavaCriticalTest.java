package org.sudu.experiments.win32;

public class JavaCriticalTest {
  public static void main(String[] args) {
    Helper.loadDlls();

    Runnable[] r = { JavaCriticalTest::invokeStandard, JavaCriticalTest::invokeCritical };
    String[] t = { "invokeStandard", "invokeCritical" };
    iterate(r ,t);
  }

  private static void iterate(Runnable[] r, String[] title) {
    for (int N = 10; N < 100000000; N *= 10) {
      for (int i = 0; i < r.length; i++) {
        iterate(N, r[i], title[i]);
      }
    }
  }

  private static void iterate(int N, Runnable r, String title) {
    long t0 = Win32.GetPerformanceCounter();
    for (int i = 0; i < N; i++) { r.run();}
    long t1 = Win32.GetPerformanceCounter();
    System.out.println(title);
    System.out.println("N = " + N + ", " + Helper.dtOps(N, t1 - t0, Win32.GetPerformanceFrequency()));
  }

  static int[] arg = { 33, 44 };

  static void invokeCritical() {
    int l = TestHelper.invokeCritical(1, arg);
  }
  static void invokeStandard() {
    int l = TestHelper.invokeStandard(1, arg);
  }

  static final String max = Helper.fmLong(Long.MAX_VALUE);
}