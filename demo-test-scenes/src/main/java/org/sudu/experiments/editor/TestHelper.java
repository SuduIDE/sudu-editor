package org.sudu.experiments.editor;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.DprUtil;
import org.sudu.experiments.GL;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.editor.ui.colors.IdeaCodeColors;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.ui.PopupMenu;
import org.sudu.experiments.ui.UiContext;

import java.util.function.Consumer;

public class TestHelper {
  public static class Cross {
    private final V2i hLine = new V2i();
    private final V2i vLine = new V2i();

    public void draw(UiContext uiContext) {
      V2i windowSize = uiContext.windowSize;

      hLine.set(windowSize.x, DprUtil.toPx(2, uiContext.dpr));
      vLine.set(DprUtil.toPx(2, uiContext.dpr), windowSize.y);
      V4f crossColors = IdeaCodeColors.Darcula.scrollBarLine;
      uiContext.graphics.drawRect(0, windowSize.y / 2 - hLine.y / 2, hLine, crossColors);
      uiContext.graphics.drawRect(windowSize.x / 2 - vLine.x / 2, 0, vLine, crossColors);
    }
  }

  static GL.Texture canvasTexture(WglGraphics g) {
    Canvas h = g.createCanvas(250, 64);
    h.setFont(Fonts.CourierNew, 25);
    h.setFillColor(187, 187, 187);
    String s = "|The sample text";
    h.drawText(s, 0, 24);
    h.drawText(s, .25f, 56);
    GL.Texture texture = g.createTexture();
    texture.setContent(h);
    h.dispose();
    return texture;
  }

  static void drawTiles(TextRect demoRect, GL.Texture texture, V2i size, WglGraphics graphics) {
    int y = 0;
    do {
      int x = 0;
      do {
        demoRect.drawText(graphics, texture, x, y);
        x += texture.width();
      } while (x < size.x);
      y += texture.height();
    } while (y < size.y);
  }

  public static MouseListener popupMouseListener(PopupMenu popupMenu) {
    return new MouseListener() {
      @Override
      public boolean onMouseMove(MouseEvent event) {
        return popupMenu.onMouseMove(event);
      }

      @Override
      public boolean onMouseClick(MouseEvent event, int button, int clickCount) {
        return popupMenu.onMouseClick(event, button, clickCount);
      }

      @Override
      public Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
        return popupMenu.onMouseDown(event, button);
      }

      @Override
      public boolean onMouseUp(MouseEvent event, int button) {
        return popupMenu.onMouseUp(event, button);
      }
    };
  }
}
