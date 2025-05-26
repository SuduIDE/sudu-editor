package org.sudu.experiments.editor;

import org.sudu.experiments.Cursor;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.editor.ui.colors.DiffColors;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffRange;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.ui.SetCursor;
import org.sudu.experiments.ui.UiContext;
import org.sudu.experiments.ui.window.View;

import java.util.function.IntConsumer;
import java.util.Arrays;

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
  private Color bgColor;
  private Color syncPointColor;
  private Color midLineHoverSyncPointColor;
  private DiffColors diffColors;

  private int[] syncL, syncR;
  private int hoverSyncPoint = -1;

  private IntConsumer onMidSyncPointHover;
  private IntConsumer onMidSyncPointClick;
  private Visible[] visible = new Visible[4];
  private int nVisible;

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

  public void setTheme(DiffColors diffColors, Color bgColor, Color syncLineColor, Color midLineHoverSyncPointColor) {
    this.diffColors = diffColors;
    this.bgColor = bgColor;
    this.syncPointColor = syncLineColor;
    this.midLineHoverSyncPointColor = midLineHoverSyncPointColor;
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
        bgColor);

    if (diffModel == null || diffColors == null) return;

    computeVisible();
    if (nVisible == 0) return;

    g.enableBlend(true);

    V2i editor1Pos = editor1.pos();
    V2i editor2Pos = editor2.pos();
    V2i editor1Size = editor1.size();
    V2i editor2Size = editor2.size();

    V2i rSize = uiContext.v2i2;

    int lineWidth = uiContext.toPx(lineWidthDp);

    for (int i = 0; i < nVisible; i++) {
      Visible vr = visible[i];
      int leftY0 = editor1.lineToPos(vr.fromL);
      int leftY1 = editor1.lineToPos(vr.fromL + vr.lenL);

      int rightY0 = editor2.lineToPos(vr.fromR);
      int rightY1 = editor2.lineToPos(vr.fromR + vr.lenR);

      setLinePos(leftY0, leftY1, rightY0, rightY1);

      int rectY0 = Math.max(Math.min(leftY0, rightY0), pos.y);
      int rectY1 = Math.min(Math.max(leftY1, rightY1), pos.y + size.y);

      if (rectY1 <= rectY0) continue;
      rSize.set(size.x, rectY1 - rectY0);

      V4f color = diffColors.getDiffColor(vr.type, bgColor);

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

    drawSyncPoints(g, rSize);
    if (first <= last) g.enableBlend(false);
  }

  private void drawSyncPoints(WglGraphics g, V2i rSize) {
    if (syncL == null || syncR == null) return;
    for (int i = 0; i < syncL.length; i++) {
      int lineL = syncL[i];
      int lineR = syncR[i];
      int d = EditorConst.SYNC_LINE_HEIGHT / 2;

      int leftY = editor1.lineToPos(lineL),
          leftY0 = leftY - d,
          leftY1 = leftY0 + EditorConst.SYNC_LINE_HEIGHT;

      int rightY = editor2.lineToPos(lineR),
          rightY0 = rightY - d,
          rightY1 = rightY0 + EditorConst.SYNC_LINE_HEIGHT;

      setLinePos(leftY0, leftY1, rightY0, rightY1);

      int rectY0 = Math.max(Math.min(leftY0, rightY0), pos.y);
      int rectY1 = Math.min(Math.max(leftY1, rightY1), pos.y + size.y);
      if (rectY1 <= rectY0) continue;

      int x = EditorConst.SYNC_LINE_HEIGHT;

      if (leftY > rightY) {
        p12.x -= x;
        p21.x += x;
      } else if (leftY < rightY) {
        p11.x += x;
        p22.x -= x;
      }

      rSize.set(size.x, rectY1 - rectY0);
      var color = i == hoverSyncPoint
          ? midLineHoverSyncPointColor
          : syncPointColor;

      g.drawLineFill(
          pos.x, rectY0, rSize,
          p11, p12,
          p21, p22, color
      );
    }
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

  public void setSyncLines(int[] syncL, int[] syncR) {
    this.syncL = syncL;
    this.syncR = syncR;
  }
  private void computeVisible() {
    CompactCodeMapping cml = diffModel.codeMappingL;
    CompactCodeMapping cmr = diffModel.codeMappingR;

    // todo: redesign later, last should be beyond the range
    int lFirst = editor1.getFirstLine();
    int lLast = editor1.getLastLine();
    int rFirst = editor2.getFirstLine();
    int rLast = editor2.getLastLine();

    nVisible = 0;
    DiffRange[] ranges = diffModel.ranges;
    for (DiffRange range : ranges) {
      if (range.type == 0) continue;
      int fromL = cml != null ? cml.docToView(range.fromL) : range.fromL;
      if (fromL < 0) continue;
      int fromR = cmr != null ? cmr.docToView(range.fromR) : range.fromR;
      if (fromR < 0) continue;
      int lenL = range.lenL, lenR = range.lenR;

      boolean lVis = lFirst <= fromL + lenL && fromL <= lLast;
      boolean rVis = rFirst <= fromR + lenR && fromR <= rLast;
      if (lVis || rVis)
        addVisible(fromL, lenL, fromR, lenR, range.type);
    }
  }

  void addVisible(int fromL, int lenL, int fromR, int lenR, int type) {
    if (nVisible == visible.length)
      visible = Arrays.copyOf(visible, visible.length * 2);
    Visible r = visible[nVisible];
    if (r == null) visible[nVisible] = r = new Visible();
    r.fromL = fromL;
    r.lenL = lenL;
    r.fromR = fromR;
    r.lenR = lenR;
    r.type = type;
    nVisible++;
  }

  @Override
  public void onMouseMove(MouseEvent event, SetCursor setCursor) {
    if (hitTest(event.position)) {
      if (checkSyncPointsHover(event)) {
        setCursor.set(Cursor.pointer);
      } else {
        setCursor.set(null);
        onSyncLineHover(-1);
      }
    } else {
      onSyncLineHover(-1);
    }
  }

  @Override
  protected boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    if (hitTest(event.position)) {
      int syncPoint = getSyncPointInd(event.position);
      if (syncPoint != -1) {
        onSyncLineClick(syncPoint);
        return true;
      }
    }
    return false;
  }

  private boolean checkSyncPointsHover(MouseEvent event) {
    int syncPoint = getSyncPointInd(event.position);
    onSyncLineHover(syncPoint);
    return syncPoint != -1;
  }

  private int getSyncPointInd(V2i position) {
    if (syncL == null || syncR == null) return -1;
    double minD = Double.POSITIVE_INFINITY;
    int minInd = -1;
    for (int i = 0; i < syncL.length; i++) {
      int lineL = syncL[i];
      int lineR = syncR[i];
      int d = EditorConst.SYNC_LINE_HEIGHT / 2;

      int leftY = editor1.lineToPos(lineL),
          leftY0 = leftY - d,
          leftY1 = leftY0 + EditorConst.SYNC_LINE_HEIGHT;

      int rightY = editor2.lineToPos(lineR),
          rightY0 = rightY - d,
          rightY1 = rightY0 + EditorConst.SYNC_LINE_HEIGHT;

      int rectY0 = Math.max(Math.min(leftY0, rightY0), pos.y);
      int rectY1 = Math.min(Math.max(leftY1, rightY1), pos.y + size.y);
      if (rectY1 <= rectY0) continue;

      double curD = dist(position.x, position.y, pos.x, leftY, pos.x + size.x, rightY);
      if (curD <= 10 * d && curD < minD) {
        minD = curD;
        minInd = i;
      }
    }
    return minInd;
  }

  private double dist(
      int px, int py,
      int x0, int y0,
      int x1, int y1
  ) {
    int numerator = Math.abs((x1 - x0) * (y0 - py) - (y1 - y0) * (x0 - px));
    double denominator = Math.hypot(y1 - y0, x1 - x0);
    return numerator / denominator;
  }

  private void onSyncLineHover(int line) {
    hoverSyncPoint = line;
    if (onMidSyncPointHover != null) onMidSyncPointHover.accept(line);
  }

  private void onSyncLineClick(int line) {
    if (onMidSyncPointClick != null) onMidSyncPointClick.accept(line);
  }

  public void setOnMidSyncPointHover(IntConsumer onMidSyncPointHover) {
    this.onMidSyncPointHover = onMidSyncPointHover;
  }

  public void setOnMidSyncPointClick(IntConsumer onMidSyncPointClick) {
    this.onMidSyncPointClick = onMidSyncPointClick;
  }

  static class Visible {
    int fromL, lenL, fromR, lenR;
    int type;
  }
}
