package org.sudu.experiments.exclude.pattern;

public class NoWildcardPattern extends BasePattern {

  protected NoWildcardPattern(String pattern) {
    super(pattern);
  }

  @Override
  public boolean match(String path, boolean isDir) {
    if (matchDirOnly && !isDir) return false;
    if (matchFromRoot) return pattern.equals(path);
    return path.endsWith(pattern);
  }
}
