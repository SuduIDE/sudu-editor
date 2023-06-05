package org.sudu.experiments.parser;

import org.antlr.v4.runtime.*;

public class ErrorHighlightingStrategy extends DefaultErrorStrategy {

  @Override
  public void reportError(Parser recognizer, RecognitionException e) {
    super.reportError(recognizer, e);
    makeErrorToken(recognizer);
  }

  private void makeErrorToken(Parser recognizer) {
    var errorNode = recognizer.createErrorNode(recognizer.getContext(), recognizer.getCurrentToken());
    recognizer.getContext().addErrorNode(errorNode);
  }
  
}
