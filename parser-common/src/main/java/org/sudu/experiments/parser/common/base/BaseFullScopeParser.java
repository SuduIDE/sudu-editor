package org.sudu.experiments.parser.common.base;

import org.antlr.v4.runtime.Parser;
import org.sudu.experiments.parser.ErrorHighlightingStrategy;
import org.sudu.experiments.parser.common.graph.ScopeWalker;
import org.sudu.experiments.parser.common.graph.writer.ScopeGraphWriter;
import org.sudu.experiments.parser.common.tree.IntervalNode;

public abstract class BaseFullScopeParser<P extends Parser> extends BaseFullParser<P> {

  private ScopeGraphWriter writer;
  protected ScopeWalker scopeWalker;

  public int[] parse(char[] source) {
    long parsingStartTime = System.currentTimeMillis();

    initLexer(source);
    var parser = initParser();
    var errorStrategy = new ErrorHighlightingStrategy(tokenTypes, tokenStyles);
    parser.setErrorHandler(errorStrategy);
    parser.removeErrorListeners();
    parser.addErrorListener(parserRecognitionListener);

    highlightTokens();

    IntervalNode node;
    int[] result;
    try {
      var rule = getStartRule(parser);
      throwIfError(errorStrategy);
      node = walk(rule);
    } catch (Exception e) {
      System.err.println("Exception during parsing: " + e.getMessage());
      node = defaultIntervalNode();
      scopeWalker = new ScopeWalker(node);
    }
    if (parserErrorOccurred()) node = defaultIntervalNode();

    result = getInts(null);
    writer = new ScopeGraphWriter(scopeWalker.graph, node);
    writer.toInts();
    if (printResult) System.out.println("Parsing done in: " + (System.currentTimeMillis() - parsingStartTime) + "ms");
    return result;
  }

  public int[] getGraphInts() {
    return writer.graphInts;
  }

  public char[] getGraphChars() {
    return writer.graphChars;
  }
}
