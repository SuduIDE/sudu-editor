package org.sudu.experiments.exclude.pattern;

import org.sudu.experiments.exclude.FnMatch;
import java.util.List;

public class PathWildcardPattern extends BasePattern {

  protected List<String> patternSeq;

  protected PathWildcardPattern(String pattern) {
    super(pattern);
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
      } else if (FnMatch.fnmatch(pattern, path, 0, FnMatch.Flag.PATHNAME)) {
        patternInd++;
        pathInd++;
      } else return false;
    }

    return patternInd == patternSeq.size() && pathInd == pathSeq.size();
  }
}
