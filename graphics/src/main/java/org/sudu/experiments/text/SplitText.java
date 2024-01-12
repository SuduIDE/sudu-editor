package org.sudu.experiments.text;

import org.sudu.experiments.SplitInfo;
import org.sudu.experiments.math.ArrayOp;

import java.util.Arrays;
import java.util.function.Function;

public interface SplitText {

  static SplitInfo split(
      int length,
      Function<Integer, Character> charAt
  ) {
    String[] lines = new String[8];
    byte[] lineSeparators = new byte[8];
    char[] buffer = new char[16];
    int column = 0, line = 0;

    for (int i = 0; i <= length; ++i) {
      char codeAt = i < length ? charAt.apply(i) : '\n';

      if (codeAt == '\n') {
        String value = new String(buffer, 0, column);
        lines = ArrayOp.addAt(value, lines, line);
        lineSeparators = ArrayOp.addAt(SplitInfo.LF, lineSeparators, line);
        line++;
        column = 0;
      } else if (codeAt == '\r') {
        String value = new String(buffer, 0, column);
        lines = ArrayOp.addAt(value, lines, line);
        if (i + 1 < length && charAt.apply(i + 1) == '\n') {
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

  static String[] split(String source) {
    return splitInfo(source).lines;
  }

  static SplitInfo splitInfo(String source) {
    return split(source.length(), source::charAt);
  }
}
