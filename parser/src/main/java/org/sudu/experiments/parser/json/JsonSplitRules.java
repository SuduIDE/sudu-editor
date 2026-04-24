package org.sudu.experiments.parser.json;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.json.gen.JsonLexer;

import java.util.List;

public class JsonSplitRules extends SplitRules {
  @Override
  public List<TokenSplitRule> getRules() {
    return List.of(
        makeRule(this::isMultiline, this::splitTokenByLine)
    );
  }

  private boolean isMultiline(Token token) {
    int type = token.getType();
    return type == JsonLexer.COMMENT;
  }
}
