package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.ErrorHighlightingStrategy;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.SplitToken;
import org.sudu.experiments.parser.common.BaseFullParser;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.gen.help.JavaStringSplitter;
import org.sudu.experiments.parser.java.walker.ClassWalker;
import org.sudu.experiments.parser.java.walker.JavaWalker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.sudu.experiments.parser.ParserConstants.*;

public class JavaFullParser extends BaseFullParser {

  public int[] parse(String source) {
    long parsingStartTime = System.currentTimeMillis();
    initLexer(source);

    JavaParser parser = new JavaParser(tokenStream);
    parser.setErrorHandler(new ErrorHighlightingStrategy());

    var compUnit = parser.compilationUnit();
    ParseTreeWalker walker = new ParseTreeWalker();

    highlightTokens();

    var classWalker = new ClassWalker();
    walker.walk(classWalker, compUnit);

    var javaWalker = new JavaWalker(tokenTypes, tokenStyles, classWalker.dummy, usageToDefinition);
    walker.walk(javaWalker, compUnit);
    classWalker.intervals.add(new Interval(0, source.length(), IntervalTypes.Java.COMP_UNIT));

    var result = getInts(classWalker.intervals);
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

}
