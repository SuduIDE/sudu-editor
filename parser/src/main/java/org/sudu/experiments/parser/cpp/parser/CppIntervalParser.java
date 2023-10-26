package org.sudu.experiments.parser.cpp.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.common.base.BaseIntervalParser;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.graph.type.TypeMap;
import org.sudu.experiments.parser.cpp.CppSplitRules;
import org.sudu.experiments.parser.cpp.gen.CPP14Lexer;
import org.sudu.experiments.parser.cpp.gen.CPP14Parser;
import org.sudu.experiments.parser.cpp.walker.CppWalker;
import org.sudu.experiments.parser.ParserConstants.IntervalTypes;
import org.sudu.experiments.parser.cpp.walker.CppClassWalker;

import java.util.HashMap;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.ANNOTATION;
import static org.sudu.experiments.parser.ParserConstants.TokenTypes.COMMENT;

public class CppIntervalParser extends BaseIntervalParser<CPP14Parser> {

  @Override
  protected IntervalNode parseInterval(Interval interval, TypeMap typeMap) {
    CPP14Parser parser = new CPP14Parser(tokenStream);
    ParserRuleContext ruleContext;
    Interval initInterval;

    if (interval.intervalType == IntervalTypes.Cpp.TRANS_UNIT) {
      ruleContext = parser.translationUnitOrAny();
      initInterval = new Interval(0, fileSourceLength, IntervalTypes.Java.COMP_UNIT);
    } else {
      ruleContext = parser.unknownInterval();
      initInterval = defaultInterval();
    }

    ParseTreeWalker walker = new ParseTreeWalker();

    var classWalker = new CppClassWalker(new IntervalNode(initInterval));
    classWalker.intervalStart = intervalStart;
    walker.walk(classWalker, ruleContext);
    var cppWalker = new CppWalker(tokenTypes, tokenStyles, classWalker.current, new HashMap<>());
    walker.walk(cppWalker, ruleContext);
    highlightTokens();

    return classWalker.node;
  }

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new CPP14Lexer(stream);
  }

  @Override
  protected CPP14Parser initParser() {
    return new CPP14Parser(tokenStream);
  }

  @Override
  protected SplitRules initSplitRules() {
    return new CppSplitRules();
  }

  @Override
  protected boolean tokenFilter(Token token) {
    int type = token.getType();
    return type != CPP14Lexer.Newline
        && type != CPP14Lexer.EOF;
  }

  @Override
  protected void highlightTokens() {
    for (var token : allTokens) {
      int ind = token.getTokenIndex();
      if (isComment(token.getType())) tokenTypes[ind] = COMMENT;
      else if (isDirective(token.getType())) tokenTypes[ind] = ANNOTATION;
    }
  }

  public static boolean isDirective(int tokenType) {
    return tokenType == CPP14Lexer.Directive
        || tokenType == CPP14Lexer.MultiLineMacro;
  }

  public static boolean isComment(int tokenType) {
    return tokenType == CPP14Lexer.BlockComment
        || tokenType == CPP14Lexer.LineComment;
  }

}
