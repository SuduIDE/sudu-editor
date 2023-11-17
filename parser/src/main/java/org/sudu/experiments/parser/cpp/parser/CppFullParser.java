package org.sudu.experiments.parser.cpp.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.IterativeParseTreeWalker;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.cpp.CppSplitRules;
import org.sudu.experiments.parser.cpp.gen.CPP14Lexer;
import org.sudu.experiments.parser.cpp.gen.CPP14Parser;
import org.sudu.experiments.parser.cpp.parser.highlighting.CppLexerHighlighting;
import org.sudu.experiments.parser.cpp.walker.CppWalker;
import org.sudu.experiments.parser.cpp.walker.CppClassWalker;

import static org.sudu.experiments.parser.ParserConstants.*;
import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class CppFullParser extends BaseFullParser<CPP14Parser> {

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new CPP14Lexer(stream);
  }

  @Override
  protected CPP14Parser initParser() {
    return new CPP14Parser(tokenStream);
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
  protected ParserRuleContext getStartRule(CPP14Parser parser) {
    return parser.translationUnit();
  }

  @Override
  protected IntervalNode walk(ParserRuleContext startRule) {
    ParseTreeWalker walker = new IterativeParseTreeWalker();
    Interval compUnitInterval = new Interval(0, fileSourceLength, IntervalTypes.Cpp.TRANS_UNIT);
    var classWalker = new CppClassWalker(new IntervalNode(compUnitInterval));
    walker.walk(classWalker, startRule);

    CppWalker cppWalker = new CppWalker(tokenTypes, tokenStyles, classWalker.current, usageToDefinition);
    walker.walk(cppWalker, startRule);

    return classWalker.node;
  }

  @Override
  protected void highlightTokens() {
    CppLexerHighlighting.highlightTokens(allTokens, tokenTypes);
  }

  public static boolean isDirective(int tokenType) {
    return tokenType == CPP14Lexer.Directive
        || tokenType == CPP14Lexer.MultiLineMacro;
  }

  public static boolean isComment(int tokenType) {
    return tokenType == CPP14Lexer.BlockComment
        || tokenType == CPP14Lexer.LineComment;
  }

}
