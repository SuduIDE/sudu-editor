package org.sudu.experiments.editor;

import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.UiContext;

import static org.sudu.experiments.editor.Diff0.lineWidthDp;

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

    int leftFirst = diffModel.rangeBinSearch(editor1.getFirstLine(), true);
    int leftLast = diffModel.rangeBinSearch(editor1.getLastLine(), true);
    int rightFirst = diffModel.rangeBinSearch(editor2.getFirstLine(), false);
    int rightLast = diffModel.rangeBinSearch(editor2.getLastLine(), false);

    int first = Math.min(leftFirst, rightFirst);
    int last = Math.max(leftLast, rightLast);

    if (first <= last) g.enableBlend(true);

    for (int i = first; i <= last; i++) {
      var range = diffModel.ranges[i];
      if (range.type == 0) continue;

      int leftY0 = editor1.lineHeight * range.fromL - editor1.vScrollPos;
      int leftY1 = leftY0 + range.lenL * editor1.lineHeight;

      int rightY0 = editor2.lineHeight * range.fromR - editor2.vScrollPos;
      int rightY1 = rightY0 + range.lenR * editor2.lineHeight;

      setLinePos(leftY0, leftY1, rightY0, rightY1);

      int rectY0 = Math.max(Math.min(leftY0, rightY0), pos.y);
      int rectY1 = Math.min(Math.max(leftY1, rightY1), pos.y + size.y);

      rect.set(pos.x, rectY0, size.x, rectY1 - rectY0);
      rect.bgColor.set(theme.lineNumber.bgColor);
      rect.color.set(theme.diff.getDiffColor(theme, range.type));

      if (leftY0 == leftY1) {
        drawLeftLine(g, leftY0, rightY0,
            lineWidth, uiContext, editor1, rect);
      }
      if (rightY0 == rightY1) {
        drawRightLine(g, rightY0, leftY0,
            lineWidth, uiContext, editor2, rect);
      }
      g.drawLineFill(
          rect.pos.x, rect.pos.y, rect.size,
          p11, p12,
          p21, p22, rect.color
      );
    }
    if (first <= last) g.enableBlend(false);
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
