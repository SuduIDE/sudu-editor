package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

class CodeLineRenderer implements Disposable {
  static boolean dumpMeasure = 1<0;
  static boolean print = 1<0;
  static boolean bw = 1<0;

  static boolean individualWords = false;
  CodeLine line;
  GL.Texture lineTexture;

  public boolean needsUpdate(CodeLine content) {
    return line != content || line.contentDirty;
  }

  public void updateTexture(CodeLine content, Canvas renderingCanvas, FontDesk font, WglGraphics g, int lineHeight) {
    line = content;

    String accum = content.measure(g.mCanvas, individualWords);

    if (dumpMeasure) {
      Debug.consoleInfo("fMeasure", content.fMeasure);
      Debug.consoleInfo("iMeasure", content.iMeasure);
      dumpMeasure = false;
    }

    renderingCanvas.clear();

//    int texWidth = renderingCanvas.width();

    int baseLineBase = lineHeight - font.descent;
    int baseline = baseLineBase - (lineHeight - font.ascent - font.descent) / 2;

    if (print) {
      print = false;
    }

    if (individualWords) {
      CodeElement[] words = content.elements;
      int[] iMeasure = content.iMeasure;
      for (int i = 0, x = 0, l = words.length; i < l; i++) {
        CodeElement entry = words[i];
        renderingCanvas.drawText(entry.s, x, baseline);
        x += iMeasure[i];
      }
    } else {
      renderingCanvas.drawText(accum, 0, baseline);
    }
    if (lineTexture == null) {
      lineTexture = g.createTexture();
    }
    lineTexture.setContent(renderingCanvas);
    line.contentDirty = false;
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
      int pxLen = individualWords ? xPos + iMeasure[i] : iMeasure[i];
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
