package org.sudu.experiments.diff;

public interface ItemKind {

  int FOLDER = 0;
  int FILE = 1;
  int LEFT_ONLY_FILE = 2;
  int RIGHT_ONLY_FILE = 3;

  static String name(int type) {
    return switch (type) {
      case FOLDER -> "FOLDER";
      case FILE -> "FILE";
      case LEFT_ONLY_FILE -> "LEFT_ONLY_FILE";
      case RIGHT_ONLY_FILE -> "RIGHT_ONLY_FILE";
      default -> null;
    };
  }
}
