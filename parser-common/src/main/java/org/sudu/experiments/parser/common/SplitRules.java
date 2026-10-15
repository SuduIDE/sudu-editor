package org.sudu.experiments.parser.common;

import org.antlr.v4.runtime.Token;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class SplitRules {

  public static final SplitRules EMPTY = new SplitRules() {
    @Override
    public List<TokenSplitRule> getRules() {
      return Collections.emptyList();
    }
  };

  public abstract List<TokenSplitRule> getRules();

  public static TokenSplitRule makeRule(
      Predicate<Token> predicate,
      Function<Token, List<Token>> function
  ) {
    return new TokenSplitRule() {

      @Override
      public boolean test(Token token) {
        return predicate.test(token);
      }

      @Override
      public List<Token> split(Token token) {
        return function.apply(token);
      }
    };
  }

  public abstract static class TokenSplitRule {

    public abstract boolean test(Token token);

    public abstract List<Token> split(Token token);

  }

}
