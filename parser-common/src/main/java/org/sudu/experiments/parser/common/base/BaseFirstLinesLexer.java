package org.sudu.experiments.parser.common.base;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.sudu.experiments.parser.common.tree.IntervalNode;

import java.util.Arrays;

public abstract class BaseFirstLinesLexer<P extends Parser> extends BaseFullParser<P> implements FirstLinesIntLexer {

  @Override
  public int[] parse(char[] source) {
    return parse(source, Integer.MAX_VALUE);
  }

  @Override
  public int[] parse(char[] chars, int numOfLines) {
    long parsingStartTime = System.currentTimeMillis();
    initLexer(prepareChars(chars, numOfLines));

    highlightTokens();

    var result = getInts(defaultIntervalNode());
    System.out.println("Lexing viewport time " + (System.currentTimeMillis() - parsingStartTime) + "ms");
    return result;
  }

  private char[] prepareChars(char[] source, int numOfLines) {
    int i = 0;
    int lineCounter = 0;
    for (; i < source.length; i++) {
      if (source[i] == '\n') lineCounter++;
      if (lineCounter >= numOfLines) break;
    }
    return Arrays.copyOf(source, i);
  }

  @Override
  protected P initParser() {
    return null;
  }

  @Override
  protected ParserRuleContext getStartRule(P parser) {
    return null;
  }

  @Override
  protected IntervalNode walk(ParserRuleContext startRule) {
    return null;
  }
}
