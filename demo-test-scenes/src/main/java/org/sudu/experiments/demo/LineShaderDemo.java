package org.sudu.experiments.demo;

import org.sudu.experiments.Scene;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.demo.ui.colors.IdeaCodeColors;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

public class LineShaderDemo extends Scene implements MouseListener  {

  final V4f bgColor = new Color(20);
  final DemoRect rect = new DemoRect(0, 0, 300, 300);
  final V2i p11 = new V2i(0, 0);
  final V2i p12 = new V2i(300, 300);
  final V2i p21 = new V2i();
  final V2i p22 = new V2i();

  public LineShaderDemo(SceneApi api) {
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
  }

  public void onResize(V2i size, float dpr) {
    rect.pos.set((size.x - rect.size.x) / 2, (size.y - rect.size.y) / 2);
    p11.set(0,0);
    p12.set(Math.min(size.x, size.y), Math.min(size.x, size.y));
    p21.set(p11.x, p11.y + 20);
    p22.set(p12.x, p12.y + 40);
  }

  boolean onKeyEvent(KeyEvent event) {
    return false;
  }

  public boolean onMouseMove(MouseEvent event) {
    p11.set(event.position);
    p12.set(rect.pos.x + rect.size.x / 2, rect.pos.y + rect.size.y / 2);
    p21.set(p11.x, p11.y + 20);
    p22.set(p12.x, p12.y + 40);

    return false;
  }
}
