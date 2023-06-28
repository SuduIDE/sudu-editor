package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.ErrorHighlightingStrategy;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.SplitToken;
import org.sudu.experiments.parser.common.BaseFullParser;
import org.sudu.experiments.parser.cpp.parser.highlighting.CppLexerHighlighting;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.gen.help.JavaStringSplitter;
import org.sudu.experiments.parser.java.walker.JavaClassWalker;
import org.sudu.experiments.parser.java.walker.JavaWalker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
  protected List<Token> splitToken(Token token) {
    int tokenType = token.getType();
    if (tokenType == JavaLexer.COMMENT) return splitTokenByLine(token);
    if (tokenType == JavaLexer.TEXT_BLOCK || tokenType == JavaLexer.STRING_LITERAL || tokenType == JavaLexer.CHAR_LITERAL) {
      JavaStringSplitter splitter = new JavaStringSplitter(CharStreams.fromString(token.getText()));
      var splitTokenStream = new CommonTokenStream(splitter);
      splitTokenStream.fill();
      ArrayList<Token> result = new ArrayList<>();
      int line = token.getLine() - 1, start = token.getStartIndex();

      for (var splitToken : splitTokenStream.getTokens()) {
        int splitTokenType = splitToken.getType();
        if (splitTokenType == JavaStringSplitter.NEW_LINE || splitTokenType == JavaStringSplitter.EOF) continue;

        int type = splitTokenType == JavaStringSplitter.ESCAPE ? TokenTypes.KEYWORD : TokenTypes.STRING;
        result.add(new SplitToken(splitToken, line, start, type));
      }
      return result;
    }
    return Collections.singletonList(token);
  }

  @Override
  protected boolean isMultilineToken(int tokenType) {
    return tokenType == JavaLexer.COMMENT
        || tokenType == JavaLexer.TEXT_BLOCK;
  }

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new JavaLexer(stream);
  }

  @Override
  protected boolean tokenFilter(Token token) {
    int type = token.getType();
    return type != JavaLexer.NEW_LINE
        && type != JavaLexer.EOF;
  }

  @Override
  protected boolean isComment(int type) {
    return type == JavaLexer.COMMENT
        || type == JavaLexer.LINE_COMMENT;
  }

  @Override
  protected boolean isErrorToken(int tokenType) {
    return tokenType == JavaLexer.ERROR;
  }
}
