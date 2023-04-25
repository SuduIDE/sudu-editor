package org.sudu.experiments.demo.worker;

import org.sudu.experiments.demo.CodeElement;
import org.sudu.experiments.demo.CodeLine;
import org.sudu.experiments.demo.Document;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.Interval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseParser {

  public static Document makeDocument(int[] ints, char[] chars) {
    int N = ints[0];
    int M = ints[1];
    int K = ints[2];
    CodeLine[] newDoc = new CodeLine[N];

    List<Interval> intervalList = getIntervalList(ints, 3 + N + 4 * M, K);

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

  public static void updateDocument(Document document, int[] ints, char[] chars) {
    if (ints.length == 1 && ints[0] == -1) return;

    int intervalStart = ints[0];
    int intervalStop = ints[1];
    int N = ints[2];
    int M = ints[3];
    int K = ints[4];

    V2i stLine = document.getLine(intervalStart);
    V2i endLine = document.getLine(intervalStop);
    CodeElement[] left = document.line(stLine.x).getElementsToLeft(stLine.y);
    CodeElement[] right = document.line(endLine.x).getElementsToRight(endLine.y);

    for (int i = 0, wordInd = 0; i < N; i++) {
      int len = ints[5 + i];
      CodeElement[] elements = new CodeElement[len];
      for (int j = 0; j < len; j++, wordInd++) {
        int wordStart = ints[5 + N + 4 * wordInd];
        int wordStop = ints[5 + N + 4 * wordInd + 1];
        int wordType = ints[5 + N + 4 * wordInd + 2];
        int wordStyle = ints[5 + N + 4 * wordInd + 3];
        String word = new String(chars, intervalStart + wordStart,wordStop - wordStart);
        elements[j] = new CodeElement(word, wordType, wordStyle);
      }

      if (i == 0) elements = ArrayOp.add(left, elements);
      else if (i == N - 1) elements = ArrayOp.add(elements, right);
      document.setLine(stLine.x + i, new CodeLine(elements));
    }

    List<Interval> intervalList = BaseParser.getIntervalList(ints, 5 + N + 4 * M, K);
    if (!intervalList.isEmpty()) document.tree.replaceInterval(new Interval(intervalStart, intervalStop, -1), intervalList);
  }

  public static List<Interval> getIntervalList(int[] ints, int from, int K) {
    if (K == 0) return List.of();

    int[] intervals = Arrays.copyOfRange(ints, from, from + 3 * K);
    List<Interval> intervalList = new ArrayList<>();
    for (int i = 0; i < intervals.length; ) {
      int start = intervals[i++];
      int end = intervals[i++];
      int type = intervals[i++];
      intervalList.add(new Interval(start, end, type));
    }
    return intervalList;
  }

}
