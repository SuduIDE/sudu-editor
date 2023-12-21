package org.sudu.experiments.parser.cpp.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.IterativeParseTreeWalker;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.base.BaseFullScopeParser;
import org.sudu.experiments.parser.common.graph.ScopeWalker;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.cpp.CppSplitRules;
import org.sudu.experiments.parser.cpp.gen.CPP14Lexer;
import org.sudu.experiments.parser.cpp.gen.CPP14Parser;
import org.sudu.experiments.parser.cpp.parser.highlighting.CppLexerHighlighting;
import org.sudu.experiments.parser.cpp.walker.CppScopeWalker;

public class CppFullScopesParser extends BaseFullScopeParser<CPP14Parser> {

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new CPP14Lexer(stream);
  }

  @Override
  protected CPP14Parser initParser() {
    return new CPP14Parser(tokenStream);
  }

  @Override
  protected ParserRuleContext getStartRule(CPP14Parser parser) {
    return parser.translationUnit();
  }

  @Override
  protected IntervalNode walk(ParserRuleContext startRule) {
    ParseTreeWalker treeWalker = new IterativeParseTreeWalker();
    var defaultInterval = defaultIntervalNode(ParserConstants.IntervalTypes.Cpp.TRANS_UNIT);
    scopeWalker = new ScopeWalker(defaultInterval);
    var cppScopeWalker = new CppScopeWalker(scopeWalker, 0, tokenTypes, tokenStyles);
    treeWalker.walk(cppScopeWalker, startRule);
    return scopeWalker.currentNode;
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
    CppLexerHighlighting.highlightTokens(allTokens, tokenTypes);
  }
}
