package org.sudu.experiments.parser.json;

import org.sudu.experiments.parser.common.SplitRules;

import java.util.List;

public class JsonSplitRules extends SplitRules {
  @Override
  public List<TokenSplitRule> getRules() {
    return List.of(
        makeRule((_1) -> true, this::splitTokenByLine)
    );
  }
}
