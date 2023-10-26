package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.ErrorHighlightingStrategy;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.common.base.BaseIntervalParser;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.graph.type.TypeMap;
import org.sudu.experiments.parser.common.graph.writer.ScopeGraphWriter;
import org.sudu.experiments.parser.java.JavaSplitRules;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.parser.highlighting.JavaLexerHighlighting;
import org.sudu.experiments.parser.java.walker.JavaScopeWalker;


import static org.sudu.experiments.parser.ParserConstants.*;

public class JavaIntervalParser extends BaseIntervalParser<JavaParser> {

  public ScopeGraphWriter writer;

  @Override
  protected IntervalNode parseInterval(Interval interval, TypeMap typeMap) {
    JavaParser parser = new JavaParser(tokenStream);
    parser.setErrorHandler(new ErrorHighlightingStrategy());
    parser.removeErrorListeners();
    parser.addErrorListener(parserRecognitionListener);

    ParserRuleContext ruleContext;
    Interval initInterval;

    if (interval.intervalType == IntervalTypes.Java.COMP_UNIT) {
      ruleContext = parser.compilationUnitOrAny();
      initInterval = new Interval(intervalStart, intervalStart + fileSourceLength, IntervalTypes.Java.COMP_UNIT);
    } else {
      ruleContext = parser.unknownInterval();
      initInterval = defaultInterval();
    }

    JavaLexerHighlighting.highlightTokens(allTokens, tokenTypes);
    ParseTreeWalker walker = new ParseTreeWalker();
    JavaScopeWalker scopeWalker = new JavaScopeWalker(new IntervalNode(initInterval), intervalStart, tokenTypes, tokenStyles);
    scopeWalker.scopeWalker.graph.typeMap = typeMap;
    scopeWalker.offset = intervalStart;
    walker.walk(scopeWalker, ruleContext);

    var graph = scopeWalker.scopeWalker.graph;
    var node = scopeWalker.scopeWalker.currentNode;
    normalize(scopeWalker.scopeWalker.currentNode.children);
    writer = new ScopeGraphWriter(graph, node);
    writer.toInts();
    return null;
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
  protected boolean tokenFilter(Token token) {
    int type = token.getType();
    return type != JavaLexer.NEW_LINE
        && type != JavaLexer.EOF;
  }

  @Override
  protected SplitRules initSplitRules() {
    return new JavaSplitRules();
  }

  @Override
  protected void highlightTokens() {
    JavaLexerHighlighting.highlightTokens(allTokens, tokenTypes);
//    for (var token: allTokens) {
//      int ind = token.getTokenIndex();
//      if (JavaLexerHighlighting.isComment(token.getType())) tokenTypes[ind] = TokenTypes.COMMENT;
//      if (JavaLexerHighlighting.isJavadoc(token.getType())) tokenTypes[ind] = TokenTypes.JAVADOC;
//      if (JavaLexerHighlighting.isErrorToken(token.getType())) tokenTypes[ind] = ParserConstants.TokenTypes.ERROR;
//    }
  }
}
