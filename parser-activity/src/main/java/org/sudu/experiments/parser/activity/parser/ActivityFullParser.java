package org.sudu.experiments.parser.activity.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.ErrorHighlightingStrategy;
import org.sudu.experiments.parser.Utils;
import org.sudu.experiments.parser.activity.gen.ActivityLexer;
import org.sudu.experiments.parser.activity.gen.ActivityParser;
import org.sudu.experiments.parser.activity.walker.ActivityWalker;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.tree.IntervalNode;

import java.util.Collections;
import java.util.List;

public class ActivityFullParser extends BaseFullParser<ActivityParser> {

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new ActivityLexer(stream);
  }

  @Override
  protected ActivityParser initParser() {
    ActivityParser parser = new ActivityParser(tokenStream);
    parser.setErrorHandler(new ErrorHighlightingStrategy(tokenTypes, tokenStyles));
    return parser;
  }

  public int[] parseActivity(char[] source) {
    initLexer(source);
    parseInternal();
    return getInts(defaultIntervalNode());
  }

  private void parseInternal() {
    ActivityParser parser = initParser();

    var program = getStartRule(parser);
    var walker = new ActivityWalker(tokenTypes, tokenStyles, usageToDefinition);
    var parseTreeWalker = new ParseTreeWalker();
    parseTreeWalker.walk(walker, program);
  }

  @Override
  protected SplitRules initSplitRules() {
    return new SplitRules() {
      @Override
      public List<TokenSplitRule> getRules() {
        return Collections.emptyList();
      }
    };
  }

  @Override
  protected boolean tokenFilter(Token token) {
    int type = token.getType();
    return type != ActivityLexer.EOF && type != ActivityLexer.NEW_LINE;
  }

  @Override
  protected void highlightTokens() {
    for (var token: allTokens) {
      if (token.getType() == ActivityLexer.ERROR)
        Utils.markError(tokenTypes, tokenStyles, token.getTokenIndex());
    }
  }

  @Override
  protected ParserRuleContext getStartRule(ActivityParser parser) {
    return parser.activity();
  }

  @Override
  protected IntervalNode walk(ParserRuleContext startRule) {
    throw new UnsupportedOperationException();
  }
}
