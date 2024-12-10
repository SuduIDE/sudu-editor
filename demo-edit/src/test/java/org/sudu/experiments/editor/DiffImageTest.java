package org.sudu.experiments.editor;

import org.sudu.experiments.diff.LineDiff;

import java.util.Arrays;

public class DiffImageTest {

  public static void main(String[] args) {
    int doc = 12;

    testMag(doc);
    System.out.println("-------------------------");
    testMin(doc);

//    DiffColors diffColors = DiffColors.codeDiffDark();
//    GL.ImageData img = new GL.ImageData(1, diffCode.length);
//    applyDiffPalette(diffCode, img, diffColors);

  }

  static LineDiff[] model(int doc) {
    LineDiff[] model = new LineDiff[doc];

    for (int i = 0; i < model.length; i++) {
      model[i] = new LineDiff(i % 4);
    }
    return model;
  }

  static void diffImageIterateTest(int document, int height) {
    int a = 0, b;
    int round = Math.min(document, height) / 2;

    for (int i = 0; i < height; i++) {
      b = (document * i + document + round) / height;
      char[] sss = new char[a == b ? 1 : b - a];
      Arrays.fill(sss, '.');

      System.out.println(
          "[" + i + "] = [" + a + ", " + b + ")" + new String(sss));

      a = b;
    }

    DiffImage.diffImage(model(document), height);
  }

  static void testMin(int modelLength) {
    System.out.println("   " + modelLength + " -> " + 5);
    diffImageIterateTest(modelLength, 5);

    System.out.println("   " + modelLength + " -> " + 7);
    diffImageIterateTest(modelLength, 7);

    System.out.println("   " + modelLength + " -> " + modelLength);
    diffImageIterateTest(modelLength, modelLength);

    System.out.println("   " + modelLength + " -> " + 4);
    diffImageIterateTest(modelLength, 4);
  }

  static void testMag(int modelLength) {
    System.out.println("--------  model.length * 3 + 2  ----------");

    int height1 = modelLength * 3 + 2;
    System.out.println("   " + modelLength + " -> " + height1);
    diffImageIterateTest(modelLength, height1);

    System.out.println("--------  modelLength * 3 + 3  ----------");

    int height2 = modelLength * 3 + 3;
    System.out.println("   " + modelLength + " -> " + height2);
    diffImageIterateTest(modelLength, height2);

    System.out.println("--------  modelLength * 3 + 4  ----------");

    int height3 = modelLength * 3 + 4;
    System.out.println("   " + modelLength + " -> " + height3);
    diffImageIterateTest(modelLength, height3);
  }

}
