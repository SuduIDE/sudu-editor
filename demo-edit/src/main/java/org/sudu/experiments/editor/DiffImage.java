package org.sudu.experiments.editor;

import org.sudu.experiments.GL;
import org.sudu.experiments.diff.LineDiff;

class DiffImage {
  static void diffImage(int doc, LineDiff[] model, GL.ImageData image) {
    int view = image.height, v2 = view / 2;
    int lA = 0;
    for (int i = 0; i < view; i++) {
      int lB = (doc * i + v2 + doc) / view;
      System.out.println("[" + i + "] = ["
          + lA + ", " + lB + "]");
    }
  }

  public static void main(String[] args) {
    GL.ImageData view = new GL.ImageData(1, 45);
    int doc = 10;
    LineDiff[] model = new LineDiff[5];

//    dump(10, 45);
//    dump(177, 55);
  }

  static void dump(int doc, int view) {
    System.out.println("doc = " + doc + ", view = " + view);
    int v2 = view / 2;
    for (int i = 0; i < view; i++) {
      int a = (doc * i + v2) / view;
      int b = (doc * i + v2 + doc) / view;
      System.out.println("[" + i + "] = [" + a + ", " + b + "]");
    }
  }
}
