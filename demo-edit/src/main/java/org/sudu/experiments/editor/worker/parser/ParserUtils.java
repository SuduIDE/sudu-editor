package org.sudu.experiments.editor.worker.parser;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.editor.CodeElement;
import org.sudu.experiments.editor.CodeLine;
import org.sudu.experiments.editor.Document;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.common.tree.IntervalTree;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.common.graph.ScopeGraph;
import org.sudu.experiments.parser.common.graph.reader.ScopeGraphReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class ParserUtils {

  public static Document makeDocument(int[] ints, char[] chars) {
    Document document = new Document();
    updateDocument(document, ints, chars);
    return document;
  }

  public static void updateDocument(Document document, int[] ints, char[] chars) {
    updateDocument(document, ints, chars, false);
  }

  public static void updateDocument(Document document, int[] docInts, char[] docChars, boolean saveOldLines) {
    updateDocument(document, docInts, docChars, null, null, saveOldLines);
  }

  public static void updateDocument(
      Document document,
      int[] docInts, char[] docChars,
      int[] graphInts, char[] graphChars,
      boolean saveOldLines
  ) {
    ArrayReader reader = new ArrayReader(docInts);

    int N = reader.next();
    int K = reader.next();
    int L = reader.next();

    int documentLength = document.length();

    int[] linePrefixSum = new int[N + 1];
    document.document = saveOldLines
        ? ArrayOp.resizeOrReturn(document.document, N)
        : new CodeLine[N];

    for (int i = 0; i < N; i++) {
      if (saveOldLines && i < documentLength) {
        int len = reader.next();
        reader.skip(4 * len);
        linePrefixSum[i + 1] = linePrefixSum[i] + document.line(i).totalStrLength + 1;
        continue;
      }
      CodeElement[] elements = readElements(reader, docChars, 0);
      CodeLine line = new CodeLine(elements);
      document.document[i] = line;
      linePrefixSum[i + 1] = linePrefixSum[i] + document.line(i).totalStrLength + 1;
    }

    if (K != 0) document.tree = new IntervalTree(IntervalNode.getNode(reader));
    document.linePrefixSum = linePrefixSum;

    document.usageToDef.clear();
    document.defToUsages.clear();
    ParserUtils.getUsageToDefMap(reader, L, document.usageToDef);
    ParserUtils.getDefToUsagesMap(document.usageToDef, document.defToUsages);
    reader.checkSize();

    if (graphInts != null && graphChars != null) {
      updateGraph(document, graphInts, graphChars);
    }
  }

  public static void updateGraph(Document document, int[] graphInts, char[] graphChars) {
    ScopeGraphReader reader = new ScopeGraphReader(graphInts, graphChars);
    reader.readFromInts();
    document.scopeGraph = new ScopeGraph(reader.scopeRoot, reader.typeMap);
    document.tree = new IntervalTree(reader.intervalRoot);
  }

  public static void updateDocumentInterval(Document document, int[] ints, char[] chars) {
    updateDocumentInterval(document, ints, chars, null, null);
  }

  public static void updateDocumentInterval(
      Document document,
      int[] ints, char[] chars,
      int[] graphInts, char[] graphChars
  ) {
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

    if (K != 0) {
      IntervalNode intervalNode = IntervalNode.getNode(reader);
      Interval oldInterval = new Interval(intervalStart, intervalStop, -1);
      document.tree.replaceInterval(oldInterval, intervalNode);
    } else if (graphInts != null && graphChars != null) {
      ScopeGraphReader graphReader = new ScopeGraphReader(graphInts, graphChars);
      graphReader.readFromInts();
      Interval oldInterval = new Interval(intervalStart, intervalStop, -1);
      document.tree.replaceInterval(oldInterval, graphReader.intervalRoot);
      document.scopeGraph.typeMap = graphReader.typeMap;
    }
    reader.checkSize();
  }

  public static CodeElement[] readElements(ArrayReader reader, char[] chars, int startDx) {
    int len = reader.next();
    CodeElement[] elements = new CodeElement[len];
    for (int j = 0; j < len; j++) {
      int start = reader.next();
      int stop = reader.next();
      int type = reader.next();
      int style = reader.next();
      String word = new String(chars, startDx + start, stop - start);
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

  public static void getUsageToDefMap(ArrayReader reader, int L, Map<Pos, Pos> usageMap) {
    for (int i = 0; i < L; i++) {
      Pos usage = new Pos(reader.next(), reader.next());
      Pos def = new Pos(reader.next(), reader.next());
      usageMap.put(usage, def);
    }
  }

  public static void getDefToUsagesMap(Map<Pos, Pos> usageToDef, Map<Pos, List<Pos>> defMap) {
    for (var entry : usageToDef.entrySet()) {
      var usage = entry.getKey();
      var definition = entry.getValue();
      defMap.putIfAbsent(definition, new ArrayList<>());
      defMap.get(definition).add(usage);
    }
    for (var usages : defMap.values()) Collections.sort(usages);
  }

}
