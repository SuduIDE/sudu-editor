package org.sudu.experiments.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ColorTest {

  static String[] invalidColors() {
    return new String[]{
        "", "23452352525", "2345", "7C2B93",
    };
  }

  @Test
  void testHexColor() {
    for (String s : invalidColors()) {
      testHex(s, 0, 0, 0, 0);
    }

    testHex("#7C2B93", 124, 43, 147, 255);
    testHex("#dda0dd", 221, 160, 221, 255);
    testHex("#ffb800", 255, 184, 0, 255);
    testHex("#cccc00", 204, 204, 0, 255);
    testHex("#658f3d", 101, 143, 61, 255);
    testHex("#658f3d01", 101, 143, 61, 1);
    testHex("#658f3dF0", 101, 143, 61, 240);
    testHex("#3377dd", 51, 119, 221, 255);
    testHex("#37d", 51, 119, 221, 255);
  }

  static void testHex(String s, int r, int g, int b, int a) {
    Color color1 = new Color(s);
    Color color2 = new Color(r, g, b, a);
    Assertions.assertEquals(color1, color2);
  }

  @Test void testToHex() {
    for (int i = 0; i < 100; i++) {
      int r = (int) (Math.random() * 255);
      int g = (int) (Math.random() * 255);
      int b = (int) (Math.random() * 255);
      String webString = Color.Cvt.toHexString(r, g, b);
      V4f fromRGB = Color.Cvt.fromRGB(r, g, b);
      Color fromString = new Color(webString);
      Assertions.assertEquals(fromRGB, fromString.v4f);
    }
  }
}




