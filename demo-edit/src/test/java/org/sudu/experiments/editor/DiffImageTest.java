package org.sudu.experiments.editor;

import org.junit.jupiter.api.Assertions;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.XorShiftRandom;

import java.util.Arrays;

public class DiffImageTest {

  static boolean print = true;

  public static void main(String[] args) {
    int doc = 12;

    testMag(doc);
    print("-------------------------");
    testMin(doc);
    testBlur();

    testImageToDoc();

//    DiffColors diffColors = DiffColors.codeDiffDark();
//    GL.ImageData img = new GL.ImageData(1, diffCode.length);
//    applyDiffPalette(diffCode, img, diffColors);

  }

  static int imageToDocumentBad(int imgPos, int imageLength, int docLength) {
    int line1 = (int) ((double) (imgPos * docLength) / imageLength + 0.5);

    return Numbers.clamp(0, line1, docLength - 1);
  }


  private static void testImageToDoc() {
    int qqq = 620 / 50;

    int i0 = imageToDocumentBad(6, 620, 50);
    int i1 = imageToDocumentBad(7, 620, 50);
    int e0 = imageToDocumentBad(11, 620, 50);
    int j1 = imageToDocumentBad(18, 620, 50);
    int i48 = imageToDocumentBad(601, 620, 50);
    int i49 = imageToDocumentBad(602, 620, 50);
    int j49 = imageToDocumentBad(619, 620, 50);
//    System.out.println("j49 = " + j49);
    int f0 = DiffImage.imageToDocument(11, 620, 50);
    int f1 = DiffImage.imageToDocument(12, 620, 50);
    Assertions.assertEquals(0, f0);
    Assertions.assertEquals(1, f1);
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

      print(
          "[" + i + "] = [" + a + ", " + b + ")" + new String(sss));

      a = b;
    }
    int[] viewToDoc = new int[document];
    for (int i = 0; i < viewToDoc.length; i++)
      viewToDoc[i] = i;

    DiffImage.diffMap(model(document), height, viewToDoc);
  }

  static void testMin(int modelLength) {
    print("   " + modelLength + " -> " + 5);
    diffImageIterateTest(modelLength, 5);

    print("   " + modelLength + " -> " + 7);
    diffImageIterateTest(modelLength, 7);

    print("   " + modelLength + " -> " + modelLength);
    diffImageIterateTest(modelLength, modelLength);

    print("   " + modelLength + " -> " + 4);
    diffImageIterateTest(modelLength, 4);
  }

  private static void print(String s) {
    if (print) System.out.println(s);
  }

  static void testMag(int modelLength) {
    print("--------  model.length * 3 + 2  ----------");

    int height1 = modelLength * 3 + 2;
    print("   " + modelLength + " -> " + height1);
    diffImageIterateTest(modelLength, height1);

    print("--------  modelLength * 3 + 3  ----------");

    int height2 = modelLength * 3 + 3;
    print("   " + modelLength + " -> " + height2);
    diffImageIterateTest(modelLength, height2);

    print("--------  modelLength * 3 + 4  ----------");

    int height3 = modelLength * 3 + 4;
    print("   " + modelLength + " -> " + height3);
    diffImageIterateTest(modelLength, height3);
  }

  static void testBlur() {
    testBlurEdgeCases();

    XorShiftRandom r = new XorShiftRandom(34, 55);
    byte[] data = new byte[100];
    byte[] copy = new byte[data.length];
    for (int i = 0; i < 100; i++) {
      for (int j = 0; j < data.length; j++)
        data[j] = (byte) r.nextInt(4);
      System.arraycopy(data, 0, copy, 0, data.length);
      DiffImage.blurDiffImage(data);
      for (int j = 0; j < data.length; j++) {
        int pr = Math.max(j - 1, 0);
        int nx = Math.min(j + 1, data.length - 1);
        boolean ok = data[j] == 0
            ? copy[pr] == 0 && copy[nx] == 0 && copy[j] == 0
            : copy[j] != 0 || copy[pr] != 0 || copy[nx] != 0;
        Assertions.assertTrue(ok);
      }
    }
  }

  private static void testBlurEdgeCases() {
    byte[] data2_1 = new byte[] { 0, 1 };
    byte[] data2_2 = new byte[] { 2, 0 };
    byte[] data2_1e = new byte[] { 1, 1 };
    byte[] data2_2e = new byte[] { 2, 2 };

    DiffImage.blurDiffImage(data2_1);
    Assertions.assertArrayEquals(data2_1e, data2_1);
    DiffImage.blurDiffImage(data2_2);
    Assertions.assertArrayEquals(data2_2e, data2_2);

    byte[] data1_1 = new byte[] { 0 };
    byte[] data1_1e = new byte[] { 0 };

    DiffImage.blurDiffImage(data1_1);
    Assertions.assertArrayEquals(data1_1e, data1_1);

    data1_1[0] = 1; data1_1e[0] = 1;
    DiffImage.blurDiffImage(data1_1);
    Assertions.assertArrayEquals(data1_1e, data1_1);
  }
}
