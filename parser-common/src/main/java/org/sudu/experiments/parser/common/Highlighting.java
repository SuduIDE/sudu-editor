package org.sudu.experiments.parser.common;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.parser.Utils;

import java.util.List;

public abstract class Highlighting {

  private int[] rules; // {from, to, color}[]
  private int error;
  private int length;

  protected Highlighting() {
    rules = new int[0];
    length = 0;
    error = -1;
  }

  protected void addRules(int... intervals) {
    if (intervals.length % 3 != 0) throw new IllegalArgumentException();
    rules = ArrayOp.add(rules, intervals);
    length += intervals.length / 3;
  }

  protected void addErrorRule(int error) {
    this.error = error;
  }

  public void highlight(
      List<Token> allTokens,
      int[] tokenTypes,
      int[] tokenStyles
  ) {
    for (var token: allTokens) highlight(token, tokenTypes, tokenStyles);
  }

  public void highlight(
      Token token,
      int[] tokenTypes,
      int[] tokenStyles
  ) {
    int ind = token.getTokenIndex();
    int type = token.getType();
    if (type == error || type == -1) {
      Utils.markError(tokenTypes, tokenStyles, ind);
      return;
    }
    for (int i = 0; i < length; i++) {
      int from = rules[3 * i];
      int to = rules[3  * i + 1];
      int color = rules[3 * i + 2];
      if (from <= type && type <= to) {
        tokenTypes[ind] = color;
        return;
      }
    }
  }
}
