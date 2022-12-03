package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

class CodeLineRenderer implements Disposable {
  static boolean dumpMeasure;
  static boolean bw;
  static boolean useTop = false;

  CodeLine line;
  GL.Texture lineTexture;

  public boolean needsUpdate(CodeLine content) {
    return line != content || line.contentDirty;
  }

  public void updateTexture(CodeLine content, Canvas renderingCanvas, FontDesk[] fonts, WglGraphics g, int lineHeight) {
    line = content;

    content.measure(g.mCanvas, fonts);

    if (dumpMeasure) {
      Debug.consoleInfo("fMeasure", content.fMeasure);
      dumpMeasure = false;
    }

    renderingCanvas.clear();

//    int texWidth = renderingCanvas.width();

    int yPos = useTop ? topBase(fonts[0], lineHeight) : baselineShift(fonts[0], lineHeight);
    renderingCanvas.setTopMode(useTop);

    CodeElement[] words = content.elements;
    float[] fMeasure = content.fMeasure;
    for (int i = 0, l = words.length; i < l; i++) {
      CodeElement entry = words[i];
      float x = i == 0 ? 0 : fMeasure[i - 1];
      renderingCanvas.setFont(fonts[entry.fontIndex]);
      renderingCanvas.drawText(entry.s, x, yPos);
    }

    if (lineTexture == null) {
      lineTexture = g.createTexture();
    }
    lineTexture.setContent(renderingCanvas);
    line.contentDirty = false;
  }

  static int topBase(FontDesk font, int lineHeight) {
    return (lineHeight - font.lineHeight()) / 2;
  }

  static int baselineShift(FontDesk font, int lineHeight) {
    return topBase(font, lineHeight) + font.iAscent;
  }

  public void draw(int yPosition, int dx, WglGraphics g, V4f region, V2i size, float contrast) {
    region.y = 0;
    region.w = size.y = lineTexture.height();
    // todo: right limit is different, rework later
    int limit = lineTexture.width();
    int xPos = 0;
    int[] iMeasure = line.iMeasure;
    CodeElement[] words = line.elements;
    for (int i = 0; i < words.length; i++) {
      CodeElement e = words[i];
      int pxLen = iMeasure[i];
      if (pxLen > limit) pxLen = limit;
      int drawWidth = pxLen - xPos;
      region.x = xPos;
      region.z = drawWidth;
      size.x = drawWidth;
      g.drawText(xPos + dx, yPosition, size,
          region, lineTexture,
          bw ? Colors.white : e.colorF.v4f,
          bw ? Colors.black : e.colorB(Colors.editBgColor).v4f,
          bw ? 0 : contrast);
      if (pxLen == limit) break;
      xPos = pxLen;
    }
  }

  @Override
  public void dispose() {
    line = null;
    lineTexture = Disposable.assign(lineTexture, null);
  }
}
