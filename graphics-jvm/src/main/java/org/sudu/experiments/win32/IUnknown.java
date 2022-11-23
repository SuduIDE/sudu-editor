package org.sudu.experiments.win32;

public class IUnknown {
  public static native int AddRef(long p);
  public static native int Release(long p);

  public static long release(long p) {
    Release(p);
    return 0;
  }

  public static long safeRelease(long p) {
    if (p != 0) Release(p);
    return 0;
  }

  public static long requireNonNull(long p) {
    if (p == 0) throw new NullPointerException();
    return p;
  }
}
