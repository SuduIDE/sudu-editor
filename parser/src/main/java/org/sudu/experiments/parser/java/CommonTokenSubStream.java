package org.sudu.experiments.parser.java;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;

// The same as CommonTokenStream but didn't change token indices
public class CommonTokenSubStream extends CommonTokenStream {

  public CommonTokenSubStream(TokenSource tokenSource) {
    super(tokenSource);
  }

  @Override
  protected int fetch(int n) {
    if (fetchedEOF) {
      return 0;
    }

    for (int i = 0; i < n; i++) {
      Token t = tokenSource.nextToken();
      tokens.add(t);
      if ( t.getType()==Token.EOF ) {
        fetchedEOF = true;
        return i + 1;
      }
    }

    return n;
  }

}
