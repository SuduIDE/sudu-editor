package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.demo.ui.colors.IdeaCodeColors;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;

public class ScissorDemo extends Scene0 {
  final TextRect demoRect = new TextRect(0, 0, 300, 300);
  GL.Texture texture;
  V2i lastMouse = new V2i();
  final V2i scissorPos = new V2i();
  final V2i scissorSize = new V2i();

  public ScissorDemo(SceneApi api) {
    super(api);
    WglGraphics g = api.graphics;
    api.input.onMouse.add(mouseListener());

    texture = TestHelper.canvasTexture(g);
    demoRect.setTextureRegionDefault(texture);
    demoRect.setSizeToTextureRegion();
    demoRect.bgColor.set(IdeaCodeColors.Darcula.editBg);
    demoRect.color.set(new Color(204, 120, 50));
  }

  public void dispose() {
    texture = Disposable.assign(texture, null);
  }

  @Override
  public void paint() {
    super.paint();
    api.graphics.enableScissor(scissorPos.x, scissorPos.y, scissorSize);
    TestHelper.drawTiles(demoRect, texture, size, api.graphics);
    api.graphics.disableScissor();
  }

  public void onResize(V2i size, float dpr) {
    super.onResize(size, dpr);
    scissorSize.set(size.x * 7 / 10, size.y * 7 / 10);
  }

  MouseListener mouseListener() {
    return new MouseListener() {
      @Override
      public boolean onMouseMove(MouseEvent event) {
        if (lastMouse != null) {
          int dx = event.position.x - lastMouse.x;
          int dy = event.position.y - lastMouse.y;
          scissorPos.x = Numbers.clamp(0,
                  scissorPos.x + dx,
                  size.x - scissorSize.x);
          scissorPos.y = Numbers.clamp(0,
                  scissorPos.y + dy,
                  size.y - scissorSize.y);
        }
        lastMouse = event.position;
        return true;
      }
    };
  }
}
