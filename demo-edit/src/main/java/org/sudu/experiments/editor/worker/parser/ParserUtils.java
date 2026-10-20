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
import java.util.function.Function;

public abstract class ParserUtils {

  public static void updateDocument(Document document, ParseResult parseRes) {
    updateDocument(document, parseRes, false);
  }

  public static void updateDocument(Document document, ParseResult parseRes, boolean saveOldLines) {
    updateDocument(document, parseRes.ints, parseRes.source, parseRes.graphInts, parseRes.graphChars, saveOldLines);
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

    int sourceCount = document.sourceCount();
    boolean mapped = document.isMapped();

    if (!mapped && document.lines.length < N) {
      document.lines = ArrayOp.resizeOrReturn(document.lines, N);
    }

    for (int i = 0; i < N; i++) {
      if (saveOldLines && i < sourceCount) {
        int len = reader.next();
        reader.skip(4 * len);
        continue;
      }
      int docSlot = mapped ? document.docLineOfSource(i) : i;
      if (docSlot < 0 || docSlot >= document.lines.length) {
        readElements(reader, docChars, 0);
        continue;
      }
      CodeElement[] elements = readElements(reader, docChars, 0);
      // Source lines are rebuilt from parser output; only lineNumber is carried
      // over here. Document.setLine may then copy semantic-token colors from the
      // old elements by position (when the element count is unchanged), so colors
      // supplied through Model.setDocumentTokens are best-effort on numbered
      // lines. Attached lines are never visited by the parser and keep theirs.
      CodeLine line = new CodeLine(elements);
      CodeLine prev = document.lines[docSlot];
      line.lineNumber = prev != null ? prev.lineNumber : (mapped ? i + 1 : 0);
      document.setLine(docSlot, line, true);
    }
    document.countPrefixes();

    if (K != 0) document.tree = new IntervalTree(IntervalNode.getNode(reader));

    document.usageToDef.clear();
    document.defToUsages.clear();
    ParserUtils.getUsageToDefMap(reader, L, document.usageToDef);
    ParserUtils.getDefToUsagesMap(document.usageToDef, document.defToUsages);
    reader.checkSize();

    if (graphInts != null && graphChars != null) {
      updateGraph(document, graphInts, graphChars);
    } else {
      document.tree = new IntervalTree(new Interval(0, document.getFullLength(), 0));
    }
  }

  public static void updateGraph(Document document, int[] graphInts, char[] graphChars) {
    ScopeGraphReader reader = new ScopeGraphReader(graphInts, graphChars);
    reader.readFromInts();
    document.scopeGraph = new ScopeGraph(reader.scopeRoot, reader.typeMap);
    document.tree = new IntervalTree(reader.intervalRoot);
  }

  public static void updateDocumentInterval(Document document, ParseResult parseRes) {
    updateDocumentInterval(document, parseRes.ints, parseRes.source, parseRes.graphInts, parseRes.graphChars);
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
    boolean success = reader.next() == 1;
    int N = reader.next();
    int K = reader.next();

    int stDoc, stChar, endDoc, endChar;
    if (document.isMapped()) {
      int s = document.sourceLineAtOffset(intervalStart);
      int e = document.sourceLineAtEndOffset(intervalStop);
      if (N != e - s + 1) {
        document.warnApplyAbort();
        return;
      }
      stDoc = document.docLineOfSource(s);
      endDoc = document.docLineOfSource(e);
      stChar = intervalStart - document.srcStart[s];
      endChar = intervalStop - document.srcStart[e];
      for (int i = 0; i < N; i++) {
        int docSlot = document.docLineOfSource(s + i);
        CodeElement[] elements = readElements(reader, chars, intervalStart);
        if (i == 0) elements = ArrayOp.add(
            document.lines[stDoc].getElementsToLeft(stChar), elements);
        if (i == N - 1) elements = ArrayOp.add(
            elements, document.lines[endDoc].getElementsToRight(endChar));
        CodeLine line = new CodeLine(elements);
        CodeLine prev = document.lines[docSlot];
        line.lineNumber = prev != null ? prev.lineNumber : s + i + 1;
        document.setLine(docSlot, line, success);
      }
    } else {
      V2i stLine = document.getLine(intervalStart);
      V2i endLine = document.getLine(intervalStop);
      stDoc = stLine.x;
      stChar = stLine.y;
      endDoc = endLine.x;
      endChar = endLine.y;
      CodeElement[] left = document.lines[stDoc].getElementsToLeft(stChar);
      CodeElement[] right = document.lines[endDoc].getElementsToRight(endChar);

      for (int i = 0; i < N; i++) {
        CodeElement[] elements = readElements(reader, chars, intervalStart);
        if (i == 0) elements = ArrayOp.add(left, elements);
        if (i == N - 1) elements = ArrayOp.add(elements, right);
        CodeLine line = new CodeLine(elements);
        int slot = stDoc + i;
        if (slot < document.lines.length && document.lines[slot] != null) {
          line.lineNumber = document.lines[slot].lineNumber;
        }
        document.setLine(slot, line, success);
      }
    }

    if (success) {
      if (K != 0) {
        IntervalNode intervalNode = IntervalNode.getNode(reader);
        Interval oldInterval = new Interval(intervalStart, intervalStop, -1);
        document.tree.replaceInterval(oldInterval, intervalNode);
      } else if (graphInts != null && graphChars != null) {
        ScopeGraphReader graphReader = new ScopeGraphReader(graphInts, graphChars);
        graphReader.readFromInts();
        Interval oldInterval = new Interval(intervalStart, intervalStop, -1);
        document.tree.replaceInterval(oldInterval, graphReader.intervalRoot);
        document.scopeGraph.root = document.tree.root.scope;
        document.scopeGraph.typeMap = graphReader.typeMap;
      }
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

  static final Function<Pos, List<Pos>> mapFunc = key -> new ArrayList<>();

  public static void getDefToUsagesMap(Map<Pos, Pos> usageToDef, Map<Pos, List<Pos>> defMap) {
    for (var entry : usageToDef.entrySet()) {
      var usage = entry.getKey();
      var definition = entry.getValue();
      defMap.computeIfAbsent(definition, mapFunc).add(usage);
    }
    for (var usages : defMap.values()) Collections.sort(usages);
  }

}
