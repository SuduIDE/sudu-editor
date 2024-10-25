package org.sudu.experiments.diff;

public interface DiffTypes {

  int DEFAULT = 0;
  int DELETED = 1;  // LEFT ONLY
  int INSERTED = 2; // RIGHT ONLY
  int EDITED = 3;
  int EDITED2 = 4; // edited element on edited region

  static String name(int type) {
    return switch (type) {
      case DEFAULT -> "DEFAULT";
      case DELETED -> "DELETED";
      case INSERTED -> "INSERTED";
      case EDITED -> "EDITED";
      default -> null;
    };
  }
}
