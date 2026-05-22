package org.sudu.experiments.parser.python.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.sudu.experiments.parser.common.NullParser;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.base.BaseIntervalParser;
import org.sudu.experiments.parser.common.graph.ScopeWalker;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.help.Helper;
import org.sudu.experiments.parser.python.PythonSplitRules;
import org.sudu.experiments.parser.python.gen.PythonLexer;
import org.sudu.experiments.parser.python.parser.highlighting.PythonHighlighting;

import java.util.Arrays;

public class PythonIntervalParser extends BaseIntervalParser<NullParser> {

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
