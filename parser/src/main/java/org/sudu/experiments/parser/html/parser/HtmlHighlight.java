package org.sudu.experiments.parser.html.parser;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.html.gen.HTMLLexer;

import java.util.List;

public class HtmlHighlight {

  static void highlightTokens(List<Token> allTokens, int[] tokenTypes) {
    for (var token : allTokens) {
      int type = token.getType();
      int ind = token.getTokenIndex();
      if (type == HTMLLexer.ERROR) {
        tokenTypes[ind] = ParserConstants.TokenTypes.ERROR;
      } else if (type == HTMLLexer.NEW_LINE
          || type == HTMLLexer.SEA_WS
          || type == HTMLLexer.TAG_WHITESPACE
          || type == HTMLLexer.HTML_TEXT) {
        continue;
      } else if (type == HTMLLexer.HTML_COMMENT
          || type == HTMLLexer.HTML_CONDITIONAL_COMMENT
          || type == HTMLLexer.XML
          || type == HTMLLexer.CDATA
          || type == HTMLLexer.DTD
          || type == HTMLLexer.SCRIPTLET) {
        tokenTypes[ind] = ParserConstants.TokenTypes.COMMENT;
      } else if (type == HTMLLexer.SCRIPT_OPEN
          || type == HTMLLexer.STYLE_OPEN
          || type == HTMLLexer.TAG_OPEN
          || type == HTMLLexer.TAG_CLOSE
          || type == HTMLLexer.TAG_SLASH_CLOSE
          || type == HTMLLexer.TAG_SLASH
          || type == HTMLLexer.TAG_NAME) {
        tokenTypes[ind] = ParserConstants.TokenTypes.ANNOTATION;
      } else if (type == HTMLLexer.TAG_EQUALS) {
        tokenTypes[ind] = ParserConstants.TokenTypes.OPERATOR;
      } else if (type == HTMLLexer.ATTRIBUTE || type == HTMLLexer.ATTVALUE_VALUE) {
        tokenTypes[ind] = ParserConstants.TokenTypes.STRING;
      }
    }
  }
}
