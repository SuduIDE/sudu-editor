package org.sudu.experiments.parser.javascript.parser;

import org.antlr.v4.runtime.*;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.javascript.JsSplitRules;
import org.sudu.experiments.parser.javascript.gen.JavaScriptParser;
import org.sudu.experiments.parser.javascript.gen.LightJavaScriptLexer;
import org.sudu.experiments.parser.javascript.parser.highlighting.JavaScriptLexerHighlighting;
import org.sudu.experiments.parser.javascript.parser.highlighting.LightJavaScriptLexerHighlighting;

public class JavaScriptLightParser extends BaseFullParser<JavaScriptParser> {

  @Override
  public int[] parse(char[] source) {
    long parsingTime = System.currentTimeMillis();
    initLexer(source);
    highlightTokens();
    var result = getInts(defaultIntervalNode());
    System.out.println("Light lexing js time: " + (System.currentTimeMillis() - parsingTime) + "ms");
    return result;
  }

//  public int[] parse(String source) {
//    long parsingTime = System.currentTimeMillis();
//
//    initLexer(source);
//
//    JavaScriptParser parser = new JavaScriptParser(tokenStream);
//
//    var program = parser.program();
//    ParseTreeWalker walker = new ParseTreeWalker();
//
//    highlightTokens();
//
//    JsWalker jsWalker = new JsWalker(tokenTypes, tokenStyles);
//    walker.walk(jsWalker, program);
//
//    jsWalker.intervals.add(new Interval(0, source.length(), ParserConstants.IntervalTypes.Js.PROGRAM));
//
//    //todo
//    var result = getInts(null);
//    System.out.println("Parsing full js time: " + (System.currentTimeMillis() - parsingTime) + "ms");
//    return result;
//  }

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new LightJavaScriptLexer(stream);
  }

  @Override
  protected JavaScriptParser initParser() {
    return null;
  }

  @Override
  protected SplitRules initSplitRules() {
    return new JsSplitRules();
  }

  @Override
  protected boolean tokenFilter(Token token) {
    int type = token.getType();
    return type != LightJavaScriptLexer.LineTerminator
        && type != LightJavaScriptLexer.EOF;
  }

  @Override
  protected ParserRuleContext getStartRule(JavaScriptParser parser) {
    return null;
  }

  @Override
  protected IntervalNode walk(ParserRuleContext startRule) {
    return null;
  }

  @Override
  protected void highlightTokens() {
    LightJavaScriptLexerHighlighting.highlightTokens(allTokens, tokenTypes);
  }

  public static boolean isComment(int tokenType) {
    return JavaScriptLexerHighlighting.isComment(tokenType);
  }

}
