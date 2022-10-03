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

  public Consumer<V2i> onMouseClick(V2i p, Consumer<IntUnaryOperator> onMove) {
    boolean hitScroll = Rect.isInside(p, bgPos, bgSize);
    boolean hitButton = Rect.isInside(p, buttonPos, buttonSize);

    if (hitScroll || hitButton) {
      if (!hitButton) {
        onMove.accept(getClickLocationResult(p.y));
      }
      int buttonCenter = buttonPos.y + buttonSize.y / 2;
      return dragInfo(hitButton ? buttonCenter - p.y : 0, onMove);
    }
    return null;
  }

  Consumer<V2i> dragInfo(int hitOffset, Consumer<IntUnaryOperator> onMove) {
    return pos -> onMove.accept(getClickLocationResult(pos.y + hitOffset));
  }

  static IntUnaryOperator result(int position, int maxPosition) {
    return maxValue -> Numbers.iDivRound(position * maxValue, maxPosition);
  }

  // returns the new scroll position assuming that
  // user wants to set the button center to mouseY
  private IntUnaryOperator getClickLocationResult(int mouseY) {
    int editorHeight = bgSize.y;
    int buttonHeight = buttonSize.y;
    int virtualSize = editorHeight - buttonHeight;
    int virtualPos = mouseY - buttonHeight / 2;

    return result(Math.min(Math.max(0, virtualPos), virtualSize), virtualSize);
  }

  public void layoutVertical(
      int editorVScrollPos,
      int editorRight,
      int editorHeight,
      int editorFullHeight,
      int width
  ) {
    if (editorHeight < editorFullHeight) {
      int scrollControlVSize = scrollControlVSize(editorHeight, editorFullHeight, width * BUTTON_SIZE);
      int scrollControlVPos = scrollControlVPos(editorVScrollPos, editorHeight, editorFullHeight, scrollControlVSize);
      buttonPos.x = editorRight - width;
      buttonPos.y = scrollControlVPos;
      buttonSize.x = width;
      buttonSize.y = scrollControlVSize;
      bgPos.x = buttonPos.x;
      bgPos.y = 0;
      bgSize.x = width;
      bgSize.y = editorHeight;
    } else {
      bgSize.set(0, 0);
      buttonSize.set(0, 0);
    }
  }

  private int scrollControlVSize(int editorHeight, int editorFullHeight, int minSize) {
    return Math.max(Numbers.iDivRound(editorHeight * editorHeight, editorFullHeight), minSize);
  }

  private int scrollControlVPos(int editorVScrollPos, int editorHeight, int editorFullHeight, int scrollControlVSize) {
    int maxEditorPosY = editorFullHeight - editorHeight;
    int displayScrollRange = editorHeight - scrollControlVSize;
    return Numbers.iDivRound(editorVScrollPos * displayScrollRange, maxEditorPosY);
  }

  public void draw(WglGraphics g) {
    g.drawRect(bgPos.x, bgPos.y, bgSize, color1);
//    g.drawRect(pos.x, pos.y, size, color1);
    buttonSize.x -= 2; buttonSize.y -= 2;
    g.drawRect(buttonPos.x + 1, buttonPos.y + 1, buttonSize, color2);
    buttonSize.x += 2; buttonSize.y += 2;
  }

  public boolean onMouseMove(V2i position) {
    return false;
  }
}
