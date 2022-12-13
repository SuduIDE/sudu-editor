package org.sudu.experiments.demo;

import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.Rect;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;

public class ScrollBar {
  public static final int BUTTON_SIZE = 3;
  final V2i buttonPos = new V2i();
  final V2i buttonSize = new V2i();
  final V2i bgPos = new V2i();
  final V2i bgSize = new V2i();
  final V4f color1 = new V4f();
  final V4f color2 = new V4f();

  public ScrollBar() {
    color1.set(Colors.scrollBarBody1);
    color2.set(Colors.scrollBarBody2);
  }

  public boolean visible() {
    return buttonSize.x * buttonSize.y != 0;
  }

  public boolean hitTest(V2i p) {
    return Rect.isInside(p, bgPos, bgSize);
  }

  public Consumer<V2i> onMouseClick(V2i p, Consumer<IntUnaryOperator> onMove, boolean isVertical) {
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
      return dragInfo(hitButton ? buttonCenter - delta : 0, onMove, isVertical);
    }
    return null;
  }

  Consumer<V2i> dragInfo(int hitOffset, Consumer<IntUnaryOperator> onMove, boolean isVertical) {
    if (isVertical)
      return pos -> onMove.accept(getClickLocationResultY(pos.y + hitOffset - bgPos.y));
    else
      return pos -> onMove.accept(getClickLocationResultX(pos.x + hitOffset - bgPos.x));
  }

  static IntUnaryOperator result(int position, int maxPosition) {
    return maxValue -> Numbers.iDivRound(position * maxValue, maxPosition);
  }

  // returns the new scroll position assuming that
  // user wants to set the button center to mouseY
  private IntUnaryOperator getClickLocationResultY(int mouseY) {
    int editorHeight = bgSize.y;
    int buttonHeight = buttonSize.y;
    int virtualSize = editorHeight - buttonHeight;
    int virtualPos = mouseY - buttonHeight / 2;

    return result(Math.min(Math.max(0, virtualPos), virtualSize), virtualSize);
  }

  private IntUnaryOperator getClickLocationResultX(int mouseX) {
    int editorWidth = bgSize.x;
    int buttonWidth = buttonSize.x;
    int virtualSize = editorWidth - buttonWidth;
    int virtualPos = mouseX - buttonWidth / 2;

    return result(Math.min(Math.max(0, virtualPos), virtualSize), virtualSize);
  }

  public void layoutVertical(
    int editorVScrollPos,
    int editorRight,
    int editorHeight,
    int editorFullHeight,
    int width
  ) {
    layout(editorVScrollPos, editorRight, editorHeight, editorFullHeight, 0, width, true);
  }

  public void layoutHorizontal(
    int editorHScrollPos,
    int editorBottom,
    int editorWidth,
    int editorFullWidth,
    int height
  ) {
    layout(editorHScrollPos, editorBottom, editorWidth, editorFullWidth, 0, height, false);
  }

  public void layoutHorizontal(
    int editorHScrollPos,
    int editorBottom,
    int editorWidth,
    int editorFullWidth,
    int editorLeft,
    int height
  ) {
    layout(editorHScrollPos, editorBottom, editorWidth, editorFullWidth, editorLeft, height, false);
  }

  private void layout(
    int scrollPos,
    int editorBorder,
    int editorSize,
    int editorFullSize,
    int editorStart,
    int buttonSize,
    boolean isVertical
  ) {
    if (editorFullSize <= editorSize) {
      bgSize.set(0, 0);
      this.buttonSize.set(0, 0);
    } else {
      int scrollControlHSize = scrollControlSize(editorSize, editorFullSize, buttonSize * BUTTON_SIZE);
      int scrollControlHPos = scrollControlPos(scrollPos, editorSize, editorFullSize, scrollControlHSize);
      if (isVertical) {
        buttonPos.x = editorBorder - buttonSize;
        buttonPos.y = scrollControlHPos + editorStart;
        this.buttonSize.x = buttonSize;
        this.buttonSize.y = scrollControlHSize;
        bgPos.x = buttonPos.x;
        bgPos.y = editorStart;
        bgSize.x = buttonSize;
        bgSize.y = editorSize;
      } else {
        buttonPos.x = scrollControlHPos + editorStart;
        buttonPos.y = editorBorder - buttonSize;
        this.buttonSize.x = scrollControlHSize;
        this.buttonSize.y = buttonSize;
        bgPos.x = editorStart;
        bgPos.y = buttonPos.y;
        bgSize.x = editorSize;
        bgSize.y = buttonSize;
      }
    }
  }

  private int scrollControlSize(int editorSize, int editorFullSize, int minSize) {
    return Math.max(Numbers.iDivRound(editorSize * editorSize, editorFullSize), minSize);
  }

  private int scrollControlPos(int editorScrollPos, int editorSize, int editorFullSize, int scrollControlSize) {
    int maxEditorPosY = editorFullSize - editorSize;
    int displayScrollRange = editorSize - scrollControlSize;
    return Numbers.iDivRound(editorScrollPos * displayScrollRange, maxEditorPosY);
  }

  public void draw(WglGraphics g) {
    g.drawRect(bgPos.x, bgPos.y, bgSize, color1);
//    g.drawRect(pos.x, pos.y, size, color1);
    buttonSize.x -= 2; buttonSize.y -= 2;
    g.drawRect(buttonPos.x + 1, buttonPos.y + 1, buttonSize, color2);
    buttonSize.x += 2; buttonSize.y += 2;
  }

  public boolean onMouseMove(V2i position, SetCursor setCursor) {
    return hitTest(position) && setCursor.setDefault();
  }
}