package org.sudu.experiments.parser.python;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.help.Helper;
import org.sudu.experiments.parser.python.gen.PythonLexer;

import java.util.List;

public class PythonSplitRules extends SplitRules {
  @Override
  public List<TokenSplitRule> getRules() {
    return List.of(
        makeRule(this::isMultiline, Helper::splitMultilineToken),
        makeRule(this::isString, Helper::splitStringOrCharLiteral)
    );
  }

  private boolean isMultiline(Token token) {
    int type = token.getType();
    return type == PythonLexer.LINE_JOIN;
  }

  private boolean isString(Token token) {
    int type = token.getType();
    return type >= PythonLexer.STRING
        && type <= PythonLexer.MULTILINE_STRING;
  }
}
