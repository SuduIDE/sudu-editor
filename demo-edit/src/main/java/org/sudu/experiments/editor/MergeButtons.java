package org.sudu.experiments.editor;

import org.sudu.experiments.*;
import org.sudu.experiments.editor.ui.colors.MergeButtonsColors;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.*;
import org.sudu.experiments.ui.SetCursor;
import org.sudu.experiments.ui.WindowPaint;
import org.sudu.experiments.ui.fonts.Codicons;

import java.util.function.Consumer;

public class MergeButtons implements Disposable {

  static final char arrowL = '≪';
  static final char arrowR = '≫';
  static final char arrowR1 = '→';
  static final char arrowL1 = '←';

  public final V2i pos = new V2i();
  public final V2i size = new V2i();
  public float dpr;

  private Runnable[] actions;
  private int[] lines;
  private byte[] colors;

  private GL.Texture texture;

  private final V2i bSize = new V2i();
  private final V2i debug = new V2i();
  private int lineHeight, scrollPos;
  private int firstLine, lastLine;
  private int hoverBtLine = -1, hoverBtIndex = -1;
  private final boolean drawBg;
  private boolean toLeft;
  private FontDesk font;

  private CodeLineMapping lineMapping;

  static final boolean drawFrames = false;

  public MergeButtons() {
    this.drawBg = false;
  }

  public MergeButtons(boolean drawBg) {
    this.drawBg = drawBg;
  }

  void setCodeLineMapping(CodeLineMapping mapping) {
    lineMapping = mapping;
  }

  public void setPosition(int x, int y, int width, int height, float dpr) {
    pos.set(x, y);
    size.set(width, height);
    this.dpr = dpr;
  }

  public void setFont(int lineHeight, boolean rtl, FontDesk font) {
    this.lineHeight = lineHeight;
    this.toLeft = rtl;
    this.font = font;
//    System.out.println("MergeButtons.setFont " + font.name + " lh=" + lineHeight);
    texture = Disposable.dispose(texture);
  }

  public void setModel(Runnable[] actions, int[] lines) {
    this.lines = lines;
    this.actions = actions;
  }

  public void setColors(byte[] c) {
    this.colors = c == null ? new byte[0] : c;
  }

  public void setScrollPos(int scrollPos) {
    this.scrollPos = scrollPos;
  }

  public void draw(
      int firstLine, int lastLine, int caretLine,
      WglGraphics g, MergeButtonsColors theme, ClrContext c,
      int[] viewToDocMap
  ) {
//    var hoverColors = scheme.hoverColors;
    //  var diffColors = scheme.diff;
    if (drawBg) {
      g.drawRect(pos.x, pos.y, size, theme.bgColor);
    }
    this.firstLine = firstLine;
    this.lastLine = lastLine;
    if (lines == null) return;
    int bIndex = findBIndex(firstLine);

    if (texture == null) {
      texture = renderIcon(g, c.cleartype);
    }

    g.enableScissor(pos, size);
    int nextBt = bIndex < lines.length ? lines[bIndex] : -1;
    int x = pos.x;
    bSize.set(texture.width(), lineHeight);

    var bgColors = theme.bgColors;
    var textColors = theme.textColors;

    for (int l = firstLine; l <= lastLine; l++) {
      int y = pos.y + l * lineHeight - scrollPos;
      int docL = viewToDocMap == null ? l : viewToDocMap[l - firstLine];
      byte diffType = docL >= 0 && docL < colors.length ? colors[docL] : 0;

      V4f bgColor = diffType != 0 && bgColors != null ?
          bgColors.getDiffColor(diffType, null) :
//            l == caretLine ?
//              scheme.error() :
//              theme.selectedBg :
              theme.bgColor;
      if (docL >= 0 && nextBt == docL) {
        var textColor = diffType != 0 && textColors != null ?
            textColors.getDiffColor(diffType, null) : theme.textColor;
        var bg = hoverBtLine == docL && (theme.bgColors == null || diffType == 0) ?
            theme.bgColorHovered : bgColor;
        c.drawIcon(g, texture, x, y, bg, textColor);

        if (drawFrames) {
          debug.set(x, y);
          WindowPaint.drawInnerFrame(g, bSize, debug,
              theme.textColor, -1, c.size);
        }
        if (++bIndex < lines.length)
          nextBt = lines[bIndex];
      } else {
        g.drawRect(x, y, bSize, bgColor);
      }
    }
    g.disableScissor();
    int y = (lastLine + 1) * lineHeight - scrollPos;
    if (y < size.y) {
      bSize.y = size.y - y;
      V4f bgColor = theme.bgColor;
      g.drawRect(x, pos.y + y, bSize, bgColor);
      if (drawFrames) {
        debug.set(x, pos.y + y);
        WindowPaint.drawInnerFrame(g, bSize, debug,
            theme.textColor, -1, c.size);
      }
    }
    if (drawFrames) {
      WindowPaint.drawInnerFrame(g, size, pos,
          theme.textColor, -1, c.size);
    }
  }

  private int findBIndex(int firstLine) {
    for (int i = 0; i < lines.length; i++)
      if (lines[i] >= firstLine)
        return i;
    return lines.length;
  }

  private boolean buttonHitTest(V2i e, int viewLine) {
    int x = pos.x;
    int y = pos.y + viewLine * lineHeight - scrollPos;
    int size = lineHeight;
    return Rect.isInside(e, x, y, size, size);
  }

  public boolean onMouseMove(MouseEvent event, SetCursor setCursor) {
    hoverBtLine = -1;
    if (!hitTest(event.position)) return false;
    for (int btDocLine : lines) {
      int btLine = docToView(btDocLine);
      if (btLine >= 0 && firstLine <= btLine && btLine <= lastLine) {
        if (buttonHitTest(event.position, btLine)) {
          hoverBtLine = btDocLine;
          return setCursor.set(Cursor.pointer);
        }
      }
    }
    return setCursor.setDefault();
  }

  public void onMouseLeave() {
    hoverBtLine = -1;
  }

  public Consumer<MouseEvent> onMouseDown(MouseEvent event, int button, SetCursor setCursor) {
    if (button != MouseListener.MOUSE_BUTTON_LEFT) return null;
    if (!hitTest(event.position)) return null;
    for (int i = 0; i < lines.length; i++) {
      int btDocLine = lines[i];
      int btLine = docToView(btDocLine);
      boolean hit = btLine >= 0 &&
          firstLine <= btLine && btLine <= lastLine &&
          buttonHitTest(event.position, btLine);
      if (hit) {
        hoverBtLine = btDocLine;
        hoverBtIndex = i;
        setCursor.setDefault();
        return MouseListener.Static.emptyConsumer;
      }
    }
    hoverBtLine = hoverBtIndex = -1;
    return MouseListener.Static.emptyConsumer;
  }

  private int docToView(int btDocLine) {
    return lineMapping == null ? btDocLine
        : lineMapping.docToView(btDocLine);
  }

  public boolean onMouseUp(MouseEvent event, int button) {
    Runnable r = null;
    if (button == MouseListener.MOUSE_BUTTON_LEFT) {
      if (hoverBtLine >= 0) {
        int btLine = docToView(hoverBtLine);
        if (btLine >= 0 && buttonHitTest(event.position, btLine)) {
          r = actions[hoverBtIndex];
        }
      }
    }

    hoverBtLine = hoverBtIndex = -1;
    if (r != null) r.run();
    return r != null;
  }

  public boolean hitTest(V2i point) {
    return Rect.isInside(point, pos, size);
  }

  public void onTextRenderingSettingsChange() {

  }

  static final int iconTextureMargin = 1;

  private FontDesk fontDesk(WglGraphics g, float size) {
    return g.fontDesk(
        Codicons.typeface, size,
        FontDesk.WEIGHT_REGULAR, FontDesk.STYLE_NORMAL);
  }

  private GL.Texture renderIcon(WglGraphics g, boolean cleartype) {
//    char icon = toLeft ? Codicons.chevron_left : Codicons.chevron_right;
//    FontDesk font = fontDesk(g, lineHeight);
//    int yOffset = -Numbers.iDivRound(lastLine, 3, 32);
    char icon = toLeft ? arrowL : arrowR;
    int yOffset = 0;
    int margin = DprUtil.toPx(iconTextureMargin, dpr);
    GL.Texture t = g.renderTexture(
        String.valueOf(icon), font, margin,
        lineHeight, yOffset, cleartype);
//    System.out.println("MergeButtons.renderIcon: t.w=" + t.width() + ", w = " + size.x);
    return t;
  }

  public int measure(FontDesk font, Canvas mCanvas, float dpr) {
    int margin = DprUtil.toPx(iconTextureMargin, dpr);
    char icon = toLeft ? arrowL : arrowR;
    return mCanvas.measurePx(font, String.valueOf(icon), margin * 2);
  }

  @Override
  public void dispose() {
    texture = Disposable.dispose(texture);
  }
}
