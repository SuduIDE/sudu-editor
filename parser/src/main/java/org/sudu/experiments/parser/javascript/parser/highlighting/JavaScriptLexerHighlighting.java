package org.sudu.experiments.parser.javascript.parser.highlighting;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.javascript.gen.JavaScriptLexer;

import java.util.List;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class JavaScriptLexerHighlighting {

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
    return tokenType == JavaScriptLexer.NullLiteral;
  }

  public static boolean isBoolean(int tokenType) {
    return tokenType == JavaScriptLexer.BooleanLiteral;
  }

  public static boolean isNumeric(int tokenType) {
    return tokenType >= JavaScriptLexer.DecimalLiteral
        && tokenType <= JavaScriptLexer.BigDecimalIntegerLiteral;
  }

  public static boolean isKeyword(int tokenType) {
    return tokenType >= JavaScriptLexer.Break
        && tokenType <= JavaScriptLexer.Static
//        && tokenType != JavaScriptLexer.NonStrictLet
        && tokenType != JavaScriptLexer.Async
        && tokenType != JavaScriptLexer.As
        && tokenType != JavaScriptLexer.From;
  }

  public static boolean isString(int tokenType) {
    return tokenType == JavaScriptLexer.StringLiteral
        || tokenType == JavaScriptLexer.BackTick;
  }

  public static boolean isComment(int tokenType) {
    return tokenType == JavaScriptLexer.HtmlComment
        || tokenType == JavaScriptLexer.CDataComment
        || tokenType == JavaScriptLexer.MultiLineComment
        || tokenType == JavaScriptLexer.SingleLineComment;
  }

  public static boolean isSemi(int tokenType) {
    return tokenType == JavaScriptLexer.Comma
        || tokenType == JavaScriptLexer.SemiColon;
  }

}
