package org.sudu.experiments;

import org.sudu.experiments.math.ArrayOp;


public class BmpWriter {

  static final int BITMAPFILEHEADER_SIZE = 14;
  static final int BITMAPINFOHEADER_SIZE = 40;
  static final int BI_RGB = 0;

  public static byte[] toBmp(int w, int h, int[] rgba, boolean swapRB, boolean vFlip) {
    byte[] bmp = new byte[rgba.length * 4 + BITMAPFILEHEADER_SIZE + BITMAPINFOHEADER_SIZE];
    int imageFileOffset = BITMAPFILEHEADER_SIZE + BITMAPINFOHEADER_SIZE;
    int pos = addBitmapInfoHeader(bmp, imageFileOffset);
    addBitmapInfoHeader(pos, bmp, 32, w, h);

    pos = imageFileOffset;

    int bytesPerLine = w * 4;
    for (int y = h - 1; y >= 0; pos += bytesPerLine, y--) {
      int srcPos = (vFlip ? h - 1 - y : y) * w;
      for (int x = 0; x < w; x++) {
        int value = rgba[srcPos + x];
        int x4 = x * 4;
        if (swapRB) {
          bmp[pos + x4] = (byte) ((value >> 16) & 0xFF);
          bmp[pos + x4 + 2] = (byte) (value & 0xFF);
        } else {
          bmp[pos + x4 + 2] = (byte) ((value >> 16) & 0xFF);
          bmp[pos + x4] = (byte) (value & 0xFF);
        }
        bmp[pos + x4 + 1] = (byte) ((value >> 8) & 0xFF);
        bmp[pos + x4 + 3] = (byte) ((value >> 24) & 0xFF);;
      }
    }
    return bmp;
  }

  public static byte[] toBmp(GL.ImageData image) {
    return toBmp(image, false);
  }

  public static byte[] toBmp(GL.ImageData image, boolean vFlip) {
    byte[] data = image.data;
    byte[] bmp = new byte[data.length + BITMAPFILEHEADER_SIZE + BITMAPINFOHEADER_SIZE];

    int imageFileOffset = BITMAPFILEHEADER_SIZE + BITMAPINFOHEADER_SIZE;
    int bitCount = image.format == GL.ImageData.Format.RGBA ? 32 : 8;
    int pos = addBitmapInfoHeader(bmp, imageFileOffset);
    int w = image.width, h = image.height;
    addBitmapInfoHeader(pos, bmp, bitCount, w, h);

    pos = imageFileOffset;
    int bytesPerLine = GL.ImageData.bytesPerLine(w, image.format);
    for (int y = h - 1; y >= 0; pos += bytesPerLine, y--) {
      int srcPos = (vFlip ? h - 1 - y : y) * bytesPerLine;
      for (int x = 0; x < w; x++) {
        int x4 = x * 4;
        bmp[pos + x4]     = data[srcPos + x4 + 2];
        bmp[pos + x4 + 1] = data[srcPos + x4 + 1];
        bmp[pos + x4 + 2] = data[srcPos + x4];
        bmp[pos + x4 + 3] = data[srcPos + x4 + 3];
      }
    }

    return bmp;
  }

  // BITMAPFILEHEADER
  private static int addBitmapInfoHeader(byte[] bmp, int imageFileOffset) {
    int pos = ArrayOp.writeInt16Le(bmp, 0, 0x4d42);
    pos = ArrayOp.writeInt32Le(bmp, pos, bmp.length);
    pos = ArrayOp.writeInt32Le(bmp, pos, 0);
    pos = ArrayOp.writeInt32Le(bmp, pos, imageFileOffset);
    return pos;
  }

  // BITMAPINFOHEADER
  private static void addBitmapInfoHeader(
      int pos, byte[] bmp, int bitCount, int width, int height
  ) {
    pos = ArrayOp.writeInt32Le(bmp, pos, BITMAPINFOHEADER_SIZE); // DWORD biSize;
    pos = ArrayOp.writeInt32Le(bmp, pos, width);           // LONG  biWidth;
    pos = ArrayOp.writeInt32Le(bmp, pos, height);          // LONG  biHeight;
    pos = ArrayOp.writeInt16Le(bmp, pos, 1);         // WORD  biPlanes;
    pos = ArrayOp.writeInt16Le(bmp, pos, bitCount);        // WORD  biBitCount;
    ArrayOp.writeInt32Le(bmp, pos, BI_RGB);                // DWORD biCompression;
  }
}
