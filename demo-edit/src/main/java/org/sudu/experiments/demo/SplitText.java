package org.sudu.experiments.demo;

import org.sudu.experiments.math.ArrayOp;

public class SplitText {
  public static String[] split(String t) {
    return split(t, '\n');
  }

  public static String[] split(String t, char splitChar) {
    String[] res = new String[8];
    int length = t.length();
    int line = 0, i = 0;
    do {
      int indexOf = t.indexOf(splitChar, i);
      String value = indexOf < 0 ? t.substring(i) : t.substring(i, indexOf);
      res = ArrayOp.addAt(value, res, line++);
      i = indexOf < 0 ? length + 1 : indexOf + 1;
    } while (i <= length);

    return ArrayOp.resizeOrReturn(res, line);
  }
}
