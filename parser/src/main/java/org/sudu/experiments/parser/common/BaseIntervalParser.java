package org.sudu.experiments.parser.common;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.common.graph.reader.ScopeGraphReader;
import org.sudu.experiments.parser.common.graph.type.TypeMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Base class for parsers, that parse only fragment in random place in file
public abstract class BaseIntervalParser extends BaseParser {

  protected int intervalStart = 0;
  protected int intervalStop = 0;

  public int[] parseIntervalScope(
      String source, int[] interval,
      int[] graphInts, char[] graphChars
  ) {
    intervalStart = interval[0];
    intervalStop = interval[1];
    int intervalType = interval[2];
    initLexer(source.substring(intervalStart, intervalStop));

    if (tokenErrorOccurred()) return makeErrorInts();

    Interval parsingInterval = new Interval(0, intervalStop - intervalStart, intervalType);
    TypeMap typeMap = null;
    if (graphInts != null && graphChars != null) {
      var reader = new ScopeGraphReader(graphInts, graphChars);
      reader.readFromInts();
      typeMap = reader.typeMap;
    }
    IntervalNode intervalNode = parseInterval(parsingInterval, typeMap);

//    if (parserRecognitionListener.errorOccurred) return makeErrorInts();
    return getVpInts(intervalStart, intervalStop, intervalNode);
  }

  public int[] parseInterval(String source, int[] interval) {
    return parseIntervalScope(source, interval, null, null);
  }

  protected abstract IntervalNode parseInterval(Interval interval, TypeMap typeMap);
  protected IntervalNode parseInterval(Interval interval) {
    return parseInterval(interval, null);
  };
  protected void normalize(List<IntervalNode> children) {
    if (children.isEmpty()) return;
    IntervalNode first = children.get(0), last = children.get(children.size() - 1);
    first.interval.start = intervalStart;
    last.interval.stop = intervalStop;
  }

  // {intervalStart, intervalStop, N, K, }
  protected int[] getVpInts(int intervalStart, int intervalStop, IntervalNode node) {
    int N = allTokens.get(allTokens.size() - 1).getLine();
    int M = 0;
    int[] nodeInts = node != null ? node.toInts() : new int[]{};
    int K = nodeInts.length;

    Map<Integer, List<Token>> tokensByLine = groupTokensByLine(allTokens);
    for (var entry: tokensByLine.entrySet()) {
      var filtered = entry.getValue().stream()
          .filter(this::tokenFilter)
          .collect(Collectors.toList());
      entry.setValue(filtered);
      M += filtered.size();
      N = Math.max(N, entry.getKey());
    }

    writer = new ArrayWriter(4 + N + K + 4 * M);
    writer.write(intervalStart, intervalStop, N, K);

    writeTokens(N, tokensByLine);
    writer.write(nodeInts);

    return writer.getInts();
  }

  protected int[] makeErrorInts() {
    return new int[]{-1};
  }

}
