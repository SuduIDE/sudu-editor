package org.sudu.experiments.parser.cpp.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.common.BaseIntervalParser;
import org.sudu.experiments.parser.cpp.gen.CPP14Lexer;
import org.sudu.experiments.parser.cpp.gen.CPP14Parser;
import org.sudu.experiments.parser.cpp.walker.CppWalker;
import org.sudu.experiments.parser.ParserConstants.IntervalTypes;
import org.sudu.experiments.parser.cpp.walker.CppClassWalker;

import java.util.HashMap;
import java.util.List;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.ANNOTATION;
import static org.sudu.experiments.parser.ParserConstants.TokenTypes.COMMENT;

public class CppIntervalParser extends BaseIntervalParser {

  @Override
  protected List<Interval> parseInterval(Interval interval) {
    CPP14Parser parser = new CPP14Parser(tokenStream);
    ParserRuleContext ruleContext;

    ruleContext = switch (interval.intervalType) {
      case IntervalTypes.Cpp.TRANS_UNIT -> parser.translationUnitOrAny();
      default -> parser.unknownInterval();
    };
    ParseTreeWalker walker = new ParseTreeWalker();

    CppClassWalker classWalker = new CppClassWalker();
    walker.walk(classWalker, ruleContext);

    var cppWalker = new CppWalker(tokenTypes, tokenStyles, classWalker.current, new HashMap<>());
    walker.walk(cppWalker, ruleContext);
    highlightTokens();

    if (interval.intervalType == IntervalTypes.Cpp.TRANS_UNIT) {
      var compUnitInterval = new Interval(0, fileSourceLength, IntervalTypes.Cpp.TRANS_UNIT);
      classWalker.intervals.add(0, compUnitInterval);
    }
    return classWalker.intervals;
  }

  @Override
  protected boolean isMultilineToken(int tokenType) {
    return tokenType == CPP14Lexer.BlockComment
        || tokenType == CPP14Lexer.Directive
        || tokenType == CPP14Lexer.MultiLineMacro
        || tokenType == CPP14Lexer.StringLiteral;
  }

  @Override
  protected boolean isComment(int tokenType) {
    return tokenType == CPP14Lexer.BlockComment
        || tokenType == CPP14Lexer.LineComment;
  }

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new CPP14Lexer(stream);
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
}
