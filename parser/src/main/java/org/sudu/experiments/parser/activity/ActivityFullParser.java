package org.sudu.experiments.parser.activity;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.ParserConstants;
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
    return new ActivityParser(tokenStream);
  }

  @Override
  protected SplitRules initSplitRules() {
    return new SplitRules() {
      @Override
      public List<TokenSplitRule> getRules() {
        return List.of(makeRule(_1 -> true, super::splitTokenByLine));
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
      if (token.getType() == ActivityLexer.ERROR) {
        tokenTypes[token.getTokenIndex()] = ParserConstants.TokenTypes.ERROR;
      }
    }
  }

  @Override
  protected ParserRuleContext getStartRule(ActivityParser parser) {
    return parser.program();
  }

  @Override
  protected IntervalNode walk(ParserRuleContext startRule) {
    var walker = new ActivityWalker(tokenTypes, tokenStyles, usageToDefinition);
    var parseTreeWalker = new ParseTreeWalker();
    parseTreeWalker.walk(walker, startRule);
    var node = defaultIntervalNode();
    node.interval.intervalType = 0;
    return node;
  }
}
