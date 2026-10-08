package org.sudu.experiments.parser.ascendc;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.ascendc.gen.AscendCLexer;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.cpp.gen.CPP14Lexer;
import org.sudu.experiments.parser.cpp.gen.help.CPP14DirectiveParser;
import org.sudu.experiments.parser.cpp.parser.CppDirectiveSplitter;
import org.sudu.experiments.parser.help.Helper;

import java.util.List;

public class AscendCSplitRules extends SplitRules {

  @Override
  public List<TokenSplitRule> getRules() {
    return List.of(
        makeRule(this::isStringOrCharLiteral, Helper::splitStringOrCharLiteral),
        makeRule(this::isMacroOrDirective, CppDirectiveSplitter::divideDirective),
        makeRule(this::isMultilineToken, Helper::splitMultilineToken)
    );
  }

  private boolean isStringOrCharLiteral(Token token) {
    int type = token.getType();
    return type == AscendCLexer.STRING_LITERAL
        || type == AscendCLexer.CHAR_LITERAL;
  }

  private boolean isMacroOrDirective(Token token) {
    int type = token.getType();
    return AscendCLexer.DIRECTIVE <= type && type <= AscendCLexer.MULTILINE_MACRO;
  }

  private boolean isMultilineToken(Token token) {
    int type = token.getType();
    return type == AscendCLexer.BLOCK_COMMENT
        || type == AscendCLexer.DOCUMENTATION
        || type == AscendCLexer.MULTILINE_MACRO
        || type == AscendCLexer.RAW_STRING_LITERAL;
  }
}
