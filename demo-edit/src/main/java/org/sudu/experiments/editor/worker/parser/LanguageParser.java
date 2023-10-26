package org.sudu.experiments.editor.worker.parser;

import java.util.List;

public abstract class LanguageParser {

  public abstract String getLangName();

  public abstract int getLangInd();

  public void fullScopesParse(char[] chars, List<Object> result) {
    int[] ints = fullParseScopes(chars);
    result.add(ints);
    result.add(chars);
    result.add(new int[]{getLangInd()});
//    result.add();
  }

  protected abstract int[] fullParseScopes(char[] chars);

}
