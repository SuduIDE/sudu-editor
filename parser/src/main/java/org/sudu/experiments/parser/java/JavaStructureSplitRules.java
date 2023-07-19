package org.sudu.experiments.parser.java;

import org.sudu.experiments.parser.common.SplitRules;

import java.util.List;

public class JavaStructureSplitRules extends SplitRules {

  @Override
  public List<TokenSplitRule> getRules() {
    return List.of(
        makeRule(__ -> true, super::splitTokenByLine)
    );
  }

}
