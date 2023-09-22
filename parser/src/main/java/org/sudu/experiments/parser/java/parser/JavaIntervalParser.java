package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.common.BaseIntervalParser;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.common.IntervalNode;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.java.JavaSplitRules;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.parser.highlighting.JavaLexerHighlighting;
import org.sudu.experiments.parser.java.walker.JavaClassWalker;
import org.sudu.experiments.parser.java.walker.JavaWalker;

import static org.sudu.experiments.parser.ParserConstants.*;

import java.util.HashMap;

public class JavaIntervalParser extends BaseIntervalParser {

  @Override
  protected IntervalNode parseInterval(Interval interval) {
    JavaParser parser = new JavaParser(tokenStream);
    ParserRuleContext ruleContext;
    Interval initInterval;

    if (interval.intervalType == IntervalTypes.Java.COMP_UNIT) {
      ruleContext = parser.compilationUnitOrAny();
      initInterval = new Interval(0, fileSourceLength, IntervalTypes.Java.COMP_UNIT);
    } else {
      ruleContext = parser.unknownInterval();
      initInterval = defaultInterval();
    }

    ParseTreeWalker walker = new ParseTreeWalker();

    var classWalker = new JavaClassWalker(new IntervalNode(initInterval));
    classWalker.intervalStart = intervalStart;
    walker.walk(classWalker, ruleContext);
    var javaWalker = new JavaWalker(tokenTypes, tokenStyles, classWalker.dummy, classWalker.types, new HashMap<>());
    walker.walk(javaWalker, ruleContext);
    highlightTokens();

    return classWalker.node;
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
  protected SplitRules initSplitRules() {
    return new JavaSplitRules();
  }

  @Override
  protected void highlightTokens() {
    for (var token: allTokens) {
      int ind = token.getTokenIndex();
      if (JavaLexerHighlighting.isComment(token.getType())) tokenTypes[ind] = TokenTypes.COMMENT;
      if (JavaLexerHighlighting.isJavadoc(token.getType())) tokenTypes[ind] = TokenTypes.JAVADOC;
      if (isErrorToken(token.getType())) tokenTypes[ind] = ParserConstants.TokenTypes.ERROR;
    }
  }

}
