package org.sudu.experiments.parser.typescript.parser;

import org.antlr.v4.runtime.*;
import org.sudu.experiments.parser.common.NullParser;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.help.Helper;
import org.sudu.experiments.parser.typescript.TsSplitRules;
import org.sudu.experiments.parser.typescript.gen.LightTypeScriptLexer;
import org.sudu.experiments.parser.typescript.parser.highlighting.LightTypeScriptHighlighting;

public class TypeScriptLightParser extends BaseFullParser<NullParser> {

  @Override
  public int[] parse(char[] source) {
    long parsingTime = System.currentTimeMillis();
    initLexer(source);
    highlightTokens();
    var result = getIntsWithLinesIntervalNode();
    System.out.println("Light lexing ts time: " + (System.currentTimeMillis() - parsingTime) + "ms");
    return result;
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
  protected boolean tokenFilter(Token token) {
    int type = token.getType();
    return type != LightTypeScriptLexer.LineTerminator
        && type != LightTypeScriptLexer.EOF;
  }

  @Override
  protected void highlightTokens() {
    LightTypeScriptHighlighting.highlightTokens(allTokens, tokenTypes);
  }
}
