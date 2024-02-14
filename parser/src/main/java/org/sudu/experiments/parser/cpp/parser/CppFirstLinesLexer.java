package org.sudu.experiments.parser.cpp.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.common.base.BaseFirstLinesLexer;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.cpp.CppSplitRules;
import org.sudu.experiments.parser.cpp.gen.CPP14Lexer;
import org.sudu.experiments.parser.cpp.gen.CPP14Parser;
import org.sudu.experiments.parser.cpp.parser.highlighting.CppLexerHighlighting;

public class CppFirstLinesLexer extends BaseFirstLinesLexer<CPP14Parser> {

  @Override
  protected void highlightTokens() {
    CppLexerHighlighting.highlightTokens(allTokens, tokenTypes, tokenStyles);
  }

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new CPP14Lexer(stream);
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
}
