package org.sudu.experiments.js;

import org.sudu.experiments.SplitInfo;
import org.sudu.experiments.math.ArrayOp;
import org.teavm.jso.core.JSString;

import java.util.Arrays;

public interface SplitJsText {

  static SplitInfo split(JSString t) {
    String[] lines = new String[8];
    byte[] lineSeparators = new byte[8];
    char[] buffer = new char[16];
    int column = 0, line = 0, length = t.getLength();
    for (int i = 0; i <= length; ++i) {
      char codeAt = i < length ? (char) t.charCodeAt(i) : '\n';

      if (codeAt == '\n') {
        String value = new String(buffer, 0, column);
        lines = ArrayOp.addAt(value, lines, line);
        lineSeparators = ArrayOp.addAt(SplitInfo.LF, lineSeparators, line);
        line++;
        column = 0;
      } else if (codeAt == '\r') {
        String value = new String(buffer, 0, column);
        lines = ArrayOp.addAt(value, lines, line);
        if (i + 1 < length && t.charCodeAt(i + 1) == '\n') {
          i++;
          lineSeparators = ArrayOp.addAt(SplitInfo.CRLF, lineSeparators, line);
        } else {
          lineSeparators = ArrayOp.addAt(SplitInfo.CR, lineSeparators, line);
        }
        line++;
        column = 0;
      } else {
        if (buffer.length == column) {
          buffer = Arrays.copyOf(buffer, buffer.length * 2);
        }
        buffer[column++] = codeAt;
      }
    }
    return new SplitInfo(
        ArrayOp.resizeOrReturn(lines, line),
        ArrayOp.resizeOrReturn(lineSeparators, line)
    );
  }

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
