package org.sudu.experiments.parser.ascendc.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.sudu.experiments.parser.common.NullParser;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.help.Helper;
import org.sudu.experiments.parser.ascendc.AscendCSplitRules;
import org.sudu.experiments.parser.ascendc.gen.AscendCLexer;
import org.sudu.experiments.parser.ascendc.parser.highlighting.AscendCHighlighting;

public class AscendCLightParser extends BaseFullParser<NullParser> {

  @Override
  public int[] parse(char[] source) {
    return lightParse(source);
  }

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new AscendCLexer(stream);
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
    return new AscendCSplitRules();
  }

  @Override
  protected String language() {
    return Helper.ASCEND_C;
  }

  @Override
  protected void highlightTokens() {
    AscendCHighlighting.INSTANCE.highlight(allTokens, tokenTypes, tokenStyles);
  }
}
