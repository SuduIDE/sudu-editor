package org.sudu.experiments.editor;

import org.sudu.experiments.*;
import org.sudu.experiments.editor.ui.colors.MergeButtonsColors;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.*;
import org.sudu.experiments.ui.SetCursor;
import org.sudu.experiments.ui.WindowPaint;

import java.util.Arrays;
import java.util.function.Consumer;

public class MergeButtons implements Disposable {

  static final boolean showAcceptReject = false;
  static final char acceptCh = '✔';
  static final char rejectCh = '✖';
  static final char arrowL = showAcceptReject ? acceptCh : '≪';
  static final char arrowR = showAcceptReject ? rejectCh : '≫';
  static final char arrowR1 = '→';
  static final char arrowL1 = '←';
  static final int iconTextureMarginL = 3;
  static final int iconTextureMarginM = 1;
  static final int iconTextureMarginR = 3;

  public final V2i pos = new V2i();
  public final V2i size = new V2i();
  public float dpr;

  private Runnable[] actions;
  private BooleanConsumer[] acceptReject;
  private int[] lines;
  private byte[] colors;

  private GL.Texture texture, texture2;

  private final V2i bSize = new V2i();
  private final V2i debug = new V2i();
  private int lineHeight, scrollPos;
  private int firstLine, lastLine;
  private int hoverBtLine = -1, hoverBtIndex = -1;
  private boolean hoverBtAccept = true;
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
    disposeTextures();
  }

  public void setModel(Runnable[] act, int[] ln) {
    lines = ln;
    actions = act;
    acceptReject = null;
  }

  public void setModel(BooleanConsumer[] act, int[] ln) {
    lines = ln;
    acceptReject = act;
    actions = null;
  }

  private boolean isAcceptReject() {
    return acceptReject != null;
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

    var acceptReject = isAcceptReject();

    if (texture == null)
      texture = renderIcon(g, c.cleartype,
          acceptReject ? acceptCh : toLeft ? arrowL : arrowR,
          iconTextureMarginL,
          acceptReject ? iconTextureMarginM : iconTextureMarginR);

    if (acceptReject && texture2 == null)
      texture2 = renderIcon(g, c.cleartype, rejectCh,
          iconTextureMarginM, iconTextureMarginR);

    g.enableScissor(pos, size);
    int x = pos.x;
    bSize.set(
        texture.width() + (acceptReject ? texture2.width() : 0),
        lineHeight);

    var bgColors = theme.bgColors;
    var textColors = theme.textColors;

    for (int l = firstLine; l <= lastLine; l++) {
      int y = pos.y + l * lineHeight - scrollPos;
      int docL = viewToDocMap == null ? l : viewToDocMap[l - firstLine];
      byte diffType = docL >= 0 && docL < colors.length ? colors[docL] : 0;

      V4f bgColor = diffType != 0 && bgColors != null ?
          bgColors.getDiffColor(diffType, null) : theme.bgColor;

      boolean found = docL >= 0 && Arrays.binarySearch(lines, docL) >= 0;

      if (found) {
        var textColor = diffType != 0 && textColors != null ?
                textColors.getDiffColor(diffType, null) : theme.textColor;
        boolean hoveredA = hoverBtLine == docL && hoverBtAccept;
        var bg = hoveredA && (theme.bgColors == null || diffType == 0) ?
            theme.bgColorHovered : bgColor;
        c.drawIcon(g, texture, x, y, bg,
            hoveredA && isAcceptReject() ? theme.acceptColor : textColor);
        if (acceptReject) {
          boolean hoveredR = hoverBtLine == docL && !hoverBtAccept;
          bg = hoveredR && (theme.bgColors == null || diffType == 0) ?
              theme.bgColorHovered : bgColor;
          c.drawIcon(g, texture2, x + texture.width(), y, bg,
              hoveredR ? theme.rejectColor : textColor);
        }

        if (drawFrames) {
          debug.set(x, y);
          WindowPaint.drawInnerFrame(g, bSize, debug,
              theme.textColor, -1, c.size);
        }
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

  private boolean buttonHitTest(V2i e, int viewLine, int x, int sizeX) {
    int y = pos.y + viewLine * lineHeight - scrollPos;
    return Rect.isInside(e, x, y, sizeX, lineHeight);
  }

  public boolean onMouseMove(MouseEvent event, SetCursor setCursor) {
    hoverBtLine = -1;
    V2i evPos = event.position;
    if (!hitTest(evPos)) return false;
    int sizeX1 = texture != null ? texture.width() : 0;
    int sizeX2 = texture2 != null ? texture2.width() : 0;
    int x1 = pos.x;
    for (int btDocLine : lines) {
      int btLine = docToView(btDocLine);
      if (btLine >= 0 && firstLine <= btLine && btLine <= lastLine) {
        if (buttonHitTest(evPos, btLine, x1, sizeX1 + sizeX2)) {
          hoverBtAccept = !isAcceptReject() || buttonHitTest(evPos, btLine, x1, sizeX1);
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
    int x0 = pos.x;
    int sizeX1 = texture != null ? texture.width() : 0;
    int sizeX2 = texture2 != null ? texture2.width() : 0;

    for (int i = 0; i < lines.length; i++) {
      int btDocLine = lines[i];
      int btLine = docToView(btDocLine);
      boolean hit = btLine >= 0 &&
          firstLine <= btLine && btLine <= lastLine &&
          buttonHitTest(event.position, btLine, x0, sizeX1 + sizeX2);
      if (hit) {
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
    BooleanConsumer bc = null;
    if (button == MouseListener.MOUSE_BUTTON_LEFT) {
      if (hoverBtLine >= 0) {
        int btLine = docToView(hoverBtLine);
        V2i evPos = event.position;
        int sizeX1 = texture != null ? texture.width() : 0;
        int sizeX2 = texture2 != null ? texture2.width() : 0;
        if (btLine >= 0 && buttonHitTest(evPos, btLine, pos.x, sizeX1 + sizeX2)) {
          if (isAcceptReject()) {
            if (hoverBtAccept == buttonHitTest(evPos, btLine, pos.x, sizeX1)) {
              bc = acceptReject[hoverBtIndex];
            }
          } else {
            r = actions[hoverBtIndex];
          }
        }
      }
    }

    hoverBtLine = hoverBtIndex = -1;
    if (r != null) r.run();
    if (bc != null) bc.accept(hoverBtAccept);
    return r != null;
  }

  public boolean hitTest(V2i point) {
    return Rect.isInside(point, pos, size);
  }

  public void onTextRenderingSettingsChange() {

  }

  private GL.Texture renderIcon(WglGraphics g, boolean cleartype, char icon, float marginL, float marginR) {
//    char icon = toLeft ? Codicons.chevron_left : Codicons.chevron_right;
//    FontDesk font = fontDesk(g, lineHeight);
//    int yOffset = -Numbers.iDivRound(lastLine, 3, 32);
    int yOffset = 0;
    return g.renderTexture(
        String.valueOf(icon), font, marginL * dpr, marginR * dpr,
        lineHeight, yOffset, cleartype);
  }

  public int measure(FontDesk font, Canvas mCanvas, float dpr) {
    if (isAcceptReject()) {
      float marginL = (iconTextureMarginL + iconTextureMarginM) * dpr;
      float marginR = (iconTextureMarginM + iconTextureMarginR) * dpr;
      int measurePx = mCanvas.measurePx(font, String.valueOf(acceptCh), marginL) +
          mCanvas.measurePx(font, String.valueOf(rejectCh), marginR);
      System.out.println(this + ": measurePx1 = " + measurePx);
      return measurePx;
    } else {
      float margin = (iconTextureMarginL + iconTextureMarginR) * dpr;
      char icon = toLeft ? arrowL : arrowR;
      int measurePx = mCanvas.measurePx(font, String.valueOf(icon), margin);
      System.out.println(this + ": measurePx2 = " + measurePx);
      return measurePx;
    }
  }

  private void disposeTextures() {
    texture = Disposable.dispose(texture);
    texture2 = Disposable.dispose(texture2);
  }

  @Override
  public void dispose() {
    disposeTextures();
  }
}
