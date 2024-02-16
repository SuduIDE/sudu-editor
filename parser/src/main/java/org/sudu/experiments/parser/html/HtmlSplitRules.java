package org.sudu.experiments.parser.html;

import org.sudu.experiments.parser.common.SplitRules;

import java.util.List;

public class HtmlSplitRules extends SplitRules {
  @Override
  public List<TokenSplitRule> getRules() {
    return List.of(
        makeRule((_1) -> true, this::splitTokenByLine)
    );
  }

}
