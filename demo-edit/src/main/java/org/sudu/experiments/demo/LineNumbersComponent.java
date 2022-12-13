package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.math.V2i;

import java.util.ArrayList;
import java.util.List;

public class LineNumbersComponent implements Disposable {

  private final WglGraphics g;
  private final int numberOfLines = EditorConst.LINE_NUMBERS_TEXTURE_SIZE;
  private final int textureWidth;

  private final V2i componentPos;
  private FontDesk fontDesk;
  private int lineHeight;

  private int textureHeight;
  private double devicePR;

  private final List<LineNumbersTexture> textures = new ArrayList<>();
  private Canvas textureCanvas;
  private Canvas updateCanvas;

  private int curFirstLine;

  public LineNumbersComponent(WglGraphics g, V2i componentPos, int textureWidth) {
    this.g = g;
    this.componentPos = componentPos;
    this.textureWidth = textureWidth;
  }

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
          curFirstLine = texture.updateTexture(textureCanvas, updateCanvas, curFirstLine, firstLine, updateOn, devicePR);
        }
      } else {
        for (int i = endTextureUpdate; i >= startTextureUpdate; i--) {
          LineNumbersTexture texture = textures.get(i % numOfTextures);
          curFirstLine = texture.updateTexture(textureCanvas, updateCanvas, curFirstLine, firstLine, updateOn, devicePR);
        }
      }
    }
  }

  public void draw(
      int editorHeight, int textHeight, int scrollPos,
      int firstLine, int lastLine, int caretLine,
      LineNumbersColors colors
  ) {
    update(firstLine);
    draw(scrollPos, textHeight, colors);
    drawBottom(textHeight, editorHeight, colors);

    if (firstLine <= caretLine && caretLine <= lastLine) {
      drawCaretLine(scrollPos, caretLine, colors);
    }
  }

  public void draw(int scrollPos, int editorHeight, LineNumbersColors colorScheme) {
    for (var text : textures) {
      text.draw(g, componentPos, editorHeight, scrollPos, textures.size() * textureHeight, colorScheme);
    }
  }

  private void drawCaretLine(int scrollPos, int caretLine, LineNumbersColors colorScheme) {
    int caretTexture = (caretLine / numberOfLines) % textures.size();

    textures.get(caretTexture).drawCurrentLine(
        g, componentPos, scrollPos,
        textures.size() * textureHeight, caretLine, colorScheme);
  }

  private void drawBottom(int textHeight, int editorBottom, LineNumbersColors colorScheme) {
    if (textHeight < editorBottom) {
      g.drawRect(componentPos.x, componentPos.y + textHeight,
        new V2i(textureWidth, editorBottom - textHeight),
        colorScheme.bgColor);
    }
  }

  public void initTextures(int editorHeight) {
    initTextures(0, editorHeight);
  }

  public void initTextures(int firstLine, int editorHeight) {
    int oldSize = textures.size();

    while (textures.size() * textureHeight <= editorHeight) {
      int number = textures.size();
      V2i texturePos = new V2i(0, number * textureHeight);
      LineNumbersTexture texture = new LineNumbersTexture(
        texturePos,
        numberOfLines,
        textureWidth, lineHeight,
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

  public void setFont(FontDesk font, int lineHeight) {
    this.fontDesk = font;
    this.lineHeight = lineHeight;
    this.textureHeight = lineHeight * numberOfLines;

    textureCanvas = Disposable.assign(
      textureCanvas, g.createCanvas(textureWidth, textureHeight)
    );
    textureCanvas.setFont(fontDesk);
    textureCanvas.setTextAlign(Canvas.TextAlign.RIGHT);

    updateCanvas = Disposable.assign(
      updateCanvas, g.createCanvas(textureWidth, lineHeight)
    );
    updateCanvas.setFont(fontDesk);
    updateCanvas.setTextAlign(Canvas.TextAlign.RIGHT);
  }

  public void setDevicePR(double devicePR) {
    this.devicePR = devicePR;
  }

  @Override
  public void dispose() {
    textures.forEach(LineNumbersTexture::dispose);
    textures.clear();
    textureCanvas = Disposable.assign(textureCanvas, null);
    updateCanvas = Disposable.assign(updateCanvas, null);
  }

}
