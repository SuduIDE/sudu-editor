package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.CommonTokenSubStream;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.sudu.experiments.parser.ParserConstants.IntervalTypes.Java.*;

public class JavaViewportIntervalsParser extends BaseIntervalParser {

  // viewport - {leftInd, rightInd, firstLine}
  public int[] parseViewport(String source, int[] viewport, int[] intervals) {
    int vpStart = viewport[0];
    int vpEnd = viewport[1];
    initLexer(source.substring(vpStart, vpEnd));

    List<Interval> intervalList = makeIntervalList(intervals, vpStart, vpEnd);

    highlightTokens();
    parseIntervals(intervalList);
    return getVpInts(vpStart, vpEnd, null);
  }

  List<Interval> makeIntervalList(int[] intervals, int vpStart, int vpEnd) {
    List<Interval> intervalList = new ArrayList<>();
    for (int i = 0; i < intervals.length; ) {
      int start = intervals[i++];
      int end = intervals[i++];
      int type = intervals[i++];
      intervalList.add(new Interval(start, end, type));
    }
    intervalList = intervalList.stream()
        .filter(interval -> filterIntervals(interval, vpStart, vpEnd))
        .peek(interval -> {
              interval.start -= vpStart;
              interval.stop -= vpStart;
            }
        ).collect(Collectors.toList());
    return intervalList;
  }

  @Override
  protected IntervalNode parseInterval(Interval interval) {
    var tokenSrc = getSubSource(interval);
    CommonTokenStream tokenStream = new CommonTokenSubStream(tokenSrc);
    tokenStream.fill();

    JavaParser parser = new JavaParser(tokenStream);
    ParserRuleContext ruleContext;

    ruleContext = switch (interval.intervalType) {
      case COMP_UNIT -> parser.compilationUnit();
      case PACKAGE -> parser.packageDeclaration();
      case IMPORT -> parser.importDeclaration();
      case TYPE_DECL -> parser.typeDeclaration();
      case CLASS_BODY -> parser.classOrInterfaceBodyDeclaration();
      default -> throw new IllegalStateException("Unexpected value: " + interval.intervalType);
    };
    ParseTreeWalker walker = new ParseTreeWalker();

    var classWalker = new JavaClassWalker(defaultIntervalNode());
    walker.walk(classWalker, ruleContext);
    var javaWalker = new JavaWalker(tokenTypes, tokenStyles, classWalker.dummy, classWalker.types, new HashMap<>());
    walker.walk(javaWalker, ruleContext);
    return null;
  }

  private TokenSource getSubSource(Interval interval) {
    List<Token> tokensInInterval = allTokens.stream()
        .filter(interval::contains)
        .collect(Collectors.toList());
    return new ListTokenSource(tokensInInterval);
  }

  private void parseIntervals(List<Interval> intervalList) {
    List<Interval> parsedIntervals = new ArrayList<>();
    for (var interval : intervalList) {
      if (isAlreadyParsed(parsedIntervals, interval)) {
        parsedIntervals.add(interval);
        continue;
      }
      if (interval.intervalType != COMMENT) parseInterval(interval);
      else highlightComment(interval);

      parsedIntervals.add(interval);
    }
  }

  private void highlightComment(Interval interval) {
    for (var token : allTokens) {
      if (interval.contains(token.getStartIndex(), token.getStopIndex()))
        tokenTypes[token.getTokenIndex()] = ParserConstants.TokenTypes.COMMENT;
    }
  }

  private static boolean isAlreadyParsed(List<Interval> intervals, Interval curInterval) {
    for (var interval : intervals) {
      if (curInterval.containsIn(interval.start, interval.stop)) return true;
    }
    return false;
  }

  private boolean filterIntervals(Interval interval, int vpStart, int vpEnd) {
    if (interval.containsIn(vpStart, vpEnd)) return true;
    return interval.intervalType == COMMENT
        && (interval.intersect(vpStart, vpEnd)
        || interval.contains(vpStart, vpEnd)
    );
  }

  @Override
  protected void highlightTokens() {
    JavaLexerHighlighting.highlightTokens(allTokens, tokenTypes);
  }

  @Override
  protected SplitRules initSplitRules() {
    return new JavaSplitRules();
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

  public static boolean isComment(int type) {
    return type == JavaLexer.COMMENT
        || type == JavaLexer.LINE_COMMENT;
  }

}
