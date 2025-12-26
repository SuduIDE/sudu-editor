package org.sudu.experiments.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.misc.Pair;

public class LangToken extends CommonToken {

  private final String language;

  public LangToken(Token oldToken, String language) {
    super(oldToken);
    this.language = language;
  }

  public LangToken(int type, String language) {
    super(type);
    this.language = language;
  }

  public LangToken(int type, String text, String language) {
    super(type, text);
    this.language = language;
  }

  public LangToken(
      Pair<TokenSource, CharStream> source,
      int type, int channel,
      int start, int stop,
      String language
  ) {
    super(source, type, channel, start, stop);
    this.language = language;
  }

  public String getLanguage() {
    return language;
  }
}
