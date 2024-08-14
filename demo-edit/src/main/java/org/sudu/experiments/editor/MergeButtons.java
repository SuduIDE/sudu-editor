package org.sudu.experiments.editor;

import org.sudu.experiments.Cursor;
import org.sudu.experiments.Disposable;
import org.sudu.experiments.GL;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.ui.colors.LineNumbersColors;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.*;
import org.sudu.experiments.ui.SetCursor;
import org.sudu.experiments.ui.fonts.Codicons;

import java.util.function.Consumer;

public class MergeButtons implements Disposable {

  public final V2i pos = new V2i();
  public final V2i size = new V2i();
  public float dpr;

  private Runnable[] actions;
  private int[] lines;
  private byte[] colors;

  private GL.Texture texture;

  private final V2i bSize = new V2i();
  private int lineHeight, scrollPos;
  private int firstLine, lastLine;
  private int selectedBtLine = -1, selectedBtIndex = -1;
  private final boolean drawBg;
  private boolean toLeft;

  public MergeButtons() {this.drawBg = false;}

  public MergeButtons(boolean drawBg) {
    this.drawBg = drawBg;
  }

  public void setPosition(int x, int y, int width, int height, float dpr) {
    pos.set(x, y);
    size.set(width, height);
    this.dpr = dpr;
  }

  public void setFont(int lineHeight, boolean rtl) {
    this.lineHeight = lineHeight;
    this.toLeft = rtl;
    System.out.println("MergeButtons.setFont " +
        lineHeight);
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
    if (bIndex == lines.length) return;

    if (texture == null) {
      texture = renderIcon(g, c.cleartype);
    }

    g.enableScissor(pos, size);
    int nextBt = lines[bIndex];
    int x = pos.x + 2;
    for (int l = firstLine; l <= lastLine ; l++) {
      if (nextBt == l) {
        int y = pos.y + l * lineHeight - scrollPos;
        bSize.set(lineHeight, lineHeight);
//        var bg = selectedBtLine == l ? lnColors.caretBgColor : lnColors.bgColor;
        V4f bgColor = LineNumbersComponent.getItemColor(scheme, colors, l);
//        g.drawRect(x, y, bSize, bg);
        c.drawIcon(
            g, texture, x, y,
            bgColor, lnColors.textColor
        );
        if (++bIndex == lines.length) break;
        nextBt = lines[bIndex];
      }
    }
    g.disableScissor();
  }

  private int findBIndex(int firstLine) {
    for (int i = 0; i < lines.length; i++)
      if (lines[i] >= firstLine)
        return i;
    return lines.length;
  }

  private boolean buttonHitTest(V2i e, int lNumber) {
    int x = pos.x + 2;
    int y = lNumber * lineHeight - scrollPos;
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
    return hitTest(event.position) && setCursor.setDefault();
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
    char icon = toLeft ? Codicons.chevron_left : Codicons.chevron_right;
    FontDesk font = fontDesk(g, lineHeight);
    int yOffset = -Numbers.iDivRound(lastLine, 3, 32);
    return g.renderTexture(
        String.valueOf(icon), font, iconTextureMargin,
        lineHeight, yOffset, cleartype);
  }

  @Override
  public void dispose() {
    texture = Disposable.dispose(texture);
  }
}
