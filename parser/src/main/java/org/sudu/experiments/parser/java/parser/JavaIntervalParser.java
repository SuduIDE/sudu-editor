package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.ErrorToken;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.java.ParserConstants;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.walker.ClassWalker;
import org.sudu.experiments.parser.java.walker.JavaWalker;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JavaIntervalParser extends BaseJavaParser {

  // interval = {intervalStart, intervalStop, intervalType}
  public int[] parseInterval(String source, int[] interval) {
    int intervalStart = interval[0];
    int intervalStop = interval[1];
    int intervalType = interval[2];

    initLexer(source.substring(intervalStart, intervalStop));

    if (tokenErrorOccurred()) return makeErrorInts();

    Interval parsingInterval = new Interval(0, intervalStop - intervalStart, intervalType);
    List<Interval> intervalList = parseInterval(parsingInterval);

    expendIntervals(intervalList, parsingInterval);
    return getIntervalInts(intervalStart, intervalStop, intervalList);
  }

  int[] getIntervalInts(int intervalStart, int intervalStop, List<Interval> intervalList) {
    int N = allTokens.get(allTokens.size() - 1).getLine();
    int M = 0;
    int K = intervalList.size();
    Map<Integer, List<Token>> tokensByLine = groupTokensByLine(allTokens);
    for (var entry : tokensByLine.entrySet()) {
      var filtered = entry.getValue().stream()
          .filter(this::tokenFilter)
          .collect(Collectors.toList());
      entry.setValue(filtered);
      M += filtered.size();
      N = Math.max(N, entry.getKey());
    }

    int[] result = new int[5 + N + 3 * K + 4 * M];
    result[0] = intervalStart;
    result[1] = intervalStop;
    result[2] = N;
    result[3] = M;
    result[4] = K;

    int ind = 0;
    for (int i = 0; i < N; i++) {
      var tokensOnLine = tokensByLine.getOrDefault(i + 1, Collections.emptyList());
      result[5 + i] = tokensOnLine.size();
      for (var token : tokensOnLine) {
        result[5 + N + 4 * ind] = token.getStartIndex();
        result[5 + N + 4 * ind + 1] = token.getStopIndex() + 1;
        result[5 + N + 4 * ind + 2] = tokenTypes[token.getTokenIndex()];
        result[5 + N + 4 * ind + 3] = tokenStyles[token.getTokenIndex()];
        ind++;
      }
    }

    for (int i = 0; i < K; i++) {
      result[5 + N + 4 * M + 3 * i] = intervalList.get(i).start + intervalStart;
      result[5 + N + 4 * M + 3 * i + 1] = intervalList.get(i).stop + intervalStart;
      result[5 + N + 4 * M + 3 * i + 2] = intervalList.get(i).intervalType;
    }

    return result;
  }

  int[] makeErrorInts() {
    return new int[]{-1};
  }

  private List<Interval> parseInterval(Interval interval) {
    JavaParser parser = new JavaParser(tokenStream);
    ParserRuleContext ruleContext;

    ruleContext = switch (interval.intervalType) {
      case ParserConstants.IntervalTypes.COMP_UNIT -> parser.compilationUnit();
      default -> parser.unknownInterval();
    };
    ParseTreeWalker walker = new ParseTreeWalker();

    var classWalker = new ClassWalker();
    walker.walk(classWalker, ruleContext);
    var javaWalker = new JavaWalker(tokenTypes, tokenStyles, classWalker.dummy);
    walker.walk(javaWalker, ruleContext);
    highlightTokens();

    if (interval.intervalType == ParserConstants.IntervalTypes.COMP_UNIT) {
      var compUnitInterval = new Interval(0, fileSource.length(), ParserConstants.IntervalTypes.COMP_UNIT);
      classWalker.intervals.add(0, compUnitInterval);
    }
    return classWalker.intervals;
  }

  private void expendIntervals(List<Interval> intervalList, Interval interval) {
    if (intervalList.isEmpty()) {
      intervalList.add(interval);
      return;
    }
    Interval left = intervalList.get(0);
    Interval right = intervalList.get(0);
    for (var cur: intervalList) {
      if (cur.start < left.start || (cur.start == left.start && cur.stop < left.stop)) left = cur;
      if (cur.stop > right.stop || (cur.stop == right.stop && cur.start > right.stop)) right = cur;
    }
    left.start = interval.start;
    right.stop = interval.stop;
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
