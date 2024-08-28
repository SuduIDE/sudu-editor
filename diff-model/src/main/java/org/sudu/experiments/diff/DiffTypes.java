package org.sudu.experiments.diff;

public interface DiffTypes {

  int DEFAULT = 0;
  int DELETED = 1;
  int INSERTED = 2;
  int EDITED = 3;

  int FOLDER_ALIGN_DIFF_TYPE = INSERTED;

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
