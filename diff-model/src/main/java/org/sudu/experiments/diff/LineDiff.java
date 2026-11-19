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
    if (diff == null || diff.length == 0) return false;
    return diff.length > 1 || (diff[0] != null && diff[0].type != 0);
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

  // Edited-range tail: DELETED/INSERTED when that span continues onto the
  // next line, or when a blank line's own last element is deleted/inserted.
  public static int editedDelInsTailType(LineDiff[] diffs, int line, boolean isBlank) {
    if (diffs == null || line < 0 || line >= diffs.length) return 0;
    LineDiff cur = diffs[line];
    if (cur == null || cur.type != DiffTypes.EDITED) return 0;
    int last = lastElementType(cur);
    if (!isDelIns(last)) return 0;
    if (isBlank) return last;
    LineDiff next = line + 1 < diffs.length ? diffs[line + 1] : null;
    if (next == null) return 0;
    return firstElementType(next) == last ? last : 0;
  }

  private static boolean isDelIns(int type) {
    return type == DiffTypes.DELETED || type == DiffTypes.INSERTED;
  }

  private static int lastElementType(LineDiff d) {
    int[] e = d.elementTypes;
    return e != null && e.length > 0 ? e[e.length - 1] : 0;
  }

  private static int firstElementType(LineDiff d) {
    int[] e = d.elementTypes;
    if (e != null && e.length > 0) return e[0];
    int t = d.type;
    return isDelIns(t) ? t : 0;
  }
}
