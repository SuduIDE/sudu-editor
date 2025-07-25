package org.sudu.experiments.editor;

import org.sudu.experiments.Cursor;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.arrays.ObjPool;
import org.sudu.experiments.editor.ui.colors.DiffColors;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffRange;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.ui.SetCursor;
import org.sudu.experiments.ui.UiContext;
import org.sudu.experiments.ui.window.View;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class MiddleLine extends View {
  public static final boolean debug = true;
  public static final float lineWidthDp = 2;
  public static final float syncLineWidthDp = 3;

  public static final int middleLineThicknessDp = 20;
  public static final int syncLineHitThreshold = 5;

  public final UiContext uiContext;

  final V2i p11 = new V2i();
  final V2i p12 = new V2i();
  final V2i p21 = new V2i();
  final V2i p22 = new V2i();

  private DiffInfo diffModel;
  private DiffRef editor1, editor2;
  private Color bgColor, syncPointColor, currentSyncColor, hoverSyncColor;
  private Color midLineHoverSyncPointColor;
  private DiffColors diffColors;

  private SyncPoints syncPoints;
  private int hoverSyncPoint = -1;
  private int editedSyncL = -1, editedSyncR = -1;
  private boolean editingLeftGreen;

  private IntConsumer onMidSyncPointHover;
  private IntConsumer onMidSyncPointClick;
  private final ObjPool<Visible> visible =
      new ObjPool<>(new Visible[2], Visible::new);
  private final ObjPool<Visible> alignLines =
      new ObjPool<>(new Visible[4], Visible::new);

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

  public void setTheme(
      DiffColors diffColors, Color bgColor, Color syncLineColor,
      Color midLineHoverSyncPointColor, Color currentSyncPoint, Color hoverSyncPoint
  ) {
    this.diffColors = diffColors;
    this.bgColor = bgColor;
    this.syncPointColor = syncLineColor;
    this.midLineHoverSyncPointColor = midLineHoverSyncPointColor;
    this.currentSyncColor = currentSyncPoint;
    this.hoverSyncColor = hoverSyncPoint;
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
    if (visible.size() == 0 && alignLines.size() == 0
        && editedSyncL == -1 && editedSyncR == -1) return;

    g.enableBlend(true);

    V2i editor1Pos = editor1.pos();
    V2i editor2Pos = editor2.pos();
    V2i editor1Size = editor1.size();
    V2i editor2Size = editor2.size();

    V2i rSize = uiContext.v2i2;

    int lineWidth = uiContext.toPx(lineWidthDp);

    var vis = visible.data();
    for (int i = 0, n = visible.size(); i < n; i++) {
      Visible vr = vis[i];
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

    drawSyncPoints(g);
    g.enableBlend(false);
  }

  private void drawSyncPoints(WglGraphics g) {
    int nLines = alignLines.size();
    int slw1 = editor1.getSyncLineWidth();
    int slw2 = editor2.getSyncLineWidth();
    int lineWidth = uiContext.toPx(syncLineWidthDp);
    V2i rSize = uiContext.v2i2;

    if (nLines > 0) {
      var alData = alignLines.data();
      for (int i = 0; i < nLines; i++) {
        Visible aLine = alData[i];

        int leftY = editor1.lineToPos(aLine.fromL);
        int rightY = editor2.lineToPos(aLine.fromR);
        int leftY1 = leftY + lineWidth;
        int rightY1 = rightY + lineWidth;

        setLinePos(leftY, leftY1, rightY, rightY1);

        int rectY0 = Math.max(Math.min(leftY, rightY), pos.y);
        int rectY1 = Math.min(Math.max(leftY1, rightY1), pos.y + size.y);
        if (debug) if (rectY1 <= rectY0) {
          System.out.println("(rectY1 <= rectY0)");
          continue;
        }

        int t = lineWidth / 2;

        if (leftY > rightY) {
          p12.x -= t;
          p21.x += lineWidth - t;
        } else if (leftY < rightY) {
          p11.x += t;
          p22.x -= lineWidth - t;
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

        // draw left right lines
        V2i temp = uiContext.v2i1;
        temp.x = slw1;
        temp.y = p21.y - p11.y;
        g.drawRect(p11.x - slw1, p11.y, temp, color);

        temp.x = slw2;
        temp.y = p22.y - p12.y;
        g.drawRect(p12.x, p12.y, temp, color);
      }
    }

    if (editedSyncL != -1) {
      int leftY = editor1.lineToPos(editedSyncL);
      rSize.x = slw1;
      rSize.y = lineWidth;
      g.drawRect(pos.x - slw1, leftY, rSize,
          editingLeftGreen ? currentSyncColor : hoverSyncColor);
    }

    if (editedSyncR != -1) {
      int rightY = editor2.lineToPos(editedSyncR);
      rSize.x = slw2;
      rSize.y = lineWidth;
      g.drawRect(pos.x + size.x, rightY, rSize,
          editingLeftGreen ? hoverSyncColor : currentSyncColor);
    }
  }

  private void drawLine(
      WglGraphics g,
      int yLeftStartPosition, int yRightStartPosition,
      int lineWidth, int x, int width,
      V4f color, V2i p11, V2i p21
  ) {
    V2i temp = uiContext.v2i1;
    temp.set(width, lineWidth);
    int y = yLeftStartPosition;
    if (yRightStartPosition < yLeftStartPosition) {
      y -= lineWidth;
      p11.y = p11.y - lineWidth;
    } else {
      p21.y = p21.y + lineWidth;
    }
    g.drawRect(x, y, temp, color);
  }

  public void setSyncPoints(SyncPoints syncPoints) {
    this.syncPoints = syncPoints;
  }

  private void computeVisible() {
    CompactCodeMapping cml = diffModel.codeMappingL;
    CompactCodeMapping cmr = diffModel.codeMappingR;

    // todo: redesign later, last should be beyond the range
    int lFirst = editor1.getFirstLine();
    int lLast = editor1.getLastLine();
    int rFirst = editor2.getFirstLine();
    int rLast = editor2.getLastLine();

    visible.clear();
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
      if (lVis || rVis) {
        Visible r = visible.add();
        r.fromL = fromL;
        r.lenL = lenL;
        r.fromR = fromR;
        r.lenR = lenR;
        r.type = range.type;
      }
    }

    alignLines.clear();

    if (syncPoints == null) return;
    int[] syncL = syncPoints.syncL, syncR = syncPoints.syncR;

    for (int i = 0; i < syncL.length; i++) {
      int lineL = syncL[i];
      int lineR = syncR[i];
      if (cml != null)
        lineL = cml.docToViewCursor(lineL);
      if (cmr != null)
        lineR = cmr.docToViewCursor(lineR);

      if (lineL < lFirst && lineR < rFirst ||
          lineL > lLast && lineR > rLast
      ) continue;
      Visible syncRecord = alignLines.add();
      syncRecord.fromL = lineL;
      syncRecord.fromR = lineR;
    }

    int curL = syncPoints.curL;
    int curR = syncPoints.curR;
    int edited2 = syncPoints.hoverSyncPoint;

    editedSyncL = -1;
    editedSyncR = -1;

    if (curL != -1) {
      editingLeftGreen = true;
      if (cml != null) curL = cml.docToViewCursor(curL);
      if (lFirst <= curL && curL <= lLast) editedSyncL = curL;

      if (edited2 != -1) {
        if (cmr != null) edited2 = cmr.docToViewCursor(edited2);
        if (rFirst <= edited2 && edited2 <= rLast) editedSyncR = edited2;
      }
    }

    if (curR != -1) {
      editingLeftGreen = false;
      if (cmr != null) curR = cmr.docToViewCursor(curR);
      if (rFirst <= curR && curR <= rLast) editedSyncR = curR;
      if (edited2 != -1) {
        edited2 = cml != null ? cml.docToViewCursor(edited2) : edited2;
        if (lFirst <= edited2 && edited2 <= lLast) editedSyncL = edited2;
      }
    }
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
  protected Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    return MouseListener.Static.emptyConsumer;
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
    if (syncPoints == null) return -1;
    double minD = Double.POSITIVE_INFINITY;
    int minInd = -1;
    int lineWidth = uiContext.toPx(lineWidthDp);
    int lineWidthHalf = lineWidth / 2;
    int nLines = alignLines.size();
    var alData = alignLines.data();
    for (int i = 0; i < nLines; i++) {
      Visible aLine = alData[i];

      int leftY = editor1.lineToPos(aLine.fromL),
          leftY0 = leftY - lineWidthHalf,
          leftY1 = leftY0 + lineWidth;

      int rightY = editor2.lineToPos(aLine.fromR),
          rightY0 = rightY - lineWidthHalf,
          rightY1 = rightY0 + lineWidth;

      int rectY0 = Math.max(Math.min(leftY0, rightY0), pos.y);
      int rectY1 = Math.min(Math.max(leftY1, rightY1), pos.y + size.y);

      double curD;
      if (rectY0 <= position.y && position.y <= rectY1) {
        curD = dist(position.x, position.y, pos.x, leftY, pos.x + size.x, rightY);
      } else {
        curD = Math.min(
            dist(position.x, position.y, pos.x, leftY),
            dist(position.x, position.y, pos.x + size.x, rightY)
        );
      }

      if (curD <= syncLineHitThreshold * lineWidth && curD < minD) {
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

  private double dist(
      int px, int py,
      int x0, int y0
  ) {
    return Math.hypot(x0 - px, y0 - py);
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
