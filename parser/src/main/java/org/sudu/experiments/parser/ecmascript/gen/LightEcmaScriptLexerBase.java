package org.sudu.experiments.parser.ecmascript.gen;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;

public abstract class LightEcmaScriptLexerBase extends Lexer {

  private boolean typeScript;

  protected LightEcmaScriptLexerBase(CharStream input) {
    super(input);
  }

  public void setTypeScript(boolean typeScript) {
    this.typeScript = typeScript;
  }

  public boolean isTypeScript() {
    return typeScript;
  }

  public boolean isJavaScript() {
    return !typeScript;
  }
}
