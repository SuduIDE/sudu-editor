package org.sudu.experiments.parser.html.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.base.BaseFirstLinesLexer;
import org.sudu.experiments.parser.help.Helper;
import org.sudu.experiments.parser.html.HtmlSplitRules;
import org.sudu.experiments.parser.html.gen.HTMLLexer;
import org.sudu.experiments.parser.html.gen.HTMLParser;

public class HtmlFirstLinesLexer extends BaseFirstLinesLexer<HTMLParser> {

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new HTMLLexer(stream);
  }

  @Override
  protected SplitRules initSplitRules() {
    return new HtmlSplitRules();
  }

  @Override
  protected String language() {
    return Helper.HTML;
  }

  @Override
  protected void highlightTokens() {
    HtmlHighlight.highlightTokens(allTokens, tokenTypes);
  }
}
