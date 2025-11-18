package org.sudu.experiments;

public interface BufferedImageCvt {
  static void glPixelsToBufferedImage(int[] bits, int w, int h) {
    if (bits.length != w * h)
      throw new RuntimeException("Array lengths do not match");

    for (int y = 0, e = h / 2; y < e; y++) {
      int pos0 = y * w, pos1 = (h - y - 1) * w;

      for (int x = 0; x < w; x++) {
        int value0 = bits[pos0 + x];
        int value1 = bits[pos1 + x];
        bits[pos0 + x] = (value1 & 0xFF00FF00) |
            ((value1 << 16) & 0xFF0000) |
            ((value1 >> 16) & 0xFF);
        bits[pos1 + x] = (value0 & 0xFF00FF00) |
            ((value0 << 16) & 0xFF0000) |
            ((value0 >> 16) & 0xFF);
      }
    }

    if ((h & 1) == 1) {
      int pos = (h / 2) * w;
      for (int x = 0; x < w; x++) {
        int value = bits[pos + x];
        bits[pos + x] = (value & 0xFF00FF00) |
            ((value << 16) & 0xFF0000) |
            ((value >> 16) & 0xFF);
      }
    }
  }

}
