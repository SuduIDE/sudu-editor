package org.sudu.experiments.parser.typescript.parser.highlighting;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.typescript.gen.LightTypeScriptLexer;

import java.util.List;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class LightTypeScriptHighlighting {

  public static void highlightTokens(List<Token> allTokens, int[] tokenTypes) {
    for (var token: allTokens) {
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

  private static boolean isSemi(int tokenType) {
    return tokenType == LightTypeScriptLexer.SemiColon;
  }

  private static boolean isString(int tokenType) {
    return tokenType == LightTypeScriptLexer.StringLiteral;
  }

  private static boolean isKeyword(int tokenType) {
    return tokenType >= LightTypeScriptLexer.Break
        && tokenType <= LightTypeScriptLexer.Is;
  }

  private static boolean isNumeric(int tokenType) {
    return tokenType >= LightTypeScriptLexer.DecimalLiteral
        && tokenType <= LightTypeScriptLexer.BigDecimalIntegerLiteral;
  }

  private static boolean isBoolean(int tokenType) {
    return tokenType == LightTypeScriptLexer.BooleanLiteral;
  }

  private static boolean isNull(int tokenType) {
    return tokenType == LightTypeScriptLexer.NullLiteral;
  }

  public static boolean isComment(int tokenType) {
    return tokenType == LightTypeScriptLexer.MultiLineComment
        || tokenType == LightTypeScriptLexer.SingleLineComment
        || tokenType == LightTypeScriptLexer.HtmlComment
        || tokenType == LightTypeScriptLexer.CDataComment;
  }
}
