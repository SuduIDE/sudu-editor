package org.sudu.experiments.demo.worker;

import org.sudu.experiments.demo.CodeElement;
import org.sudu.experiments.demo.CodeLine;
import org.sudu.experiments.demo.Document;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.java.parser.JavaFullParser;
import org.sudu.experiments.parser.java.parser.JavaViewportIntervalsParser;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaParser {

  public static final String PARSE_BYTES_JAVA = "JavaParser.parseBytes";

  public static void parseBytes(byte[] bytes, List<Object> result) {
    String source = new String(bytes, StandardCharsets.UTF_8);

    int[] ints = new JavaFullParser().parse(source);
    char[] chars = source.toCharArray();
    result.add(ints);
    result.add(chars);
    result.add(new int[]{FileParser.JAVA_FILE});
  }

  public static final String PARSE_BYTES_JAVA_VIEWPORT = "JavaParser.parseViewport";

  public static void parseViewport(byte[] bytes, int[] viewport, int[] intervals, List<Object> result) {
    String source = new String(bytes, StandardCharsets.UTF_8).replace("\r", "");

    int[] ints = new JavaViewportIntervalsParser().parseViewport(source, viewport, intervals);
    char[] chars = source.toCharArray();
    result.add(ints);
    result.add(chars);
  }

  public static Document makeDocument(int[] ints, char[] chars) {
    int N = ints[0];
    int M = ints[1];
    int K = ints[2];
    CodeLine[] newDoc = new CodeLine[N];

    int[] intervals = Arrays.copyOfRange(ints, 3 + N + 4 * M, 3 + N + 4 * M + 3 * K);
    List<Interval> intervalList = new ArrayList<>();
    for (int i = 0; i < intervals.length; ) {
      int start = intervals[i++];
      int end = intervals[i++];
      int type = intervals[i++];
      intervalList.add(new Interval(start, end, type));
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
    Document document = new Document(newDoc);
    document.tree = new IntervalTree(intervalList);
    return document;
  }

  public static void makeViewport(Document document, int[] ints, char[] chars) {
    int N0 = ints[0];
    int M0 = ints[1];
    int N = ints[2];
    int M = ints[3];

    for (int i = 0, wordInd = 0; i < N; i++) {
      int len = ints[4 + i];
      CodeElement[] elements = new CodeElement[len];
      for (int j = 0; j < len; j++, wordInd++) {
        int wordStart = ints[4 + N + 4 * wordInd];
        int wordStop = ints[4 + N + 4 * wordInd + 1];
        int wordType = ints[4 + N + 4 * wordInd + 2];
        int wordStyle = ints[4 + N + 4 * wordInd + 3];
        String word = new String(chars, M0 + wordStart,wordStop - wordStart);
        elements[j] = new CodeElement(word, wordType, wordStyle);
      }
      document.setLine(N0 + i, new CodeLine(elements));
    }
  }

}
