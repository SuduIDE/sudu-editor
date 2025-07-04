package org.sudu.experiments.editor;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.Disposable;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.ui.colors.LineNumbersColors;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.Rect;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.SetCursor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class LineNumbersComponent implements Disposable {

  private final static boolean debugTexture = false;
  private final int numberOfLines = EditorConst.LINE_NUMBERS_TEXTURE_SIZE;

  public final V2i pos = new V2i();
  public final V2i size = new V2i();
  private final V2i bottomSize = new V2i();
  private final V2i syncLineSize = new V2i(0, EditorConst.SYNC_LINE_HEIGHT);
  public float dpr;

  private FontDesk fontDesk;
  private boolean cleartype;
  private boolean mirrored;
  private int mergeButtonsWidth;
  private int lineHeight;
  private int textureHeight;

  private byte[] colors = new byte[0];
  private Canvas textureCanvas;

  private final List<LineNumbersTexture> textures = new ArrayList<>();
  private final Deque<LineNumbersTexture> old = new ArrayDeque<>();

  private int frameId;

  public void setPosition(int x, int y, int width, int height, float dpr) {
    pos.set(x, y);
    size.set(width, height);
    this.dpr = dpr;
    old.addAll(textures);
    textures.clear();
  }

  public int width() {
    return size.x;
  }

  public void setColors(byte[] c) {
    this.colors = c == null ? new byte[0] : c;
  }

  byte[] colors() {
    return colors;
  }

  public void drawRange(
      int dY,
      int firstLine, int lastLine,
      WglGraphics g, EditorColorScheme scheme
  ) {
    for (int i = firstLine; i < lastLine; ) {
      int ind = i / numberOfLines;
      int startLine = ind * numberOfLines;
      var texture = texture(g, startLine);
      int begin = Math.max(startLine, firstLine);
      int end = Math.min(startLine + numberOfLines, lastLine);
      texture.draw(pos, dY, begin, end, scheme, colors, g);
      texture.lastFrame = frameId;
      dY += (end - begin) * lineHeight;
      i = (ind + 1) * numberOfLines;
    }
  }

  public void drawEmptyLines(
      int yFrom, int yTo,
      WglGraphics g, EditorColorScheme scheme
  ) {
    var bgColor = scheme.editor.bg;
    bottomSize.x = size.x;
    bottomSize.y = yTo - yFrom;
    if (yFrom < size.y)
      g.drawRect(pos.x, pos.y + yFrom, bottomSize, bgColor);
  }

  public void drawEmptyLines(
      int yPos,
      WglGraphics g, EditorColorScheme scheme
  ) {
    drawEmptyLines(yPos, size.y, g, scheme);
  }

  public void drawCaretLine(
      int dY,
      int caretViewLine, int caretDocLine,
      EditorColorScheme colorScheme,
      WglGraphics g
  ) {
    int y0 = dY + caretViewLine * lineHeight;
    int y1 = y0 + lineHeight;
    var intersect = y0 < size.y && 0 < y1;
    if (intersect) {
      var texture = texture(g, caretDocLine);
      texture.drawCaretLine(pos, y0, caretDocLine, colorScheme, colors, g);
    }
  }

  public void beginDraw(WglGraphics g, int frameId) {
    ensureCanvas(g);
    g.enableScissor(pos, size);
    this.frameId = frameId;
    for (var texture: textures) {
      if (frameId - texture.lastFrame >= 2) {
        old.add(texture);
      }
    }
    textures.removeAll(old);
  }

  public void endDraw(WglGraphics g) {
    g.disableScissor();
  }


  // todo: move to MiddleLine
  public void drawSyncPoints(
      int yPos,
      int firstLine, int lastLine,
      CodeLineMapping mapping,
      int curSyncPoint,
      int hoverSyncPoint,
      int midLineHoverSyncPoint,
      WglGraphics g, LineNumbersColors scheme
  ) {
    int viewCurSyncPoint = docToViewCursor(mapping, curSyncPoint);
    if (firstLine <= viewCurSyncPoint && viewCurSyncPoint <= lastLine)
      drawSyncLine(yPos, viewCurSyncPoint, firstLine, scheme.currentSyncPoint, g);
    int viewHoverSyncPoint = docToViewCursor(mapping, hoverSyncPoint);
    if (firstLine <= viewHoverSyncPoint && viewHoverSyncPoint <= lastLine)
      drawSyncLine(yPos, viewHoverSyncPoint, firstLine, scheme.hoverSyncPoint, g);
    int viewMidLineHoverSyncPoint = docToViewCursor(mapping, midLineHoverSyncPoint);
    if (firstLine <= viewMidLineHoverSyncPoint && viewMidLineHoverSyncPoint <= lastLine)
      drawSyncLine(yPos, viewMidLineHoverSyncPoint, firstLine, scheme.midLineHoverSyncPoint, g);
  }

  private int docToViewCursor(CodeLineMapping mapping, int sp) {
    if (mapping == null) return sp;
    return mapping.docToViewCursor(sp);
  }

  private void drawSyncLine(
      int yPos,
      int syncLine,
      int startLine,
      Color lineColor,
      WglGraphics g
  ) {
    syncLineSize.x = width();
    if (!mirrored) syncLineSize.x += mergeButtonsWidth;
    int y = yPos + (syncLine - startLine) * lineHeight - (EditorConst.SYNC_LINE_HEIGHT / 2);
    g.drawRect(pos.x, y + pos.y, syncLineSize, lineColor);
  }

  private LineNumbersTexture texture(WglGraphics g, int line) {
    int startLine = (line / numberOfLines) * numberOfLines;
    for (var texture: textures) {
      if (texture.startLine == startLine) return texture;
    }
    LineNumbersTexture texture;
    if (!old.isEmpty()) {
      texture = old.removeFirst();
    } else {
      if (debugTexture) System.out.println("Created new texture");
      texture = new LineNumbersTexture();
    }
    texture.init(g, width(), lineHeight, startLine, textureCanvas, fontDesk, dpr);
    textures.add(texture);
    return texture;
  }

  public void setFont(FontDesk font, int lineHeight, boolean cleartype) {
    this.fontDesk = font;
    this.lineHeight = lineHeight;
    this.textureHeight = lineHeight * numberOfLines;
    this.cleartype = cleartype;
    disposeCanvas();
    old.addAll(textures);
    textures.clear();
  }

  public void setMirrored(boolean mirrored) {
    this.mirrored = mirrored;
  }

  public void setMergeButtonsWidth(int mergeButtonsWidth) {
    this.mergeButtonsWidth = mergeButtonsWidth;
  }

  private void ensureCanvas(WglGraphics g) {
    if (textureCanvas == null || textureCanvas.cleartype != cleartype) {
      textureCanvas = Disposable.assign(textureCanvas,
          g.createCanvas(size.x, textureHeight, cleartype));
      textureCanvas.setFont(fontDesk);
      textureCanvas.setTextAlign(Canvas.TextAlign.RIGHT);
    }
  }

  @Override
  public void dispose() {
    for (LineNumbersTexture t : old)
      t.dispose();

    for (var t: textures)
      t.dispose();
    textures.clear();
    old.clear();
    disposeCanvas();
  }

  private void disposeCanvas() {
    textureCanvas = Disposable.assign(textureCanvas, null);
  }

  public boolean onMouseMove(MouseEvent event, SetCursor setCursor) {
    return hitTest(event.position) && setCursor.setDefault();
  }

  public boolean hitTest(V2i point) {
    return Rect.isInside(point, pos, size);
  }

}
