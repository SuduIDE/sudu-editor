package org.sudu.experiments.parser.cpp.parser.highlighting;

import org.sudu.experiments.parser.common.Highlighting;
import org.sudu.experiments.parser.cpp.gen.CPP14Lexer;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class CppLexerHighlighting extends Highlighting {

  public static final Highlighting INSTANCE = new CppLexerHighlighting();

  private CppLexerHighlighting() {
    addRules(
        // Keyword
        CPP14Lexer.ALIGNAS, CPP14Lexer.WHILE, KEYWORD,
        // Numeric
        CPP14Lexer.INTEGER_LITERAL, CPP14Lexer.INTEGER_LITERAL, NUMERIC,
        CPP14Lexer.FLOATING_LITERAL, CPP14Lexer.FLOATING_LITERAL, NUMERIC,
        // Boolean
        CPP14Lexer.BOOLEAN_LITERAL, CPP14Lexer.BOOLEAN_LITERAL, BOOLEAN,
        // CHAR
        CPP14Lexer.CHARACTER_LITERAL, CPP14Lexer.CHARACTER_LITERAL, STRING,
        // String
        CPP14Lexer.STRING_LITERAL, CPP14Lexer.STRING_LITERAL, STRING,
        // Null
        CPP14Lexer.POINTER_LITERAL, CPP14Lexer.POINTER_LITERAL, NULL,
        CPP14Lexer.NULLPTR, CPP14Lexer.NULLPTR, NULL,
        // SEMI
        CPP14Lexer.SEMI, CPP14Lexer.SEMI, SEMI,
        CPP14Lexer.COMMA, CPP14Lexer.COMMA, SEMI,
        // Comment
        CPP14Lexer.BLOCK_COMMENT, CPP14Lexer.LINE_COMMENT, COMMENT,
        // Doc
        CPP14Lexer.DOCUMENTATION, CPP14Lexer.DOCUMENTATION, DOCUMENTATION,
        // DIRECTIVE
        CPP14Lexer.MULTI_LINE_MACRO, CPP14Lexer.DIRECTIVE, CPP_DIRECTIVE,
        // OPERATOR
        CPP14Lexer.LEFT_BRACKET, CPP14Lexer.RIGHT_BRACKET, OPERATOR,
        CPP14Lexer.PLUS, CPP14Lexer.ELLIPSIS, OPERATOR,
        // ERROR
        CPP14Lexer.ERROR, CPP14Lexer.ERROR, ERROR
    );
    addErrorRule(CPP14Lexer.ERROR);
  }
}
