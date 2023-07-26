package org.sudu.experiments.parser.common;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.parser.Interval;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Base class for parsers, that parse all text in file
public abstract class BaseFullParser extends BaseParser {

  protected Map<Pos, Pos> usageToDefinition = new HashMap<>();

  protected int[] getInts(IntervalNode node) {
    int N = allTokens.get(allTokens.size() - 1).getLine();
    int M = 0;
    int[] nodeInts = node != null ? node.toInts() : new int[]{};
    int K = nodeInts.length;
    int L = usageToDefinition.size();

    Map<Integer, List<Token>> tokensByLine = groupTokensByLine(allTokens);
    for (var entry : tokensByLine.entrySet()) {
      var filtered = entry.getValue().stream()
          .filter(this::tokenFilter)
          .collect(Collectors.toList());
      entry.setValue(filtered);
      M += filtered.size();
    }

    writer = new ArrayWriter(3 + N + 4 * M + K + 4 * L);
    writer.write(N, K, L);

    writeTokens(N, tokensByLine);
    writer.write(nodeInts);
    writeUsageToDefinitions(usageToDefinition);

    return writer.getInts();
  }

  protected abstract boolean tokenFilter(Token token);

}
