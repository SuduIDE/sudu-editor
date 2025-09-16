package org.sudu.experiments.editor.ui.window;

import org.sudu.experiments.DprUtil;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.math.XorShiftRandom;
import org.sudu.experiments.ui.SetCursor;
import org.sudu.experiments.ui.window.ScrollContent;

import java.util.function.Consumer;

import static org.sudu.experiments.math.XorShiftRandom.intToDouble01;

public class ScrollContentDemo extends ScrollContent {

  final V4f color = new V4f();
  final V2i sz = new V2i();
  final Consumer<V2i> onSizeListener;
  float v;

  public ScrollContentDemo(float v, Consumer<V2i> onSizeListener) {
    this.onSizeListener = onSizeListener;
    this.v = v;
  }

  @Override
  public V2i minimalSize() {
    int px20 = DprUtil.toPx(20, dpr);
    return new V2i(px20, px20);
  }

  @Override
  public void setPosition(V2i newPos, V2i newSize, float newDpr) {
    super.setPosition(newPos, newSize, newDpr);
    setVirtualSize(newSize.x * 3, newSize.y * 5);
    onSizeListener.accept(newSize);
  }

  @Override
  public void draw(WglGraphics g) {
    super.draw(g);
    enableScissor(g);
    int step = DprUtil.toPx(30, dpr);
    int visX0 = (scrollPos.x / step) * step;
    int visY0 = (scrollPos.y / step) * step;
    int visX1 = ((scrollPos.x + size.x - 1) / step) * step;
    int visY1 = ((scrollPos.y + size.y - 1) / step) * step;
    sz.x = sz.y = step;
    color.w = 1;

    for (int y = visY0; y <= visY1; y += step) {
      int posY = pos.y - scrollPos.y;
      for (int x = visX0; x <= visX1; x += step) {
        int posX = pos.x - scrollPos.x;
        Color.Cvt.fromHSV(value(x, y), .75f, v, color);
        g.drawRect(posX + x, posY + y, sz, color);
      }
    }
    disableScissor(g);
  }

  public double value(int x, int y) {
    int t = 37 * x + 17 * y + 9;
    for (int i = 0; i < 17; i++) {
      t = XorShiftRandom.roll_7_1_9(t);
    }
    return intToDouble01(t);
  }

  @Override
  protected Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    return MouseListener.Static.emptyConsumer;
  }

  static int id;

  @Override
  protected void onMouseLeaveWindow() {
    System.out.println("[" + (++id) + "] " +
        "ScrollContentDemo.onMouseLeaveWindow: " + this);
  }

  @Override
  public void onMouseMove(MouseEvent event, SetCursor setCursor) {
    if (hitTest(event.position))
      setCursor.set(null);
  }
}
