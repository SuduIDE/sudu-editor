package org.sudu.experiments.editor;

import org.sudu.experiments.*;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.ui.colors.CodeElementColor;
import org.sudu.experiments.editor.ui.colors.CodeLineColorScheme;
import org.sudu.experiments.editor.ui.colors.IdeaCodeColors;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.math.*;

import java.util.Arrays;
import java.util.List;

public class CodeLineRenderer implements Disposable {
  static boolean dumpMeasure;
  static boolean useTop = false;
  static final int TEXTURE_WIDTH = EditorConst.TEXTURE_WIDTH;
  static final int initialOffset = 3;
  private int xOffset = initialOffset;

  final ClrContext context;
  CodeLine line;
  GL.Texture[] textures;
  int curFromTexture = 0;
  private final V2i selTemp = new V2i();
  /** Reused across paints so highlight pixel segments are not allocated every frame. */
  private V2i[] highlightScratch = new V2i[0];

  public CodeLineRenderer(ClrContext context) {
    this.context = context;
    this.textures = context.textures0;
  }

  // returns lineMeasure()
  public int updateTexture(
      CodeLine content,
      WglGraphics g,
      int lineHeight,
      int editorWidth,
      int horScrollPos,
      int lineIdx, int rLineIdx
  ) {
    boolean lineChanged = line != content || line.contentDirty;
    if (lineChanged) {
      line = content;
      content.measure(g.mCanvas, context.fonts);
    }
    int lineMeasure = content.lineMeasure();
    int newSize = countNumOfTextures(editorWidth, lineMeasure);
    int oldSize = textures.length;
    boolean needsResize = newSize > oldSize;
    if (needsResize) allocateTextures(g, newSize);

    Canvas renderingCanvas = context.renderingCanvas;

    if (lineChanged || needsResize) {

      if (dumpMeasure) {
        Debug.consoleInfo("fMeasure", content.fMeasure);
        dumpMeasure = false;
      }

      renderingCanvas.setTopMode(useTop);
      curFromTexture = horScrollPos / TEXTURE_WIDTH;

      for (int i = 0; i < newSize; i++) {
//        System.out.println("update texture lineIdx " + lineIdx
//            + ", slot index = " + rLineIdx);

        drawOnTexture(renderingCanvas, lineHeight, curFromTexture + i);
      }

      line.contentDirty = false;
    }
    updateTextureOnScroll(renderingCanvas, lineHeight, horScrollPos);
    return lineMeasure;
  }

  public void setXOffset(int xOffset) {
    this.xOffset = xOffset;
  }

  private void updateTextureOnScroll(Canvas renderingCanvas, int lineHeight, int horScrollPos) {
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
      int yPos = useTop
          ? font.topBase(lineHeight)
          : font.baselineShift(lineHeight);
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
      CodeLineColorScheme colors,
      Model model,
      int lineIndex,
      boolean paintSelection,
      V4f overrideBackground, V4f overrideForeground
  ) {
    int lineMeasure = line.lineMeasure();
    if (lineMeasure == 0 || horScrollPos >= lineMeasure)
      return;

    CodeElement def = model != null ? model.definition : null;
    List<CodeElement> usages = model != null ? model.usages : null;
    boolean isCurrentLine = model != null && model.caretLine == lineIndex;
    LineDiff diff = model != null && model.diffModel != null && lineIndex < model.diffModel.length
        ? model.diffModel[lineIndex]
        : null;
    V2i selectedSegment = paintSelection && model != null
        ? selectedSegment(model, lineIndex, g) : null;
    int highlightCount = model != null ? fillHighlightSegments(model, lineIndex, g) : 0;

    if (selectedSegment != null) selectedSegment.y = Math.min(selectedSegment.y, lineMeasure);
    for (int h = 0; h < highlightCount; h++) {
      highlightScratch[h].y = Math.min(highlightScratch[h].y, lineMeasure);
    }

    int[] iMeasure = line.iMeasure;
    CodeElement[] words = line.elements;

    int curTexture = horScrollPos / TEXTURE_WIDTH;
    int curWord = getWordIndex(horScrollPos);

    int texturePos = horScrollPos;
    int xPos = -xOffset;
    int tLength = textures.length;

    for (int i = curWord; i < words.length; i++) {
      boolean isLastWord = i == words.length - 1;
      if (xPos >= editorWidth) break;

      GL.Texture texture = textures[curTexture % tLength];
      CodeElement e = words[i];
      int pxLen = iMeasure[i] + xOffset;

      boolean drawOnCurTexture = pxLen - curTexture * TEXTURE_WIDTH <= TEXTURE_WIDTH;

      int drawWidth = Math.min((curTexture + 1) * TEXTURE_WIDTH, pxLen) - texturePos;
      if (drawOnCurTexture && isLastWord) drawWidth += xOffset;

      CodeElementColor c = colors.elementColor(e.color);
      V4f colorF = overrideForeground != null ? overrideForeground : c.colorF;
      V4f colorB = requireNonNullElse(
          resolveElementBg(colors, e, def, usages, isCurrentLine, overrideBackground, diff, i),
          c.colorB != null ? c.colorB : colors.defaultBg);

      int regionX = texturePos - curTexture * TEXTURE_WIDTH;
      int screenX = xPos + dx;
      drawTextRegion(g, texture, screenX, yPosition, regionX, drawWidth, lineHeight, colorF, colorB);

      int endPad = isLastWord ? 2 * xOffset : xOffset;
      int midPad = isLastWord ? 0 : xOffset;
      int chunkEnd = Math.min(pxLen, (curTexture + 1) * TEXTURE_WIDTH);

      for (int h = 0; h < highlightCount; h++) {
        drawRangeOverlay(g, texture, screenX, yPosition, lineHeight,
            texturePos, drawWidth, regionX, chunkEnd, midPad, endPad,
            highlightScratch[h], colorF, colors.highlightRangesBg);
      }
      if (selectedSegment != null) {
        drawRangeOverlay(g, texture, screenX, yPosition, lineHeight,
            texturePos, drawWidth, regionX, chunkEnd, midPad, endPad,
            selectedSegment, colorF, colors.selectionBg);
      }

      int underlineIndex = e.underlineIndex();
      if (underlineIndex > 0) {
        drawUnderline(screenX, yPosition, g, drawWidth, underlineIndex);
      }

      texturePos += drawWidth;
      xPos += drawWidth;

      if (!drawOnCurTexture) {
        curTexture++;
        i--;
      }
    }
  }

  private V2i selectedSegment(Model model, int lineIndex, WglGraphics g) {
    V2i selLine = model.selection.getLine(lineIndex, selTemp);
    if (selLine == null) return null;
    if (selLine.y == -1) selLine.y = line.totalStrLength;
    selLine.x = line.computePixelLocation(selLine.x, g.mCanvas, context.fonts);
    selLine.y = line.computePixelLocation(selLine.y, g.mCanvas, context.fonts);
    return selLine;
  }

  /**
   * Fills {@link #highlightScratch} with pixel segments for {@code lineIndex}.
   * @return number of segments written (0 when none)
   */
  private int fillHighlightSegments(Model model, int lineIndex, WglGraphics g) {
    Range[] ranges = model.highlightRanges;
    if (ranges == null || ranges.length == 0) return 0;
    int n = 0;
    for (Range r : ranges) {
      if (r != null && r.startLineNumber <= lineIndex && lineIndex <= r.endLineNumber) n++;
    }
    if (n == 0) return 0;
    ensureHighlightScratch(n);
    int j = 0;
    for (Range r : ranges) {
      if (r == null || lineIndex < r.startLineNumber || lineIndex > r.endLineNumber) continue;
      int start = lineIndex > r.startLineNumber ? 0 : r.startColumn;
      int end = lineIndex < r.endLineNumber ? line.totalStrLength : r.endColumn;
      highlightScratch[j++].set(
          line.computePixelLocation(start, g.mCanvas, context.fonts),
          line.computePixelLocation(end, g.mCanvas, context.fonts));
    }
    return n;
  }

  private void ensureHighlightScratch(int n) {
    if (highlightScratch.length >= n) return;
    V2i[] next = Arrays.copyOf(highlightScratch, n);
    for (int i = 0; i < n; i++) {
      if (next[i] == null) next[i] = new V2i();
    }
    highlightScratch = next;
  }

  private V4f resolveElementBg(
      CodeLineColorScheme colors,
      CodeElement e,
      CodeElement def,
      List<CodeElement> usages,
      boolean isCurrentLine,
      V4f overrideBackground,
      LineDiff diff,
      int elementIndex
  ) {
    if (overrideBackground != null) return overrideBackground;
    V4f elemBgColor = null;
    if (isCurrentLine) elemBgColor = colors.currentLineBg;
    if (e == def) elemBgColor = colors.definitionBg;
    if (usages != null && usages.contains(e)) elemBgColor = colors.usageBg;
    if (diff != null) {
      int[] types = diff.elementTypes;
      int elementType = types != null && elementIndex < types.length ? types[elementIndex] : 0;
      int t = elementType != 0
          ? elementType == DiffTypes.EDITED && diff.type == DiffTypes.EDITED
              ? DiffTypes.EDITED2 : elementType
          : diff.type;
      elemBgColor = colors.diff.getDiffColor(t, colors.defaultBg);
    }
    return elemBgColor;
  }

  private void drawTextRegion(
      WglGraphics g, GL.Texture texture,
      int x, int y, int regionX, int width, int lineHeight,
      V4f colorF, V4f colorB
  ) {
    if (width <= 0) return;
    context.tRegion.set(regionX, 0, width, lineHeight);
    context.size.set(width, lineHeight);
    context.drawText(g, texture, x, y, colorF, colorB);
  }

  /** Redraws the intersection of {@code range} with the current chunk using {@code overlayBg}. */
  private void drawRangeOverlay(
      WglGraphics g, GL.Texture texture,
      int xPos, int yPosition, int lineHeight,
      int texturePos, int drawWidth, int regionX, int chunkEnd,
      int midPad, int endPad,
      V2i range, V4f colorF, V4f overlayBg
  ) {
    if (isFullUnselected(range, texturePos, drawWidth, endPad)) return;
    if (isFullSelected(range, texturePos, drawWidth, endPad)) {
      drawTextRegion(g, texture, xPos, yPosition, regionX, drawWidth, lineHeight, colorF, overlayBg);
      return;
    }
    int pre = texturePos >= range.x
        ? drawWidth
        : chunkEnd - range.x - midPad;
    int post = texturePos + drawWidth <= range.y + endPad
        ? 0
        : chunkEnd - range.y - midPad;
    int midWidth = pre - post;
    int midOffset = drawWidth - pre;
    drawTextRegion(g, texture,
        xPos + midOffset, yPosition,
        regionX + midOffset, midWidth, lineHeight,
        colorF, overlayBg);
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
        IdeaCodeColors.Dark.error, 0);
    g.enableBlend(false);
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

  private int countNumOfTextures(int editorWidth, int lineMeasure) {
    int width = Math.min(lineMeasure, editorWidth + TEXTURE_WIDTH * 2);
    return Numbers.iDivRoundUp(width, TEXTURE_WIDTH);
  }

  private void allocateTextures(WglGraphics g, int newSize) {
    int length = textures.length;
    textures = Arrays.copyOf(textures, newSize);

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
      V4f editBgColor
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

  public static CodeLineRenderer[] allocRenderLines(
      int newSize,
      CodeLineRenderer[] lines, ClrContext context,
      int first, int last, CodeLines document
  ) {
    CodeLineRenderer[] r = new CodeLineRenderer[newSize];
    int pSrc = 0;
    if (lines.length > 0) for (int i = first; i < last; i++) {
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

  public static void makeContentDirty(CodeLineRenderer[] lines) {
    for (CodeLineRenderer line : lines) {
      CodeLine codeLine = line.line;
      if (codeLine != null) codeLine.contentDirty = true;
    }
  }

  public static void disposeLines(CodeLineRenderer[] lines) {
    for (CodeLineRenderer line : lines) {
      line.dispose();
    }
  }
}
