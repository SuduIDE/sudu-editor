package org.sudu.experiments.editor;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.Disposable;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.ui.colors.LineNumbersColors;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.Rect;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.ui.SetCursor;

import java.util.Arrays;
import java.util.Set;

public class LineNumbersComponent implements Disposable {

  private final int numberOfLines = EditorConst.LINE_NUMBERS_TEXTURE_SIZE;

  public final V2i pos = new V2i();
  public final V2i size = new V2i();
  public float dpr;

  private FontDesk fontDesk;
  private boolean cleartype;
  private int lineHeight;
  private int textureHeight;

  private LineNumbersTexture[] textures = new LineNumbersTexture[0];
  private byte[] colors = new byte[0];
  private Canvas textureCanvas;
  private Canvas updateCanvas;

  private int curFirstLine;

  public void setPosition(int x, int y, int width, int height, float dpr) {
    pos.set(x, y);
    size.set(width, height);
    this.dpr = dpr;
  }

  public int width() {
    return size.x;
  }

  private void update(int firstLine, WglGraphics g) {
    if (firstLine == curFirstLine) return;

    int updateOn = textures.length * numberOfLines;
    int startTextureUpdate;
    int endTextureUpdate;

    if (curFirstLine < firstLine) {
      startTextureUpdate = curFirstLine / numberOfLines;
      endTextureUpdate = firstLine / numberOfLines;
    } else {
      endTextureUpdate = Math.max(0, (curFirstLine - 1) / numberOfLines);
      startTextureUpdate = Math.max(0, (firstLine - 1) / numberOfLines);
    }

    if (endTextureUpdate - startTextureUpdate >= textures.length) {
      updateToFirstLine(firstLine);
      curFirstLine = firstLine;
    } else {
      ensureCanvas(g);
      if (curFirstLine < firstLine) {
        for (int i = startTextureUpdate; i <= endTextureUpdate; i++) {
          curFirstLine = texture(i).updateTexture(
              textureCanvas, updateCanvas,
              curFirstLine, firstLine, updateOn, dpr);
        }
      } else {
        for (int i = endTextureUpdate; i >= startTextureUpdate; i--) {
          curFirstLine = texture(i).updateTexture(
              textureCanvas, updateCanvas, curFirstLine,
              firstLine, updateOn, dpr);
        }
      }
    }
  }

  public void setColors(byte[] c) {
    this.colors = c == null ? new byte[0] : c;
  }

  byte[] colors() { return colors; }

  public void draw(
      int textHeight,
      int scrollPos, int firstLine,
      int lastLine, int caretLine,
      WglGraphics g, EditorColorScheme scheme
  ) {
    g.enableScissor(pos, size);
    initTextures(g, size.y);
    updateToFirstLine(firstLine);
    update(firstLine, g);

    int curTexture = scrollPos / textureHeight;
    for (int texturePos = -(scrollPos % textureHeight); texturePos < textHeight; texturePos += textureHeight) {
      texture(curTexture).draw(pos, texturePos, scrollPos, scheme, colors, g);
      curTexture++;
    }

    drawBottom(textHeight, size.y, scheme.editor.bg, g);

    if (firstLine <= caretLine && caretLine <= lastLine) {
      var bgColor = scheme.getDiffColor(colors, caretLine);
      drawCaretLine(scrollPos, caretLine, scheme.lineNumber, g, bgColor);
    }
    g.disableScissor();
  }

  public void drawSyncPoints(
      int scrollPos,
      int firstLine, int lastLine,
      Set<Integer> syncPoints,
      WglGraphics g, EditorColorScheme scheme
  ) {
    for (int i = firstLine; i <= lastLine; i++) {
      if (syncPoints.contains(i)) {
        drawSyncLine(scrollPos, i, scheme.lineNumber, g);
      }
    }
  }

  private void drawCaretLine(
      int scrollPos, int caretLine,
      LineNumbersColors colorScheme, WglGraphics g,
      V4f bgColor
  ) {
    texture(caretLine / numberOfLines).drawCurrentLine(
        g, pos, scrollPos,
        textures.length * textureHeight,
        caretLine, colorScheme, bgColor);
  }

  private void drawSyncLine(
      int scrollPos, int syncLine,
      LineNumbersColors colorScheme,
      WglGraphics g
  ) {
    texture(syncLine / numberOfLines).drawSyncLine(
        g, pos, scrollPos,
        textures.length * textureHeight,
        syncLine, colorScheme
    );
  }

  private void drawBottom(
      int textHeight, int editorBottom,
      V4f bgColor, WglGraphics g
  ) {
    if (textHeight < editorBottom) {
      g.drawRect(pos.x, pos.y + textHeight,
        new V2i(size.x, editorBottom - textHeight), bgColor);
    }
  }

  private void initTextures(WglGraphics g, int editorHeight) {
    int oldSize = textures.length;
    int newSize = (editorHeight + lineHeight) / textureHeight + 1;
    if (newSize == oldSize) return;
    if (oldSize < newSize) {
      textures = Arrays.copyOf(textures, newSize);
      for (; oldSize < newSize; oldSize++) {
        LineNumbersTexture texture = new LineNumbersTexture(
            oldSize * textureHeight,
            numberOfLines,
            size.x, lineHeight,
            fontDesk
        );
        texture.createTexture(g);
        textures[oldSize] = texture;
      }
    }
    ensureCanvas(g);
  }

  private void updateToFirstLine(int firstLine) {
    int size = textures.length;
    int numberOfFullScroll = firstLine / (size * numberOfLines);
    int startNum = (numberOfFullScroll + 1) * size * numberOfLines;

    for (LineNumbersTexture texture : textures) {
      startNum = texture.initTexture(textureCanvas, startNum, firstLine, size, dpr);
    }
  }

  public void setFont(FontDesk font, int lineHeight, boolean cleartype) {
    this.fontDesk = font;
    this.lineHeight = lineHeight;
    this.textureHeight = lineHeight * numberOfLines;
    this.cleartype = cleartype;
    disposeCanvas();
  }

  private void ensureCanvas(WglGraphics g) {
    if (textureCanvas == null || textureCanvas.cleartype != cleartype) {
      textureCanvas = Disposable.assign(textureCanvas,
          g.createCanvas(size.x, textureHeight, cleartype));
      textureCanvas.setFont(fontDesk);
      textureCanvas.setTextAlign(Canvas.TextAlign.RIGHT);
    }
    if (updateCanvas == null || updateCanvas.cleartype != cleartype) {
      updateCanvas = Disposable.assign(updateCanvas,
          g.createCanvas(size.x, lineHeight, cleartype));
      updateCanvas.setFont(fontDesk);
      updateCanvas.setTextAlign(Canvas.TextAlign.RIGHT);
    }
  }

  private LineNumbersTexture texture(int i) {
    return textures[i % textures.length];
  }

  @Override
  public void dispose() {
    for (var t : textures) t.dispose();
    textures = new LineNumbersTexture[0];
    disposeCanvas();
  }

  private void disposeCanvas() {
    textureCanvas = Disposable.assign(textureCanvas, null);
    updateCanvas = Disposable.assign(updateCanvas, null);
  }

  public boolean onMouseMove(MouseEvent event, SetCursor setCursor) {
    return hitTest(event.position) && setCursor.setDefault();
  }

  public boolean hitTest(V2i point) {
    return Rect.isInside(point, pos, size);
  }

}
