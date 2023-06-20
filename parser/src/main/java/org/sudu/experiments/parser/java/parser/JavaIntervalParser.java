package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.ErrorToken;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.common.BaseIntervalParser;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.walker.JavaClassWalker;
import org.sudu.experiments.parser.java.walker.JavaWalker;

import static org.sudu.experiments.parser.ParserConstants.*;

import java.util.HashMap;
import java.util.List;

public class JavaIntervalParser extends BaseIntervalParser {

  @Override
  protected List<Interval> parseInterval(Interval interval) {
    JavaParser parser = new JavaParser(tokenStream);
    ParserRuleContext ruleContext;

    ruleContext = switch (interval.intervalType) {
      case ParserConstants.IntervalTypes.Java.COMP_UNIT -> parser.compilationUnitOrAny();
      default -> parser.unknownInterval();
    };
    ParseTreeWalker walker = new ParseTreeWalker();

    var classWalker = new JavaClassWalker();
    walker.walk(classWalker, ruleContext);
    var javaWalker = new JavaWalker(tokenTypes, tokenStyles, classWalker.dummy, new HashMap<>());
    walker.walk(javaWalker, ruleContext);
    highlightTokens();

    if (interval.intervalType == IntervalTypes.Java.COMP_UNIT) {
      var compUnitInterval = new Interval(0, fileSourceLength, IntervalTypes.Java.COMP_UNIT);
      classWalker.intervals.add(0, compUnitInterval);
    }
    return classWalker.intervals;
  }

  @Override
  protected boolean isMultilineToken(int tokenType) {
    return tokenType == JavaLexer.COMMENT
        || tokenType == JavaLexer.TEXT_BLOCK
        || tokenType == ErrorToken.ERROR_TYPE;
  }

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new JavaLexer(stream);
  }

  @Override
  protected boolean tokenFilter(Token token) {
    int type = token.getType();
    return type != JavaLexer.NEW_LINE
        && type != JavaLexer.EOF;
  }

  @Override
  protected boolean isComment(int type) {
    return type == JavaLexer.COMMENT
        || type == JavaLexer.LINE_COMMENT;
  }

}
