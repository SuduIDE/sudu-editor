package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.java.ParserConstants;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.walker.ClassWalker;
import org.sudu.experiments.parser.java.walker.JavaWalker;

public class JavaFullParser extends BaseJavaFullParser {

  public int[] parse(String source) {
    long parsingStartTime = System.currentTimeMillis();
    initLexer(source);

    JavaParser parser = new JavaParser(tokenStream);

    var compUnit = parser.compilationUnit();
    ParseTreeWalker walker = new ParseTreeWalker();

    highlightTokens();

    var classWalker = new ClassWalker();
    walker.walk(classWalker, compUnit);

    var javaWalker = new JavaWalker(tokenTypes, tokenStyles, classWalker.dummy);
    walker.walk(javaWalker, compUnit);
    classWalker.intervals.add(new Interval(0, source.length(), ParserConstants.IntervalTypes.COMP_UNIT));

    var result = getInts(classWalker.intervals);
    System.out.println("Parsing full java time: " + (System.currentTimeMillis() - parsingStartTime) + "ms");
    return result;
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
