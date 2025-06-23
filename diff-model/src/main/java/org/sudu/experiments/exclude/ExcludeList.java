package org.sudu.experiments.exclude;

import org.sudu.experiments.exclude.pattern.BasePattern;
import org.sudu.experiments.text.SplitText;

import java.util.ArrayList;
import java.util.List;

public class ExcludeList {

  private final List<BasePattern> patterns, exceptions;

  /**
   * <a href="https://git-scm.com/docs/gitignore">Gitignore rules</a>
   */
  public ExcludeList(String source) {
    patterns = new ArrayList<>();
    exceptions = new ArrayList<>();
    String[] lines = SplitText.split(source);
    for (String line: lines) {
      if (isBlankOrComment(line)) continue;
      var pattern = BasePattern.mkPattern(line);
      if (pattern.exclude) patterns.add(pattern);
      else exceptions.add(pattern);
    }
  }

  public boolean isExcluded(String path, boolean isDir) {
    boolean excluded = false;
    for (var pattern: patterns) {
      excluded = pattern.match(path, isDir);
      if (excluded) break;
    }
    if (!excluded) return false;
    for (var pattern: exceptions) {
      excluded = !pattern.match(path, isDir);
      if (!excluded) return false;
    }
    return true;
  }

  private static boolean isBlankOrComment(String line) {
    return line.isBlank() || line.charAt(0) == '#';
  }
}
