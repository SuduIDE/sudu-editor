package org.sudu.experiments.parser.json.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.json.JsonSplitRules;
import org.sudu.experiments.parser.json.gen.JsonLexer;
import org.sudu.experiments.parser.json.gen.JsonParser;

public class JsonFullParser extends BaseFullParser<JsonParser> {

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new JsonLexer(stream);
  }

  @Override
  protected JsonParser initParser() {
    return new JsonParser(tokenStream);
  }

  @Override
  protected ParserRuleContext getStartRule(JsonParser parser) {
    return parser.json();
  }

  @Override
  protected IntervalNode walk(ParserRuleContext startRule) {
    var walker = new JsonWalker(tokenTypes, tokenStyles, usageToDefinition);
    var parseTreeWalker = new ParseTreeWalker();
    parseTreeWalker.walk(walker, startRule);
    return defaultIntervalNode();
  }

  @Override
  protected SplitRules initSplitRules() {
    return new JsonSplitRules();
  }

  @Override
  protected boolean tokenFilter(Token token) {
    return token.getType() != JsonLexer.NEWLINE
        && token.getType() != JsonLexer.EOF;
  }

  @Override
  protected void highlightTokens() {
    JsonHighlight.highlightTokens(allTokens, tokenTypes);
  }
}
