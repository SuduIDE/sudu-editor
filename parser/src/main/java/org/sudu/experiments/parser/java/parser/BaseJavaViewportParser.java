package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.parser.highlighting.JavaLexerHighlighting;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class BaseJavaViewportParser extends BaseJavaParser {

  protected int[] getVpInts(int intervalStart, int intervalStop) {
    int N = allTokens.get(allTokens.size() - 1).getLine() - 1;
    int M = 0;
    int K = 0;
    Map<Integer, List<Token>> tokensByLine = groupTokensByLine(allTokens);
    for (var entry : tokensByLine.entrySet()) {
      var filtered = entry.getValue().stream()
          .filter(this::tokenFilter)
          .collect(Collectors.toList());
      entry.setValue(filtered);
      M += filtered.size();
      N = Math.max(N, entry.getKey());
    }

    int[] result = new int[5 + N + 4 * M];
    result[0] = intervalStart;
    result[1] = intervalStop;
    result[2] = N;
    result[3] = M;
    result[4] = K;

    int ind = 0;
    for (int i = 0; i < N; i++) {
      var tokensOnLine = tokensByLine.getOrDefault(i + 1, Collections.emptyList());
      result[5 + i] = tokensOnLine.size();
      for (var token : tokensOnLine) {
        result[5 + N + 4 * ind] = token.getStartIndex();
        result[5 + N + 4 * ind + 1] = token.getStopIndex() + 1;
        result[5 + N + 4 * ind + 2] = tokenTypes[token.getTokenIndex()];
        result[5 + N + 4 * ind + 3] = tokenStyles[token.getTokenIndex()];
        ind++;
      }
    }

    return result;
  }

  @Override
  protected void highlightTokens() {
    JavaLexerHighlighting.highlightTokens(allTokens, tokenTypes);
  }

  @Override
  protected boolean isComment(int tokenType) {
    return JavaLexerHighlighting.isComment(tokenType);
  }

  @Override
  protected boolean isMultilineToken(int tokenType) {
    return tokenType == JavaLexer.COMMENT
        || tokenType == JavaLexer.TEXT_BLOCK;
  }

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new JavaLexer(stream);
  }

  @Override
  protected boolean tokenFilter(Token token) {
    int type = token.getType();
    return type != JavaLexer.NEW_LINE
        && type != JavaLexer.EOF;
  }

}
