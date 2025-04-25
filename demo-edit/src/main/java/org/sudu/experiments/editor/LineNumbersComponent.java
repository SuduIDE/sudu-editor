package org.sudu.experiments.editor;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.Disposable;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.Rect;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.SetCursor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class LineNumbersComponent implements Disposable {

  private final int numberOfLines = EditorConst.LINE_NUMBERS_TEXTURE_SIZE;

  public final V2i pos = new V2i();
  public final V2i size = new V2i();
  public final V2i bottomSize = new V2i();
  public float dpr;

  private FontDesk fontDesk;
  private boolean cleartype;
  private int lineHeight;
  private int textureHeight;

  private byte[] colors = new byte[0];
  private Canvas textureCanvas;

  private final List<LineNumbersTexture> textures = new ArrayList<>();
  private final Deque<LineNumbersTexture> old = new ArrayDeque<>();

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

  public void drawEditorLines(
      int yPos,
      int firstLine, int lastLine,
      int caretLine,
      WglGraphics g, EditorColorScheme scheme
  ) {
    ensureCanvas(g);
    if (textures.size() + old.size() >= 10) filterTextures(firstLine, lastLine);
    g.enableScissor(pos, size);
    doDrawRange(yPos, firstLine, lastLine, g, scheme);
    int dY = yPos + (lastLine - firstLine) * lineHeight;
    drawBottom(dY, size.y, g, scheme);
    drawCaretLine(yPos, firstLine, caretLine, scheme, g);
    g.disableScissor();
  }

  // range = {yPos, firstLine, lastLine}
  public void drawRanges(
      int[][] ranges,
      WglGraphics g, EditorColorScheme scheme
  ) {
    ensureCanvas(g);
    if (textures.size() + old.size() >= 10) filterTextures(ranges[0][0], ranges[ranges.length - 1][1]);
    g.enableScissor(pos, size);
    for (var range: ranges) {
      int yPos = range[0];
      int firstLine = range[1];
      int lastLine = range[2];
      doDrawRange(yPos, firstLine, lastLine, g, scheme);
    }
    g.disableScissor();
  }

  public void drawBottom(
      int textHeight, int editorBottom,
      WglGraphics g, EditorColorScheme scheme
  ) {
    var bgColor = scheme.editor.bg;
    bottomSize.x = size.x;
    bottomSize.y = editorBottom - textHeight;
    if (textHeight < editorBottom)
      g.drawRect(pos.x, pos.y + textHeight, bottomSize, bgColor);
  }

  private void doDrawRange(
      int yPos,
      int firstLine, int lastLine,
      WglGraphics g, EditorColorScheme scheme
  ) {
    int dY = yPos;
    for (int i = firstLine; i < lastLine; ) {
      int ind = i / numberOfLines;
      int startLine = ind * numberOfLines;
      var texture = texture(g, startLine);
      int begin = Math.max(startLine, firstLine);
      int end = Math.min(startLine + numberOfLines, lastLine);
      texture.draw(pos, dY, begin, end, scheme, colors, g);
      dY += (end - begin) * lineHeight;
      i = (ind + 1) * numberOfLines;
    }
  }

  public void drawCaretLine(
      int yPos,
      int firstLine, int caretLine,
      EditorColorScheme colorScheme,
      WglGraphics g
  ) {
    var texture = texture(g, caretLine);
    int dY = yPos + (caretLine - firstLine) * lineHeight;
    texture.drawCaretLine(pos, dY, caretLine, colorScheme, colors, g);
  }

  private void filterTextures(int firstLine, int lastLine) {
    for (var texture: textures) {
      if (lastLine < texture.startLine || texture.startLine + numberOfLines < firstLine) {
        old.add(texture);
      }
    }
    textures.removeAll(old);
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
    for (var t: textures) t.dispose();
    textures.clear();
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
