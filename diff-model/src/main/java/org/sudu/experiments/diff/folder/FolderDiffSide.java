package org.sudu.experiments.diff.folder;

public interface FolderDiffSide {
  int BOTH = 0;
  int LEFT = 1;
  int RIGHT = 2;

  static String name(int side) {
    return switch (side) {
      case BOTH -> "BOTH";
      case LEFT -> "LEFT";
      case RIGHT -> "RIGHT";
      default -> null;
    };
  }
}