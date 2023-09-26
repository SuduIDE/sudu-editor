package org.sudu.experiments.demo;

import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.demo.ui.UiContext;
import org.sudu.experiments.demo.ui.colors.EditorColorScheme;
import org.sudu.experiments.demo.worker.diff.DiffInfo;
import org.sudu.experiments.math.V2i;

import static org.sudu.experiments.demo.Diff0.lineWidthDp;

public class MiddleLine {
  public static final int middleLineThicknessDp = 20;
  public final V2i pos = new V2i();
  public final V2i size = new V2i();
  public final V2i p11 = new V2i();
  public final V2i p12 = new V2i();
  public final V2i p21 = new V2i();
  public final V2i p22 = new V2i();

  public MiddleLine() {
  }

  private void setLinePos(
      int yLeftStartPosition, int yLeftLastPosition,
      int yRightStartPosition, int yRightLastPosition
  ) {
    p11.set(pos.x, yLeftStartPosition);
    p21.set(pos.x, yLeftLastPosition);
    p12.set(pos.x + size.x, yRightStartPosition);
    p22.set(pos.x + size.x, yRightLastPosition);
  }

  void draw(
      DiffInfo diffModel,
      UiContext uiContext,
      EditorComponent editor1,
      EditorComponent editor2,
      DemoRect rect,
      EditorColorScheme theme
  ) {
    var g = uiContext.graphics;

    g.drawRect(
        pos.x, pos.y,
        size,
        theme.editor.bg);

    if (diffModel == null || diffModel.ranges == null) return;
    int lineWidth = uiContext.toPx(lineWidthDp);

    int leftStartLine = editor1.getFirstLine();
    int leftLastLine = editor1.getLastLine();
    int rightStartLine = editor2.getFirstLine();
    int rightLastLine = editor2.getLastLine();

    int leftStartRangeInd = diffModel.rangeBinSearch(leftStartLine, true);
    int leftLastRangeInd = diffModel.rangeBinSearch(leftLastLine, true);
    int rightStartRangeInd = diffModel.rangeBinSearch(rightStartLine, false);
    int rightLastRangeInd = diffModel.rangeBinSearch(rightLastLine, false);

    for (int i = Math.min(leftStartRangeInd, rightStartRangeInd);
         i <= Math.max(leftLastRangeInd, rightLastRangeInd); i++) {
      var range = diffModel.ranges[i];
      if (range.type == 0) continue;

      int yLeftStartPosition = editor1.lineHeight * range.fromL - editor1.vScrollPos;
      int yLeftLastPosition = yLeftStartPosition + range.lenL * editor1.lineHeight;

      int yRightStartPosition = editor2.lineHeight * range.fromR - editor2.vScrollPos;
      int yRightLastPosition = yRightStartPosition + range.lenR * editor2.lineHeight;

      setLinePos(
          yLeftStartPosition, yLeftLastPosition,
          yRightStartPosition, yRightLastPosition
      );

      int rectY = Math.min(yLeftStartPosition, yRightStartPosition);
      int rectW = Math.max(yLeftLastPosition, yRightLastPosition) - rectY;

      rect.set(pos.x, rectY, size.x, rectW);
      rect.bgColor.set(theme.lineNumber.bgColor);
      rect.color.set(theme.diff.getDiffColor(theme, range.type));

      g.enableBlend(true);
      if (yLeftStartPosition == yLeftLastPosition) {
        drawLeftLine(g, yLeftStartPosition, yRightStartPosition,
            lineWidth, uiContext, editor1, rect);
      }
      if (yRightStartPosition == yRightLastPosition) {
        drawRightLine(g, yRightStartPosition, yLeftStartPosition,
            lineWidth, uiContext, editor2, rect);
      }
      g.drawLineFill(
          rect.pos.x, rect.pos.y, rect.size,
          p11, p12,
          p21, p22, rect.color
      );
      g.enableBlend(false);
    }

  }

  private void drawLeftLine(
      WglGraphics g,
      int yLeftStartPosition,
      int yRightStartPosition,
      int lineWidth,
      UiContext uiContext,
      EditorComponent editor,
      DemoRect rect
  ) {
    V2i temp = uiContext.v2i1;
    temp.set(pos.x - editor.pos.x, lineWidth);
    int y = yLeftStartPosition;
    if (yRightStartPosition < yLeftStartPosition) {
      y -= lineWidth;
      p11.set(p11.x, p11.y - lineWidth);
    } else p21.set(p21.x, p21.y + lineWidth);
    g.drawRect(editor.pos.x, y, temp, rect.color);
  }

  private void drawRightLine(
      WglGraphics g,
      int yRightStartPosition,
      int yLeftStartPosition,
      int lineWidth,
      UiContext uiContext,
      EditorComponent editor,
      DemoRect rect
  ) {
    V2i temp = uiContext.v2i1;
    temp.set(editor.size.x, lineWidth);
    int y = yRightStartPosition;
    if (yLeftStartPosition < yRightStartPosition) {
      y -= lineWidth;
      p12.set(p12.x, p12.y - lineWidth);
    } else p22.set(p22.x, p22.y + lineWidth);
    g.drawRect(pos.x + size.x, y, temp, rect.color);
  }
}
