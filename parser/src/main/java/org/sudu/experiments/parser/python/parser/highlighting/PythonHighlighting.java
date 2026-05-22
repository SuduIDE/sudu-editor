package org.sudu.experiments.parser.python.parser.highlighting;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.python.gen.PythonLexer;

import java.util.List;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class PythonHighlighting {

  public static void highlightTokens(List<Token> allTokens, int[] tokenTypes) {
    for (var token: allTokens) {
      int ind = token.getTokenIndex();
      int type = token.getType();
      if (isKeyword(type)) tokenTypes[ind] = KEYWORD;
      else if (isBoolean(type)) tokenTypes[ind] = BOOLEAN;
      else if (isOperator(type)) tokenTypes[ind] = OPERATOR;
      else if (isString(type)) tokenTypes[ind] = STRING;
      else if (isNumber(type)) tokenTypes[ind] = NUMERIC;
      else if (isComment(type)) tokenTypes[ind] = COMMENT;
    }
  }

  public static boolean isKeyword(int tokenType) {
    return tokenType >= PythonLexer.DEF
        && tokenType <= PythonLexer.EXEC;
  }

  public static boolean isBoolean(int tokenType) {
    return tokenType >= PythonLexer.TRUE
        && tokenType <= PythonLexer.FALSE;
  }

  public static boolean isOperator(int tokenType) {
    return tokenType >= PythonLexer.DOT
        && tokenType <= PythonLexer.IDIV_ASSIGN;
  }

  public static boolean isString(int tokenType) {
    return tokenType >= PythonLexer.STRING
        && tokenType <= PythonLexer.MULTILINE_STRING;
  }

  public static boolean isNumber(int tokenType) {
    return tokenType >= PythonLexer.DECIMAL_INTEGER
        && tokenType <= PythonLexer.FLOAT_NUMBER;
  }

  public static boolean isComment(int tokenType) {
    return tokenType == PythonLexer.COMMENT;
  }
}
