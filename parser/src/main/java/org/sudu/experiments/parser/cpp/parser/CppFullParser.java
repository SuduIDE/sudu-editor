package org.sudu.experiments.parser.cpp.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.common.BaseFullParser;
import org.sudu.experiments.parser.cpp.gen.CPP14Lexer;
import org.sudu.experiments.parser.cpp.gen.CPP14Parser;
import org.sudu.experiments.parser.cpp.walker.CppWalker;

import static org.sudu.experiments.parser.ParserConstants.*;
import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class CppFullParser extends BaseFullParser {

  public int[] parse(String source) {
    long parsingTime = System.currentTimeMillis();

    initLexer(source);

    CPP14Parser parser = new CPP14Parser(tokenStream);

    var transUnit = parser.translationUnit();
    ParseTreeWalker walker = new ParseTreeWalker();

    highlightTokens();

    CppWalker cppWalker = new CppWalker(tokenTypes, tokenStyles);
    walker.walk(cppWalker, transUnit);

    cppWalker.intervals.add(new Interval(0, source.length(), IntervalTypes.Cpp.TRANS_UNIT));

    var result = getInts(cppWalker.intervals);
    System.out.println("Parsing full cpp time: " + (System.currentTimeMillis() - parsingTime) + "ms");
    return result;
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
