package org.sudu.experiments.exclude.pattern;

import java.util.ArrayList;
import java.util.List;

public abstract class BasePattern {

  public final boolean exclude;
  protected final String pattern;
  protected final boolean matchDirOnly;
  protected final boolean matchFromRoot;

  protected BasePattern(String pattern) {
    this.exclude = !pattern.startsWith("!");
    if (!exclude) pattern = pattern.substring(1);

    this.matchDirOnly = pattern.endsWith("/");
    if (matchDirOnly) pattern = pattern.substring(0, pattern.length() - 1);

    if (pattern.startsWith("/")) {
      this.matchFromRoot = true;
      pattern = pattern.substring(1);
    } else {
      this.matchFromRoot = pattern.contains("/");
    }
    this.pattern = pattern;
  }

  public static BasePattern mkPattern(String pattern) {
    // TODO add new patterns types based on containWildcards && containPathWildcards
    return new WildcardPattern(pattern);
  }

  public abstract boolean match(String path, boolean isDir);

  protected static List<String> splitBySlash(String str) {
    List<String> result = new ArrayList<>();
    int pPos = 0;
    while (pPos < str.length()) {
      int wPos = str.indexOf("/", pPos);
      if (wPos == -1) break;
      String p = str.substring(pPos, wPos);
      if (!p.equals("**") ||
          result.isEmpty() ||
          !result.get(result.size() - 1).equals("**")
      ) result.add(p);
      pPos = wPos + 1;
    }
    if (pPos != str.length()) result.add(str.substring(pPos));
    return result;
  }
}
