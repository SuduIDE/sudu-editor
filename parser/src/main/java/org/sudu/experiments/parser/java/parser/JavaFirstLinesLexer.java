package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.common.BaseFullParser;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.java.JavaSplitRules;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.parser.highlighting.JavaLexerHighlighting;

import java.util.Arrays;

public class JavaFirstLinesLexer extends BaseFullParser {

  public int[] parse(String source, int numOfStrings) {
    long parsingStartTime = System.currentTimeMillis();
    initLexer(prepareString(source, numOfStrings));

    highlightTokens();

    var result = getInts(defaultIntervalNode());
    System.out.println("Lexing viewport java time: " + (System.currentTimeMillis() - parsingStartTime) + "ms");
    return result;
  }

  private String prepareString(String source, int numOfStrings) {
    String[] lines = source.split("\n", -1);
    if (lines.length < numOfStrings) return source;
    else return String.join("\n", Arrays.copyOf(lines, numOfStrings));
  }

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
