package org.sudu.experiments.math;

public class RngHelper {

  public static String rngString(XorShiftRandom r, int length) {
    if (length <= 0) return "";
    char[] data = new char[length];
    data[0] = azAZ09(r.nextInt(26 + 26));
    for (int i = 1; i < length; i++) data[i] = azAZ09(r.nextInt(26 + 26 + 10));
    return new String(data);
  }

  private static char azAZ09(int x) {
    return (char) (x < 26 ? 'a' + x : x < 26 + 26
        ? 'A' - 26 + x : '0' - 26 - 26 + x);
  }
}
