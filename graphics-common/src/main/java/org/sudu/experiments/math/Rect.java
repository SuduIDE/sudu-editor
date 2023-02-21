package org.sudu.experiments.math;

public class Rect {
  public static boolean isInside(V2i point, V2i pos, V2i size) {
    return
        point.x >= pos.x && point.x < (pos.x + size.x) &&
        point.y >= pos.y && point.y < (pos.y + size.y);
  }

  public static boolean isInside(V2i point, int posX, int posY, V2i size) {
    return
        point.x >= posX && point.x < (posX + size.x) &&
        point.y >= posY && point.y < (posY + size.y);
  }
}
