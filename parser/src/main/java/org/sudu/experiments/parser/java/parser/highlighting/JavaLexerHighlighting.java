package org.sudu.experiments.parser.java.parser.highlighting;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.java.gen.JavaLexer;

import java.util.List;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;
import static org.sudu.experiments.parser.ParserConstants.TokenTypes.COMMENT;

public class JavaLexerHighlighting {

  public static void highlightTokens(List<Token> allTokens, int[] tokenTypes) {
    for (var token : allTokens) {
      int ind = token.getTokenIndex();
      int type = token.getType();
      if (isKeyword(type)) tokenTypes[ind] = KEYWORD;
      else if (isNumeric(type)) tokenTypes[ind] = NUMERIC;
      else if (isBooleanLiteral(type)) tokenTypes[ind] = BOOLEAN;
      else if (isStringOrChar(type)) tokenTypes[ind] = STRING;
      else if (isNull(type)) tokenTypes[ind] = NULL;
      else if (isSemi(type)) tokenTypes[ind] = SEMI;
      else if (isAT(type)) tokenTypes[ind] = ANNOTATION;
      else if (isComment(token.getType())) tokenTypes[ind] = COMMENT;
      else if (isJavadoc(token.getType())) tokenTypes[ind] = JAVADOC;
      else if (isError(token.getType())) tokenTypes[ind] = ERROR;
    }
  }

  public static void highlightCommentTokens(List<Token> allTokens, int[] tokenTypes) {
    for (var token: allTokens) {
      int ind = token.getTokenIndex();
      if (JavaLexerHighlighting.isComment(token.getType())) tokenTypes[ind] = ParserConstants.TokenTypes.COMMENT;
      if (JavaLexerHighlighting.isJavadoc(token.getType())) tokenTypes[ind] = ParserConstants.TokenTypes.JAVADOC;
      if (isErrorToken(token.getType())) tokenTypes[ind] = ParserConstants.TokenTypes.ERROR;
    }
  }

  public static boolean isComment(int type) {
    return type == JavaLexer.COMMENT
        || type == JavaLexer.LINE_COMMENT;
  }

  public static boolean isJavadoc(int type) {
    return type == JavaLexer.JAVADOC;
  }

  // Tokens from MODULE to VAR can be used as identifiers
  public static boolean isKeyword(int type) {
    return (type >= JavaLexer.ABSTRACT && type <= JavaLexer.WHILE)
        || (type >= JavaLexer.YIELD && type <= JavaLexer.NON_SEALED);
  }

  public static boolean isKeywordIdentifier(int type) {
    return type >= JavaLexer.MODULE && type <= JavaLexer.VAR;
  }

  public static boolean isNumeric(int type) {
    return type >= JavaLexer.DECIMAL_LITERAL && type <= JavaLexer.HEX_FLOAT_LITERAL;
  }

  public static boolean isBooleanLiteral(int type) {
    return type == JavaLexer.BOOL_LITERAL;
  }

  public static boolean isStringOrChar(int type) {
    return type >= JavaLexer.CHAR_LITERAL && type <= JavaLexer.TEXT_BLOCK;
  }

  public static boolean isNull(int type) {
    return type == JavaLexer.NULL_LITERAL;
  }

  public static boolean isSemi(int type) {
    return type == JavaLexer.SEMI || type == JavaLexer.COMMA;
  }

  public static boolean isAT(int type) {
    return type == JavaLexer.AT;
  }

  public static boolean isError(int type) {
    return type == JavaLexer.ERROR;
  }

  public static boolean isErrorToken(int type) {
    return type == JavaLexer.ERROR;
  }

}
