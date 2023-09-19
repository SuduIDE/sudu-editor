package org.sudu.experiments.demo;

import org.sudu.experiments.Scene;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.demo.ui.colors.IdeaCodeColors;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

public class LineShaderDemo2 extends Scene implements MouseListener  {

  final V4f bgColor = new Color(20);
  final DemoRect rect = new DemoRect(150, 50, 350, 250);
  final V2i p11 = new V2i(150, 140);
  final V2i p12 = new V2i(500, 100);
  final V2i p21 = new V2i(150, 200);
  final V2i p22 = new V2i(500, 250);
  final V2i pos = new V2i();
  final V2i size = new V2i();

  public LineShaderDemo2(SceneApi api) {
    super(api);
    api.input.onKeyPress.add(this::onKeyEvent);
    api.input.onMouse.add(this);

    rect.bgColor.set(IdeaCodeColors.Darcula.editBg);
    rect.color.set(IdeaCodeColors.Darcula.defaultText);
  }

  public void dispose() {}

  public void paint() {
    WglGraphics g = api.graphics;
    g.clear(bgColor);
    g.drawLineFill(rect.pos.x, rect.pos.y, rect.size,
        p11, p12, p21, p22, rect.color, rect.bgColor);

    int x1 = 10;
    int sizeX = rect.pos.x - x1;
    drawLeftRight(g, x1, sizeX, p11, p21);

    int x2 = rect.pos.x + rect.size.x;
    drawLeftRight(g, x2, sizeX, p12, p22);
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

  boolean onKeyEvent(KeyEvent event) {
    return false;
  }
}
