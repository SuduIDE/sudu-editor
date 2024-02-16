package org.sudu.experiments.parser.common.base;

import org.antlr.v4.runtime.*;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.graph.ScopeWalker;
import org.sudu.experiments.parser.common.tree.IntervalNode;

public abstract class FullIntervalParser<P extends Parser> extends BaseIntervalParser<P> {

  protected final BaseFullParser<P> fullParser;

  protected FullIntervalParser(BaseFullParser<P> fullParser) {
    this.fullParser = fullParser;
  }

  @Override
  public int[] parseInterval(char[] source, int[] interval, int[] graphInts, char[] graphChars) {
    return fullParser.parse(source);
  }

  @Override
  protected void walkScopes(ParserRuleContext startRule, ScopeWalker scopeWalker) {

  }

  @Override
  protected Lexer initLexer(CharStream stream) {
    return null;
  }

  @Override
  protected P initParser() {
    return null;
  }

  @Override
  protected ParserRuleContext getStartRule(P parser) {
    return null;
  }

  @Override
  protected IntervalNode walk(ParserRuleContext startRule) {
    return null;
  }

  @Override
  protected SplitRules initSplitRules() {
    return null;
  }

  @Override
  protected boolean tokenFilter(Token token) {
    return false;
  }

  @Override
  protected void highlightTokens() {

  }
}
