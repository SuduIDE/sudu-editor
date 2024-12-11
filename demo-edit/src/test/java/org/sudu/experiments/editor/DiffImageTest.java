package org.sudu.experiments.editor;

import org.junit.jupiter.api.Assertions;
import org.sudu.experiments.diff.LineDiff;
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

      print(
          "[" + i + "] = [" + a + ", " + b + ")" + new String(sss));

      a = b;
    }

    DiffImage.diffImage(model(document), height);
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
    testBlur2bytes();

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

  private static void testBlur2bytes() {
    byte[] data2_1 = new byte[] { 0, 1 };
    byte[] data2_2 = new byte[] { 2, 0 };
    byte[] data2_1e = new byte[] { 1, 1 };
    byte[] data2_2e = new byte[] { 2, 2 };

    DiffImage.blurDiffImage(data2_1);
    Assertions.assertArrayEquals(data2_1e, data2_1);
    DiffImage.blurDiffImage(data2_2);
    Assertions.assertArrayEquals(data2_2e, data2_2);
  }
}
