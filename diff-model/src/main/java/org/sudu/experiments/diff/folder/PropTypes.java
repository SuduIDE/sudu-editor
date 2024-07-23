package org.sudu.experiments.diff.folder;

public interface PropTypes {

  int NO_PROP = 0;
  int PROP_UP = 1;
  int PROP_DOWN = 2;

  static String name(int type) {
    return switch (type) {
      case NO_PROP -> "NoProp";
      case PROP_UP -> "PropUp";
      case PROP_DOWN -> "PropDown";
      default -> null;
    };
  }
}
