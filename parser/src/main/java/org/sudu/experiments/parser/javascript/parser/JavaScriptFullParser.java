package org.sudu.experiments.parser.javascript.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.common.BaseFullParser;
import org.sudu.experiments.parser.javascript.gen.JavaScriptLexer;
import org.sudu.experiments.parser.javascript.gen.JavaScriptParser;
import org.sudu.experiments.parser.javascript.parser.highlighting.JavaScriptLexerHighlighting;
import org.sudu.experiments.parser.javascript.walker.JavaScriptWalker;

import java.util.List;

public class JavaScriptFullParser extends BaseFullParser {

  public int[] parse(String source) {
    long parsingTime = System.currentTimeMillis();

    initLexer(source);

    JavaScriptParser parser = new JavaScriptParser(tokenStream);

    var program = parser.program();
    ParseTreeWalker walker = new ParseTreeWalker();

    highlightTokens();

    JavaScriptWalker jsWalker = new JavaScriptWalker(tokenTypes, tokenStyles);
    walker.walk(jsWalker, program);

    jsWalker.intervals.add(new Interval(0, source.length(), ParserConstants.IntervalTypes.Js.PROGRAM));

    var result = getInts(jsWalker.intervals);
    System.out.println("Parsing full js time: " + (System.currentTimeMillis() - parsingTime) + "ms");
    return result;
  }

  @Override
  protected boolean isMultilineToken(int tokenType) {
    return tokenType == JavaScriptLexer.MultiLineComment
        || tokenType == JavaScriptLexer.HtmlComment
        || tokenType == JavaScriptLexer.CDataComment
        || tokenType == JavaScriptLexer.StringLiteral;
  }

  @Override
  protected boolean isComment(int tokenType) {
    return JavaScriptLexerHighlighting.isComment(tokenType);
  }

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new JavaScriptLexer(stream);
  }

  @Override
  protected boolean tokenFilter(Token token) {
    int type = token.getType();
    return type != JavaScriptLexer.LineTerminator
        && type != JavaScriptLexer.EOF;
  }
}
