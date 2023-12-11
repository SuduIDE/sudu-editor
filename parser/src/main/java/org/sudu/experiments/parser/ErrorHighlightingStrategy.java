package org.sudu.experiments.parser;

import org.antlr.v4.runtime.*;

public class ErrorHighlightingStrategy extends DefaultErrorStrategy {

  private final int[] tokenTypes;

  public ErrorHighlightingStrategy(int[] tokenTypes) {
    this.tokenTypes = tokenTypes;
    errorRecoveryMode = true;
  }

  @Override
  protected void reportUnwantedToken(Parser recognizer) {
    super.reportUnwantedToken(recognizer);
    markErrorToken(recognizer);
  }

  @Override
  protected void reportFailedPredicate(Parser recognizer, FailedPredicateException e) {
    super.reportFailedPredicate(recognizer, e);
    markErrorToken(recognizer);
  }

  @Override
  protected void reportInputMismatch(Parser recognizer, InputMismatchException e) {
    super.reportInputMismatch(recognizer, e);
    markErrorToken(recognizer);
  }

  @Override
  protected void reportMissingToken(Parser recognizer) {
    super.reportMissingToken(recognizer);
    markErrorToken(recognizer);
  }

  @Override
  protected void reportNoViableAlternative(Parser recognizer, NoViableAltException e) {
    super.reportNoViableAlternative(recognizer, e);
    markErrorToken(recognizer);
  }

  @Override
  public void reportError(Parser recognizer, RecognitionException e) {
    super.reportError(recognizer, e);
    markErrorToken(recognizer);
  }

  private void markErrorToken(Parser recognizer) {
    int ind = recognizer.getCurrentToken().getTokenIndex();
    tokenTypes[ind] = ParserConstants.TokenTypes.ERROR;
  }
  
}
