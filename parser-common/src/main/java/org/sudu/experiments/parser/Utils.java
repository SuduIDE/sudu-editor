package org.sudu.experiments.parser;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

public class Utils {

  public static void markError(int[] tokenTypes, int[] tokenStyles, int ind) {
    if (ind < 0 || ind > tokenTypes.length - 1) return;
    tokenTypes[ind] = ParserConstants.TokenTypes.ERROR;
    tokenStyles[ind] = ParserConstants.TokenStyles.error(tokenStyles[ind]);
  }

  public static void printError(TerminalNode errorNode, String msg) {
    printError(errorNode.getSymbol(), msg);
  }

  public static void printError(Token errorToken, String msg) {
    int line = errorToken.getLine(), pos = errorToken.getCharPositionInLine();
    printError(line, pos, msg);
  }

  public static void printError(int line, int charPos, String msg) {
    System.err.println(line + ":" + charPos + " " + msg);
  }

}
