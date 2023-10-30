package org.sudu.experiments.parser.javascript.parser.highlighting;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.javascript.gen.LightJavaScriptLexer;

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
    return tokenType == LightJavaScriptLexer.NullLiteral;
  }

  public static boolean isBoolean(int tokenType) {
    return tokenType == LightJavaScriptLexer.BooleanLiteral;
  }

  public static boolean isNumeric(int tokenType) {
    return tokenType >= LightJavaScriptLexer.DecimalLiteral
        && tokenType <= LightJavaScriptLexer.BigDecimalIntegerLiteral;
  }

  public static boolean isKeyword(int tokenType) {
    return tokenType >= LightJavaScriptLexer.Break
        && tokenType <= LightJavaScriptLexer.Static
        && tokenType != LightJavaScriptLexer.Let
        && tokenType != LightJavaScriptLexer.Async
        && tokenType != LightJavaScriptLexer.As
        && tokenType != LightJavaScriptLexer.From;
  }

  public static boolean isString(int tokenType) {
    return tokenType == LightJavaScriptLexer.StringLiteral
        || tokenType == LightJavaScriptLexer.BackTick;
  }

  public static boolean isComment(int tokenType) {
    return tokenType == LightJavaScriptLexer.HtmlComment
        || tokenType == LightJavaScriptLexer.CDataComment
        || tokenType == LightJavaScriptLexer.MultiLineComment
        || tokenType == LightJavaScriptLexer.SingleLineComment;
  }

  public static boolean isSemi(int tokenType) {
    return tokenType == LightJavaScriptLexer.Comma
        || tokenType == LightJavaScriptLexer.SemiColon;
  }

}
