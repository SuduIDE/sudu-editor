package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

public class LineNumbersTexture implements Disposable {

  private GL.Texture lineTexture;

  private final V2i texturePos;
  private final V2i textureSize;
  private final int numberOfLines;
  private final int lineHeight;
  private final V4f textColor;
  private final V4f bgColor;

  private final V2i rectSize = new V2i();
  private final V4f rectRegion = new V4f();

  private final int baseline;

  public LineNumbersTexture(
    V2i texturePos,
    int numberOfLines,
    int textureWidth,
    int lineHeight,
    FontDesk fontDesk,
    Color textColor, Color bgColor
  ) {
    this.texturePos = texturePos;
    this.numberOfLines = numberOfLines;
    this.lineHeight = lineHeight;
    this.textureSize = new V2i(textureWidth, this.numberOfLines * lineHeight);
    this.textColor = textColor.v4f;
    this.bgColor = bgColor.v4f;

    int baseLineBase = lineHeight - fontDesk.descent;
    this.baseline = baseLineBase - (lineHeight - fontDesk.ascent - fontDesk.descent) / 2;
  }

  public int updateTexture(Canvas textureCanvas, Canvas updateCanvas, int curFirstLine, int firstLine, int updateOn) {
    if (firstLine > curFirstLine)
      return scrollDown(textureCanvas, updateCanvas, firstLine, updateOn, curFirstLine);
    else
      return scrollUp(textureCanvas, updateCanvas, firstLine, updateOn, curFirstLine);
  }

  public void createTexture(final WglGraphics g) {
    if (lineTexture == null) lineTexture = g.createTexture();
  }

  public int initTexture(Canvas textureCanvas, int startNum, int toNum, int texturesSize) {
    textureCanvas.clear();

    for (int i = 0; i < numberOfLines; i++) {
      if (startNum - texturesSize * numberOfLines >= toNum)
        startNum -= texturesSize * numberOfLines;

      String lineNum = String.valueOf(startNum++ + 1);
      int yPos = lineHeight * i + baseline;
      float xPos = textureSize.x;
      textureCanvas.drawText(lineNum, xPos, yPos);
    }

    lineTexture.setContent(textureCanvas);
    return startNum;
  }

  public void draw(WglGraphics g, V2i dXdY, int componentHeight, int scrollPos, int fullTexturesSize) {
    int height = textureSize.y;
    int yPos = ((texturePos.y - (scrollPos % fullTexturesSize)) + fullTexturesSize) % fullTexturesSize;

    if ((yPos + height) <= componentHeight) {
      rectSize.set(lineTexture.width(), height);
      rectRegion.set(0, 0, lineTexture.width(), height);

      draw(g, yPos, dXdY, textColor, bgColor);
    } else {
      if (yPos + height > componentHeight && yPos < componentHeight) {
        int topHeight = Math.max(componentHeight - yPos, 0);
        rectSize.set(lineTexture.width(), topHeight);
        rectRegion.set(0, 0, lineTexture.width(), topHeight);

        draw(g, yPos, dXdY, textColor, bgColor);
      }
      if (yPos + height > fullTexturesSize) {
        height = (yPos + height) % fullTexturesSize;
        rectSize.set(lineTexture.width(), height);
        rectRegion.set(0, scrollPos % lineTexture.height(), lineTexture.width(), height);

        draw(g, 0, dXdY, textColor, bgColor);
      }
    }
  }

  public void drawCurrentLine(WglGraphics g, V2i dXdY, int scrollPos, int fullTexturesSize, int caretLine) {
    int height = textureSize.y;
    int yPos = ((texturePos.y - (scrollPos % fullTexturesSize)) + fullTexturesSize) % fullTexturesSize;
    if (yPos + height > fullTexturesSize) yPos = -(scrollPos % lineTexture.height());
    yPos += (caretLine % numberOfLines) * lineHeight;

    rectSize.set(textureSize.x, lineHeight);
    rectRegion.set(0, (caretLine % numberOfLines) * lineHeight, textureSize.x, lineHeight);

    draw(g, yPos, dXdY, Colors.white, Colors.black);
  }

  private void draw(WglGraphics g, int yPos, V2i dXdY, V4f textColor, V4f bgColor) {
    g.drawText(texturePos.x + dXdY.x, yPos + dXdY.y,
      rectSize,
      rectRegion,
      lineTexture,
      textColor, bgColor, 1f);
  }

  private int scrollDown(Canvas textureCanvas, Canvas updateCanvas, int firstLine, int updateOn, int curFirstLine) {
    int stNum = (curFirstLine / numberOfLines) * numberOfLines;
    int endNum = stNum + numberOfLines;

    if (curFirstLine + numberOfLines / 3 < Math.min(endNum, firstLine - 1)) {
      textureCanvas.clear();
      for (int i = 0; i < numberOfLines; i++) {
        int line = stNum++ + 1;
        if (line > firstLine) line -= updateOn;

        String lineNum = String.valueOf(line + updateOn);

        drawLine(textureCanvas, lineNum, lineHeight * i + baseline);
      }
      lineTexture.setContent(textureCanvas);
      return Math.min(endNum, firstLine);
    }

    while (curFirstLine < firstLine - 1) {
      updateCanvas.clear();

      String lineNum = String.valueOf(curFirstLine + updateOn + 1);
      int yPos = (curFirstLine++ * lineHeight) % textureSize.y;

      drawLine(updateCanvas, lineNum, baseline);
      lineTexture.update(updateCanvas, 0, yPos);
      if (curFirstLine % numberOfLines == 0) break;
    }
    return curFirstLine;
  }

  private int scrollUp(Canvas textureCanvas, Canvas updateCanvas, int firstLine, int updateOn, int curFirstLine) {
    int stNum = (curFirstLine / numberOfLines) * numberOfLines;
    int endNum = stNum + numberOfLines;

    if (curFirstLine - numberOfLines / 3 > Math.max(stNum, firstLine)) {
      textureCanvas.clear();
      for (int i = numberOfLines - 1; i >= 0; i--) {
        int line = endNum--;
        if (line < firstLine) line += updateOn;
        String lineNum = String.valueOf(line);

        drawLine(textureCanvas, lineNum, lineHeight * i + baseline);
      }
      lineTexture.setContent(textureCanvas);
      return Math.max(stNum, firstLine) - 1;
    }

    while (curFirstLine > firstLine) {
      updateCanvas.clear();

      String lineNum = String.valueOf(curFirstLine--);
      int yPos = (curFirstLine * lineHeight) % textureSize.y;

      drawLine(updateCanvas, lineNum, baseline);
      lineTexture.update(updateCanvas, 0, yPos);
      if (curFirstLine % numberOfLines == 0) break;
    }
    return curFirstLine;
  }

  private void drawLine(Canvas canvas, String lineNumber, int yPos) {
    canvas.drawText(lineNumber, textureSize.x, yPos);
  }

  @Override
  public void dispose() {
    lineTexture = Disposable.assign(lineTexture, null);
  }

}
