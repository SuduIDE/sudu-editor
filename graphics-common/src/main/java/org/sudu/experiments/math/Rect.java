package org.sudu.experiments.math;

public class Rect {
  public static boolean isInside(V2i point, V2i pos, V2i size) {
    return
        pos.x <= point.x && point.x < (pos.x + size.x) &&
        pos.y <= point.y && point.y < (pos.y + size.y);
  }

  public static boolean isInside(V2i point, int posX, int posY, V2i size) {
    return
        posX <= point.x && point.x < (posX + size.x) &&
        posY <= point.y && point.y < (posY + size.y);
  }

  public static boolean isInside(V2i point, int posX, int posY, int sizeX, int sizeY) {
    return
        posX <= point.x && point.x < (posX + sizeX) &&
        posY <= point.y && point.y < (posY + sizeY);
  }
}
