package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.common.base.BaseFirstLinesLexer;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.java.JavaSplitRules;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.parser.highlighting.JavaLexerHighlighting;

public class JavaFirstLinesLexer extends BaseFirstLinesLexer<JavaParser> {

  @Override
  protected void highlightTokens() {
    JavaLexerHighlighting.highlightTokens(allTokens, tokenTypes);
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
}
