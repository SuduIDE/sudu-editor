package org.sudu.experiments.parser.html.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.base.BaseFirstLinesLexer;
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
  protected boolean doTokenFilter(Token token) {
    return token.getType() != HTMLLexer.SEA_NEW_LINE
        && token.getType() != HTMLLexer.TAG_NEW_LINE
        && token.getType() != HTMLLexer.EOF;
  }

  @Override
  protected void highlightTokens() {
    HtmlHighlight.highlightTokens(allTokens, tokenTypes);
  }
}
