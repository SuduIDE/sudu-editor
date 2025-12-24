package org.sudu.experiments.parser.json;

import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.help.Helper;

import java.util.List;

public class JsonSplitRules extends SplitRules {
  @Override
  public List<TokenSplitRule> getRules() {
    return List.of(
        makeRule((_1) -> true, Helper::splitMultilineToken)
    );
  }
}
