package org.sudu.experiments.editor;

import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.ui.SetCursor;
import org.sudu.experiments.ui.UiContext;
import org.sudu.experiments.ui.window.View;

public class MiddleLine extends View {
  public static final float lineWidthDp = 2;

  public static final int middleLineThicknessDp = 20;

  public final UiContext uiContext;

  final V2i p11 = new V2i();
  final V2i p12 = new V2i();
  final V2i p21 = new V2i();
  final V2i p22 = new V2i();

  private DiffInfo diffModel;
  private DiffRef editor1, editor2;
  private EditorColorScheme theme;

  public MiddleLine(UiContext context) {
    this.uiContext = context;
  }

  public void setModel(DiffInfo diffModel) {
    this.diffModel = diffModel;
  }

  public void setLeftRight(DiffRef left, DiffRef right) {
    editor1 = left;
    editor2 = right;
  }

  public void setTheme(EditorColorScheme theme) {
    this.theme = theme;
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

  public void paint() {
    draw(uiContext.graphics);
  }

  @Override
  public void draw(WglGraphics g) {
    g.drawRect(
        pos.x, pos.y,
        size,
        theme.editor.bg);

    if (diffModel == null) return;

    int lineWidth = uiContext.toPx(lineWidthDp);

    // todo: redesign later, last should be beyond the range
    int lFirst = diffModel.rangeBinSearch(editor1.getFirstLine(), true);
    int lLast = diffModel.rangeBinSearch(editor1.getLastLine(), true);
    int rFirst = diffModel.rangeBinSearch(editor2.getFirstLine(), false);
    int rLast = diffModel.rangeBinSearch(editor2.getLastLine(), false);

    int first = Math.min(lFirst, rFirst);
    int last = Math.max(lLast, rLast);

    if (first <= last) g.enableBlend(true);

    V2i editor1Pos = editor1.pos();
    V2i editor1Size = editor1.size();
    V2i editor2Size = editor2.size();
    V2i editor2Pos = editor2.pos();

    V2i rSize = uiContext.v2i2;

    for (int i = first; i <= last; i++) {
      var range = diffModel.ranges[i];
      if (range.type == 0) continue;

      int leftY0 = editor1.lineToPos(range.fromL);
      int leftY1 = editor1.lineToPos(range.toL());

      int rightY0 = editor2.lineToPos(range.fromR);
      int rightY1 = editor2.lineToPos(range.toR());

      setLinePos(leftY0, leftY1, rightY0, rightY1);

      int rectY0 = Math.max(Math.min(leftY0, rightY0), pos.y);
      int rectY1 = Math.min(Math.max(leftY1, rightY1), pos.y + size.y);

      if (rectY1 <= rectY0) continue;
      rSize.set(size.x, rectY1 - rectY0);

      V4f color = theme.diff.getDiffColor(theme, range.type);

      if (leftY0 == leftY1) {
        drawLine(g, leftY0, rightY0, lineWidth,
            editor1Pos.x, editor1Size.x, color, p11, p21);
      }
      if (rightY0 == rightY1) {
        drawLine(g, rightY0, leftY0, lineWidth,
            editor2Pos.x, editor2Size.x, color, p12, p22);
      }
      g.drawLineFill(
          pos.x, rectY0, rSize,
          p11, p12,
          p21, p22, color
      );
    }
    if (first <= last) g.enableBlend(false);
  }

  private void drawLine(
      WglGraphics g,
      int yLeftStartPosition, int yRightStartPosition,
      int lineWidth, int editorPos, int editorSize,
      V4f color, V2i p11, V2i p21
  ) {
    V2i temp = uiContext.v2i1;
    temp.set(editorSize, lineWidth);
    int y = yLeftStartPosition;
    if (yRightStartPosition < yLeftStartPosition) {
      y -= lineWidth;
      p11.y = p11.y - lineWidth;
    } else {
      p21.y = p21.y + lineWidth;
    }
    g.drawRect(editorPos, y, temp, color);
  }

  @Override
  public void onMouseMove(MouseEvent event, SetCursor setCursor) {
    if (hitTest(event.position))
      setCursor.set(null);
  }
}
