package org.sudu.experiments.text;

public interface TextFormat {

  static String toString3(double t) {
    int t3 = (int) (t * 1000);
    int s = t3 / 1000, ms = t3 % 1000;
    char[] c4 = new char[4]; c4[0] = '.';
    for (int i = 0; i != 3; ms /= 10, ++i) {
      c4[3 - i] = (char) ('0' + (ms % 10));
    }
    return Integer.toString(s).concat(new String(c4));
  }
}
