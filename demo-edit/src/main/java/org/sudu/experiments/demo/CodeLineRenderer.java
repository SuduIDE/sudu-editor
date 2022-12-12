package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CodeLineRenderer implements Disposable {
  static boolean dumpMeasure;
  static boolean bw;
  static boolean useTop = false;
  static final int TEXTURE_WIDTH = EditorConst.TEXTURE_WIDTH;

  CodeLine line;
  List<GL.Texture> lineTextures = new ArrayList<>();
  int numOfTextures;
  int curFromTexture = 0;

  public boolean needsUpdate(CodeLine content) {
    return line != content || line.contentDirty;
  }

  public void updateTexture(
      CodeLine content,
      Canvas renderingCanvas,
      FontDesk[] fonts,
      WglGraphics g,
      int lineHeight,
      int editorWidth,
      int horScrollPos
  ) {
    line = content;

    content.measure(g.mCanvas, fonts);

    if (dumpMeasure) {
      Debug.consoleInfo("fMeasure", content.fMeasure);
      dumpMeasure = false;
    }

    renderingCanvas.clear();



    renderingCanvas.setTopMode(useTop);

    numOfTextures = countNumOfTextures(editorWidth);
    initNTextures(renderingCanvas, g, fonts, lineHeight, horScrollPos);

    line.contentDirty = false;
  }

  public void resize(
    CodeLine content,
    Canvas renderingCanvas,
    FontDesk[] fonts,
    WglGraphics g,
    int lineHeight,
    int editorWidth,
    int horScrollPos
  ) {
    if (line == null) {
      line = content;
      line.measure(g.mCanvas, fonts);
    }

    renderingCanvas.setTopMode(useTop);

    int newNumOfTextures = countNumOfTextures(editorWidth);
    if (newNumOfTextures != 0 && numOfTextures >= newNumOfTextures) return;
    numOfTextures = newNumOfTextures;

    initNTextures(renderingCanvas, g, fonts, lineHeight, horScrollPos);
  }

  static int topBase(FontDesk font, int lineHeight) {
    return (lineHeight - font.lineHeight()) / 2;
  }

  static int baselineShift(FontDesk font, int lineHeight) {
    return topBase(font, lineHeight) + font.iAscent;
  }

  public void updateTextureOnScroll(Canvas renderingCanvas, FontDesk[] fonts, int lineHeight, int horScrollPos) {
    if (lineTextures.isEmpty()) return;
    if (horScrollPos > line.lineMeasure()) return;

    int fromTexture = horScrollPos / TEXTURE_WIDTH;
    if (fromTexture == curFromTexture) return;

    if (Math.abs(fromTexture - curFromTexture) >= numOfTextures) {
      for (int i = 0; i < numOfTextures; i++) {
        drawOnTexture(renderingCanvas, lineHeight, fonts, fromTexture + i);
      }
      curFromTexture = fromTexture;
      return;
    }

    for (; curFromTexture < fromTexture; curFromTexture++) {
      drawOnTexture(renderingCanvas, lineHeight, fonts, curFromTexture + numOfTextures);
    }
    for (; curFromTexture > fromTexture; curFromTexture--) {
      drawOnTexture(renderingCanvas, lineHeight, fonts, (curFromTexture - 1));
    }
  }

  public void drawOnTexture(Canvas renderingCanvas, int lineHeight, FontDesk[] fonts, int numberOfTexture) {
    renderingCanvas.clear();

    int[] iMeasure = line.iMeasure;
    int offset = numberOfTexture * TEXTURE_WIDTH;

    int curWord = getWordIndex(offset);
    if (curWord >= line.elements.length) return;

    int wordMeasure = curWord == 0 ? 0 : iMeasure[curWord - 1];
    int x = wordMeasure - offset;

    for (; curWord < line.elements.length; curWord++) {
      CodeElement entry = line.get(curWord);
      int yPos = useTop ? topBase(fonts[entry.fontIndex], lineHeight) : baselineShift(fonts[0], lineHeight);
      renderingCanvas.setFont(fonts[entry.fontIndex]);
      renderingCanvas.drawText(entry.s, x, yPos);
      if (iMeasure[curWord] - offset <= TEXTURE_WIDTH) {
        x = iMeasure[curWord] - offset;
      } else break;
    }
    lineTextures.get(numberOfTexture % numOfTextures).setContent(renderingCanvas);
  }

  public void draw(
    int yPosition, int dx,
    WglGraphics g,
    V4f region, V2i size,
    float contrast,
    int editorWidth,
    int lineHeight,
    int horScrollPos
  ) {
    if (lineTextures.isEmpty()) return;
    if (horScrollPos > line.lineMeasure()) return;

    float[] iMeasure = line.fMeasure;
    CodeElement[] words = line.elements;

    int curTexture = horScrollPos / TEXTURE_WIDTH;
    int curWord = getWordIndex(horScrollPos);
    if (curWord > iMeasure.length) return;

    int texturePos = horScrollPos;
    int xPos = 0;

    for (int i = curWord; i < words.length; i++) {
      //if (curTexture >= numOfTextures) return;
      if (xPos >= editorWidth) return;

      GL.Texture texture = lineTextures.get(curTexture % numOfTextures);
      CodeElement e = words[i];
      int pxLen = (int) iMeasure[i];

      if (pxLen - curTexture * TEXTURE_WIDTH <= TEXTURE_WIDTH) {
        int drawWidth = pxLen - texturePos;

        region.set(texturePos - curTexture * TEXTURE_WIDTH, 0, drawWidth, lineHeight);
        size.set(drawWidth, lineHeight);

        drawWord(g, xPos + dx, yPosition, size, region, e, texture, contrast);

        texturePos = pxLen;
        xPos += drawWidth;
      } else {
        int drawWidth = (curTexture + 1) * TEXTURE_WIDTH - texturePos;

        region.set(texturePos - curTexture * TEXTURE_WIDTH, 0, drawWidth, lineHeight);
        size.set(drawWidth, lineHeight);

        drawWord(g, xPos + dx, yPosition, size, region, e, texture, contrast);

        texturePos += drawWidth;
        xPos += drawWidth;
        curTexture++;
        i--;
      }
    }
  }

  public int getWordIndex(int horScrollPos) {
    int curWord = Arrays.binarySearch(line.iMeasure, horScrollPos);
    if (curWord < 0) curWord = -curWord - 1;
    return curWord;
  }

  private void drawWord(WglGraphics g, int xPos, int yPos, V2i size, V4f region, CodeElement e, GL.Texture texture, float contrast) {
    g.drawText(xPos, yPos, size,
      region, texture,
      bw ? Colors.white : e.colorF.v4f,
      bw ? Colors.black : e.colorB(Colors.editBgColor).v4f,
      bw ? 0 : contrast);
  }

  private int countNumOfTextures(int editorWidth) {
    if (line.lineMeasure() >= editorWidth) {
      return Numbers.iDivRoundUp(editorWidth, TEXTURE_WIDTH) + 1;
    } else {
      return Numbers.iDivRoundUp(line.lineMeasure(), TEXTURE_WIDTH);
    }
  }

  private void initNTextures(Canvas renderingCanvas, WglGraphics g, FontDesk[] fonts, int lineHeight, int horScrollPos) {
    curFromTexture = horScrollPos / TEXTURE_WIDTH;

    while (lineTextures.size() < numOfTextures) {
      lineTextures.add(g.createTexture());
    }

    for (int i = 0; i < numOfTextures; i++) {
      drawOnTexture(renderingCanvas, lineHeight, fonts, curFromTexture + i);
    }
  }

  @Override
  public void dispose() {
    line = null;
    lineTextures.forEach(GL.Texture::dispose);
    lineTextures.clear();
  }

  public void debug() {
    Debug.consoleInfo("\tcurTexture: " + curFromTexture);
    Debug.consoleInfo("\tnumOfTexture: " + numOfTextures);
    Debug.consoleInfo("\tlineTextures.size(): " + lineTextures.size());
  }

  public void drawDebug(int yPosition, int dx, WglGraphics g) {
    int testHeight = 20;
    for (int i = 0; i < lineTextures.size(); i++) {
      var texture = lineTextures.get(i);
      g.drawText(dx, yPosition + (testHeight + 5) * i,
        new V2i(TEXTURE_WIDTH, testHeight),
        new V4f(0, 0, TEXTURE_WIDTH, testHeight),
        texture, Colors.comma.v4f, Colors.defaultText.v4f, 1f);
    }
  }
}
