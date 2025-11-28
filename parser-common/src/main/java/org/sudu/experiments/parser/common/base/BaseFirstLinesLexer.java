package org.sudu.experiments.parser.common.base;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.common.tree.IntervalNode;

import java.util.Arrays;
import java.util.List;

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
    markBrackets(allTokens, tokenTypes);

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

  private static void markBrackets(List<Token> allTokens, int[] tokenTypes) {
    int[] brackets = new int[]{
        ParserConstants.TokenTypes.BR_1,
        ParserConstants.TokenTypes.BR_2,
        ParserConstants.TokenTypes.BR_3
    };
    int currentDepth = 0;
    for (var token : allTokens) {
      int ind = token.getTokenIndex();
      String text = token.getText();
      if (text.length() != 1) continue;
      boolean isOp = isOpeningBracket(text);
      boolean isCl = isClosingBracket(text);
      if (!isOp && !isCl) continue;
      if (isOp) {
        tokenTypes[ind] = brackets[currentDepth % 3];
        currentDepth++;
      }
      if (isCl) {
        currentDepth--;
        if (currentDepth < 0) currentDepth += 3;
        tokenTypes[ind] = brackets[currentDepth % 3];
      }
    }
  }

  private static boolean isOpeningBracket(String br) {
    return switch (br) {
      case "(", "{", "[" -> true;
      default -> false;
    };
  }

  private static boolean isClosingBracket(String br) {
    return switch (br) {
      case ")", "}", "]" -> true;
      default -> false;
    };
  }
}
