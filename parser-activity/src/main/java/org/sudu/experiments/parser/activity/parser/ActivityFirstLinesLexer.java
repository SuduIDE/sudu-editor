package org.sudu.experiments.parser.activity.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.sudu.experiments.parser.activity.gen.ActivityLexer;
import org.sudu.experiments.parser.common.NullParser;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.base.BaseFirstLinesLexer;

public class ActivityFirstLinesLexer extends BaseFirstLinesLexer<NullParser> {
  @Override
  protected Lexer initLexer(CharStream stream) {
    return new ActivityLexer(stream);
  }

  @Override
  protected SplitRules initSplitRules() {
    return SplitRules.EMPTY;
  }

  @Override
  protected String language() {
    return "";
  }

  @Override
  protected void highlightTokens() {

  }
}
