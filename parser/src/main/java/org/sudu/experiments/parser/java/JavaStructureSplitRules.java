package org.sudu.experiments.parser.java;

import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.help.Helper;

import java.util.List;

public class JavaStructureSplitRules extends SplitRules {

  @Override
  public List<TokenSplitRule> getRules() {
    return List.of(
        makeRule(_1 -> true, Helper::splitMultilineToken)
    );
  }

}
