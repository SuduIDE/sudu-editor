package org.sudu.experiments.parser.cpp.parser.highlighting;

import org.sudu.experiments.parser.common.Highlighting;
import org.sudu.experiments.parser.cpp.gen.CPP14Lexer;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class CppLexerHighlighting extends Highlighting {

  public static final Highlighting INSTANCE = new CppLexerHighlighting();

  private CppLexerHighlighting() {
    addRules(
        // Keyword
        CPP14Lexer.Alignas, CPP14Lexer.While, KEYWORD,
        // Numeric
        CPP14Lexer.IntegerLiteral, CPP14Lexer.IntegerLiteral, NUMERIC,
        CPP14Lexer.FloatingLiteral, CPP14Lexer.FloatingLiteral, NUMERIC,
        // Boolean
        CPP14Lexer.BooleanLiteral, CPP14Lexer.BooleanLiteral, BOOLEAN,
        // Char
        CPP14Lexer.CharacterLiteral, CPP14Lexer.CharacterLiteral, STRING,
        // String
        CPP14Lexer.StringLiteral, CPP14Lexer.StringLiteral, STRING,
        // Null
        CPP14Lexer.PointerLiteral, CPP14Lexer.PointerLiteral, NULL,
        CPP14Lexer.Nullptr, CPP14Lexer.Nullptr, NULL,
        // Semi
        CPP14Lexer.Semi, CPP14Lexer.Semi, SEMI,
        CPP14Lexer.Comma, CPP14Lexer.Comma, SEMI,
        // Comment
        CPP14Lexer.BlockComment, CPP14Lexer.LineComment, COMMENT,
        // Doc
        CPP14Lexer.Documentation, CPP14Lexer.Documentation, DOCUMENTATION,
        // Directive
        CPP14Lexer.MultiLineMacro, CPP14Lexer.Directive, CPP_DIRECTIVE,
        // Operator
        CPP14Lexer.LeftBracket, CPP14Lexer.RightBracket, OPERATOR,
        CPP14Lexer.Plus, CPP14Lexer.Ellipsis, OPERATOR,
        // Error
        CPP14Lexer.ERROR, CPP14Lexer.ERROR, ERROR
    );
    addErrorRule(CPP14Lexer.ERROR);
  }
}
