package org.sudu.experiments.parser.common;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.parser.Interval;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Base class for parsers, that parse only fragment in random place in file
public abstract class BaseIntervalParser extends BaseParser {

  public int[] parseInterval(String source, int[] interval) {
    int intervalStart = interval[0];
    int intervalStop = interval[1];
    int intervalType = interval[2];
    initLexer(source.substring(intervalStart, intervalStop));

    if (tokenErrorOccurred()) return makeErrorInts();

    Interval parsingInterval = new Interval(0, intervalStop - intervalStart, intervalType);
    List<Interval> intervalList = parseInterval(parsingInterval);

    expendIntervals(intervalList, parsingInterval);
    return getVpInts(intervalStart, intervalStop, intervalList);
  }

  protected abstract List<Interval> parseInterval(Interval interval);

  // {intervalStart, intervalStop, N, K, }
  protected int[] getVpInts(int intervalStart, int intervalStop, List<Interval> intervalList) {
    int N = allTokens.get(allTokens.size() - 1).getLine();
    int M = 0;
    int K = intervalList.size();
    Map<Integer, List<Token>> tokensByLine = groupTokensByLine(allTokens);
    for (var entry : tokensByLine.entrySet()) {
      var filtered = entry.getValue().stream()
          .filter(this::tokenFilter)
          .collect(Collectors.toList());
      entry.setValue(filtered);
      M += filtered.size();
      N = Math.max(N, entry.getKey());
    }

    writer = new ArrayWriter(4 + N + 3 * K + 4 * M);
    writer.write(intervalStart, intervalStop, N, K);

    writeTokens(N, tokensByLine);
    writeIntervals(intervalList, intervalStart);

    return writer.getInts();
  }

  protected void expendIntervals(List<Interval> intervalList, Interval interval) {
    if (intervalList.isEmpty()) {
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
    right.stop = interval.stop;
  }

  protected int[] makeErrorInts() {
    return new int[]{-1};
  }

}
