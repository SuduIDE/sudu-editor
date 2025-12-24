package org.sudu.experiments.parser.json.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.base.BaseFirstLinesLexer;
import org.sudu.experiments.parser.help.Helper;
import org.sudu.experiments.parser.json.JsonSplitRules;
import org.sudu.experiments.parser.json.gen.JsonLexer;
import org.sudu.experiments.parser.json.gen.JsonParser;

public class JsonFirstLinesLexer extends BaseFirstLinesLexer<JsonParser> {

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new JsonLexer(stream);
  }

  @Override
  protected SplitRules initSplitRules() {
    return new JsonSplitRules();
  }

  @Override
  protected boolean doTokenFilter(Token token) {
    return token.getType() != JsonLexer.NEWLINE
        && token.getType() != JsonLexer.EOF;
  }

  @Override
  protected void highlightTokens() {
    JsonHighlight.highlightTokens(allTokens, tokenTypes);
  }
}
