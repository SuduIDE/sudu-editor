package org.sudu.experiments.editor;

import org.sudu.experiments.GL;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.ui.colors.DiffColors;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.math.Color;

class DiffImage {


  static byte[] diffImage(LineDiff[] model, int height) {
    byte[] result = new byte[height];
    int lA = 0, lB;
    int doc = model.length, round = height / 2;
    for (int i = 0; i < height; i++) {
      lA = (doc * i + round) / height;
      lB = (doc * i + doc + round) / height;
//      for (int j = lA; j < lB; j++) {}
      System.out.println(
          "[" + i + "] = [" + lA + ", " + lB + ")");

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

  public static void main(String[] args) {
    int doc = 12;
    LineDiff[] model = new LineDiff[doc];

    for (int i = 0; i < model.length; i++) {
      model[i] = new LineDiff(i % 4);
    }

//    var diffCode1 = diffImage(model, model.length * 3);
    var diffCode2 = diffImage(model, 5);

//    DiffColors diffColors = DiffColors.codeDiffDark();
//    GL.ImageData img = new GL.ImageData(1, diffCode.length);
//    applyDiffPalette(diffCode, img, diffColors);

  }
}
