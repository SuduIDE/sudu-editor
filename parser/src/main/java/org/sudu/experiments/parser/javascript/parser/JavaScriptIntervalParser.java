package org.sudu.experiments.parser.javascript.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.common.BaseIntervalParser;
import org.sudu.experiments.parser.javascript.gen.JavaScriptLexer;
import org.sudu.experiments.parser.javascript.gen.JavaScriptParser;
import org.sudu.experiments.parser.javascript.parser.highlighting.JavaScriptLexerHighlighting;
import org.sudu.experiments.parser.javascript.walker.JavaScriptWalker;

import java.util.List;

public class JavaScriptIntervalParser extends BaseIntervalParser {

  @Override
  protected List<Interval> parseInterval(Interval interval) {
    JavaScriptParser parser = new JavaScriptParser(tokenStream);
    ParserRuleContext ruleContext;

    ruleContext = switch (interval.intervalType) {
      case ParserConstants.IntervalTypes.Js.PROGRAM -> parser.programOrAny();
      default -> parser.unknownInterval();
    };
    ParseTreeWalker walker = new ParseTreeWalker();

    var classWalker = new JavaScriptWalker(tokenTypes, tokenStyles);
    walker.walk(classWalker, ruleContext);
    highlightTokens();

    if (interval.intervalType == ParserConstants.IntervalTypes.Java.COMP_UNIT) {
      var compUnitInterval = new Interval(0, fileSource.length(), ParserConstants.IntervalTypes.Java.COMP_UNIT);
      classWalker.intervals.add(0, compUnitInterval);
    }
    return classWalker.intervals;
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
