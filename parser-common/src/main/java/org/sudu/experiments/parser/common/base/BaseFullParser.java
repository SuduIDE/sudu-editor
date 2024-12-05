package org.sudu.experiments.parser.common.base;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.parser.ErrorHighlightingStrategy;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.common.Pos;

import java.util.*;

// Base class for parsers, that parse all text in file
public abstract class BaseFullParser<P extends Parser> extends BaseParser<P> implements IntParser {

  public Map<Pos, Pos> usageToDefinition = new HashMap<>();

  public int[] parse(char[] source) {
    long parsingStartTime = System.currentTimeMillis();

    initLexer(source);
    var parser = initParser();
    var errorStrategy = new ErrorHighlightingStrategy(tokenTypes, tokenStyles);
    parser.setErrorHandler(errorStrategy);
    parser.removeErrorListeners();
    parser.addErrorListener(parserRecognitionListener);

    highlightTokens();

    int[] result;
    try {
      var rule = getStartRule(parser);
      throwIfError(errorStrategy);
      var node = walk(rule);
      result = getInts(node);
    } catch (Exception e) {
      System.err.println("Exception while parsing: " + e.getMessage());
      result = getInts(defaultIntervalNode());
    }
    if (printResult) System.out.println("Parsing done in: " + (System.currentTimeMillis() - parsingStartTime) + "ms");
    return result;
  }

  protected int[] getInts() {
    return getInts(null);
  }

  protected int[] getInts(IntervalNode node) {
    int N = allTokens.get(allTokens.size() - 1).getLine();
    int M;
    int[] nodeInts = node != null ? node.toInts() : new int[]{};
    int K = nodeInts.length;
    int L = usageToDefinition.size();

    List<Token>[] tokensByLine = groupTokensByLine(allTokens, N);
    N = tokensByLine.length;
    M = filter(tokensByLine);

    writer = new ArrayWriter(3 + N + 4 * M + K + 4 * L);
    writer.write(N, K, L);

    writeTokens(N, tokensByLine);
    writer.write(nodeInts);
    writeUsageToDefinitions(usageToDefinition);

    return writer.getInts();
  }

  protected int[] getIntsWithLinesIntervalNode() {
    int N = allTokens.get(allTokens.size() - 1).getLine();
    int M;
    int K;
    int L = usageToDefinition.size();

    List<Token>[] tokensByLine = groupTokensByLine(allTokens, N);
    N = tokensByLine.length;
    M = filter(tokensByLine);
    IntervalNode node = getLinesIntervalNode(tokensByLine);
    int[] nodeInts = node.toInts();
    K = nodeInts.length;

    writer = new ArrayWriter(3 + N + 4 * M + K + 4 * L);
    writer.write(N, K, L);

    writeTokens(N, tokensByLine);
    writer.write(nodeInts);
    writeUsageToDefinitions(usageToDefinition);

    return writer.getInts();
  }

  protected void throwIfError(ErrorHighlightingStrategy strategy) {
    if (strategy.haveParseErrors && throwOnError()) throw new RuntimeException();
  }

  protected boolean throwOnError() {
    return false;
  }

  // Tokens, that
  protected abstract boolean tokenFilter(Token token);
}
