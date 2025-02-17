package org.sudu.experiments.parser.common;

import org.antlr.v4.runtime.Parser;

public abstract class NullParser extends Parser {
  public NullParser() {
    super(null);
    throw new IllegalArgumentException();
  }
}
