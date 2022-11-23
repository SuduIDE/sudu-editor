package org.sudu.experiments.win32;

public class PerfTimerTest {
  static final long nanoFr = 1_000_000_000;

  static class R {
    int n;
    long fillNanoDt, fillCounterDt;
    long fr;
    long fillNanoDn, fillCounterDn;

    public R(int N, long ndt, long cdt, long fr, long ndn, long cdn) {
      n = N;
      fillNanoDt = ndt; fillCounterDt = cdt;
      this.fr = fr;
      fillNanoDn = ndn; fillCounterDn = cdn;
    }

    public String toString() {
      return "N = " + n + "\n" +
          "fillNano " + Helper.dtOps(n, fillNanoDt, fr) + "\n" +
          "fillCounter " + Helper.dtOps(n, fillCounterDt, fr) + "\n" +
          "fillNano dNano = " + Helper.dtOps(n, fillNanoDn, nanoFr) + "\n" +
          "fillCounter dNano = " + Helper.dtOps(n, fillCounterDn, nanoFr) + "\n";
    }
  }
  public static void main(String[] args) {
    Helper.loadDlls();

    long[] counters = new long[10_000_000];
    long[] nanos = new long[counters.length];

    long performanceFrequency = Win32.GetPerformanceFrequency();

    R[] data = new R[6];

    for (int N = 100, r = 0; N <= counters.length; N *= 10) {
      long counter0 = Win32.GetPerformanceCounter();
      long nanoTime0 = System.nanoTime();

      fillNano(nanos, N);

      long counter1 = Win32.GetPerformanceCounter();
      long nanoTime1 = System.nanoTime();

      fillCounter(counters, N);

      long counter2 = Win32.GetPerformanceCounter();
      long nanoTime2 = System.nanoTime();

      data[r++] = new R(N, counter1 - counter0, counter2 - counter1,
          performanceFrequency,nanoTime1 - nanoTime0, nanoTime2 - nanoTime1);
    }

    System.out.println("performanceFrequency = " + performanceFrequency);

    for (R result : data) {
      System.out.println(result.toString());
    }
  }

  static void fillCounter(long[] counters, int n) {
    for (int i = 0; i < n; i++) {
      counters[i] = Win32.GetPerformanceCounter();
    }
  }

  static void fillNano(long[] nanos, int n) {
    for (int i = 0; i < n; i++) {
      nanos[i] = System.nanoTime();
    }
  }
}