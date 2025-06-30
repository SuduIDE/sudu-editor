package org.sudu.experiments.exclude.pattern;

import org.sudu.experiments.exclude.FnMatch;

public class WildcardPattern extends BasePattern {

  protected WildcardPattern(String pattern) {
    super(pattern);
  }

  @Override
  public boolean match(String path, boolean isDir) {
    if (matchDirOnly && !isDir) return false;
    if (matchFromRoot) return FnMatch.fnmatch(pattern, path, 0, FnMatch.Flag.PATHNAME);
    int sPos = -1;
    do {
      if (FnMatch.fnmatch(pattern, path, sPos + 1, FnMatch.Flag.PATHNAME)) return true;
      sPos = path.indexOf('/', sPos + 1);
    } while (sPos > -1);
    return false;
  }
}
