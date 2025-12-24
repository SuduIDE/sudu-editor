package org.sudu.experiments.parser.javascript;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.help.Helper;
import org.sudu.experiments.parser.javascript.gen.LightJavaScriptLexer;

import java.util.List;

public class JsSplitRules extends SplitRules {

  @Override
  public List<TokenSplitRule> getRules() {
    return List.of(
        makeRule(JsSplitRules::isStringLiteral, Helper::splitStringOrCharLiteral),
        makeRule(JsSplitRules::isMultiline, Helper::splitMultilineToken)
    );
  }

  public static boolean isStringLiteral(Token token) {
    int type = token.getType();
    return type == LightJavaScriptLexer.StringLiteral
        || type == LightJavaScriptLexer.RegularExpressionLiteral;
  }

  public static boolean isMultiline(Token token) {
    int type = token.getType();
    return type == LightJavaScriptLexer.MultiLineComment
        || type == LightJavaScriptLexer.HtmlComment
        || type == LightJavaScriptLexer.CDataComment
        || type == LightJavaScriptLexer.StringLiteral;
  }

}
