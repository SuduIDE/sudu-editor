package org.sudu.experiments.parser.html;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.help.Helper;
import org.sudu.experiments.parser.html.gen.HTMLLexer;
import org.sudu.experiments.parser.javascript.JsSplitRules;
import org.sudu.experiments.parser.javascript.gen.LightJavaScriptLexer;
import org.sudu.experiments.parser.javascript.parser.highlighting.LightJavaScriptLexerHighlighting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HtmlSplitRules extends SplitRules {
  @Override
  public List<TokenSplitRule> getRules() {
    return List.of(
        makeRule(this::isScript, this::splitScript),
        makeRule((_1) -> true, Helper::splitMultilineToken)
    );
  }

  private boolean isScript(Token token) {
    int type = token.getType();
    return type == HTMLLexer.SCRIPT_SHORT_BODY || type == HTMLLexer.SCRIPT_BODY;
  }

  private List<Token> splitScript(Token script) {
    String text = script.getText();
    var jsLexer = new LightJavaScriptLexer(CharStreams.fromString(text));
    var scriptStream = new CommonTokenStream(jsLexer);
    scriptStream.fill();

    var allTokens = scriptStream.getTokens();
    int[] tokenTypes = new int[allTokens.size()];
    LightJavaScriptLexerHighlighting.highlightTokens(allTokens, tokenTypes);

    var result = new ArrayList<Token>();
    int baseLine = script.getLine() - 1;
    int baseStartIndex = script.getStartIndex();
    int totalDelta = 0;

    for (var token: allTokens) {
      if (token.getType() == Token.EOF) continue;
      int ind = token.getTokenIndex();
      int delta = Helper.tokenDelta(token);

      List<Token> splitTokens = JsSplitRules.isMultiline(token)
          ? Helper.splitMultilineToken(token)
          : Collections.singletonList(token);

      for (var splitToken: splitTokens) {
        result.add(Helper.mkSplitToken(splitToken,
            baseStartIndex, baseLine,
            totalDelta, delta,
            tokenTypes[ind])
        );
      }
      totalDelta += delta;
    }
    return result;
  }
}
