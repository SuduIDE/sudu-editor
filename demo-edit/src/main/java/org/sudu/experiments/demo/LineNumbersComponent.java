package org.sudu.experiments.demo;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.Disposable;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.demo.ui.colors.EditorColorScheme;
import org.sudu.experiments.demo.ui.colors.LineNumbersColors;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.math.Rect;
import org.sudu.experiments.math.V2i;

import java.util.ArrayList;
import java.util.List;

public class LineNumbersComponent implements Disposable {

  private final int numberOfLines = EditorConst.LINE_NUMBERS_TEXTURE_SIZE;

  private final V2i pos = new V2i();
  private final V2i size = new V2i();
  private FontDesk fontDesk;
  private int lineHeight;

  private int textureHeight;
  private double devicePR;

  private final List<LineNumbersTexture> textures = new ArrayList<>();
  private byte[] colors = new byte[0];
  private Canvas textureCanvas;
  private Canvas updateCanvas;

  private int curFirstLine;

  public void update(int firstLine) {
    if (firstLine == curFirstLine) return;

    int numOfTextures = textures.size();
    int updateOn = numOfTextures * numberOfLines;
    int startTextureUpdate;
    int endTextureUpdate;

    if (curFirstLine < firstLine) {
      startTextureUpdate = curFirstLine / numberOfLines;
      endTextureUpdate = firstLine / numberOfLines;
    } else {
      endTextureUpdate = Math.max(0, (curFirstLine - 1) / numberOfLines);
      startTextureUpdate = Math.max(0, (firstLine - 1) / numberOfLines);
    }

    if (endTextureUpdate - startTextureUpdate >= numOfTextures) {
      updateToFirstLine(firstLine);
      curFirstLine = firstLine;
    } else {
      if (curFirstLine < firstLine) {
        for (int i = startTextureUpdate; i <= endTextureUpdate; i++) {
          LineNumbersTexture texture = textures.get(i % numOfTextures);
          curFirstLine = texture.updateTexture(
              textureCanvas, updateCanvas,
              curFirstLine, firstLine, updateOn, devicePR);
        }
      } else {
        for (int i = endTextureUpdate; i >= startTextureUpdate; i--) {
          LineNumbersTexture texture = textures.get(i % numOfTextures);
          curFirstLine = texture.updateTexture(
              textureCanvas, updateCanvas, curFirstLine,
              firstLine, updateOn, devicePR);
        }
      }
    }
  }

  public void setColors(byte[] c) {
    this.colors = c == null ? new byte[0] : c;
  }

  public void draw(
      int editorHeight, int textHeight,
      int scrollPos, int firstLine,
      int lastLine, int caretLine,
      WglGraphics g, EditorColorScheme colors
  ) {
    g.enableScissor(pos, size);
    initTextures(g, firstLine, editorHeight);
    update(firstLine);
    draw(scrollPos, textHeight, colors, g);
    drawBottom(textHeight, editorHeight, colors.lineNumber, g);

    if (firstLine <= caretLine && caretLine <= lastLine) {
      drawCaretLine(scrollPos, caretLine, colors.lineNumber, g);
    }
    g.disableScissor();
  }

  public void draw(
      int scrollPos, int editorHeight,
      EditorColorScheme colorScheme, WglGraphics g
  ) {
    for (var text : textures) {
      text.draw(pos, editorHeight, scrollPos, textures.size() * textureHeight, colorScheme, colors, g);
    }
  }

  private void drawCaretLine(
      int scrollPos, int caretLine,
      LineNumbersColors colorScheme, WglGraphics g
  ) {
    int caretTexture = (caretLine / numberOfLines) % textures.size();

    textures.get(caretTexture).drawCurrentLine(
        g, pos, scrollPos,
        textures.size() * textureHeight, caretLine, colorScheme);
  }

  private void drawBottom(
      int textHeight, int editorBottom,
      LineNumbersColors colorScheme, WglGraphics g
  ) {
    if (textHeight < editorBottom) {
      g.drawRect(pos.x, pos.y + textHeight,
        new V2i(size.x, editorBottom - textHeight),
        colorScheme.bgColor);
    }
  }

  public void initTextures(WglGraphics g, int editorHeight) {
    initTextures(g, 0, editorHeight);
  }

  public void initTextures(WglGraphics g, int firstLine, int editorHeight) {
    int oldSize = textures.size();

    while (textures.size() * textureHeight <= editorHeight + lineHeight) {
      int number = textures.size();
      V2i texturePos = new V2i(0, number * textureHeight);
      LineNumbersTexture texture = new LineNumbersTexture(
          texturePos,
          numberOfLines,
          size.x, lineHeight,
          fontDesk
      );
      texture.createTexture(g);
      textures.add(texture);
    }
    int newSize = textures.size();
    if (newSize == oldSize) return;
    updateToFirstLine(firstLine);
  }

  private void updateToFirstLine(int firstLine) {
    int size = textures.size();
    int numberOfFullScroll = firstLine / (size * numberOfLines);
    int startNum = (numberOfFullScroll + 1) * size * numberOfLines;

    for (LineNumbersTexture texture : textures) {
      startNum = texture.initTexture(textureCanvas, startNum, firstLine, size, devicePR);
    }
  }

  public void setFont(FontDesk font, int lineHeight, WglGraphics g) {
    this.fontDesk = font;
    this.lineHeight = lineHeight;
    this.textureHeight = lineHeight * numberOfLines;

    textureCanvas = Disposable.assign(
        textureCanvas, g.createCanvas(size.x, textureHeight)
    );
    textureCanvas.setFont(fontDesk);
    textureCanvas.setTextAlign(Canvas.TextAlign.RIGHT);

    updateCanvas = Disposable.assign(
        updateCanvas, g.createCanvas(size.x, lineHeight)
    );
    updateCanvas.setFont(fontDesk);
    updateCanvas.setTextAlign(Canvas.TextAlign.RIGHT);
  }

  @Override
  public void dispose() {
    textures.forEach(LineNumbersTexture::dispose);
    textures.clear();
    textureCanvas = Disposable.assign(textureCanvas, null);
    updateCanvas = Disposable.assign(updateCanvas, null);
  }

  public boolean onMouseMove(V2i position, SetCursor setCursor) {
    return Rect.isInside(position, pos, size) && setCursor.setDefault();
  }

  void setPos(V2i pos, int width, int height, double dpr) {
    this.pos.set(pos);
    size.set(width, height);
    devicePR = dpr;
  }
}
