package org.sudu.experiments.editor;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.Disposable;
import org.sudu.experiments.GL;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.ui.colors.LineNumbersColors;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import static org.sudu.experiments.editor.LineNumbersComponent.*;

public class LineNumbersTexture implements Disposable {

  private GL.Texture lineTexture;

  private final V2i texturePos;
  private final V2i textureSize;
  private final int numberOfLines;
  private final int lineHeight;

  private final V2i rectSize = new V2i();
  private final V4f rectRegion = new V4f();

  private final int baseline;

  private boolean cleartype;

  public LineNumbersTexture(
      int texturePosY,
      int numberOfLines,
      int textureWidth,
      int lineHeight,
      FontDesk fontDesk
  ) {

    this.texturePos = new V2i(0, texturePosY);
    this.numberOfLines = numberOfLines;
    this.lineHeight = lineHeight;
    this.textureSize = new V2i(textureWidth, this.numberOfLines * lineHeight);
    this.baseline = fontDesk.baselineShift(lineHeight);
  }

  public int updateTexture(
      Canvas textureCanvas, Canvas updateCanvas,
      int curFirstLine, int firstLine, int updateOn, float devicePR
  ) {
    if (firstLine > curFirstLine)
      return scrollDown(textureCanvas, updateCanvas, firstLine, updateOn, curFirstLine, devicePR);
    else
      return scrollUp(textureCanvas, updateCanvas, firstLine, updateOn, curFirstLine, devicePR);
  }

  public void createTexture(final WglGraphics g) {
    if (lineTexture == null) lineTexture = g.createTexture();
  }

  public int initTexture(
      Canvas textureCanvas,
      int startNum, int toNum,
      int texturesSize,
      float devicePR
  ) {
    textureCanvas.clear();

    for (int i = 0; i < numberOfLines; i++) {
      if (startNum - texturesSize * numberOfLines >= toNum)
        startNum -= texturesSize * numberOfLines;

      String lineNum = String.valueOf(startNum++ + 1);
      drawLine(textureCanvas, lineNum, lineHeight * i + baseline, devicePR);
    }

    lineTexture.setContent(textureCanvas);
    cleartype = textureCanvas.cleartype;
    return startNum;
  }

  public void draw(
      V2i dXdY, int yPos, int scrollPos,
      EditorColorScheme colorScheme, byte[] colors, WglGraphics g
  ) {
    int height = textureSize.y;
    int baseColorInd = (scrollPos + yPos) / lineHeight;
    drawFullTexture(dXdY, colorScheme, colors, g, height, baseColorInd, yPos);
  }

  private void drawFullTexture(
      V2i dXdY, EditorColorScheme colorScheme, byte[] colors, WglGraphics g,
      int height, int baseColorInd, int yPos
  ) {
    LineNumbersColors lineNumber = colorScheme.lineNumber;

    int upper = height / lineHeight;
    int startLine = 0;
    int i = 0;
    V4f prevColor = getItemColor(colorScheme, colors, baseColorInd + i);
    while (i < upper) {
      V4f c = getItemColor(colorScheme, colors, baseColorInd + i);
      if (c == prevColor) {
        int h = (i - startLine + 1) * lineHeight;
        rectSize.set(lineTexture.width(), h);
        rectRegion.set(0, startLine * lineHeight, lineTexture.width(), h);
      } else {
        draw(g, yPos + startLine * lineHeight, dXdY, lineNumber.textColor, prevColor);
        prevColor = c;
        startLine = i;
        rectSize.set(lineTexture.width(), lineHeight);
        rectRegion.set(0, startLine * lineHeight, lineTexture.width(), lineHeight);
      }
      i++;
    }

    draw(g, yPos + startLine * lineHeight, dXdY, lineNumber.textColor, prevColor);
  }

  void drawCurrentLine(
      WglGraphics g, V2i dXdY,
      int scrollPos, int fullTexturesSize, int caretLine,
      LineNumbersColors colorScheme
  ) {
    int caretShift = caretLine % numberOfLines;
    int height = textureSize.y;
    int yPos = ((texturePos.y - (scrollPos % fullTexturesSize)) + fullTexturesSize) % fullTexturesSize;
    if (yPos + height > fullTexturesSize) yPos = -(scrollPos % lineTexture.height());
    yPos += caretShift * lineHeight;
    if (yPos < -lineHeight) yPos += fullTexturesSize;

    rectSize.set(textureSize.x, lineHeight);
    rectRegion.set(0, caretShift * lineHeight, textureSize.x, lineHeight);

    draw(g, yPos, dXdY, colorScheme.caretTextColor, colorScheme.caretBgColor);
  }

  private void draw(WglGraphics g, int yPos, V2i dXdY, V4f textColor, V4f bgColor) {
    g.drawText(texturePos.x + dXdY.x, yPos + dXdY.y,
        rectSize,
        rectRegion,
        lineTexture,
        textColor, bgColor, cleartype);
}

  private int scrollDown(
      Canvas textureCanvas, Canvas updateCanvas,
      int firstLine, int updateOn, int curFirstLine, float devicePR
  ) {
    int stNum = (curFirstLine / numberOfLines) * numberOfLines;
    int endNum = stNum + numberOfLines;

    if (curFirstLine + numberOfLines / 3 < Math.min(endNum, firstLine - 1)) {
      textureCanvas.clear();
      for (int i = 0; i < numberOfLines; i++) {
        int line = stNum++ + 1;
        if (line > firstLine) line -= updateOn;

        String lineNum = String.valueOf(line + updateOn);
        drawLine(textureCanvas, lineNum, lineHeight * i + baseline, devicePR);
      }
      lineTexture.setContent(textureCanvas);
      return Math.min(endNum, firstLine);
    }

    while (curFirstLine < firstLine) {
      updateCanvas.clear();

      String lineNum = String.valueOf(curFirstLine + updateOn + 1);
      int yPos = (curFirstLine++ * lineHeight) % textureSize.y;

      drawLine(updateCanvas, lineNum, baseline, devicePR);
      lineTexture.update(updateCanvas, 0, yPos);
      if (curFirstLine % numberOfLines == 0) break;
    }
    return curFirstLine;
  }

  private int scrollUp(
      Canvas textureCanvas, Canvas updateCanvas,
      int firstLine, int updateOn, int curFirstLine, float devicePR
  ) {
    int stNum = (curFirstLine / numberOfLines) * numberOfLines;
    int endNum = stNum + numberOfLines;

    if (curFirstLine - numberOfLines / 3 > Math.max(stNum, firstLine)) {
      textureCanvas.clear();
      for (int i = numberOfLines - 1; i >= 0; i--) {
        int line = endNum--;
        if (line <= firstLine) line += updateOn;

        String lineNum = String.valueOf(line);
        drawLine(textureCanvas, lineNum, lineHeight * i + baseline, devicePR);
      }
      lineTexture.setContent(textureCanvas);
      return Math.max(stNum, firstLine);
    }

    while (curFirstLine > firstLine) {
      updateCanvas.clear();

      String lineNum = String.valueOf(curFirstLine--);
      int yPos = (curFirstLine * lineHeight) % textureSize.y;

      drawLine(updateCanvas, lineNum, baseline, devicePR);
      lineTexture.update(updateCanvas, 0, yPos);
      if (curFirstLine % numberOfLines == 0) break;
    }
    return curFirstLine;
  }

  private void drawLine(Canvas canvas, String lineNumber, int yPos, float devicePR) {
    int padding = EditorConst.LINE_NUMBERS_RIGHT_PADDING;
    canvas.drawText(lineNumber, textureSize.x - padding * devicePR, yPos);
  }

  @Override
  public void dispose() {
    lineTexture = Disposable.assign(lineTexture, null);
  }

}
