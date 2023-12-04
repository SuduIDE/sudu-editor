package org.sudu.experiments.parser.activity;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.ErrorHighlightingStrategy;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.activity.gen.ActivityLexer;
import org.sudu.experiments.parser.activity.gen.ActivityParser;
import org.sudu.experiments.parser.activity.graph.stat.Activity;
import org.sudu.experiments.parser.activity.graph.stat.Random;
import org.sudu.experiments.parser.activity.walker.ActivityWalker;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.tree.IntervalNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityFullParser extends BaseFullParser<ActivityParser> {

  public Activity activity;

  public ActivityFullParser() {
    Random.setGlobalSeedAndInitiateRandom(Random.getGlobalSeed());
  }

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new ActivityLexer(stream);
  }

  @Override
  protected ActivityParser initParser() {
    ActivityParser parser = new ActivityParser(tokenStream);
    parser.setErrorHandler(new ErrorHighlightingStrategy());
    return parser;
  }

  public List<Object> parseActivity(char[] source) {
    initLexer(source);
    ActivityParser parser = initParser();

    var program = getStartRule(parser);
    var walker = new ActivityWalker(tokenTypes, tokenStyles, usageToDefinition);
    var parseTreeWalker = new ParseTreeWalker();
    parseTreeWalker.walk(walker, program);

    activity = walker.getActivity();
    System.out.println("READ new ACTIVITY:>>\r\n" + activity);

    var ret = new ArrayList<>();
    ret.add(getInts(defaultIntervalNode()));
    String mermaid1 = walker.getActivity().toDag1();
    String mermaid2 = activity.dag2(false).printRecDag2(null);
    ret.add(mermaid1);
    ret.add(mermaid2);
    return ret;
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
      if (token.getType() == ActivityLexer.ERROR) {
        tokenTypes[token.getTokenIndex()] = ParserConstants.TokenTypes.ERROR;
      }
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
