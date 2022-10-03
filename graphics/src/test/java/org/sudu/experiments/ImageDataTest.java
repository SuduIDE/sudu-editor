package org.sudu.experiments;


import org.sudu.experiments.GL.ImageData.Format;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ImageDataTest {
  @Test
  public void testImageData() {
    Assertions.assertEquals(GL.ImageData.align4(-1), 0);
    Assertions.assertEquals(GL.ImageData.align4(0), 0);
    Assertions.assertEquals(GL.ImageData.align4(1), 4);
    Assertions.assertEquals(GL.ImageData.align4(2), 4);
    Assertions.assertEquals(GL.ImageData.align4(3), 4);
    Assertions.assertEquals(GL.ImageData.align4(4), 4);
    Assertions.assertEquals(GL.ImageData.align4(5), 8);

    GL.ImageData dataRGBA = new GL.ImageData(10, 10);
    Assertions.assertEquals(dataRGBA.data.length, 10 * 10 * 4);

    GL.ImageData dataA8_10 = new GL.ImageData(10, 10, Format.GRAYSCALE);
    Assertions.assertEquals(dataA8_10.data.length, 12 * 10);

    GL.ImageData dataA8_12 = new GL.ImageData(12, 10, Format.GRAYSCALE);
    Assertions.assertEquals(dataA8_12.data.length, 12 * 10);
  }

}