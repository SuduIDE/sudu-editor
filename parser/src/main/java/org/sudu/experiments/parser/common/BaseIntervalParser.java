package org.sudu.experiments.parser.common;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.parser.Interval;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Base class for parsers, that parse only fragment in random place in file
public abstract class BaseIntervalParser extends BaseParser {

  protected int intervalStart = 0;
  protected int intervalStop = 0;

  public int[] parseInterval(String source, int[] interval) {
    intervalStart = interval[0];
    intervalStop = interval[1];
    int intervalType = interval[2];
    initLexer(source.substring(intervalStart, intervalStop));

    if (tokenErrorOccurred()) return makeErrorInts();

    Interval parsingInterval = new Interval(0, intervalStop - intervalStart, intervalType);
    IntervalNode intervalNode = parseInterval(parsingInterval);

    expendIntervals(intervalNode, parsingInterval);
    return getVpInts(intervalStart, intervalStop, intervalNode);
  }

  protected abstract IntervalNode parseInterval(Interval interval);

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

  protected void expendIntervals(IntervalNode node, Interval interval) {
    // todo
/*    if (intervalList.isEmpty()) {
      intervalList.add(interval);
      return;
    }
    Interval left = intervalList.get(0);
    Interval right = intervalList.get(0);
    for (var cur: intervalList) {
      if (cur.start < left.start || (cur.start == left.start && cur.stop < left.stop)) left = cur;
      if (cur.stop > right.stop || (cur.stop == right.stop && cur.start > right.stop)) right = cur;
    }
    left.start = interval.start;
    right.stop = interval.stop;*/
  }

  protected int[] makeErrorInts() {
    return new int[]{-1};
  }

}
