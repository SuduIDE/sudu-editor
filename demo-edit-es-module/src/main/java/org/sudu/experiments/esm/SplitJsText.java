package org.sudu.experiments.esm;

import org.sudu.experiments.math.ArrayOp;
import org.teavm.jso.core.JSString;

import java.util.Arrays;

public interface SplitJsText {
  static String[] split(JSString t, char splitChar) {
    String[] res = new String[8];
    char[] buffer = new char[16];
    int column = 0, line = 0, length = t.getLength();
    for (int i = 0; i <= length; ++i) {
      char codeAt = i < length ? (char) t.charCodeAt(i) : splitChar;
      if (codeAt == splitChar) {
        String value = new String(buffer, 0, column);
        res = ArrayOp.addAt(value, res, line++);
        column = 0;
      } else {
        if (buffer.length == column) {
          buffer = Arrays.copyOf(buffer, buffer.length * 2);
        }
        buffer[column++] = codeAt;
      }
    }
    return ArrayOp.resizeOrReturn(res, line);
  }
}
