package org.sudu.experiments;

import org.sudu.experiments.math.ArrayOp;


public class BmpWriter {

  static final int BITMAPFILEHEADER_SIZE = 14;
  static final int BITMAPINFOHEADER_SIZE = 40;
  static final int BI_RGB = 0;

  public static byte[] toBmp(GL.ImageData image) {
    byte[] data = image.data;
    byte[] bmp = new byte[data.length + BITMAPFILEHEADER_SIZE + BITMAPINFOHEADER_SIZE];

    // BITMAPFILEHEADER
    int pos = ArrayOp.writeInt16Le(bmp, 0, 0x4d42);
    int imageFileOffset = BITMAPFILEHEADER_SIZE + BITMAPINFOHEADER_SIZE;
    pos = ArrayOp.writeInt32Le(bmp, pos, bmp.length);
    pos = ArrayOp.writeInt32Le(bmp, pos, 0);
    pos = ArrayOp.writeInt32Le(bmp, pos, imageFileOffset);

    // BITMAPINFOHEADER

    int bitCount = image.format == GL.ImageData.Format.RGBA ? 32 : 8;
    pos = ArrayOp.writeInt32Le(bmp, pos, BITMAPINFOHEADER_SIZE); // DWORD biSize;
    pos = ArrayOp.writeInt32Le(bmp, pos, image.width);           // LONG  biWidth;
    pos = ArrayOp.writeInt32Le(bmp, pos, image.height);          // LONG  biHeight;
    pos = ArrayOp.writeInt16Le(bmp, pos, 1);                // WORD  biPlanes;
    pos = ArrayOp.writeInt16Le(bmp, pos, bitCount);              // WORD  biBitCount;
          ArrayOp.writeInt32Le(bmp, pos, BI_RGB);                // DWORD biCompression;

    pos = imageFileOffset;
    int bytesPerLine = GL.ImageData.bytesPerLine(image.width, image.format);
    for (int y = image.height - 1; y >= 0; pos += bytesPerLine, y--) {
      int srcPos = y * bytesPerLine;
      for (int x = 0; x < image.width; x++) {
        int x4 = x * 4;
        bmp[pos + x4]     = data[srcPos + x4 + 2];
        bmp[pos + x4 + 1] = data[srcPos + x4 + 1];
        bmp[pos + x4 + 2] = data[srcPos + x4];
        bmp[pos + x4 + 3] = data[srcPos + x4 + 3];
      }
    }

    return bmp;
  }
}
