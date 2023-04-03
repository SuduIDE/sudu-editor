package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.Interval;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class BaseJavaFullParser extends BaseJavaParser {

  protected int[] getInts(List<Interval> intervalList) {
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
    }

    int[] result = new int[3 + N + 4 * M + 3 * K];
    result[0] = N;
    result[1] = M;
    result[2] = K;
    int ind = 0;
    for (int i = 0; i < N; i++) {
      var tokensOnLine = tokensByLine.getOrDefault(i + 1, Collections.emptyList());
      result[3 + i] = tokensOnLine.size();
      for (var token : tokensOnLine) {
        result[3 + N + 4 * ind] = token.getStartIndex();
        result[3 + N + 4 * ind + 1] = token.getStopIndex() + 1;
        result[3 + N + 4 * ind + 2] = tokenTypes[token.getTokenIndex()];
        result[3 + N + 4 * ind + 3] = tokenStyles[token.getTokenIndex()];
        ind++;
      }
    }
    for (int i = 0; i < K; i++) {
      result[3 + N + 4 * M + 3 * i] = intervalList.get(i).start;
      result[3 + N + 4 * M + 3 * i + 1] = intervalList.get(i).stop;
      result[3 + N + 4 * M + 3 * i + 2] = intervalList.get(i).intervalType;
    }
    return result;
  }

  protected abstract boolean tokenFilter(Token token);

}
