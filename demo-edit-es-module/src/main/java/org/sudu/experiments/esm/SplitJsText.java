package org.sudu.experiments.esm;

import org.teavm.jso.core.JSString;

import java.util.Arrays;

public interface SplitJsText {
  static String[] split(JSString t, char splitChar) {
    String[] res = new String[8];
    char[] buffer = new char[16];
    int column = 0;
    int line = 0;
    for (int i = 0; i != t.getLength(); ++i) {
      char codeAt = (char) t.charCodeAt(i);
      if (codeAt == splitChar) {
        if (res.length == line) {
          res = Arrays.copyOf(res, res.length * 2);
        }
        res[line++] = new String(buffer, 0, column);
        column = 0;
      } else {
        if (buffer.length == column) {
          buffer = Arrays.copyOf(buffer, buffer.length * 2);
        }
        buffer[column++] = codeAt;
      }
    }
    if (column > 0) {
      if (res.length == line) {
        res = Arrays.copyOf(res, res.length + 1);
      }
      res[line++] = new String(buffer, 0, column);
    }
    if (line == 0) res[line++] = "";
    res = Arrays.copyOf(res, line);
    return res;
  }
}
