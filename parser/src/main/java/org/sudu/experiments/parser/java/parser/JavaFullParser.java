package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.misc.Predicate;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.ErrorHighlightingStrategy;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.SplitToken;
import org.sudu.experiments.parser.common.BaseFullParser;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.cpp.parser.highlighting.CppLexerHighlighting;
import org.sudu.experiments.parser.java.JavaSplitRules;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.gen.help.JavaStringSplitter;
import org.sudu.experiments.parser.java.walker.JavaClassWalker;
import org.sudu.experiments.parser.java.walker.JavaWalker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.sudu.experiments.parser.ParserConstants.*;

public class JavaFullParser extends BaseFullParser {

  public int[] parse(String source) {
    long parsingStartTime = System.currentTimeMillis();
    initLexer(source);

    return parse(source.length(), parsingStartTime);
  }

  public int[] parse(char[] source) {
    long parsingStartTime = System.currentTimeMillis();
    initLexer(source);

    return parse(source.length, parsingStartTime);
  }

  private int[] parse(int sourceLength, long parsingStartTime) {
    JavaParser parser = new JavaParser(tokenStream);
    parser.setErrorHandler(new ErrorHighlightingStrategy());
    parser.removeErrorListeners();
    parser.addErrorListener(parserRecognitionListener);

    var compUnit = parser.compilationUnit();
    if (parserErrorOccurred()) CppLexerHighlighting.highlightTokens(allTokens, tokenTypes);
    else highlightTokens();

    ParseTreeWalker walker = new ParseTreeWalker();
    var classWalker = new JavaClassWalker();
    int[] result;

    try {
      walker.walk(classWalker, compUnit);

      var javaWalker = new JavaWalker(tokenTypes, tokenStyles, classWalker.dummy, classWalker.types, usageToDefinition);
      walker.walk(javaWalker, compUnit);
      classWalker.intervals.add(new Interval(0, sourceLength, IntervalTypes.Java.COMP_UNIT));

      result = getInts(classWalker.intervals);
    } catch (Exception e) {
      e.printStackTrace();
      result = getInts(List.of(defaultInterval()));
    }

    System.out.println("Parsing full java time: " + (System.currentTimeMillis() - parsingStartTime) + "ms");
    return result;
  }

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new JavaLexer(stream);
  }

  @Override
  protected SplitRules initSplitRules() {
    return new JavaSplitRules();
  }

  @Override
  protected boolean tokenFilter(Token token) {
    int type = token.getType();
    return type != JavaLexer.NEW_LINE
        && type != JavaLexer.EOF;
  }

  @Override
  protected boolean isErrorToken(int tokenType) {
    return tokenType == JavaLexer.ERROR;
  }

  public static boolean isComment(int type) {
    return type == JavaLexer.COMMENT
        || type == JavaLexer.LINE_COMMENT;
  }

  @Override
  protected void highlightTokens() {
    for (var token: allTokens) {
      int ind = token.getTokenIndex();
      if (isComment(token.getType())) tokenTypes[ind] = TokenTypes.COMMENT;
      if (isErrorToken(token.getType())) tokenTypes[ind] = ParserConstants.TokenTypes.ERROR;
    }
  }

}
