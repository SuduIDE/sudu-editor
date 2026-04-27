package org.sudu.experiments.parser.javascript.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.help.Helper;
import org.sudu.experiments.parser.javascript.gen.JavaScriptLexer;
import org.sudu.experiments.parser.javascript.gen.JavaScriptParser;
import org.sudu.experiments.parser.javascript.parser.highlighting.JavaScriptLexerHighlighting;
import org.sudu.experiments.parser.javascript.walker.JsWalker;

// todo fix
public class JavaScriptFullParser extends BaseFullParser<JavaScriptParser> {

  public int[] parse(String source) {
    long parsingTime = System.currentTimeMillis();

    initLexer(source);

    JavaScriptParser parser = new JavaScriptParser(tokenStream);

    var program = parser.program();
    ParseTreeWalker walker = new ParseTreeWalker();

    highlightTokens();

    JsWalker jsWalker = new JsWalker(tokenTypes, tokenStyles);
    walker.walk(jsWalker, program);

    jsWalker.intervals.add(new Interval(0, source.length(), ParserConstants.IntervalTypes.Js.PROGRAM));

    //todo
    var result = getInts(null);
    System.out.println("Parsing full js time: " + (System.currentTimeMillis() - parsingTime) + "ms");
    return result;
  }

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new JavaScriptLexer(stream);
  }

  @Override
  protected JavaScriptParser initParser() {
    return null;
  }

  @Override
  protected SplitRules initSplitRules() {
    return null;
  }

  @Override
  protected String language() {
    return Helper.JS_LIGHT;
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
    for (var token: allTokens) {
      int ind = token.getTokenIndex();
      if (isComment(token.getType())) tokenTypes[ind] = ParserConstants.TokenTypes.COMMENT;
    }
  }

  public static boolean isComment(int tokenType) {
    return JavaScriptLexerHighlighting.isComment(tokenType);
  }

}
