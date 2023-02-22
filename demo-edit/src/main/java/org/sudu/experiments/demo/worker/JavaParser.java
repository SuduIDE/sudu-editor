package org.sudu.experiments.demo.worker;

import org.sudu.experiments.demo.CodeElement;
import org.sudu.experiments.demo.CodeLine;
import org.sudu.experiments.demo.Document;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.java.JavaParserIntervals;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JavaParser {

  public static final String PARSE_BYTES_JAVA = "parseBytesJava";

  public static void parseBytes(byte[] bytes, List<Object> result) {
    String source = new String(bytes, StandardCharsets.UTF_8);

    int[] ints = JavaParserIntervals.parse(source);
    char[] chars = source.toCharArray();
    result.add(ints);
    result.add(chars);
  }

  public static Document makeDocument(int[] ints, char[] chars) {
    int N = ints[0];
    int M = ints[1];
    int K = ints[2];
    CodeLine[] newDoc = new CodeLine[N];

    List<Interval> intervals = new ArrayList<>();
    for (int i = 0; i < K; i++) {
      int start = ints[3 + N + 4 * M + 3 * i];
      int stop = ints[3 + N + 4 * M + 3 * i + 1];
      int type = ints[3 + N + 4 * M + 3 * i + 2];
      intervals.add(new Interval(start, stop, type));
    }

    for (int i = 0, wordInd = 0; i < N; i++) {
      int len = ints[3 + i];
      CodeElement[] elements = new CodeElement[len];
      for (int j = 0; j < len; j++, wordInd++) {
        int wordStart = ints[3 + N + 4 * wordInd];
        int wordStop = ints[3 + N + 4 * wordInd + 1];
        int wordType = ints[3 + N + 4 * wordInd + 2];
        int wordStyle = ints[3 + N + 4 * wordInd + 3];
        String word = new String(chars, wordStart, wordStop - wordStart);
        elements[j] = new CodeElement(word, wordType, wordStyle);
      }
      newDoc[i] = new CodeLine(elements);
    }
    return new Document(newDoc);
  }

}
