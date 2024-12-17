package org.sudu.experiments.editor;

import org.sudu.experiments.GL;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.ui.colors.DiffColors;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V4i;

class DiffImage {

  static final boolean debug = false;

  static GL.ImageData diffImage(
      LineDiff[] model, int height, DiffColors colors
  ) {
    GL.ImageData img = new GL.ImageData(1, height);
    byte[] map = diffMap(model, height);
    if (debug) {
      V4i s = stats(map);
      System.out.println("codeMap built: l=" + map.length + ", 1: "
          + s.x + ", 2: " + s.y + ", 3: " + s.z + ", other: " + s.w);
      System.out.println("colors DiffTypes.DELETED = " + colors.getDiffColor(DiffTypes.DELETED, null));
      System.out.println("colors DiffTypes.INSERTED = " + colors.getDiffColor(DiffTypes.INSERTED, null));
      System.out.println("colors DiffTypes.EDITED = " + colors.getDiffColor(DiffTypes.EDITED, null));
    }
    blurDiffImage(map);
    applyDiffPalette(map, img, colors);
    return img;
  }

  static byte[] diffMap(LineDiff[] model, int height) {
    byte[] result = new byte[height];
    int a = 0, b, doc = model.length;
    int round = Math.min(doc, height) / 2;
    int l, r, d, type, j;
    for (int i = 0; i < height; i++) {
      b = (doc * i + doc + round) / height;
      l = 0; r = 0; d = 0;
      j = a;
      do {
        LineDiff diff = model[j];
        if (diff != null) {
          type = diff.type;
          if (type == DiffTypes.DELETED) l++;
          else if (type == DiffTypes.INSERTED) r++;
          else if (type == DiffTypes.EDITED) d++;
        }
      } while (++j < b);

      if ((j-a) != (a == b ? 1 : b - a))
        throw new RuntimeException();

      result[i] = (l | r | d) == 0 ? 0 :
          (byte) (l > d
              ? l > r ? DiffTypes.DELETED : DiffTypes.INSERTED
              : r > d ? DiffTypes.INSERTED : DiffTypes.EDITED);

      a = b;
    }
    return result;
  }

  static void blurDiffImage(byte[] image) {
    int length = image.length;
    if (length < 2) return;
    byte prev = image[0], curr = image[1], next;
    if (length == 2) {
      if (prev == 0 && curr != 0) image[0] = curr;
      if (curr == 0 && prev != 0) image[1] = prev;
    } else {
      if (prev == 0 && curr != 0) image[0] = curr;
      for (int i = 1, e = length - 1; i < e; i++) {
        next = image[i + 1];
        if (curr == 0 && ((prev | next) != 0)) {
          image[i] = prev == 0 ? next : prev;
        }
        prev = curr;
        curr = next;
      }
      if (curr == 0 && prev != 0) image[length - 1] = prev;
    }
  }

  public static V4i stats(byte[] image) {
    V4i r = new V4i();
    for (byte b : image) {
      switch (b) {
        case DiffTypes.DELETED -> r.x++;
        case DiffTypes.INSERTED -> r.y++;
        case DiffTypes.EDITED -> r.z++;
        default -> r.w++;
      }
    }
    return r;
  }

  static void applyDiffPalette(
      byte[] diffMap, GL.ImageData img, DiffColors colors
  ) {
    if (img.height != diffMap.length || img.width != 1) {
      System.err.println("Diff image size mismatch");
      return;
    }
    var data = img.data;
    Color c0 = new Color(0, 0, 0, 0);
    for (int i = 0, p = 0; i < diffMap.length; i++) {
      byte type = diffMap[i];
      if (type == 0) {
        c0.a = 0;
      }
      if (type != 0) {
        c0.a = 0;
      }
      Color c = colors.getDiffColor(type, c0);
      data[p++] = (byte) c.r;
      data[p++] = (byte) c.g;
      data[p++] = (byte) c.g;
      data[p++] = (byte) c.a;
    }
  }
}
