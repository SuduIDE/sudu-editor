package org.sudu.experiments.editor;

import org.sudu.experiments.Scene;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.editor.ui.colors.IdeaCodeColors;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

public abstract class LineShaderDemo0 extends Scene implements MouseListener  {

  final V4f bgColor = new Color(20);
  final DemoRect rect = new DemoRect();
  final V2i p11 = new V2i();
  final V2i p12 = new V2i();
  final V2i p21 = new V2i();
  final V2i p22 = new V2i();

  public LineShaderDemo0(SceneApi api) {
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
    g.drawRect(rect.pos.x, rect.pos.y, rect.size, rect.bgColor);
    g.enableBlend(true);
    g.drawLineFill(rect.pos.x, rect.pos.y, rect.size,
        p11, p12, p21, p22, rect.color);
    g.enableBlend(false);
  }

  boolean onKeyEvent(KeyEvent event) {
    return false;
  }

}
