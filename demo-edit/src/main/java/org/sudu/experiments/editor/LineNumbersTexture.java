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

  static final int numberOfLines = 20;

  // The texture colors are banked in runs of `numberOfLines` rows: number N is stored at
  // row (N-1) % numberOfLines of the bank starting at bank(N). Batching across numbers must
  // never cross a bank boundary.
  static int bank(int number) {
    return ((number - 1) / numberOfLines) * numberOfLines;
  }

  private GL.Texture lineTexture;

  private final V2i textureSize = new V2i();
  private int lineHeight;

  private final V2i rectSize = new V2i();
  private final V4f rectRegion = new V4f();

  private boolean cleartype;
  int startLine;
  int lastFrame;

  void init(
      WglGraphics g,
      int width,
      int lineHeight,
      int startLine,
      Canvas textureCanvas,
      FontDesk fontDesk,
      float devicePR
  ) {
    textureCanvas.clear();
    this.startLine = startLine;
    this.lineHeight = lineHeight;
    textureSize.x = width;
    textureSize.y = numberOfLines * lineHeight;

    int baseline = fontDesk.baselineShift(lineHeight);

    if (lineTexture == null) lineTexture = Disposable.assign(null, g.createTexture());
    for (int i = 0; i < numberOfLines; i++) {
      int number = startLine + i + 1;
      String lineNum = String.valueOf(number);
      drawLine(textureCanvas, lineNum, lineHeight * i + baseline, devicePR);
    }
    lineTexture.setContent(textureCanvas);
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

  // Number-addressed drawing. `number` is the ordinal rendered into the gutter (>= 1);
  // `colorLine` is the document line the diff/background color is taken from.
  // Draws `count` consecutive numbers starting at `firstNumber`, sharing a texture bank.
  // `firstColorLine + i` is the document line backing the i-th number.
  void drawNumberRun(
      V2i dXdY, int yPos,
      int firstNumber, int count, int firstColorLine,
      EditorColorScheme colorScheme, byte[] colors,
      WglGraphics g
  ) {
    int base = firstNumber - 1;
    if (base < startLine || base + count > startLine + numberOfLines) return;
    LineNumbersColors lineNumberScheme = colorScheme.lineNumber;
    int i = 0;
    while (i < count) {
      V4f bg = colorScheme.getDiffColor(colors, firstColorLine + i);
      int j = i + 1;
      while (j < count && colorScheme.getDiffColor(colors, firstColorLine + j) == bg) j++;
      int d = (base + i - startLine) * lineHeight;
      int h = (j - i) * lineHeight;
      rectSize.set(textureSize.x, h);
      rectRegion.set(0, d, textureSize.x, h);
      draw(g, yPos + i * lineHeight, dXdY, lineNumberScheme.textColor, bg);
      i = j;
    }
  }

  void drawCaretNumber(
      V2i dXdY, int yPos,
      int number, int colorLine,
      EditorColorScheme colorScheme, byte[] colors,
      WglGraphics g
  ) {
    int base = number - 1;
    if (base < startLine || base >= startLine + numberOfLines) return;
    int d = (base - startLine) * lineHeight;
    V4f bg = colorScheme.getDiffColor(colors, colorLine);
    rectSize.set(textureSize.x, lineHeight);
    rectRegion.set(0, d, textureSize.x, lineHeight);
    draw(g, yPos, dXdY, colorScheme.lineNumber.caretTextColor, bg);
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
    float padding = LineNumbersComponent.rightPad;
    canvas.drawText(lineNumber, textureSize.x - padding * devicePR, yPos);
  }

  @Override
  public void dispose() {
    lineTexture = Disposable.assign(lineTexture, null);
  }
}
