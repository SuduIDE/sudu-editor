package org.sudu.experiments.win32;

import org.sudu.experiments.math.XorShiftRandom;

public class StringGen {
  static String[] asciiStrings(int n, int minChars, int maxChars) {
    XorShiftRandom rng =  new XorShiftRandom();
    String[] strings = new String[n];
    for (int i = 0; i < n; i++) {
      char[] chars = new char[minChars + rng.nextInt(maxChars-minChars)];
      for (int j = 0, l = chars.length; j < l; j++) {
        chars[j] = (char) rng.nextInt(255);
      }
      strings[i] = new String(chars);
    }
    return strings;
  }

  static String[] strings(int n, int minChars, int maxChars) {
    XorShiftRandom rng =  new XorShiftRandom();
    String[] strings = new String[n];
    for (int i = 0; i < n; i++) {
      char[] chars = new char[minChars + rng.nextInt(maxChars-minChars)];
      for (int j = 0, l = chars.length; j < l; j++) {
        chars[j] = (char) rng.nextInt(0x7FFF);
      }
      strings[i] = new String(chars);
    }
    return strings;
  }
}
