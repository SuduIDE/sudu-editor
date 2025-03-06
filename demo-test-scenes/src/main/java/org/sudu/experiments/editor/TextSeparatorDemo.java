package org.sudu.experiments.editor;

import org.sudu.experiments.*;
import org.sudu.experiments.editor.ui.colors.IdeaCodeColors;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

public class TextSeparatorDemo extends Scene implements MouseListener {

  final V2i pos = new V2i();
  final V2i size = new V2i();
  final V4f bgColor = new Color(20);
  final DemoRect rect = new DemoRect();
  final V2i p11 = new V2i();
  final V2i p12 = new V2i();
  final V2i p21 = new V2i();
  final V2i p22 = new V2i();

  int yRight = 100;
  float lineHeight = 10;
  float dpr;

  static void sinParamDefault(V4f param) {
    UnderlineConstants.sinParamsDefault(param);
  }

  public TextSeparatorDemo(SceneApi api) {
    super(api);
    api.input.onKeyPress.add(this::onKeyEvent);
    api.input.onMouse.add(this);

    rect.bgColor.set(IdeaCodeColors.Darcula.editBg);
    rect.color.set(IdeaCodeColors.Darcula.defaultText);
  }

  public void onResize(V2i size, float dpr) {
    int lineHeightPx = DprUtil.toPx(lineHeight, dpr);
    int x0 = DprUtil.toPx(150, dpr);
    int x1 = DprUtil.toPx(500, dpr);
    int y0 = (size.y - lineHeightPx) / 2;
    p11.set(x0, y0);
    p21.set(x0, y0 + lineHeightPx);
    p12.set(x1, yRight);
    p22.set(x1, yRight + lineHeightPx);
    this.dpr = dpr;
  }

  @Override
  public boolean onMouseMove(MouseEvent event) {
    System.out.println("event.position = " + event.position);
    if (event.position.x < 0 && event.position.y < 0)
      return true;
    int lineHeightPx = p22.y - p12.y;
    yRight = p12.y = event.position.y - lineHeightPx / 2;
    p22.y = yRight + lineHeightPx;
    return true;
  }

  public void paint() {
    int yBorder = DprUtil.toPx(10, dpr);
    int l = Math.max(p11.x, p21.x);
    int r = Math.min(p12.x, p22.x);
    int t = Math.min(p11.y, p12.y) - yBorder;
    int b = Math.max(p21.y, p22.y) + yBorder;

    rect.set(l, t, r - l, b - t);

    WglGraphics g = api.graphics;
    g.clear(bgColor);
    g.drawRect(rect.pos.x, rect.pos.y, rect.size, rect.bgColor);
    g.enableBlend(true);
    g.drawLineFill(rect.pos.x, rect.pos.y, rect.size,
        p11, p12, p21, p22, rect.color);
    g.enableBlend(false);

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

  public void dispose() {}

  boolean onKeyEvent(KeyEvent event) {
    return false;
  }

}
