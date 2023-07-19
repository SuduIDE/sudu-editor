package org.sudu.experiments.parser.cpp.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.common.BaseFullParser;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.cpp.CppSplitRules;
import org.sudu.experiments.parser.cpp.gen.CPP14Lexer;
import org.sudu.experiments.parser.cpp.parser.highlighting.CppLexerHighlighting;

import java.util.Arrays;
import java.util.List;

public class CppFirstLinesLexer extends BaseFullParser {

  public int[] parse(String source, int numOfStrings) {
    long parsingStartTime = System.currentTimeMillis();
    initLexer(prepareString(source, numOfStrings));

    highlightTokens();

    var result = getInts(List.of());
    System.out.println("Lexing viewport cpp time " + (System.currentTimeMillis() - parsingStartTime) + "ms");
    return result;
  }

  private String prepareString(String source, int numOfStrings) {
    String[] lines = source.split("\n", -1);
    if (lines.length < numOfStrings) return source;
    else return String.join("\n", Arrays.copyOf(lines, numOfStrings));
  }

  @Override
  protected void highlightTokens() {
    CppLexerHighlighting.highlightTokens(allTokens, tokenTypes);
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

  @Override
  protected boolean isErrorToken(int tokenType) {
    return tokenType == CPP14Lexer.ERROR;
  }

}
