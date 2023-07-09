package org.sudu.experiments.demo;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.GL;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.math.V2i;

public class TestHelper {
  static GL.Texture canvasTexture(WglGraphics g) {
    Canvas h = g.createCanvas(250, 64);
    h.setFont(Fonts.CourierNew, 25);
    h.setFillColor(187, 187, 187);
    String s = "|The sample text";
    h.drawText(s, 0, 24);
    h.drawText(s, .25f, 56);
    GL.Texture texture = g.createTexture();
    texture.setContent(h);
    h.dispose();
    return texture;
  }

  static void drawTiles(TextRect demoRect, GL.Texture texture, V2i size, WglGraphics graphics) {
    int y = 0;
    do {
      int x = 0;
      do {
        demoRect.drawText(graphics, texture, x, y, 0.5f);
        x += texture.width();
      } while (x < size.x);
      y += texture.height();
    } while (y < size.y);
  }
}
