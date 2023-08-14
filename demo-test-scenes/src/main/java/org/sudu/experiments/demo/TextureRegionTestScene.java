package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;

import java.util.function.Consumer;

public class TextureRegionTestScene extends Scene {

  final WglGraphics g;

  private Canvas linesCanvas;
  private Canvas updateCanvas;
  private GL.Texture texture;
  private int texturePos;

  private final ScrollBar scrollBar;
  private int scrollPos;

  private final int fontSize = 20;
  private final int batchSize = 10 + 1;
  private final int textureSize = fontSize * batchSize;
  private final V2i viewPortSize = new V2i();
  private final int editorFullH = 5000;
  int minNum = 1;

  private final Color[] colors = new Color[]{
    new Color(0), new Color(255)
  };

  public TextureRegionTestScene(SceneApi api) {
    super(api);
    g = api.graphics;
    api.input.onMouse.add(new MListener());
    api.input.onScroll.add(this::onMouseWheel);

    linesCanvas = g.createCanvas(200, batchSize * fontSize);
    linesCanvas.setFont(Fonts.Consolas, fontSize);

    updateCanvas = g.createCanvas(200, fontSize);
    updateCanvas.setFont(Fonts.Consolas, fontSize);

    scrollBar = new ScrollBar();
  }

  public void dispose() {
    linesCanvas = Disposable.assign(linesCanvas, null);
    texture = Disposable.assign(texture, null);
    updateCanvas = Disposable.assign(updateCanvas, null);
  }

  public void paint() {
    scrollBar.layoutVertical(scrollPos, 0, viewPortSize.y, editorFullH, viewPortSize.x, 20);
    g.enableBlend(true);
    scrollBar.draw(g);

    if (texture == null) {
      for (int i = 0; i < batchSize; i++) {
        int yPos = fontSize * (i + 1);
        linesCanvas.drawText(String.valueOf(i + 1), 0, yPos);
      }
      texture = g.createTexture();
      texture.setContent(linesCanvas);
    }

    if (scrollPos > texturePos)
      scrollDown();
    else
      scrollUp();


    TextRect allRect = new TextRect(0, 0, texture.width(), texture.height());
    allRect.setTextureRegion(0, 0, texture.width(), texture.height());

    allRect.setColor(colors[0]);
    allRect.setBgColor(colors[1]);
    allRect.drawText(g, texture, 400, 0, 1f);

    int midLine = scrollPos % textureSize;
    int downLine = Math.min(texture.height() - midLine, textureSize - fontSize);

    TextRect upRect = new TextRect(0, 0, texture.width(), downLine);
    upRect.setTextureRegion(0, midLine, texture.width(), downLine);

    upRect.setColor(colors[0]);
    upRect.setBgColor(colors[1]);
    upRect.drawText(g, texture, 0, 0, 1f);

    TextRect downRect = new TextRect(0, downLine, texture.width(), texture.height() - downLine - fontSize);
    downRect.setTextureRegion(0, 0, texture.width(), texture.height() - downLine - fontSize);

    downRect.setColor(colors[1]);
    downRect.setBgColor(colors[0]);
    downRect.drawText(g, texture, 0, 0, 1f);
  }

  public void onResize(V2i size, float dpr) {
    viewPortSize.set(size);
  }

  public boolean update(double timestamp) {
    return false;
  }

  private void scrollDown() {
    while (texturePos < scrollPos - fontSize) {
      updateCanvas.drawText(String.valueOf(++minNum + batchSize - 1), 0, fontSize);
      texture.update(updateCanvas, 0, texturePos % textureSize);
      updateCanvas.clear();
      texturePos += fontSize;
    }
  }

  private void scrollUp() {
    while (texturePos > scrollPos) {
      texturePos -= fontSize;
      updateCanvas.drawText(String.valueOf(--minNum), 0, fontSize);
      texture.update(updateCanvas, 0, texturePos % textureSize);
      updateCanvas.clear();
    }
  }

  int clampScrollPos(int pos) {
    return Math.min(Math.max(0, pos), verticalSize());
  }

  int verticalSize() {
    return editorFullH - viewPortSize.y;
  }

  boolean onMouseWheel(MouseEvent event, float dX, float dY) {
    int change = (Math.abs((int) dY) + 4) / 2;
    int change1 = dY < 0 ? -1 : 1;
    scrollPos = clampScrollPos(scrollPos + change * change1);

    return true;
  }

  private class MListener implements MouseListener {
    Consumer<MouseEvent> dragLock;
    Consumer<ScrollBar.Event> vScrollHandler =
      event -> scrollPos = event.getPosition(verticalSize());

    @Override
    public boolean onMouseDown(MouseEvent event, int button) {
      if (button == MouseListener.MOUSE_BUTTON_LEFT) {
        dragLock = scrollBar.onMouseDown(event.position, vScrollHandler, true);
        if (dragLock != null) return true;
      }
      return true;
    }

    @Override
    public boolean onMouseUp(MouseEvent event, int button) {
      if (dragLock != null) dragLock = null;
      return true;
    }

    @Override
    public boolean onMouseMove(MouseEvent event) {
      if (dragLock != null) {
        dragLock.accept(event);
        return true;
      }
      return scrollBar.onMouseMove(event.position, SetCursor.wrap(api.window));
    }
  }
}
