package org.sudu.experiments.parser.typescript.parser;

import org.antlr.v4.runtime.*;
import org.sudu.experiments.parser.common.NullParser;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.help.Helper;
import org.sudu.experiments.parser.ecmascript.gen.LightEcmaScriptLexer;
import org.sudu.experiments.parser.typescript.TsSplitRules;
import org.sudu.experiments.parser.typescript.parser.highlighting.LightTypeScriptHighlighting;

public class TypeScriptLightParser extends BaseFullParser<NullParser> {

  @Override
  public int[] parse(char[] source) {
    return lightParse(source);
  }

  @Override
  protected Lexer initLexer(CharStream stream) {
    var lexer = new LightEcmaScriptLexer(stream);
    lexer.setTypeScript(true);
    return lexer;
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
  protected String language() {
    return Helper.TS_LIGHT;
  }

  @Override
  protected void highlightTokens() {
    LightTypeScriptHighlighting.highlightTokens(allTokens, tokenTypes);
  }
}
