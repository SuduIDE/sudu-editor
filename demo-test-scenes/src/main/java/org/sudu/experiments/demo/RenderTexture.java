package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.demo.ui.colors.IdeaCodeColors;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;

import java.util.function.Consumer;

public class RenderTexture extends Scene0 {
  final TextRect demoRect = new TextRect(0, 0, 300, 300);
  final DemoRect mouse = new DemoRect(0, 0, 3, 3);

  GL.Texture mouseTexture;
  GL.Texture canvasTexture;

  Canvas textCanvas;

  V2i drag;

  public RenderTexture(SceneApi api) {
    super(api);
    WglGraphics g = api.graphics;
    api.input.onMouse.add(new MyInputListener());

    canvasTexture = TestHelper.canvasTexture(g);
    demoRect.setTextureRegionDefault(canvasTexture);
    demoRect.setSizeToTextureRegion();
    demoRect.bgColor.set(IdeaCodeColors.Darcula.editBg);
    demoRect.color.set(new Color(204, 120, 50));

    mouseTexture = mouseTexture(g);
    mouse.size.set(mouseTexture.width(), mouseTexture.height());
    mouse.bgColor.set(clearColor);
  }

  private void layout(V2i clientRect) {
    mouse.pos.set(clientRect.x / 2 - 1, clientRect.y / 2 - 1);
    demoRect.pos.set(
        (clientRect.x - demoRect.size.x) / 2,
        (clientRect.y - demoRect.size.y) / 2);
  }

  public void dispose() {
    mouseTexture = Disposable.assign(mouseTexture, null);
    canvasTexture = Disposable.assign(canvasTexture, null);
    textCanvas = Disposable.assign(textCanvas, null);
  }

  private GL.Texture mouseTexture(WglGraphics g) {
    GL.ImageData image = TGen.chess(5, 5);
    GL.Texture texture = g.createTexture();
    texture.setContent(image);
    return texture;
  }

  public boolean update(double timestamp) {
    Color.Cvt.fromHSV(fr(timestamp / 5), 1, 1, 1, mouse.color);
    return false;
  }

  static double fr(double x) {
    return x - (int) x;
  }

  @Override
  public void paint() {
    super.paint();

    demoRect.drawText(api.graphics, canvasTexture, 0, 0, 0.5f);
    mouse.drawGrayIcon(api.graphics, mouseTexture, 0, 0, 0);

    api.graphics.checkError("paint complete ");
  }

  public void onResize(V2i size, float dpr) {
    super.onResize(size, dpr);
    layout(size);
  }

  class MyInputListener implements MouseListener {
    @Override
    public boolean onMouseMove(MouseEvent event) {
//      System.out.println("event = " + event.position);
      if (drag != null) {
        V2i rcPos = demoRect.pos;
        rcPos.x += event.position.x - drag.x;
        rcPos.y += event.position.y - drag.y;
        drag = event.position;
      }

      int nextX = event.position.x - mouse.size.x; // 2;
      int nextY = event.position.y - mouse.size.y; // 2;
      mouse.pos.set(nextX, nextY);

      return true;
    }

    @Override
    public Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
      if (button == MouseListener.MOUSE_BUTTON_LEFT) {
        V2i p = event.position;
        drag = demoRect.isInside(p) ? p : null;
      }
      return Static.emptyConsumer;
    }
  }

}
