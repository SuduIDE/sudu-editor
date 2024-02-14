package org.sudu.experiments.parser;

import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;

public class ErrorMarkListener extends ErrorRecognizerListener {

  public final int[] tokenTypes, tokenStyles;

  public ErrorMarkListener(int[] tokenTypes, int[] tokenStyles) {
    this.tokenTypes = tokenTypes;
    this.tokenStyles = tokenStyles;
  }

  @Override
  public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
    super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e);
    if (offendingSymbol instanceof Token token)
      Utils.markError(tokenTypes, tokenStyles, token.getTokenIndex());
  }

}
