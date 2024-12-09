package org.sudu.experiments.editor;

import org.sudu.experiments.GL;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.ui.colors.DiffColors;
import org.sudu.experiments.math.Color;

import java.util.Arrays;

class DiffImage {

  static byte[] diffImage(LineDiff[] model, int height) {
    byte[] result = new byte[height];
    int lA = 0, lB, doc = model.length;
    int round = Math.min(doc, height) / 2;
    for (int i = 0; i < height; i++) {
      // lA = (doc * i + round) / height;
      lB = (doc * i + doc + round) / height;
      int j = lA, k = lA == lB ? lA + 1 : lB;
      char[] sss = new char[k - j];
      Arrays.fill(sss, '.');
      // for (; j < k; j++) {}
      System.out.println(
          "[" + i + "] = [" + lA + ", " + lB + ")" + new String(sss));

      lA = lB;
    }
    return result;
  }

  static void applyDiffPalette(
      byte[] diffState, GL.ImageData img,
      DiffColors colors
  ) {
    if (img.height != diffState.length || img.width != 1) {
      System.err.println("Diff image size mismatch");
      return;
    }
    var data = img.data;
    Color c0 = new Color(0, 0, 0, 0);
    for (int i = 0, p = 0; i < diffState.length; i++) {
      Color c = colors.getDiffColor(diffState[i], c0);
      data[p++] = (byte) c.r;
      data[p++] = (byte) c.g;
      data[p++] = (byte) c.g;
      data[p++] = (byte) c.a;
    }
  }
}
