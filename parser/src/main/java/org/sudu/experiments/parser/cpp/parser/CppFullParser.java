package org.sudu.experiments.parser.cpp.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.IterativeParseTreeWalker;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.ErrorHighlightingStrategy;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.common.BaseFullParser;
import org.sudu.experiments.parser.common.IntervalNode;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.cpp.CppSplitRules;
import org.sudu.experiments.parser.cpp.gen.CPP14Lexer;
import org.sudu.experiments.parser.cpp.gen.CPP14Parser;
import org.sudu.experiments.parser.cpp.parser.highlighting.CppLexerHighlighting;
import org.sudu.experiments.parser.cpp.walker.CppWalker;
import org.sudu.experiments.parser.cpp.walker.CppClassWalker;

import static org.sudu.experiments.parser.ParserConstants.*;
import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class CppFullParser extends BaseFullParser {

  public int[] parse(String source) {
    long parsingTime = System.currentTimeMillis();

    initLexer(source);
    initSplitRules();
    return parseWithLexer(parsingTime);
  }

  public int[] parse(char[] source) {
    long parsingTime = System.currentTimeMillis();

    initLexer(source);
    initSplitRules();
    return parseWithLexer(parsingTime);
  }

  private int[] parseWithLexer(long parsingTime) {
    CPP14Parser parser = new CPP14Parser(tokenStream);
    parser.setErrorHandler(new ErrorHighlightingStrategy());
    parser.removeErrorListeners();
    parser.addErrorListener(parserRecognitionListener);

    var transUnit = parser.translationUnit();
    if (parserErrorOccurred()) CppLexerHighlighting.highlightTokens(allTokens, tokenTypes);
    else highlightTokens();

    ParseTreeWalker walker = new IterativeParseTreeWalker();
    Interval compUnitInterval = new Interval(0, fileSourceLength, IntervalTypes.Cpp.TRANS_UNIT);
    var classWalker = new CppClassWalker(new IntervalNode(compUnitInterval));
    int[] result;

    try {
      walker.walk(classWalker, transUnit);

      CppWalker cppWalker = new CppWalker(tokenTypes, tokenStyles, classWalker.current, usageToDefinition);
      walker.walk(cppWalker, transUnit);

      result = getInts(classWalker.node);
    } catch (Exception e) {
      e.printStackTrace();
      result = getInts(defaultIntervalNode());
    }

    System.out.println("Parsing full cpp time: " + (System.currentTimeMillis() - parsingTime) + "ms");
    return result;
  }

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new CPP14Lexer(stream);
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
  protected boolean isErrorToken(int tokenType) {
    return tokenType == CPP14Lexer.ERROR;
  }

}
