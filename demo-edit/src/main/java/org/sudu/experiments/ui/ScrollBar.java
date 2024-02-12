package org.sudu.experiments.ui;

import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.*;

import java.util.function.Consumer;

public class ScrollBar {
  public static final int BUTTON_SIZE = 3;
  final V2i buttonPos = new V2i();
  final V2i buttonSize = new V2i();
  final V2i bgPos = new V2i();
  final V2i bgSize = new V2i();
  public final V4f cBack = new V4f();
  public final V4f cLine = new V4f();

  public ScrollBar() {
  }

  public boolean visible() {
    return buttonSize.x * buttonSize.y != 0;
  }

  public boolean hitTest(V2i p) {
    return Rect.isInside(p, bgPos, bgSize);
  }

  public Consumer<MouseEvent> onMouseDown(V2i p, Consumer<Event> onMove, boolean isVertical) {
    boolean hitScroll = hitTest(p);
    boolean hitButton = Rect.isInside(p, buttonPos, buttonSize);

    if (hitScroll || hitButton) {
      if (!hitButton) {
        if (isVertical) {
          onMove.accept(getClickLocationResultY(p.y - bgPos.y));
        } else {
          onMove.accept(getClickLocationResultX(p.x - bgPos.x));
        }
      }
      int buttonCenter = isVertical ? buttonPos.y + buttonSize.y / 2 : buttonPos.x + buttonSize.x / 2;
      int delta = isVertical ? p.y : p.x;
      int hitOffset = hitButton ? buttonCenter - delta : 0;
      return isVertical
          ? event -> onMove.accept(
          getClickLocationResultY(event.position.y + hitOffset - bgPos.y))
          : event -> onMove.accept(
          getClickLocationResultX(event.position.x + hitOffset - bgPos.x));
    }
    return null;
  }

  public void setColor(V4f line, V4f back) {
    if (back != null) cBack.set(back);
    if (line != null) cLine.set(line);
  }

  public void setColorIfNotSame(V4f scrollBarLine, V4f scrollBarBg) {
    if (scrollBarLine != null && !cBack.equals(scrollBarLine)) {
      cBack.set(scrollBarLine);
    }
    if (scrollBarBg != null && !cLine.equals(scrollBarBg)) {
      cLine.set(scrollBarBg);
    }
  }

  public static class Event {
    public int position, maxPosition;

    public Event(int position, int maxPosition) {
      this.position = position;
      this.maxPosition = maxPosition;
    }

    public int getPosition(int maxValue) {
      return Numbers.iDivRound(position, maxValue, maxPosition);
    }

  }

  static Event result(int position, int maxPosition) {
    return new Event(position, maxPosition);
  }

  // returns the new scroll position assuming that
  // user wants to set the button center to mouseY
  private Event getClickLocationResultY(int mouseY) {
    int viewHeight = bgSize.y;
    int buttonHeight = buttonSize.y;
    int virtualSize = viewHeight - buttonHeight;
    int virtualPos = mouseY - buttonHeight / 2;

    return result(Math.min(Math.max(0, virtualPos), virtualSize), virtualSize);
  }

  private Event getClickLocationResultX(int mouseX) {
    int viewWidth = bgSize.x;
    int buttonWidth = buttonSize.x;
    int virtualSize = viewWidth - buttonWidth;
    int virtualPos = mouseX - buttonWidth / 2;

    return result(Math.min(Math.max(0, virtualPos), virtualSize), virtualSize);
  }

  public void layoutVertical(
      int scrollPos,
      int y0, int ySize, int yVirtualSize,
      int x1, int width // x0 = x1 - width
  ) {
    layout(scrollPos, y0, ySize, yVirtualSize, x1, width, true);
  }

  public void layoutHorizontal(
      int scrollPos,
      int x0, int xSize, int xVirtualSize,
      int y1, int height  // y0 = y1 - height
  ) {
    layout(scrollPos, x0, xSize, xVirtualSize, y1, height, false);
  }

  private void layout(
      int scrollPos,
      int viewStart, int viewSize, int viewVirtualSize,
      int viewZMax, int buttonZSize,
      boolean isVertical
  ) {
    if (viewVirtualSize <= viewSize) {
      bgSize.set(0, 0);
      buttonSize.set(0, 0);
    } else {
      int buttonLength = scrollControlSize(
          viewSize, viewVirtualSize,
          Math.min(buttonZSize * BUTTON_SIZE, viewSize));
      int buttonPosition = scrollControlPos(
          scrollPos, viewSize, viewVirtualSize, buttonLength);
      if (isVertical) {
        buttonPos.x = viewZMax - buttonZSize;
        buttonPos.y = buttonPosition + viewStart;
        buttonSize.x = buttonZSize;
        buttonSize.y = buttonLength;
        bgPos.x = buttonPos.x;
        bgPos.y = viewStart;
        bgSize.x = buttonZSize;
        bgSize.y = viewSize;
      } else {
        buttonPos.x = buttonPosition + viewStart;
        buttonPos.y = viewZMax - buttonZSize;
        buttonSize.x = buttonLength;
        buttonSize.y = buttonZSize;
        bgPos.x = viewStart;
        bgPos.y = buttonPos.y;
        bgSize.x = viewSize;
        bgSize.y = buttonZSize;
      }
    }
  }

  private int scrollControlSize(int viewSize, int virtualSize, int minSize) {
    return Math.max(Numbers.iDivRound(viewSize, viewSize, virtualSize), minSize);
  }

  private int scrollControlPos(int scrollPos, int viewSize, int viewFullSize, int controlSize) {
    int virtualScrollRange = viewFullSize - viewSize;
    int displayScrollRange = viewSize - controlSize;
    return displayScrollRange == 0 ? 0 : Numbers.iDivRound(scrollPos, displayScrollRange, virtualScrollRange);
  }

  public void draw(WglGraphics g) {
    drawBg(g);
    drawButton(g);
  }

  public void drawBg(WglGraphics g) {
    g.drawRect(bgPos.x, bgPos.y, bgSize, cBack);
  }

  public void drawButton(WglGraphics g) {
    buttonSize.x -= 2; buttonSize.y -= 2;
    g.drawRect(buttonPos.x + 1, buttonPos.y + 1, buttonSize, cLine);
    buttonSize.x += 2; buttonSize.y += 2;
  }

  public boolean onMouseMove(V2i position, SetCursor setCursor) {
    return hitTest(position) && setCursor.setDefault();
  }
}
