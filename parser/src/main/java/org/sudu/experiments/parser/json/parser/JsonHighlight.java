package org.sudu.experiments.parser.json.parser;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.json.gen.JsonLexer;

import java.util.List;

public class JsonHighlight {

  static void highlightTokens(List<Token> allTokens, int[] tokenTypes) {
    for (var token: allTokens) {
      int type = token.getType();
      int ind = token.getTokenIndex();
      if (isBoolean(type)) tokenTypes[ind] = ParserConstants.TokenTypes.BOOLEAN;
      else if (isNumber(type)) tokenTypes[ind] = ParserConstants.TokenTypes.NUMERIC;
      else if (isNull(type)) tokenTypes[ind] = ParserConstants.TokenTypes.NULL;
      else if (isString(type)) tokenTypes[ind] = ParserConstants.TokenTypes.STRING;
      else if (isCommaOrColon(type)) tokenTypes[ind] = ParserConstants.TokenTypes.SEMI;
      else if (isComment(type)) tokenTypes[ind] = ParserConstants.TokenTypes.COMMENT;
    }
  }

  static boolean isBoolean(int type) {
    return type == JsonLexer.BOOLEAN;
  }

  static boolean isNumber(int type) {
    return type == JsonLexer.NUMBER;
  }

  static boolean isNull(int type) {
    return type == JsonLexer.NULL;
  }

  static boolean isString(int type) {
    return type == JsonLexer.STRING;
  }

  static boolean isCommaOrColon(int type) {
    return type == JsonLexer.COMMA
        || type == JsonLexer.COLON;
  }

  static boolean isComment(int type) {
    return type == JsonLexer.COMMENT
        || type == JsonLexer.LINE_COMMENT;
  }
}
