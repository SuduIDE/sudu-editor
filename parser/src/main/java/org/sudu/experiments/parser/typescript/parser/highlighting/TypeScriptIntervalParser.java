package org.sudu.experiments.parser.typescript.parser.highlighting;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.common.NullParser;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.base.BaseIntervalParser;
import org.sudu.experiments.parser.common.graph.ScopeWalker;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.typescript.TsSplitRules;
import org.sudu.experiments.parser.typescript.gen.LightTypeScriptLexer;

import java.util.Arrays;

public class TypeScriptIntervalParser extends BaseIntervalParser<NullParser> {

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
  protected void walkScopes(ParserRuleContext startRule, ScopeWalker scopeWalker) {

  }

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new LightTypeScriptLexer(stream);
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
    return new TsSplitRules();
  }

  @Override
  protected boolean doTokenFilter(Token token) {
    int type = token.getType();
    return type != LightTypeScriptLexer.LineTerminator
        && type != LightTypeScriptLexer.EOF;
  }

  @Override
  protected void highlightTokens() {
    LightTypeScriptHighlighting.highlightTokens(allTokens, tokenTypes);
  }
}
