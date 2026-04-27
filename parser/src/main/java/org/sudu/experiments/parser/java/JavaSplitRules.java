package org.sudu.experiments.parser.java;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.help.Helper;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.java.gen.JavaLexer;

import java.util.List;

public class JavaSplitRules extends SplitRules {

  @Override
  public List<TokenSplitRule> getRules() {
    return List.of(
        makeRule(this::isStringOrCharLiteral, Helper::splitStringOrCharLiteral),
        makeRule(this::isMultiline, Helper::splitMultilineToken)
    );
  }

  private boolean isMultiline(Token token) {
    int type = token.getType();
    return type == JavaLexer.COMMENT
        || type == JavaLexer.TEXT_BLOCK
        || type == JavaLexer.JAVADOC;
  }

  private boolean isStringOrCharLiteral(Token token) {
    int type = token.getType();
    return type == JavaLexer.TEXT_BLOCK
        || type == JavaLexer.STRING_LITERAL
        || type == JavaLexer.CHAR_LITERAL;
  }
}
