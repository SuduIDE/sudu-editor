package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.ErrorHighlightingStrategy;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.graph.writer.ScopeGraphWriter;
import org.sudu.experiments.parser.java.JavaSplitRules;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.walker.JavaClassWalker;
import org.sudu.experiments.parser.java.walker.JavaScopeWalker;

import org.sudu.experiments.parser.java.parser.highlighting.JavaLexerHighlighting;
import org.sudu.experiments.parser.java.walker.JavaWalker;

public class JavaFullParser extends BaseFullParser<JavaParser> {

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

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new JavaLexer(stream);
  }

  @Override
  protected JavaParser initParser() {
    return new JavaParser(tokenStream);
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
  protected ParserRuleContext getStartRule(JavaParser parser) {
    return parser.compilationUnit();
  }

  @Override
  protected IntervalNode walk(ParserRuleContext startRule) {
    ParseTreeWalker walker = new ParseTreeWalker();
    var defaultInterval = defaultInterval();
    defaultInterval.intervalType = ParserConstants.IntervalTypes.Java.COMP_UNIT;

    var classWalker = new JavaClassWalker(defaultIntervalNode());
    walker.walk(classWalker, startRule);

    var javaClass = classWalker.dummy;
    var types = classWalker.types;
    var javaWalker = new JavaWalker(tokenTypes, tokenStyles, javaClass, types, usageToDefinition);
    walker.walk(javaWalker, startRule);
    return classWalker.node;
  }

  @Override
  protected void highlightTokens() {
    JavaLexerHighlighting.highlightCommentTokens(allTokens, tokenTypes);
  }
}
