package org.sudu.experiments.parser.javascript.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.common.BaseFullParser;
import org.sudu.experiments.parser.javascript.gen.light.LightJavaScriptLexer;
import org.sudu.experiments.parser.javascript.parser.highlighting.LightJavaScriptLexerHighlighting;

import java.util.Arrays;
import java.util.List;

public class JavaScriptFirstLinesLexer extends BaseFullParser {

  public int[] parse(String source, int numOfStrings) {
    long parsingStartTime = System.currentTimeMillis();
    initLexer(prepareString(source, numOfStrings));

    highlightTokens();

    var result = getInts(List.of());
    System.out.println("Lexing viewport js time " + (System.currentTimeMillis() - parsingStartTime) + "ms");
    return result;
  }

  private String prepareString(String source, int numOfStrings) {
    String[] lines = source.split("\n", -1);
    if (lines.length < numOfStrings) return source;
    else return String.join("\n", Arrays.copyOf(lines, numOfStrings));
  }

  @Override
  protected void highlightTokens() {
    LightJavaScriptLexerHighlighting.highlightTokens(allTokens, tokenTypes);
  }

  @Override
  protected boolean isMultilineToken(int tokenType) {
    return tokenType == LightJavaScriptLexer.MultiLineComment
        || tokenType == LightJavaScriptLexer.HtmlComment
        || tokenType == LightJavaScriptLexer.CDataComment
        || tokenType == LightJavaScriptLexer.StringLiteral;
  }

  @Override
  protected boolean isComment(int tokenType) {
    return LightJavaScriptLexerHighlighting.isComment(tokenType);
  }

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new LightJavaScriptLexer(stream);
  }

  @Override
  protected boolean tokenFilter(Token token) {
    int type = token.getType();
    return type != LightJavaScriptLexer.LineTerminator
        && type != LightJavaScriptLexer.EOF;
  }
}
