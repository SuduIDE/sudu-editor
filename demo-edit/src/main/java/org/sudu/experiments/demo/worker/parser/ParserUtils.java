package org.sudu.experiments.demo.worker.parser;

import org.sudu.experiments.demo.CodeElement;
import org.sudu.experiments.demo.CodeLine;
import org.sudu.experiments.demo.Document;
import org.sudu.experiments.demo.worker.ArrayReader;
import org.sudu.experiments.demo.worker.IntervalTree;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.Pos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ParserUtils {

  public static Document makeDocument(int[] ints, char[] chars) {
    return updateDocument(null, ints, chars, false);
  }

  public static Document updateDocument(Document document, int[] ints, char[] chars, boolean isFirstLinesParsed) {
    ArrayReader reader = new ArrayReader(ints);

    int N = reader.next();
    int K = reader.next();
    int L = reader.next();
    CodeLine[] newDocument = new CodeLine[N];

    for (int i = 0; i < N; i++) {
      if (document != null && isFirstLinesParsed && i < document.length()) {
        int len = reader.next();
        newDocument[i] = document.line(i);
        reader.skip(4 * len);
        continue;
      }
      CodeElement[] elements = readElements(reader, chars, 0);
      newDocument[i] = new CodeLine(elements);
    }

    List<Interval> intervalList = ParserUtils.getIntervalList(reader, K);
    Map<Pos, Pos> usageToDef = ParserUtils.getUsageToDefMap(reader, L);
    reader.checkSize();

    Document updDocument = new Document(newDocument);
    updDocument.tree = new IntervalTree(intervalList);
    updDocument.usageToDef = usageToDef;
    return updDocument;
  }

  public static void updateDocument(Document document, int[] ints, char[] chars) {
    if (ints.length == 1 && ints[0] == -1) return;

    ArrayReader reader = new ArrayReader(ints);

    int intervalStart = reader.next();
    int intervalStop = reader.next();
    int N = reader.next();
    int K = reader.next();

    V2i stLine = document.getLine(intervalStart);
    V2i endLine = document.getLine(intervalStop);
    CodeElement[] left = document.line(stLine.x).getElementsToLeft(stLine.y);
    CodeElement[] right = document.line(endLine.x).getElementsToRight(endLine.y);

    for (int i = 0; i < N; i++) {
      CodeElement[] elements = readElements(reader, chars, intervalStart);
      if (i == 0) elements = ArrayOp.add(left, elements);
      if (i == N - 1) elements = ArrayOp.add(elements, right);
      document.setLine(stLine.x + i, new CodeLine(elements));
    }

    List<Interval> intervalList = ParserUtils.getIntervalList(reader, K);
    reader.checkSize();

    if (!intervalList.isEmpty()) {
      Interval oldInterval = new Interval(intervalStart, intervalStop, -1);
      document.tree.replaceInterval(oldInterval, intervalList);
    }
  }

  public static CodeElement[] readElements(ArrayReader reader, char[] chars, int startDx) {
    int len = reader.next();
    CodeElement[] elements = new CodeElement[len];
    for (int j = 0; j < len; j++) {
      int start = reader.next();
      int stop = reader.next();
      int type = reader.next();
      int style = reader.next();
      String word = new String(chars, startDx + start,stop - start);
      elements[j] = new CodeElement(word, type, style);
    }
    return elements;
  }

  public static List<Interval> getIntervalList(ArrayReader reader, int K) {
    List<Interval> intervalList = new ArrayList<>();
    for (int i = 0; i < K; i++) {
      int start = reader.next();
      int stop = reader.next();
      int type = reader.next();
      intervalList.add(new Interval(start, stop, type));
    }
    return intervalList;
  }

  public static Map<Pos, Pos> getUsageToDefMap(ArrayReader reader, int L) {
    Map<Pos, Pos> usageMap = new HashMap<>();
    for (int i = 0; i < L; i++) {
      Pos usage = new Pos(reader.next(), reader.next());
      Pos def = new Pos(reader.next(), reader.next());
      usageMap.put(usage, def);
    }
    return usageMap;
  }

}
