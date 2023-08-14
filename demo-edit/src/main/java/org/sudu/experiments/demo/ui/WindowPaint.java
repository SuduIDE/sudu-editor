package org.sudu.experiments.demo.ui;

import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

public class WindowPaint {

  public static void drawBody(WglGraphics g, V2i size, V2i pos, V4f bodyColor, int border, V2i v2i) {
    v2i.x = size.x - border - border;
    v2i.y = size.y - border - border;
    g.drawRect(pos.x + border, pos.y + border, v2i, bodyColor);
  }

  public static void drawInnerFrame(WglGraphics g, V2i size, V2i pos, V4f bgColor, int border, V2i v2i) {
    v2i.x = size.x;
    v2i.y = border;
    g.drawRect(pos.x, pos.y, v2i, bgColor);
    g.drawRect(pos.x, pos.y + size.y - border, v2i, bgColor);

    v2i.x = border;
    v2i.y = size.y - border - border;
    g.drawRect(pos.x, pos.y + border, v2i, bgColor);
    g.drawRect(pos.x + size.x - border, pos.y + border, v2i, bgColor);
  }

  public static void drawShadow(
      WglGraphics g, V2i size, V2i pos, int ofs,
      int shadowSize, V4f shadowColor, V2i v2i
  ) {
    int x = pos.x + shadowSize;
    int y = pos.y + size.y + ofs;
    v2i.x = size.x + ofs;
    v2i.y = shadowSize;
    g.drawRect(x, y, v2i, shadowColor);
    g.drawRect(x, y, v2i, shadowColor);
    g.drawRect(x + shadowSize, y + shadowSize, v2i, shadowColor);

    x = pos.x + size.x + ofs;
    y = pos.y + shadowSize;
    v2i.x = shadowSize;
    v2i.y = size.y - shadowSize + ofs;
    g.drawRect(x, y, v2i, shadowColor);
    g.drawRect(x, y, v2i, shadowColor);
    g.drawRect(x + shadowSize, y + shadowSize, v2i, shadowColor);
  }
}
