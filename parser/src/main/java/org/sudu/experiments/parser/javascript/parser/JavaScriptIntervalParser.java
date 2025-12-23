package org.sudu.experiments.parser.javascript.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.common.NullParser;
import org.sudu.experiments.parser.common.base.BaseIntervalParser;
import org.sudu.experiments.parser.common.graph.ScopeWalker;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.javascript.JsSplitRules;
import org.sudu.experiments.parser.javascript.gen.LightJavaScriptLexer;
import org.sudu.experiments.parser.javascript.parser.highlighting.LightJavaScriptHighlighting;
import java.util.Arrays;

public class JavaScriptIntervalParser extends BaseIntervalParser<NullParser> {

  @Override
  public int[] parseInterval(char[] source, int[] interval, int[] graphInts, char[] graphChars) {
    intervalStart = interval[0];
    intervalStop = interval[1];
    intervalType = interval[2];

    initLexer(Arrays.copyOfRange(source, intervalStart, intervalStop));
    highlightTokens();
    return getVpIntsWithLinesIntervalNode(intervalStart, intervalStop);
  }

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new LightJavaScriptLexer(stream);
  }

  @Override
  protected NullParser initParser() {
    return null;
  }

  @Override
  protected ParserRuleContext getStartRule(NullParser parser) {
    return null;
  }

  @Override
  protected IntervalNode walk(ParserRuleContext startRule) {
    return null;
  }

  @Override
  protected SplitRules initSplitRules() {
    return new JsSplitRules();
  }

  @Override
  protected boolean tokenFilter(Token token) {
    int type = token.getType();
    return type != LightJavaScriptLexer.LineTerminator
        && type != LightJavaScriptLexer.EOF;
  }

  @Override
  protected void highlightTokens() {
    LightJavaScriptHighlighting.highlightTokens(allTokens, tokenTypes);
  }

  @Override
  protected void walkScopes(ParserRuleContext startRule, ScopeWalker scopeWalker) {

  }
}
