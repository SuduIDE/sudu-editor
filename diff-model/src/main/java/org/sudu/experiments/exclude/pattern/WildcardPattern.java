package org.sudu.experiments.exclude.pattern;

import org.sudu.experiments.exclude.FnMatch;
import java.util.List;

// TODO add new patterns types based on containWildcards && containPathWildcards
public class WildcardPattern extends BasePattern {

  protected List<String> patternSeq;
  protected boolean containWildcards;
  protected boolean containPathWildcards;

  protected WildcardPattern(String pattern) {
    super(pattern);
    containWildcards = containWildcards();
    containPathWildcards = this.pattern.contains("**");
    patternSeq = splitBySlash(this.pattern);
  }

  @Override
  public boolean match(String path, boolean isDir) {
    if (matchDirOnly && !isDir) return false;
    List<String> pathSeq = splitBySlash(path);
    if (matchFromRoot) return matchRec(0, pathSeq, 0);
    else {
      for (int from = 0; from < pathSeq.size(); from++)
        if (matchRec(0, pathSeq, from)) return true;
      return false;
    }
  }

  private boolean matchRec(
      int fromPattern,
      List<String> pathSeq, int fromPath
  ) {
    int patternInd = fromPattern, pathInd = fromPath;

    while (patternInd < patternSeq.size() && pathInd < pathSeq.size()) {
      String pattern = patternSeq.get(patternInd);
      String path = pathSeq.get(pathInd);

      if (pattern.equals("**")) {
        if (patternInd == patternSeq.size() - 1) return true;
        for (; pathInd < pathSeq.size(); pathInd++) {
          if (matchRec(patternInd + 1, pathSeq, pathInd)) return true;
        }
        return false;
      } else if (FnMatch.fnmatch(pattern, path)) {
        patternInd++;
        pathInd++;
      } else return false;
    }

    return patternInd == patternSeq.size() && pathInd == pathSeq.size();
  }

  protected boolean containWildcards() {
    for (int i = 0; i < pattern.length(); i++) {
      char c = pattern.charAt(i);
      boolean escape = i > 0 && pattern.charAt(i - 1) == '\\';
      if (!escape && (c == '*' || c == '?' || c == '[')) return true;
    }
    return false;
  }
}
