package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;

import java.util.ArrayList;
import java.util.List;

public class LineNumbersComponent implements Disposable {

  private final WglGraphics g;
  private final int numberOfLines = EditorConst.LINE_NUMERATION_TEXTURE_SIZE;

  private final V2i componentPos;
  private FontDesk fontDesk;
  private int lineHeight;
  private final Color textColor;
  private final Color bgColor;

  private int textureHeight;
  private int textureWidth;
  private int editorBottom;

  private final List<LineNumbersTexture> textures;
  private Canvas textureCanvas;
  private Canvas updateCanvas;

  private int curFirstLine;

  public LineNumbersComponent(
    final WglGraphics g,
    final V2i componentPos,
    final int textureWidth,
    Color textColor, Color bgColor
  ) {
    this.g = g;
    this.componentPos = componentPos;
    this.textureWidth = textureWidth;
    this.textColor = textColor;
    this.bgColor = bgColor;

    this.textures = new ArrayList<>();
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
          curFirstLine = texture.updateTexture(textureCanvas, updateCanvas, curFirstLine, firstLine, updateOn);
        }
      } else {
        for (int i = endTextureUpdate; i >= startTextureUpdate; i--) {
          LineNumbersTexture texture = textures.get(i % numOfTextures);
          curFirstLine = texture.updateTexture(textureCanvas, updateCanvas, curFirstLine, firstLine, updateOn);
        }
      }
    }
  }

  public void draw(int scrollPos) {
    for (var text : textures) {
      text.draw(g, componentPos, editorBottom, scrollPos, textures.size() * textureHeight);
    }
  }

  public void drawCaretLine(int scrollPos, int caretLine) {
    int caretTexture = (caretLine / numberOfLines) % textures.size();

    textures.get(caretTexture).drawCurrentLine(g, componentPos, scrollPos, textures.size() * textureHeight, caretLine);
  }

  public void resize(V2i size, int editorH) {
    this.editorBottom = Math.max(0, Math.min(size.y, editorH));
  }

  public void setEditorBottom(int editorH) {
    this.editorBottom = editorH;
  }

  public void initTextures() {
    initTextures(0);
  }

  public void initTextures(int firstLine) {
    int oldSize = textures.size();

    while (textures.size() * textureHeight <= editorBottom) {
      int number = textures.size();
      V2i texturePos = new V2i(0, number * textureHeight);
      LineNumbersTexture texture = new LineNumbersTexture(
        texturePos,
        numberOfLines,
        textureWidth, lineHeight,
        fontDesk,
        textColor, bgColor
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
      startNum = texture.initTexture(textureCanvas, startNum, firstLine, size);
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

  @Override
  public void dispose() {
    textures.forEach(Disposable::dispose);
    textures.clear();
    textureCanvas = Disposable.assign(textureCanvas, null);
    updateCanvas = Disposable.assign(updateCanvas, null);
  }

}
