package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.base.BaseFullScopeParser;
import org.sudu.experiments.parser.common.graph.ScopeWalker;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.java.JavaSplitRules;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.parser.highlighting.JavaLexerHighlighting;
import org.sudu.experiments.parser.java.walker.JavaScopeWalker;

public class JavaFullScopesParser extends BaseFullScopeParser<JavaParser> {

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
  protected boolean doTokenFilter(Token token) {
    int type = token.getType();
    return type != JavaLexer.NEW_LINE
        && type != JavaLexer.EOF;
  }

  @Override
  protected void highlightTokens() {
    JavaLexerHighlighting.highlightTokens(allTokens, tokenTypes, tokenStyles);
  }

  @Override
  protected ParserRuleContext getStartRule(JavaParser parser) {
    return parser.compilationUnit();
  }

  @Override
  protected IntervalNode walk(ParserRuleContext startRule) {
    ParseTreeWalker treeWalker = new ParseTreeWalker();
    var defaultInterval = defaultIntervalNode(ParserConstants.IntervalTypes.Java.COMP_UNIT);
    scopeWalker = new ScopeWalker(defaultInterval);
    var javaScopeWalker = new JavaScopeWalker(scopeWalker, 0, tokenTypes, tokenStyles);
    treeWalker.walk(javaScopeWalker, startRule);
    return scopeWalker.currentNode;
  }
}
