package org.sudu.experiments.parser.javascript.parser.highlighting;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.ecmascript.gen.LightEcmaScriptLexer;

import java.util.List;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class LightJavaScriptLexerHighlighting {

  public static void highlightTokens(List<Token> allTokens, int[] tokenTypes) {
    for (var token : allTokens) {
      int ind = token.getTokenIndex();
      int type = token.getType();
      if (isComment(type)) tokenTypes[ind] = COMMENT;
      else if (isNull(type)) tokenTypes[ind] = NULL;
      else if (isBoolean(type)) tokenTypes[ind] = BOOLEAN;
      else if (isNumeric(type)) tokenTypes[ind] = NUMERIC;
      else if (isKeyword(type)) tokenTypes[ind] = KEYWORD;
      else if (isString(type)) tokenTypes[ind] = STRING;
      else if (isSemi(type)) tokenTypes[ind] = SEMI;
    }
  }

  public static boolean isNull(int tokenType) {
    return tokenType == LightEcmaScriptLexer.NULL_LITERAL;
  }

  public static boolean isBoolean(int tokenType) {
    return tokenType == LightEcmaScriptLexer.BOOLEAN_LITERAL;
  }

  public static boolean isNumeric(int tokenType) {
    return tokenType == LightEcmaScriptLexer.DECIMAL_LITERAL
        || tokenType == LightEcmaScriptLexer.HEX_INTEGER_LITERAL
        || tokenType == LightEcmaScriptLexer.OCTAL_INTEGER_LITERAL
        || tokenType == LightEcmaScriptLexer.OCTAL_INTEGER_LITERAL2
        || tokenType == LightEcmaScriptLexer.BINARY_INTEGER_LITERAL
        || tokenType == LightEcmaScriptLexer.BIG_HEX_INTEGER_LITERAL
        || tokenType == LightEcmaScriptLexer.BIG_OCTAL_INTEGER_LITERAL
        || tokenType == LightEcmaScriptLexer.BIG_BINARY_INTEGER_LITERAL
        || tokenType == LightEcmaScriptLexer.BIG_DECIMAL_INTEGER_LITERAL
        || tokenType == LightEcmaScriptLexer.COLOR;
  }

  public static boolean isKeyword(int tokenType) {
    return switch (tokenType) {
      case LightEcmaScriptLexer.BREAK,
           LightEcmaScriptLexer.DO,
           LightEcmaScriptLexer.INSTANCEOF,
           LightEcmaScriptLexer.TYPEOF,
           LightEcmaScriptLexer.CASE,
           LightEcmaScriptLexer.ELSE,
           LightEcmaScriptLexer.NEW,
           LightEcmaScriptLexer.VAR,
           LightEcmaScriptLexer.CATCH,
           LightEcmaScriptLexer.FINALLY,
           LightEcmaScriptLexer.RETURN,
           LightEcmaScriptLexer.VOID,
           LightEcmaScriptLexer.CONTINUE,
           LightEcmaScriptLexer.FOR,
           LightEcmaScriptLexer.SWITCH,
           LightEcmaScriptLexer.WHILE,
           LightEcmaScriptLexer.DEBUGGER,
           LightEcmaScriptLexer.FUNCTION_,
           LightEcmaScriptLexer.THIS,
           LightEcmaScriptLexer.WITH,
           LightEcmaScriptLexer.DEFAULT,
           LightEcmaScriptLexer.IF,
           LightEcmaScriptLexer.THROW,
           LightEcmaScriptLexer.DELETE,
           LightEcmaScriptLexer.IN,
           LightEcmaScriptLexer.TRY,
           LightEcmaScriptLexer.ASYNC,
           LightEcmaScriptLexer.AWAIT,
           LightEcmaScriptLexer.YIELD,
           LightEcmaScriptLexer.CLASS,
           LightEcmaScriptLexer.ENUM,
           LightEcmaScriptLexer.EXTENDS,
           LightEcmaScriptLexer.SUPER,
           LightEcmaScriptLexer.CONST,
           LightEcmaScriptLexer.EXPORT,
           LightEcmaScriptLexer.IMPORT,
           LightEcmaScriptLexer.IMPLEMENTS,
           LightEcmaScriptLexer.LET,
           LightEcmaScriptLexer.PRIVATE,
           LightEcmaScriptLexer.PUBLIC,
           LightEcmaScriptLexer.INTERFACE,
           LightEcmaScriptLexer.PACKAGE,
           LightEcmaScriptLexer.PROTECTED,
           LightEcmaScriptLexer.STATIC -> true;
      default -> false;
    };
  }

  public static boolean isString(int tokenType) {
    return tokenType == LightEcmaScriptLexer.STRING_LITERAL
        || tokenType == LightEcmaScriptLexer.BACK_TICK;
  }

  public static boolean isComment(int tokenType) {
    return tokenType == LightEcmaScriptLexer.HTML_COMMENT
        || tokenType == LightEcmaScriptLexer.C_DATA_COMMENT
        || tokenType == LightEcmaScriptLexer.MULTI_LINE_COMMENT
        || tokenType == LightEcmaScriptLexer.SINGLE_LINE_COMMENT;
  }

  public static boolean isSemi(int tokenType) {
    return tokenType == LightEcmaScriptLexer.COMMA
        || tokenType == LightEcmaScriptLexer.SEMI_COLON;
  }

}
