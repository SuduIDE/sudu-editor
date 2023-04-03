package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.java.CommonTokenSubStream;
import org.sudu.experiments.parser.java.ParserConstants;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.walker.ClassWalker;
import org.sudu.experiments.parser.java.walker.JavaWalker;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JavaViewportIntervalsParser extends BaseJavaViewportParser {

  // viewport - {leftInd, rightInd, firstLine}
  public int[] parseViewport(String source, int[] viewport, int[] intervals) {
    int vpStart = viewport[0];
    int vpEnd = viewport[1];
    int firstLine = viewport[2];
    initLexer(source.substring(vpStart, vpEnd));

    List<Interval> intervalList = makeIntervalList(intervals, vpStart, vpEnd);

    highlightTokens();
    parseIntervals(intervalList);
    return getVpInts(firstLine, vpStart);
  }

  private List<Interval> makeIntervalList(int[] intervals, int vpStart, int vpEnd) {
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
      if (interval.intervalType != ParserConstants.IntervalTypes.COMMENT) parseInterval(interval);
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
    return interval.intervalType == ParserConstants.IntervalTypes.COMMENT
        && (interval.intersect(vpStart, vpEnd)
        || interval.contains(vpStart, vpEnd)
    );
  }

  void parseInterval(Interval interval) {
    var tokenSrc = getSubSource(interval);
    CommonTokenStream tokenStream = new CommonTokenSubStream(tokenSrc);
    tokenStream.fill();

    JavaParser parser = new JavaParser(tokenStream);
    ParserRuleContext ruleContext;

    ruleContext = switch (interval.intervalType) {
      case ParserConstants.IntervalTypes.COMP_UNIT -> parser.compilationUnit();
      case ParserConstants.IntervalTypes.PACKAGE -> parser.packageDeclaration();
      case ParserConstants.IntervalTypes.IMPORT -> parser.importDeclaration();
      case ParserConstants.IntervalTypes.TYPE_DECL -> parser.typeDeclaration();
      case ParserConstants.IntervalTypes.CLASS_BODY -> parser.classOrInterfaceBodyDeclaration();
      default -> throw new IllegalStateException("Unexpected value: " + interval.intervalType);
    };
    ParseTreeWalker walker = new ParseTreeWalker();

    var classWalker = new ClassWalker();
    walker.walk(classWalker, ruleContext);
    var javaWalker = new JavaWalker(tokenTypes, tokenStyles, classWalker.dummy);
    walker.walk(javaWalker, ruleContext);
  }

  @Override
  protected boolean isComment(int tokenType) {
    return false;
  }
}
