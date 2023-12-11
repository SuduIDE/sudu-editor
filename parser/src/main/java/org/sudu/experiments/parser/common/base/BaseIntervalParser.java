package org.sudu.experiments.parser.common.base;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.parser.ErrorHighlightingStrategy;
import org.sudu.experiments.parser.common.graph.ScopeWalker;
import org.sudu.experiments.parser.common.graph.writer.ScopeGraphWriter;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.common.graph.reader.ScopeGraphReader;
import org.sudu.experiments.parser.common.graph.type.TypeMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Base class for parsers, that parse only fragment in random place in file
public abstract class BaseIntervalParser<P extends Parser> extends BaseParser<P> {

  protected int intervalStart;
  protected int intervalStop;
  protected int intervalType;
  private ScopeGraphReader scopeReader;
  private ScopeGraphWriter scopeWriter;

  protected IntervalNode parseInterval() {
    var parser = initParser();
    parser.setErrorHandler(new ErrorHighlightingStrategy(tokenTypes));
    parser.removeErrorListeners();
    parser.addErrorListener(parserRecognitionListener);

    var intervalRule = getStartRule(parser);
    var node = walk(intervalRule);

    normalize(node.children);
    return node;
  }

  protected IntervalNode parseIntervalScope() {
    var parser = initParser();
    parser.setErrorHandler(new ErrorHighlightingStrategy(tokenTypes));
    parser.removeErrorListeners();
    parser.addErrorListener(parserRecognitionListener);

    var intervalNode = defaultIntervalNode();
    var intervalRule = getStartRule(parser);

    ScopeWalker scopeWalker = new ScopeWalker(intervalNode);
    scopeWalker.graph.typeMap = getTypeMap();
    scopeWalker.offset = intervalStart;
    walkScopes(intervalRule, scopeWalker);

    normalize(scopeWalker.currentNode.children);
    scopeWriter = new ScopeGraphWriter(scopeWalker.graph, scopeWalker.currentNode);
    scopeWriter.toInts();
    return null;
  }

  public int[] parseInterval(
      char[] source, int[] interval,
      int[] graphInts, char[] graphChars
  ) {
    intervalStart = interval[0];
    intervalStop = interval[1];
    intervalType = interval[2];

    initLexer(Arrays.copyOfRange(source, intervalStart, intervalStop));
    highlightTokens();

    initReader(graphInts, graphChars);

    IntervalNode parsedNode = graphInts == null || graphChars == null ?
        parseInterval() : parseIntervalScope();

    return getVpInts(intervalStart, intervalStop, parsedNode);
  }

  public int[] parseInterval(char[] source, int[] interval) {
    return parseInterval(source, interval, null, null);
  }

  protected abstract void walkScopes(ParserRuleContext startRule, ScopeWalker scopeWalker);

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

  public int[] getGraphInts() {
    if (scopeWriter == null) return null;
    return scopeWriter.graphInts;
  }

  public char[] getGraphChars() {
    if (scopeWriter == null) return null;
    return scopeWriter.graphChars;
  }

  private void initReader(int[] graphInts, char[] graphChars) {
    if (graphInts == null || graphChars == null) return;
    scopeReader = new ScopeGraphReader(graphInts, graphChars);
    scopeReader.readFromInts();
  }

  protected TypeMap getTypeMap() {
    return scopeReader.typeMap;
  }

  protected int[] makeErrorInts() {
    return new int[]{-1};
  }

}
