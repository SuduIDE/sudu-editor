package org.sudu.experiments.ui;

import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

public class WindowPaint {

  public static void drawBody(
      WglGraphics g,
      V2i size, V2i pos,
      V4f bodyColor, int border, V2i v2i
  ) {
    v2i.x = size.x - border - border;
    v2i.y = size.y - border - border;
    g.drawRect(pos.x + border, pos.y + border, v2i, bodyColor);
  }

  public static void drawInnerFrame(
      WglGraphics g,
      V2i size, V2i pos,
      V4f bgColor, int border, V2i v2i
  ) {
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
      WglGraphics g,
      V2i size, V2i pos, int xyOfs, int yOfs,
      int shadowSize, V4f shadowColor, V2i v2i
  ) {
    int x = pos.x + shadowSize - xyOfs;
    int y = pos.y + size.y + xyOfs;
    v2i.x = size.x + xyOfs + xyOfs;
    v2i.y = shadowSize;
    g.drawRect(x, y, v2i, shadowColor);
    g.drawRect(x, y, v2i, shadowColor);
    g.drawRect(x + shadowSize, y + shadowSize, v2i, shadowColor);

    x = pos.x + size.x + xyOfs;
    y = pos.y + shadowSize - yOfs - xyOfs;
    v2i.x = shadowSize;
    v2i.y = size.y - shadowSize + xyOfs + xyOfs + yOfs;
    g.drawRect(x, y, v2i, shadowColor);
    g.drawRect(x, y, v2i, shadowColor);
    g.drawRect(x + shadowSize, y + shadowSize, v2i, shadowColor);
  }
}
