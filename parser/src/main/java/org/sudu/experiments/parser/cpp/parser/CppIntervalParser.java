package org.sudu.experiments.parser.cpp.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.common.base.BaseIntervalParser;
import org.sudu.experiments.parser.common.graph.ScopeWalker;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.common.SplitRules;
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
  protected Lexer initLexer(CharStream stream) {
    return new CPP14Lexer(stream);
  }

  @Override
  protected CPP14Parser initParser() {
    return new CPP14Parser(tokenStream);
  }

  @Override
  protected ParserRuleContext getStartRule(CPP14Parser parser) {
    return switch (intervalType) {
      case IntervalTypes.Cpp.TRANS_UNIT -> parser.translationUnitOrAny();
      default -> parser.unknownInterval();
    };
  }

  @Override
  protected IntervalNode walk(ParserRuleContext startRule) {
    ParseTreeWalker walker = new ParseTreeWalker();
    var classWalker = new CppClassWalker(defaultIntervalNode());
    classWalker.intervalStart = intervalStart;
    walker.walk(classWalker, startRule);
    var cppWalker = new CppWalker(tokenTypes, tokenStyles, classWalker.current, new HashMap<>());
    walker.walk(cppWalker, startRule);

    return classWalker.node;
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

  @Override
  protected void walkScopes(ParserRuleContext startRule, ScopeWalker scopeWalker) {
    throw new UnsupportedOperationException();
  }
}
