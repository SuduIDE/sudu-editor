package org.sudu.experiments.editor.worker.parser;

import org.sudu.experiments.editor.worker.ArgsCast;

public class ParseResult {

  public int[] ints;
  public char[] source;
  public int language, version;

  public int[] graphInts;
  public char[] graphChars;

  public ParseResult(Object[] args) {
    if (args.length < 3)
      throw new IllegalArgumentException("Illegal length of parser result array");
    ints = ArgsCast.intArray(args, 0);
    source = ArgsCast.charArray(args, 1);
    int[] langVersion = ArgsCast.intArray(args, 2);
    language = langVersion[0];
    version = langVersion[1];
    if (args.length >= 5) {
      graphInts = ArgsCast.intArray(args, 3);
      graphChars = ArgsCast.charArray(args, 4);
    }
  }

  public boolean haveGraph() {
    return graphInts != null && graphChars != null;
  }
}
