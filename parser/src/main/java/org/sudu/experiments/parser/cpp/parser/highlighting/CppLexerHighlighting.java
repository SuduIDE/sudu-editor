package org.sudu.experiments.parser.cpp.parser.highlighting;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.Utils;
import org.sudu.experiments.parser.cpp.gen.CPP14Lexer;

import java.util.List;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class CppLexerHighlighting {

  public static void highlightTokens(List<Token> allTokens, int[] tokenTypes, int[] tokenStyles) {
    for (var token : allTokens) {
      int ind = token.getTokenIndex();
      int type = token.getType();
      if (isKeyword(type)) tokenTypes[ind] = KEYWORD;
      else if (isNumeric(type)) tokenTypes[ind] = NUMERIC;
      else if (isBooleanLiteral(type)) tokenTypes[ind] = BOOLEAN;
      else if (isStringOrChar(type)) tokenTypes[ind] = STRING;
      else if (isNull(type)) tokenTypes[ind] = NULL;
      else if (isSemi(type)) tokenTypes[ind] = SEMI;
      else if (isComment(token.getType())) tokenTypes[ind] = COMMENT;
      else if (isDirective(token.getType())) tokenTypes[ind] = ANNOTATION;
      else if (isOperator(token.getType())) tokenTypes[ind] = OPERATOR;
      else if (isError(token.getType()) || token.getType() == -1) Utils.markError(tokenTypes, tokenStyles, ind);
    }
  }

  public static boolean isKeyword(int tokenType) {
    return tokenType >= CPP14Lexer.Alignas
        && tokenType <= CPP14Lexer.While;
  }

  public static boolean isNumeric(int tokenType) {
    return tokenType == CPP14Lexer.IntegerLiteral
        || tokenType == CPP14Lexer.FloatingLiteral;
  }

  public static boolean isBooleanLiteral(int tokenType) {
    return tokenType == CPP14Lexer.BooleanLiteral;
  }

  public static boolean isStringOrChar(int tokenType) {
    return tokenType == CPP14Lexer.CharacterLiteral
        || tokenType == CPP14Lexer.StringLiteral;
  }

  public static boolean isNull(int tokenType) {
    return tokenType == CPP14Lexer.PointerLiteral
        || tokenType == CPP14Lexer.Nullptr;
  }

  public static boolean isSemi(int tokenType) {
    return tokenType == CPP14Lexer.Semi
        || tokenType == CPP14Lexer.Comma;
  }

  public static boolean isComment(int tokenType) {
    return tokenType == CPP14Lexer.BlockComment
        || tokenType == CPP14Lexer.LineComment;
  }

  public static boolean isDirective(int tokenType) {
    return tokenType == CPP14Lexer.Directive
        || tokenType == CPP14Lexer.MultiLineMacro;
  }

  public static boolean isOperator(int tokenType) {
    return tokenType == CPP14Lexer.LeftBracket
        || tokenType == CPP14Lexer.RightBracket
        || (tokenType >= CPP14Lexer.Plus
        && tokenType <= CPP14Lexer.Ellipsis);
  }

  public static boolean isError(int tokenType) {
    return tokenType == CPP14Lexer.ERROR;
  }

}
