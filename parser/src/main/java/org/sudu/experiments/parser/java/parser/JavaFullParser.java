package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.ErrorHighlightingStrategy;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.common.BaseFullParser;
import org.sudu.experiments.parser.common.IntervalNode;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.graph.writer.ScopeGraphWriter;
import org.sudu.experiments.parser.java.JavaSplitRules;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.model.JavaClass;
import org.sudu.experiments.parser.java.walker.JavaClassWalker;
import org.sudu.experiments.parser.java.walker.JavaScopeWalker;
import org.sudu.experiments.parser.java.walker.JavaWalker;

import org.sudu.experiments.parser.java.parser.highlighting.JavaLexerHighlighting;

public class JavaFullParser extends BaseFullParser {

  private JavaClass javaClass;
  public ScopeGraphWriter writer;

  public int[] parseScopes(String source) {
    long parsingStartTime = System.currentTimeMillis();
    initLexer(source);

    JavaParser parser = new JavaParser(tokenStream);
    parser.setErrorHandler(new ErrorHighlightingStrategy());
    parser.removeErrorListeners();
    parser.addErrorListener(parserRecognitionListener);

    var compUnit = parser.compilationUnit();
    JavaLexerHighlighting.highlightTokens(allTokens, tokenTypes);

    ParseTreeWalker walker = new ParseTreeWalker();
    Interval compUnitInterval = new Interval(0, fileSourceLength, ParserConstants.IntervalTypes.Java.COMP_UNIT);
    IntervalNode compUnitNode = new IntervalNode(compUnitInterval);
    JavaScopeWalker scopeWalker = new JavaScopeWalker(compUnitNode, 0, tokenTypes, tokenStyles);

    int[] result;
    IntervalNode node;
    try {
      walker.walk(scopeWalker, compUnit);
      node = scopeWalker.scopeWalker.currentNode;
    } catch (Exception e) {
      e.printStackTrace();
      node = defaultIntervalNode();
    }
    result = getInts(null);
    writer = new ScopeGraphWriter(scopeWalker.scopeWalker.graph, node);
    writer.toInts();

    System.out.println("Parsing full java time: " + (System.currentTimeMillis() - parsingStartTime) + "ms");
    return result;
  }

  public int[] parse(String source) {
    long parsingStartTime = System.currentTimeMillis();
    initLexer(source);

    return parse(parsingStartTime);
  }

  public int[] parse(char[] source) {
    long parsingStartTime = System.currentTimeMillis();
    initLexer(source);

    return parse(parsingStartTime);
  }

  private int[] parse(long parsingStartTime) {
    JavaParser parser = new JavaParser(tokenStream);
    parser.setErrorHandler(new ErrorHighlightingStrategy());
    parser.removeErrorListeners();
    parser.addErrorListener(parserRecognitionListener);

    var compUnit = parser.compilationUnit();
    if (parserErrorOccurred()) JavaLexerHighlighting.highlightTokens(allTokens, tokenTypes);
    else highlightTokens();

    ParseTreeWalker walker = new ParseTreeWalker();
    Interval compUnitInterval = new Interval(0, fileSourceLength, ParserConstants.IntervalTypes.Java.COMP_UNIT);
    var classWalker = new JavaClassWalker(new IntervalNode(compUnitInterval));
    int[] result;

    try {
      walker.walk(classWalker, compUnit);

      javaClass = classWalker.dummy;
      var javaWalker = new JavaWalker(tokenTypes, tokenStyles, javaClass, classWalker.types, usageToDefinition);
      walker.walk(javaWalker, compUnit);

      result = getInts(classWalker.node);
    } catch (Exception e) {
      e.printStackTrace();
      JavaLexerHighlighting.highlightTokens(allTokens, tokenTypes);
      result = getInts(defaultIntervalNode());
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

  @Override
  protected void highlightTokens() {
    JavaLexerHighlighting.highlightCommentTokens(allTokens, tokenTypes);
  }

  public JavaClass getJavaClass() {
    return javaClass;
  }

}
