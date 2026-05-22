package org.sudu.experiments.parser.python.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.sudu.experiments.parser.common.NullParser;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.help.Helper;
import org.sudu.experiments.parser.python.PythonSplitRules;
import org.sudu.experiments.parser.python.gen.PythonLexer;
import org.sudu.experiments.parser.python.parser.highlighting.PythonHighlighting;

public class PythonLightParser extends BaseFullParser<NullParser> {

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
    return new PythonLexer(stream);
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
    return new PythonSplitRules();
  }

  @Override
  protected String language() {
    return Helper.PYTHON;
  }

  @Override
  protected void highlightTokens() {
    PythonHighlighting.highlightTokens(allTokens, tokenTypes);
  }
}
