package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.input.InputListener;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;

public class HScrollTestScene extends Scene {

  TestGraphics g;
  final int MAX_NUM = 150;
  final int dx = 50;
  final int fontSize = 20;
  final int horizontalSize = dx * MAX_NUM;
  final V2i viewportSize = new V2i();

  ScrollBar scrollBar = new ScrollBar();
  int scrollPosH = 0;

  CodeLineRenderer codeLineRenderer;
  CodeLine codeLine;
  TestCanvas renderCanvas;
  FontDesk[] fontDesk = new FontDesk[1];

  boolean needsUpdate = true;

  final EditorColorScheme colors = new EditorColorScheme();

  public HScrollTestScene(SceneApi api) {
    super(api);
    g = new TestGraphics(api.graphics);

    api.input.addListener(new HScrollInputListener());

    CodeElement[] codeElements = new CodeElement[]{
      new CodeElement("Первое слово", 0),
      new CodeElement("Второе", 0),
      new CodeElement("3-е", 0),
      new CodeElement("Слово номер четыре", 0),
      new CodeElement("Lorem ipsum dolor sit amet, ", 0),
      new CodeElement("consectetur adipiscing elit, ", 0),
      new CodeElement("sed do eiusmod tempor incididunt ut labore et dolore magna aliqua sed do eiusmod tempor incididunt ut labore et dolore magna aliqua", 0),
      new CodeElement("Ut enim ad minim veniam", 0),
      new CodeElement("3-е", 0),
      new CodeElement("Слово номер четыре", 0),
      new CodeElement("Lorem ipsum dolor sit amet, ", 0),
      new CodeElement("consectetur adipiscing elit, ", 0),
      new CodeElement("Ut enim ad minim veniam", 0),
      new CodeElement("3-е", 0),
      new CodeElement("Слово номер четыре", 0),
      new CodeElement("Lorem ipsum dolor sit amet, ", 0),
      new CodeElement("consectetur adipiscing elit, ", 0)
    };

    codeLine = new CodeLine(codeElements);
    codeLineRenderer = new CodeLineRenderer();

    fontDesk[0] = g.fontDesk(Fonts.Consolas, fontSize);
    g.mCanvas.setFont(fontDesk[0]);

    renderCanvas = new TestCanvas(g.createCanvas(EditorConst.TEXTURE_WIDTH, fontDesk[0].iSize));
  }

  @Override
  public boolean update(double timestamp) {
    return false;
  }

  Color error = new Color(188, 63, 60);
  V4f debugColor = new Color("#CC7832");
  V4f debugColorBg = new Color("#A9B7C6");
  @Override
  public void paint() {
    g.clear(IdeaCodeColors.Colors.editBgColor);
    g.enableBlend(true);
    scrollBar.layoutHorizontal(scrollPosH, viewportSize.y, viewportSize.x, horizontalSize, 20);
    scrollBar.draw(g, new V2i(0, 0));
    g.enableBlend(false);

    codeLineRenderer.updateTextureOnScroll(renderCanvas, fontDesk, fontDesk[0].iSize, scrollPosH);

    codeLineRenderer.draw(200, 0, g, new V4f(), new V2i(), 1f,
        viewportSize.x, fontSize, scrollPosH, colors, null);

    codeLineRenderer.drawDebug(300, 0, fontSize, g, debugColor, debugColorBg);

    if (needsUpdate) {
      codeLineRenderer.updateTexture(codeLine, renderCanvas, fontDesk, g, fontDesk[0].iSize, viewportSize.x, scrollPosH);
      needsUpdate = false;
    }

    g.drawRect(scrollPosH, 0, new V2i(1, viewportSize.y), error);

    Debug.consoleInfo("hScrollPos: " + scrollPosH);
    Debug.consoleInfo("lineMeasure: " + codeLine.lineMeasure());
    Debug.consoleInfo("textureWidth: " + EditorConst.TEXTURE_WIDTH);
    Debug.consoleInfo("Canvas: ");
    renderCanvas.debug();
    Debug.consoleInfo("Graphics:");
    g.debug();
    Debug.consoleInfo("Renderer:");
    codeLineRenderer.debug();
    Debug.consoleInfo("____________________");
  }

  @Override
  public void onResize(V2i size, double dpr) {
    viewportSize.set(size);
  }

  @Override
  public void dispose() {
    g.dispose();
    renderCanvas.dispose();
    codeLineRenderer.dispose();
  }

  private class HScrollInputListener implements InputListener {
    Consumer<V2i> dragLock;
    Consumer<IntUnaryOperator> vScrollHandler =
      move -> scrollPosH = move.applyAsInt(horizontalSize());

    @Override
    public boolean onMouseWheel(MouseEvent event, double dX, double dY) {
      int change = (Math.abs((int) dX) + 64) / 2;
      int changeX = (int) Math.signum(dX);
      int changeY = (int) Math.signum(dY);
      scrollPosH = clampScrollPos(scrollPosH + change * (changeX + changeY));

      return true;
    }

    @Override
    public boolean onMousePress(MouseEvent event, int button, boolean press, int clickCount) {
      if (!press && dragLock != null) {
        dragLock = null;
        return true;
      }

      if (button == InputListener.MOUSE_BUTTON_LEFT && clickCount == 1 && press) {
        dragLock = scrollBar.onMouseClick(event.position, vScrollHandler, false);
        if (dragLock != null) return true;
      }

      return true;
    }

    @Override
    public boolean onMouseMove(MouseEvent event) {
      if (dragLock != null) {
        dragLock.accept(event.position);
        return true;
      }
      return scrollBar.onMouseMove(event.position, SetCursor.wrap(api.window));
    }
  }

  int clampScrollPos(int pos) {
    return Math.min(Math.max(0, pos), horizontalSize());
  }

  int horizontalSize() {
    return horizontalSize - viewportSize.x;
  }
}