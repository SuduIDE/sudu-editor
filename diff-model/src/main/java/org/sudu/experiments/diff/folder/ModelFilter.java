package org.sudu.experiments.diff.folder;

public interface ModelFilter {
  int NO_FILTER = 0;
  int LEFT = 1;
  int RIGHT = 2;

  static String name(int filter) {
    return switch (filter) {
      case NO_FILTER -> "NO_FILTER";
      case LEFT -> "LEFT";
      case RIGHT -> "RIGHT";
      default -> null;
    };
  }
}