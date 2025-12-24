package org.sudu.experiments.parser.typescript;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.help.Helper;
import org.sudu.experiments.parser.typescript.gen.LightTypeScriptLexer;

import java.util.List;

public class TsSplitRules extends SplitRules {

  @Override
  public List<TokenSplitRule> getRules() {
    return List.of(
        makeRule(this::isStringLiteral, Helper::splitStringOrCharLiteral),
        makeRule(this::isMultiline, Helper::splitMultilineToken)
    );
  }

  private boolean isStringLiteral(Token token) {
    int type = token.getType();
    return type == LightTypeScriptLexer.StringLiteral
        || type == LightTypeScriptLexer.RegularExpressionLiteral;
  }

  private boolean isMultiline(Token token) {
    int type = token.getType();
    return type == LightTypeScriptLexer.MultiLineComment
        || type == LightTypeScriptLexer.HtmlComment
        || type == LightTypeScriptLexer.CDataComment
        || type == LightTypeScriptLexer.StringLiteral;
  }

}
