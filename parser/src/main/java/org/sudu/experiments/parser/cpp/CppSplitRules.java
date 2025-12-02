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
        makeRule(this::isMacroOrDirective, this::splitMacroOrDirective),
        makeRule(this::isMultilineToken, super::splitTokenByLine)
    );
  }

  private boolean isMultilineToken(Token token) {
    int type = token.getType();
    return type == CPP14Lexer.BlockComment
        || type == CPP14Lexer.Documentation
        || type == CPP14Lexer.Directive
        || type == CPP14Lexer.MultiLineMacro
        || type == CPP14Lexer.StringLiteral;
  }

  private boolean isMacroOrDirective(Token token) {
    int type = token.getType();
    return type == CPP14Lexer.Directive
        || type == CPP14Lexer.MultiLineMacro;
  }

  private boolean isStringOrCharLiteral(Token token) {
    int type = token.getType();
    return type == CPP14Lexer.StringLiteral
        || type == CPP14Lexer.CharacterLiteral;
  }

  private List<Token> splitMacroOrDirective(Token token) {
    return CppDirectiveSplitter.divideDirective(token);
  }
}
