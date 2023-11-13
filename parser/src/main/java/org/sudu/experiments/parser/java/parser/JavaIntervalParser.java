package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.common.base.BaseIntervalParser;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.graph.ScopeWalker;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.java.JavaSplitRules;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.parser.highlighting.JavaLexerHighlighting;
import org.sudu.experiments.parser.java.walker.JavaScopeWalker;
import static org.sudu.experiments.parser.ParserConstants.*;

public class JavaIntervalParser extends BaseIntervalParser<JavaParser> {

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new JavaLexer(stream);
  }

  @Override
  protected JavaParser initParser() {
    return new JavaParser(tokenStream);
  }

  @Override
  protected ParserRuleContext getStartRule(JavaParser parser) {
    return switch (intervalType) {
      case IntervalTypes.Java.COMP_UNIT -> parser.compilationUnitOrAny();
      default -> parser.unknownInterval();
    };
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
  }

  @Override
  protected IntervalNode walk(ParserRuleContext startRule) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected void walkScopes(ParserRuleContext startRule, ScopeWalker scopeWalker) {
    ParseTreeWalker walker = new ParseTreeWalker();
    JavaScopeWalker javaScopeWalker = new JavaScopeWalker(scopeWalker, intervalStart, tokenTypes, tokenStyles);
    javaScopeWalker.offset = intervalStart;
    walker.walk(javaScopeWalker, startRule);
  }

}
