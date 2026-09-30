package org.sudu.experiments.parser.python.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.sudu.experiments.parser.common.NullParser;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.base.BaseFirstLinesLexer;
import org.sudu.experiments.parser.help.Helper;
import org.sudu.experiments.parser.python.PythonSplitRules;
import org.sudu.experiments.parser.python.gen.PythonLexer;
import org.sudu.experiments.parser.python.parser.highlighting.PythonHighlighting;

public class PythonFirstLinesLexer extends BaseFirstLinesLexer<NullParser> {

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new PythonLexer(stream);
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
