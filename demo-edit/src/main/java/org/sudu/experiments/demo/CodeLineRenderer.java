package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.*;

class CodeLineRenderer implements Disposable {
  static boolean dumpMeasure;
  static boolean bw;
  static boolean useTop = false;
  static final int TEXTURE_WIDTH = EditorConst.TEXTURE_WIDTH;
  private int xOffset = 3;

  CodeLine line;
  List<GL.Texture> lineTextures = new ArrayList<>();
  int numOfTextures;
  int curFromTexture = 0;

  public void updateTexture(
      CodeLine content,
      Canvas renderingCanvas,
      FontDesk[] fonts,
      WglGraphics g,
      int lineHeight,
      int editorWidth,
      int horScrollPos
  ) {
    boolean lineChanged = line != content || line.contentDirty;
    if (lineChanged) {
      line = content;
      content.measure(g.mCanvas, fonts);
    }
    int newNumOfTextures = countNumOfTextures(editorWidth);
    boolean needsResize = newNumOfTextures > numOfTextures;
    if (needsResize) numOfTextures = newNumOfTextures;

    if (lineChanged || needsResize) {

      if (dumpMeasure) {
        Debug.consoleInfo("fMeasure", content.fMeasure);
        dumpMeasure = false;
      }

      renderingCanvas.setTopMode(useTop);
      initNTextures(renderingCanvas, g, fonts, lineHeight, horScrollPos);

      line.contentDirty = false;
    }
    updateTextureOnScroll(renderingCanvas, fonts, lineHeight, horScrollPos);
  }

  static int topBase(FontDesk font, int lineHeight) {
    return (lineHeight - font.lineHeight()) / 2;
  }

  static int baselineShift(FontDesk font, int lineHeight) {
    return topBase(font, lineHeight) + font.iAscent;
  }

  public void setXOffset(int xOffset) {
    this.xOffset = xOffset;
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

    float[] fMeasure = line.fMeasure;
    int offset = numberOfTexture * TEXTURE_WIDTH;

    int curWord = getWordIndex(offset);
    if (curWord >= line.elements.length) return;

    float wordMeasure = curWord == 0 ? 0 : fMeasure[curWord - 1];
    float x = wordMeasure - offset + xOffset;

    for (; curWord < line.elements.length; curWord++) {
      CodeElement entry = line.get(curWord);
      int yPos = useTop ? topBase(fonts[entry.fontIndex], lineHeight) : baselineShift(fonts[entry.fontIndex], lineHeight);
      renderingCanvas.setFont(fonts[entry.fontIndex]);
      renderingCanvas.drawText(entry.s, x, yPos);
      x = fMeasure[curWord] - offset + xOffset;
      if (x > TEXTURE_WIDTH) break;
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
      int horScrollPos,
      EditorColorScheme colors,
      V2i selectedSegment,
      CodeElement def,
      List<CodeElement> usages
  ) {
    if (lineTextures.isEmpty()) return;
    if (numOfTextures == 0) return;
    if (horScrollPos > line.lineMeasure()) return;

    int[] iMeasure = line.iMeasure;
    CodeElement[] words = line.elements;

    int curTexture = horScrollPos / TEXTURE_WIDTH;
    int curWord = getWordIndex(horScrollPos);
    if (curWord > iMeasure.length) return;

    int texturePos = horScrollPos;
    int xPos = -xOffset;

    for (int i = curWord; i < words.length; i++) {
      boolean isLastWord = i == words.length - 1;
      if (xPos >= editorWidth) break;

      GL.Texture texture = lineTextures.get(curTexture % numOfTextures);
      CodeElement e = words[i];
      int pxLen = iMeasure[i] + xOffset;

      boolean drawOnCurTexture = pxLen - curTexture * TEXTURE_WIDTH <= TEXTURE_WIDTH;

      int drawWidth = Math.min((curTexture + 1) * TEXTURE_WIDTH, pxLen) - texturePos;
      if (drawOnCurTexture && isLastWord) drawWidth += xOffset;
      if (xPos + drawWidth > editorWidth) drawWidth = editorWidth - xPos;

      boolean isNotSelected = selectedSegment == null;
      boolean isFullUnselected = isNotSelected || isFullUnselected(selectedSegment, texturePos, drawWidth, isLastWord ? 2 * xOffset : xOffset);
      boolean isFullSelected = !isNotSelected && isFullSelected(selectedSegment, texturePos, drawWidth, isLastWord ? 2 * xOffset : xOffset);

      Color elemBgColor = null;
      if (e == def) elemBgColor = colors.definitionBgColor;
      if (usages.contains(e)) elemBgColor = colors.usageBgColor;

      if (isFullSelected || isFullUnselected) {
        region.set(texturePos - curTexture * TEXTURE_WIDTH, 0, drawWidth, lineHeight);
        size.set(drawWidth, lineHeight);
        drawWord(g, xPos + dx, yPosition, size, region, e, texture, contrast, colors, isFullSelected, elemBgColor);
      } else {
        selectedSegment.y = Math.min(selectedSegment.y, line.lineMeasure());
        int pre;
        int post;

        if (texturePos >= selectedSegment.x) {
          pre = drawWidth;
        } else {
          pre = Math.min(pxLen, (curTexture + 1) * TEXTURE_WIDTH) - selectedSegment.x - (isLastWord ? 0 : xOffset);
        }
        if (texturePos + drawWidth <= selectedSegment.y + (isLastWord ? 2 * xOffset : xOffset)) {
          post = 0;
        } else {
          post = Math.min(pxLen, (curTexture + 1) * TEXTURE_WIDTH) - selectedSegment.y - (isLastWord ? 0 : xOffset);
        }

        int regionX = texturePos - curTexture * TEXTURE_WIDTH;
        drawSelected(g, xPos + dx, yPosition,
            region, size, contrast,
            lineHeight, colors,
            texture, e,
            drawWidth, pre, post, regionX, elemBgColor);
      }

      texturePos += drawWidth;
      xPos += drawWidth;

      if (!drawOnCurTexture) {
        curTexture++;
        i--;
      }
    }
  }

  private void drawSelected(
      WglGraphics g, int xPos, int yPosition,
      V4f region, V2i size, float contrast,
      int lineHeight, EditorColorScheme colors,
      GL.Texture texture, CodeElement e,
      int drawWidth, int pre, int post, int regionX,
      Color elemBgColor
  ) {
    region.set(regionX, 0, drawWidth - pre, lineHeight);
    size.set(drawWidth - pre, lineHeight);
    drawWord(g, xPos, yPosition, size, region, e, texture, contrast, colors, false, elemBgColor);

    region.set(regionX + drawWidth - post, 0, post, lineHeight);
    size.set(post, lineHeight);
    drawWord(g, xPos + drawWidth - post, yPosition, size, region, e, texture, contrast, colors, false, elemBgColor);

    region.set(regionX + drawWidth - pre, 0, pre - post, lineHeight);
    size.set(pre - post, lineHeight);
    drawWord(g, xPos + drawWidth - pre, yPosition, size, region, e, texture, contrast, colors, true, elemBgColor);
  }

  private void drawWord(
      WglGraphics g, int xPos, int yPos, V2i size, V4f region,
      CodeElement e, GL.Texture texture,
      float contrast, EditorColorScheme colors, boolean isSelected, Color elemBgColor
  ) {
    if (size.x == 0 || size.y == 0) return;
    if (region.w == 0 || region.z == 0) return;

    CodeElementColor c = colors.codeColors[e.color];
    Color bgColor = isSelected ? colors.selectionBgColor : Objects.requireNonNullElse(elemBgColor, colors.bgColor(c.colorB));
    g.drawText(xPos, yPos, size,
        region, texture, c.colorF, bgColor,
        bw ? 0 : contrast);
  }

  private int getWordIndex(int horScrollPos) {
    int curWord = Arrays.binarySearch(line.fMeasure, 0, line.elements.length, horScrollPos);
    if (curWord < 0) curWord = -curWord - 1;
    return curWord;
  }

  private boolean isFullUnselected(V2i sel, int texturePos, int drawWidth, int offset) {
    return sel.x >= sel.y
        || texturePos > sel.y
        || texturePos + drawWidth <= sel.x + offset;
  }

  private boolean isFullSelected(V2i sel, int texturePos, int drawWidth, int offset) {
    return texturePos >= sel.x
        && texturePos + drawWidth <= sel.y + offset;
  }

  private int countNumOfTextures(int editorWidth) {
    if (line == null) {
      return 0;
    } else if (line.lineMeasure() >= editorWidth) {
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

  public void drawDebug(int yPosition, int dx, int lineH, WglGraphics g, V4f color, V4f bgColor) {
    for (int i = 0; i < lineTextures.size(); i++) {
      var texture = lineTextures.get(i);
      g.drawText(dx, yPosition + (lineH + 5) * i,
          new V2i(TEXTURE_WIDTH, lineH),
          new V4f(0, 0, TEXTURE_WIDTH, lineH),
          texture, color, bgColor, 1f);
    }
  }

  public void drawTail(
      WglGraphics g,
      int dx, int yPos,
      int lineHeight,
      V2i size,
      int editorHScrollPos,
      int editorWidth,
      Color editBgColor
  ) {
    int lineMeasure = line.lineMeasure();
    if (lineMeasure != 0) lineMeasure += xOffset;
    if (lineMeasure == 0) lineMeasure -= xOffset;
    int lineEnd = Math.max(-xOffset, lineMeasure - editorHScrollPos);
    if (lineEnd >= editorWidth) return;
    int recWidth = editorWidth - lineEnd;
    size.set(recWidth, lineHeight);
    g.drawRect(dx + lineEnd, yPos, size, editBgColor);
  }

  static CodeLineRenderer[] reallocRenderLines(
      int newSize,
      CodeLineRenderer[] lines, int first, int last,
      Document document
  ) {
    CodeLineRenderer[] r = new CodeLineRenderer[newSize];
    int pSrc = 0;
    if (lines.length > 0) for (int i = first; i <= last; i++) {
      CodeLine docLine = document.line(i);
      int newIndex = i % r.length;
      int oldIndex = i % lines.length;
      CodeLineRenderer oldLine = lines[oldIndex];
      if (oldLine.line == docLine && r[newIndex] == null) {
        r[newIndex] = oldLine;
        lines[oldIndex] = null;
      }
    }
    for (int i = 0; i < r.length; i++) {
      if (r[i] == null) {
        CodeLineRenderer v = pSrc < lines.length ? lines[pSrc++] : null;
        while (pSrc < lines.length && v == null) v = lines[pSrc++];
        if (v != null) { r[i] = v; lines[pSrc-1] = null; }
        else r[i] = new CodeLineRenderer();
      }
    }
    for (; pSrc < lines.length; pSrc++) {
      CodeLineRenderer v = lines[pSrc];
      if (v != null) v.dispose();
    }
    return r;
  }
}
