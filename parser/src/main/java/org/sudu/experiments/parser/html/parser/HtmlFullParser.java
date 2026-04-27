package org.sudu.experiments.parser.html.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.help.Helper;
import org.sudu.experiments.parser.html.HtmlSplitRules;
import org.sudu.experiments.parser.html.gen.HTMLLexer;
import org.sudu.experiments.parser.html.gen.HTMLParser;

public class HtmlFullParser extends BaseFullParser<HTMLParser> {

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new HTMLLexer(stream);
  }

  @Override
  protected HTMLParser initParser() {
    return new HTMLParser(tokenStream);
  }

  @Override
  protected ParserRuleContext getStartRule(HTMLParser parser) {
    return parser.htmlDocument();
  }

  @Override
  protected IntervalNode walk(ParserRuleContext startRule) {
    var walker = new HtmlWalker(tokenTypes, tokenStyles, usageToDefinition);
    var parseTreeWalker = new ParseTreeWalker();
    parseTreeWalker.walk(walker, startRule);
    return defaultIntervalNode();
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
