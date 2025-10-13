package org.sudu.experiments.diff;

public class LineDiff {

  public int type;
  public int[] elementTypes;

  public LineDiff(int type) {
    this.type = type;
  }

  public LineDiff(int type, int lineLen) {
    this.type = type;
    this.elementTypes = new int[lineLen];
  }

  public boolean isDefault() {
    return type == DiffTypes.DEFAULT;
  }

  @Override
  public String toString() {
    return DiffTypes.name(type);
  }

  public static boolean notEmpty(LineDiff[] diff) {
    return diff != null && (diff.length > 1 || (diff[0] != null && diff[0].type != 0));
  }

  public static void replaceEdited(LineDiff[] diffs, int target) {
    for (LineDiff diff : diffs) {
      if (diff != null && diff.type == DiffTypes.EDITED) {
        diff.type = target;
        int[] eTypes = diff.elementTypes;
        if (eTypes != null) {
          for (int i = 0, l = eTypes.length; i < l; i++) {
            if (eTypes[i] == DiffTypes.EDITED)
              eTypes[i] = target;
          }
        }
      }
    }
  }

  public static byte[] colors(LineDiff[] diffs) {
    byte[] c = new byte[diffs.length];
    for (int i = 0; i < c.length; i++) {
      LineDiff ld = diffs[i];
      c[i] = ld != null ? (byte) ld.type : 0;
    }
    return c;
  }
}
