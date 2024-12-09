package org.sudu.experiments.editor;

import org.sudu.experiments.diff.LineDiff;

public class DiffImageTest {

  public static void main(String[] args) {
    int doc = 12;
    LineDiff[] model = new LineDiff[doc];

    for (int i = 0; i < model.length; i++) {
      model[i] = new LineDiff(i % 4);
    }

    testMag(model);
    System.out.println("-------------------------");
    testMin(model);

//    DiffColors diffColors = DiffColors.codeDiffDark();
//    GL.ImageData img = new GL.ImageData(1, diffCode.length);
//    applyDiffPalette(diffCode, img, diffColors);

  }

  static void testMin(LineDiff[] model) {
    System.out.println("   " + model.length + " -> " + 5);
    var r1 = DiffImage.diffImage(model, 5);

    System.out.println("   " + model.length + " -> " + 7);
    var r4 = DiffImage.diffImage(model, 7);

    System.out.println("   " + model.length + " -> " + model.length);
    var r2 = DiffImage.diffImage(model, model.length);

    System.out.println("   " + model.length + " -> " + 4);
    var r3 = DiffImage.diffImage(model, 4);
  }

  static void testMag(LineDiff[] model) {
    System.out.println("--------  model.length * 3 + 2  ----------");

    int height1 = model.length * 3 + 2;
    System.out.println("   " + model.length + " -> " + height1);
    var r1 = DiffImage.diffImage(model, height1);

    System.out.println("--------  model.length * 3 + 3  ----------");

    int height2 = model.length * 3 + 3;
    System.out.println("   " + model.length + " -> " + height2);
    var r2 = DiffImage.diffImage(model, height2);

    System.out.println("--------  model.length * 3 + 4  ----------");

    int height3 = model.length * 3 + 4;
    System.out.println("   " + model.length + " -> " + height3);
    var r3 = DiffImage.diffImage(model, height3);
  }

}
