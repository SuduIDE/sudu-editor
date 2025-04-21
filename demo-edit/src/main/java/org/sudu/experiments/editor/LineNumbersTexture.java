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

public class LineNumbersTexture implements Disposable {

  private GL.Texture lineTexture;

  private final V2i textureSize;
  private final int numberOfLines = EditorConst.LINE_NUMBERS_TEXTURE_SIZE;
  private final int lineHeight;

  private final V2i rectSize = new V2i();
  private final V4f rectRegion = new V4f();

  private final int baseline;

  private boolean cleartype;
  int startLine;

  public LineNumbersTexture(
      int startLine,
      int textureWidth,
      int lineHeight,
      FontDesk fontDesk
  ) {
    this.startLine = startLine;
    this.lineHeight = lineHeight;
    this.textureSize = new V2i(textureWidth, numberOfLines * lineHeight);
    this.baseline = fontDesk.baselineShift(lineHeight);
  }

  void init(WglGraphics g, Canvas textureCanvas, float devicePR) {
    textureCanvas.clear();
    for (int i = 0; i < numberOfLines; i++) {
      int number = startLine + i + 1;
      String lineNum = String.valueOf(number);
      drawLine(textureCanvas, lineNum, lineHeight * i + baseline, devicePR);
    }
    if (lineTexture != null) lineTexture.setContent(textureCanvas);
    else lineTexture = Disposable.assign(null, g.createTexture(textureCanvas));
    cleartype = textureCanvas.cleartype;
  }

  public void draw(
      V2i dXdY,
      int yPos,
      int fromLine, int toLine,
      EditorColorScheme colorScheme,
      byte[] colors,
      WglGraphics g
  ) {
    if (startLine + numberOfLines < fromLine || toLine < startLine) return;
    LineNumbersColors lineNumberScheme = colorScheme.lineNumber;

    int d = (fromLine % numberOfLines) * lineHeight;
    int startLine = 0;
    V4f prevColor = colorScheme.getDiffColor(colors, fromLine);
    for (int i = 0; i < toLine - fromLine; i++) {
      V4f c = colorScheme.getDiffColor(colors, fromLine + i);
      if (c == prevColor) {
        int h = (i - startLine + 1) * lineHeight;
        rectSize.set(lineTexture.width(), h);
        rectRegion.set(0, d + startLine * lineHeight, lineTexture.width(), h);
      } else {
        draw(g, yPos + startLine * lineHeight, dXdY, lineNumberScheme.textColor, prevColor);
        prevColor = c;
        startLine = i;
        rectSize.set(lineTexture.width(), lineHeight);
        rectRegion.set(0, d + startLine * lineHeight, lineTexture.width(), lineHeight);
      }
    }
    draw(g, yPos + startLine * lineHeight, dXdY, lineNumberScheme.textColor, prevColor);
  }

  void drawCaretLine(
      V2i dXdY,
      int yPos,
      int caretLine,
      EditorColorScheme colorScheme,
      byte[] colors,
      WglGraphics g
  ) {
    if (startLine + numberOfLines < caretLine || caretLine < startLine) return;
    int d = (caretLine % numberOfLines) * lineHeight;
    var bgColor = colorScheme.getDiffColor(colors, caretLine);

    rectSize.set(textureSize.x, lineHeight);
    rectRegion.set(0, d, textureSize.x, lineHeight);

    draw(g, yPos, dXdY, colorScheme.lineNumber.caretTextColor, bgColor);
  }

  private void draw(WglGraphics g, int yPos, V2i dXdY, V4f textColor, V4f bgColor) {
    g.drawText(
        dXdY.x,
        yPos + dXdY.y,
        rectSize,
        rectRegion,
        lineTexture,
        textColor,
        bgColor,
        cleartype
    );
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
