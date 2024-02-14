package org.sudu.experiments.parser;

public class Utils {

  public static void markError(int[] tokenTypes, int[] tokenStyles, int ind) {
    if (ind < 0 || ind > tokenTypes.length - 1) return;
    tokenTypes[ind] = ParserConstants.TokenTypes.ERROR;
    tokenStyles[ind] = ParserConstants.TokenStyles.error(tokenStyles[ind]);
  }

}
