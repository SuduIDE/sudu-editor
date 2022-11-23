package org.sudu.experiments.win32;

import org.sudu.experiments.nativelib.SuduDll;
import org.sudu.experiments.nativelib.AngleDll;

public class Helper {

  static String dtOps(int N, long dt, long frequency) {
    return "dt = " + dt + ", " + (1. * dt / frequency)
        + "s, " + fmLong(frequency * N / dt) + "op/s";
  }

  static String fmLong(long l) {
    char[] buf = new char[32];
    int p = buf.length;
    for (;l > 0; l = l / 10) {
      if ((buf.length - p) % 4 == 3) buf[--p] = '_';
      buf[--p] = (char) ('0' + (int) (l % 10));
    }
    return new String(buf, p, buf.length - p);
  }

  @SuppressWarnings("UnusedReturnValue")
  public static int loadDlls() {
//    System.out.println("AngleDll.pathGLESv2 = " + AngleDll.pathGLESv2);
//    System.out.println("SuduDll.path = " + SuduDll.path);
    return AngleDll.pathGLESv2.length() + SuduDll.path.length();
  }
}