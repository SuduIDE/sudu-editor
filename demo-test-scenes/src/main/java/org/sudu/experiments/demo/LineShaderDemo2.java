package org.sudu.experiments.demo;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.math.V2i;

public class LineShaderDemo2 extends LineShaderDemo0 {

  final V2i pos = new V2i();
  final V2i size = new V2i();

  public LineShaderDemo2(SceneApi api) {
    super(api);

    p11.set(150, 140);
    p12.set(500, 100);
    p21.set(150, 200);
    p22.set(500, 250);
  }

  public void paint() {
    int l = Math.max(p11.x, p21.x);
    int r = Math.min(p12.x, p22.x);
    int t = Math.min(p11.y, p12.y) - 50;
    int b = Math.max(p21.y, p22.y) + 50;

    rect.set(l, t, r - l, b - t);

    super.paint();

    int x1 = 10;
    int sizeX = rect.pos.x - x1;
    drawLeftRight(api.graphics, x1, sizeX, p11, p21);

    int x2 = rect.pos.x + rect.size.x;
    drawLeftRight(api.graphics, x2, sizeX, p12, p22);
  }

  private void drawLeftRight(WglGraphics g, int x1, int sizeX, V2i p11, V2i p21) {
    pos.set(x1, rect.pos.y);
    size.set(sizeX, p11.y - rect.pos.y);
    g.drawRect(pos.x, pos.y, size, rect.bgColor);
    pos.set(x1, p11.y);
    size.set(sizeX, p21.y - p11.y);
    g.drawRect(pos.x, pos.y, size, rect.color);
    pos.set(x1, p21.y);
    size.set(sizeX, rect.pos.y + rect.size.y - p21.y);
    g.drawRect(pos.x, pos.y, size, rect.bgColor);
  }

  public void onResize(V2i size, float dpr) {}

}
