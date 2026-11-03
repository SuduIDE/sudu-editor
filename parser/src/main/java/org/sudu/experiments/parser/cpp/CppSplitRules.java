package org.sudu.experiments.parser.cpp;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.help.Helper;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.cpp.gen.CPP14Lexer;
import org.sudu.experiments.parser.cpp.parser.CppDirectiveSplitter;

import java.util.List;

public class CppSplitRules extends SplitRules {

  @Override
  public List<TokenSplitRule> getRules() {
    return List.of(
        makeRule(this::isStringOrCharLiteral, Helper::splitStringOrCharLiteral),
        makeRule(this::isMacroOrDirective, CppDirectiveSplitter::divideDirective),
        makeRule(this::isMultilineToken, Helper::splitMultilineToken)
    );
  }

  private boolean isMultilineToken(Token token) {
    int type = token.getType();
    return type == CPP14Lexer.BLOCK_COMMENT
        || type == CPP14Lexer.DOCUMENTATION
        || type == CPP14Lexer.DIRECTIVE
        || type == CPP14Lexer.MULTI_LINE_MACRO
        || type == CPP14Lexer.STRING_LITERAL;
  }

  private boolean isMacroOrDirective(Token token) {
    int type = token.getType();
    return type == CPP14Lexer.DIRECTIVE
        || type == CPP14Lexer.MULTI_LINE_MACRO;
  }

  private boolean isStringOrCharLiteral(Token token) {
    int type = token.getType();
    return type == CPP14Lexer.STRING_LITERAL
        || type == CPP14Lexer.CHARACTER_LITERAL;
  }
}
