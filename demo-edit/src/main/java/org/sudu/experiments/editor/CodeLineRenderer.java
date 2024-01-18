package org.sudu.experiments.editor;

import org.sudu.experiments.*;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.ui.colors.CodeElementColor;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.ui.colors.IdeaCodeColors;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.math.*;

import java.util.Arrays;
import java.util.List;

import static org.sudu.experiments.editor.UnderlineConstants.*;

class CodeLineRenderer implements Disposable {
  static boolean dumpMeasure;
  static boolean useTop = false;
  static final int TEXTURE_WIDTH = EditorConst.TEXTURE_WIDTH;
  static final int initialOffset = 3;
  private int xOffset = initialOffset;

  final Context context;
  CodeLine line;
  GL.Texture[] textures;
  int curFromTexture = 0;

  CodeLineRenderer(Context context) {
    this.context = context;
    this.textures = context.textures0;
  }

  void updateTexture(
      CodeLine content,
      Canvas renderingCanvas,
      WglGraphics g,
      int lineHeight,
      int editorWidth,
      int horScrollPos
  ) {
    boolean lineChanged = line != content || line.contentDirty;
    if (lineChanged) {
      line = content;
      content.measure(g.mCanvas, context.fonts);
    }
    int newSize = countNumOfTextures(editorWidth);
    int oldSize = textures.length;
    boolean needsResize = newSize > oldSize;
    if (needsResize) allocateTextures(g, newSize);

    if (lineChanged || needsResize) {

      if (dumpMeasure) {
        Debug.consoleInfo("fMeasure", content.fMeasure);
        dumpMeasure = false;
      }

      renderingCanvas.setTopMode(useTop);
      curFromTexture = horScrollPos / TEXTURE_WIDTH;

      for (int i = 0; i < newSize; i++) {
        drawOnTexture(renderingCanvas, lineHeight, curFromTexture + i);
      }

      line.contentDirty = false;
    }
    updateTextureOnScroll(renderingCanvas, lineHeight, horScrollPos);
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

  void updateTextureOnScroll(Canvas renderingCanvas, int lineHeight, int horScrollPos) {
    int length = textures.length;
    if (length == 0) return;
    if (horScrollPos > line.lineMeasure()) return;

    int fromTexture = horScrollPos / TEXTURE_WIDTH;
    if (fromTexture == curFromTexture) return;

    if (Math.abs(fromTexture - curFromTexture) >= length) {
      for (int i = 0; i < length; i++) {
        drawOnTexture(renderingCanvas, lineHeight, fromTexture + i);
      }
      curFromTexture = fromTexture;
      return;
    }

    for (; curFromTexture < fromTexture; curFromTexture++) {
      drawOnTexture(renderingCanvas, lineHeight, curFromTexture + length);
    }
    for (; curFromTexture > fromTexture; curFromTexture--) {
      drawOnTexture(renderingCanvas, lineHeight, (curFromTexture - 1));
    }
  }

  private void drawOnTexture(Canvas renderingCanvas, int lineHeight, int textureIndex) {
    int offset = textureIndex * TEXTURE_WIDTH;

    int curWord = getWordIndex(offset);
    if (curWord >= line.elements.length) return;

    float[] fMeasure = line.fMeasure;
    float wordMeasure = curWord == 0 ? 0 : fMeasure[curWord - 1];
    float x = wordMeasure - offset + xOffset;
    FontDesk[] fonts = context.fonts;

    renderingCanvas.clear();

    for (; curWord < line.elements.length; curWord++) {
      CodeElement entry = line.get(curWord);
      FontDesk font = fonts[entry.fontIndex()];
      int yPos = useTop ? topBase(font, lineHeight) : baselineShift(font, lineHeight);
      renderingCanvas.setFont(font);
      renderingCanvas.drawText(entry.s, x, yPos);
      x = fMeasure[curWord] - offset + xOffset;
      if (x > TEXTURE_WIDTH) break;
    }
    textures[textureIndex % textures.length].setContent(renderingCanvas);
  }

  public void draw(
      int yPosition, int dx,
      WglGraphics g,
      int editorWidth,
      int lineHeight,
      int horScrollPos,
      EditorColorScheme colors,
      V2i selectedSegment,
      CodeElement def,
      List<CodeElement> usages,
      boolean isCurrentLine,
      boolean isDiff,
      LineDiff diff
  ) {
    int tLength = textures.length;
    if (tLength == 0) return;
    if (horScrollPos > line.lineMeasure()) return;

    int[] iMeasure = line.iMeasure;
    CodeElement[] words = line.elements;

    int curTexture = horScrollPos / TEXTURE_WIDTH;
    int curWord = getWordIndex(horScrollPos);

    int texturePos = horScrollPos;
    int xPos = -xOffset;

    for (int i = curWord; i < words.length; i++) {
      boolean isLastWord = i == words.length - 1;
      if (xPos >= editorWidth) break;

      GL.Texture texture = textures[curTexture % tLength];
      CodeElement e = words[i];
      int pxLen = iMeasure[i] + xOffset;

      boolean drawOnCurTexture = pxLen - curTexture * TEXTURE_WIDTH <= TEXTURE_WIDTH;

      int drawWidth = Math.min((curTexture + 1) * TEXTURE_WIDTH, pxLen) - texturePos;
      if (drawOnCurTexture && isLastWord) drawWidth += xOffset;

      boolean isNotSelected = selectedSegment == null;
      boolean isFullUnselected = isNotSelected || isFullUnselected(selectedSegment, texturePos, drawWidth, isLastWord ? 2 * xOffset : xOffset);
      boolean isFullSelected = !isNotSelected && isFullSelected(selectedSegment, texturePos, drawWidth, isLastWord ? 2 * xOffset : xOffset);

      V4f elemBgColor = null;
      if (isCurrentLine && !isDiff) elemBgColor = colors.editor.currentLineBg;
      if (e == def) elemBgColor = colors.editor.definitionBg;
      if (usages.contains(e)) elemBgColor = colors.editor.usageBg;
      if (diff != null) {
        int elementType = diff.elementTypes == null
            ? 0 : i < diff.elementTypes.length
            ? diff.elementTypes[i] : 0;
        elemBgColor = colors.diff.getDiffColor(colors, elementType, diff.type);
      }

      if (isFullSelected || isFullUnselected) {
        CodeElementColor c = colors.codeElement[e.color];
        V4f bgColor = isFullSelected ? colors.editor.selectionBg :
            requireNonNullElse(elemBgColor, colors.bgColor(c.colorB));
        context.tRegion.set(texturePos - curTexture * TEXTURE_WIDTH, 0, drawWidth, lineHeight);
        context.size.set(drawWidth, lineHeight);
        context.drawText(g, texture, xPos + dx, yPosition, c.colorF, bgColor);
      } else {
        selectedSegment.y = Math.min(selectedSegment.y, line.lineMeasure());
        int pre = texturePos >= selectedSegment.x
            ? drawWidth
            : Math.min(pxLen, (curTexture + 1) * TEXTURE_WIDTH)
                - selectedSegment.x - (isLastWord ? 0 : xOffset);
        int post = texturePos + drawWidth <= selectedSegment.y + (isLastWord ? 2 * xOffset : xOffset)
            ? 0
            : Math.min(pxLen, (curTexture + 1) * TEXTURE_WIDTH)
                - selectedSegment.y - (isLastWord ? 0 : xOffset);
        int regionX = texturePos - curTexture * TEXTURE_WIDTH;
        drawSelected(g, xPos + dx, yPosition,
            lineHeight, colors,
            texture, e,
            drawWidth, pre, post, regionX, elemBgColor
        );
      }

      int underlineIndex = e.underlineIndex();

      if (underlineIndex > 0) {
        drawUnderline(xPos+ dx, yPosition, g, drawWidth, underlineIndex);
      }

      texturePos += drawWidth;
      xPos += drawWidth;

      if (!drawOnCurTexture) {
        curTexture++;
        i--;
      }
    }
  }

  private void drawUnderline(int xPos, int yPos, WglGraphics g, int drawWidth, int underlineIndex) {
    g.enableBlend(true);

    V2i underlineSize = context.underlineSize;
    underlineSize.x = drawWidth;

    g.drawSin(
        xPos, yPos + context.underline - context.underlineHBox,
        underlineSize,
        /* xPos + */ context.underlineOffset,
        yPos + context.underline + context.underlineOffset,
        context.underlineParams,
        IdeaCodeColors.ElementsDark.error.v.colorF
    );
    g.enableBlend(false);
  }

  private void drawSelected(
      WglGraphics g, int xPos, int yPosition,
      int lineHeight, EditorColorScheme colors,
      GL.Texture texture, CodeElement e,
      int drawWidth, int pre, int post, int regionX,
      V4f elemBgColor
  ) {
    CodeElementColor c = colors.codeElement[e.color];
    V4f colorF = c.colorF;
    V4f colorB = requireNonNullElse(elemBgColor, colors.bgColor(c.colorB));
    V4f selColorB = colors.editor.selectionBg;

    V4f tRegion = context.tRegion;
    V2i size = context.size;
    tRegion.set(regionX, 0, drawWidth - pre, lineHeight);
    size.set(drawWidth - pre, lineHeight);
    context.drawText(g, texture, xPos, yPosition, colorF, colorB);

    tRegion.set(regionX + drawWidth - post, 0, post, lineHeight);
    size.set(post, lineHeight);
    context.drawText(g, texture, xPos + drawWidth - post, yPosition,
        colorF, colorB);

    tRegion.set(regionX + drawWidth - pre, 0, pre - post, lineHeight);
    size.set(pre - post, lineHeight);
    context.drawText(g, texture, xPos + drawWidth - pre, yPosition,
        colorF, selColorB);
  }

  static <T> T requireNonNullElse(T obj, T defaultObj) {
    return (obj != null) ? obj : defaultObj;
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

  private void allocateTextures(WglGraphics g, int newSize) {
    int length = textures.length;
    if (length < newSize) {
      textures = Arrays.copyOf(textures, newSize);
    }

    for (int i = length; i < newSize; i++) {
      textures[i] = g.createTexture();
    }
  }

  @Override
  public void dispose() {
    for (GL.Texture lineTexture : textures) {
      lineTexture.dispose();
    }
    textures = context.textures0;
    line = null;
  }

  public void debug() {
    Debug.consoleInfo("\tcurTexture: " + curFromTexture);
    Debug.consoleInfo("\tnumOfTexture: " + getTexLength());
  }

  public int getTexLength() {
    return textures.length;
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

  static CodeLineRenderer[] allocRenderLines(
      int newSize,
      CodeLineRenderer[] lines, Context context,
      int first, int last, Document document
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
        else r[i] = new CodeLineRenderer(context);
      }
    }
    for (; pSrc < lines.length; pSrc++) {
      CodeLineRenderer v = lines[pSrc];
      if (v != null) v.dispose();
    }

    return r;
  }

  static class Context {
    final V4f tRegion = new V4f();
    final V2i size = new V2i();
    final V2i underlineSize = new V2i();
    final V4f underlineParams = new V4f();
    final FontDesk[] fonts;
    final GL.Texture[] textures0 = new GL.Texture[0];

    float    underlineOffset;
    int      lineHeight, underline, underlineHBox;
    boolean  cleartype;

    Context(FontDesk[] fonts, boolean cleartype) {
      this.fonts = fonts;
      this.cleartype = cleartype;
      sinParamsDefault(underlineParams);
    }

    void setSinDpr(float dpr) {
      sinParamsDefault(underlineParams);
      scaleSinParams(underlineParams, dpr, underlineParams);
      underlineOffset = UnderlineConstants.offset(underlineParams);
      underlineHBox = UnderlineConstants.boxExtend(underlineParams);
      underlineSize.set(0, underlineHBox * 2);
    }

    void drawText(WglGraphics g, GL.Texture texture, int xPos, int yPos, V4f colorF, V4f bgColor) {
      if (size.x == 0 || size.y == 0  ||
          tRegion.w == 0 || tRegion.z == 0) return;

      g.drawText(xPos, yPos, size, tRegion,
          texture, colorF, bgColor, cleartype);
    }
  }
}
