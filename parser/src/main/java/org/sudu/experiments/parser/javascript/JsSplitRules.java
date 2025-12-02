package org.sudu.experiments.parser.javascript;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.help.Helper;
import org.sudu.experiments.parser.javascript.gen.JavaScriptLexer;
import org.sudu.experiments.parser.javascript.gen.LightJavaScriptLexer;
import org.sudu.experiments.parser.typescript.gen.LightTypeScriptLexer;

import java.util.List;

public class JsSplitRules extends SplitRules {

  @Override
  public List<TokenSplitRule> getRules() {
    return List.of(
        makeRule(this::isStringLiteral, Helper::splitStringOrCharLiteral),
        makeRule(this::isMultiline, super::splitTokenByLine)
    );
  }

  private boolean isStringLiteral(Token token) {
    int type = token.getType();
    return type == LightJavaScriptLexer.StringLiteral
        || type == LightJavaScriptLexer.RegularExpressionLiteral;
  }

  private boolean isMultiline(Token token) {
    int type = token.getType();
    return type == JavaScriptLexer.MultiLineComment
        || type == JavaScriptLexer.HtmlComment
        || type == JavaScriptLexer.CDataComment
        || type == JavaScriptLexer.StringLiteral;
  }

}
