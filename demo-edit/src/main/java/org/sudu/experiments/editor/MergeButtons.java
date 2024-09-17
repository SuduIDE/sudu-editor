package org.sudu.experiments.editor;

import org.sudu.experiments.*;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.ui.colors.LineNumbersColors;
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
  private int selectedBtLine = -1, selectedBtIndex = -1;
  private final boolean drawBg;
  private boolean toLeft;
  private FontDesk font;

  static final boolean drawFrames = false;

  public MergeButtons() {this.drawBg = false;}

  public MergeButtons(boolean drawBg) {
    this.drawBg = drawBg;
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
      WglGraphics g, EditorColorScheme scheme, ClrContext c
  ) {
    LineNumbersColors lnColors = scheme.lineNumber;
    if (drawBg) {
      g.drawRect(pos.x, pos.y, size, lnColors.bgColor);
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
    for (int l = firstLine; l <= lastLine ; l++) {
      int y = pos.y + l * lineHeight - scrollPos;
      byte color = l < colors.length ? colors[l] : 0;

      V4f bgColor = color != 0 ? scheme.diff.getDiffColor(scheme, color) :
          l == caretLine ? scheme.lineNumber.caretBgColor
              : scheme.lineNumber.bgColor;
      if (nextBt == l) {
//        var bg = selectedBtLine == l ? lnColors.caretBgColor : lnColors.bgColor;
//        g.drawRect(x, y, bSize, bg);
        c.drawIcon(
            g, texture, x, y,
            bgColor, lnColors.caretTextColor
        );
        if (drawFrames) {
          debug.set(x, y);
          WindowPaint.drawInnerFrame(g, bSize, debug, scheme.diff.deletedBgColor, -1, c.size);
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
      V4f bgColor = scheme.diff.getDiffColor(scheme, DiffTypes.DEFAULT);
      g.drawRect(x,pos.y + y, bSize, bgColor);
      if (drawFrames) {
        debug.set(x, pos.y+y);
        WindowPaint.drawInnerFrame(g, bSize, debug, scheme.diff.deletedBgColor, -1, c.size);
      }
    }
    if (drawFrames) {
      WindowPaint.drawInnerFrame(g, size, pos, scheme.diff.deletedBgColor, -1, c.size);
    }
  }

  private int findBIndex(int firstLine) {
    for (int i = 0; i < lines.length; i++)
      if (lines[i] >= firstLine)
        return i;
    return lines.length;
  }

  private boolean buttonHitTest(V2i e, int lNumber) {
    int x = pos.x;
    int y = pos.y + lNumber * lineHeight - scrollPos;
    int size = lineHeight;
    return Rect.isInside(e, x,y, size, size);
  }

  public boolean onMouseMove(MouseEvent event, SetCursor setCursor) {
    selectedBtLine = -1;
    if (!hitTest(event.position)) return false;
    for (int btLine : lines) {
      if (firstLine <= btLine && btLine <= lastLine) {
        if (buttonHitTest(event.position, btLine)) {
          selectedBtLine = btLine;
          return setCursor.set(Cursor.pointer);
        }
      }
    }
    return setCursor.setDefault();
  }

  public void onMouseLeave() {
    selectedBtLine = -1;
  }

  public Consumer<MouseEvent> onMouseDown(MouseEvent event, int button, SetCursor setCursor) {
    if (button != MouseListener.MOUSE_BUTTON_LEFT) return null;
    if (!hitTest(event.position)) return null;
    for (int i = 0; i < lines.length; i++) {
      int btLine = lines[i];
      boolean hit = firstLine <= btLine && btLine <= lastLine &&
          buttonHitTest(event.position, btLine);
      if (hit) {
        selectedBtLine = btLine;
        selectedBtIndex = i;
        setCursor.setDefault();
        return MouseListener.Static.emptyConsumer;
      }
    }
    selectedBtLine = selectedBtIndex = -1;
    return MouseListener.Static.emptyConsumer;
  }

  public boolean onMouseUp(MouseEvent event, int button) {
    Runnable r = null;
    if (button == MouseListener.MOUSE_BUTTON_LEFT) {
      if (selectedBtLine >= 0) {
        if (buttonHitTest(event.position, selectedBtLine)) {
          r = actions[selectedBtIndex];
        }
      }
    }

    selectedBtLine = selectedBtIndex = -1;
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
