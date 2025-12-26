package org.sudu.experiments.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.Pair;

public class LangTokenFactory extends CommonTokenFactory {

  private final String language;

  public LangTokenFactory(String language) {
    this.language = language;
  }

  @Override
  public LangToken create(int type, String text) {
    return new LangToken(type, text, language);
  }

  @Override
  public LangToken create(Pair<TokenSource, CharStream> source, int type, String text, int channel, int start, int stop, int line, int charPositionInLine) {
    LangToken t = new LangToken(source, type, channel, start, stop, language);
    t.setLine(line);
    t.setCharPositionInLine(charPositionInLine);
    if (text != null) {
      t.setText(text);
    } else if (copyText && source.b != null) {
      t.setText(source.b.getText(Interval.of(start, stop)));
    }
    return t;
  }
}
