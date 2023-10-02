package org.sudu.experiments.editor;

import org.sudu.experiments.GL;

public class TGen {
  public static GL.ImageData chess(int w, int h) {
    GL.ImageData image = new GL.ImageData(w, h);

    byte[] data = image.data;
    for (int y = 0, p = 0; y < h; y++) {
      for (int x = 0; x < w; x++) {
        byte v = (byte) (((x ^ y) & 1) == 0 ? 255 : 0);
        data[p++] = v;
        data[p++] = v;
        data[p++] = v;
        data[p++] = (byte) 255;
      }
    }
    return image;

  }
}
