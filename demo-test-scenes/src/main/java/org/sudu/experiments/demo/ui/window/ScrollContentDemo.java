package org.sudu.experiments.demo.ui.window;

import org.sudu.experiments.DprUtil;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.math.XorShiftRandom;

import static org.sudu.experiments.math.XorShiftRandom.intToDouble01;

public class ScrollContentDemo extends ScrollContent {

  final V4f color = new V4f();
  final V2i sz = new V2i();
  float v;

  public ScrollContentDemo(float v) {
    this.v = v;
  }

  @Override
  protected void updateVirtualSize() {
    virtualSize.set(size.x * 2, size.y * 2);
  }

  @Override
  protected void draw(WglGraphics g) {
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
}
