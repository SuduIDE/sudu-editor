package org.sudu.experiments.parser.javascript.parser;

import org.antlr.v4.runtime.*;
import org.sudu.experiments.parser.common.NullParser;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.javascript.JsSplitRules;
import org.sudu.experiments.parser.javascript.gen.LightJavaScriptLexer;
import org.sudu.experiments.parser.javascript.parser.highlighting.LightJavaScriptHighlighting;

public class JavaScriptLightParser extends BaseFullParser<NullParser> {

  @Override
  public int[] parse(char[] source) {
    long parsingTime = System.currentTimeMillis();
    initLexer(source);
    highlightTokens();
    var result = getIntsWithLinesIntervalNode();
    System.out.println("Light lexing js time: " + (System.currentTimeMillis() - parsingTime) + "ms");
    return result;
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
  protected ParserRuleContext getStartRule(NullParser parser) {
    return null;
  }

  @Override
  protected IntervalNode walk(ParserRuleContext startRule) {
    return null;
  }

  @Override
  protected void highlightTokens() {
    LightJavaScriptHighlighting.highlightTokens(allTokens, tokenTypes);
  }
}
