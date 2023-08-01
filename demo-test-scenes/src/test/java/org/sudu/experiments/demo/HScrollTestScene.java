package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.ArrayList;
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

  final EditorColorScheme colors = EditorColorScheme.darkIdeaColorScheme();

  public HScrollTestScene(SceneApi api) {
    super(api);
    g = new TestGraphics(api.graphics);

    api.input.onMouse.add(new HScrollInputListener());
    api.input.onScroll.add(this::onMouseWheel);

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
  ArrayList<CodeElement> usages = new ArrayList<>();

  @Override
  public void paint() {
    g.clear(IdeaCodeColors.Colors.editBgColor);
    g.enableBlend(true);
    scrollBar.layoutHorizontal(scrollPosH, 0, viewportSize.x, horizontalSize, viewportSize.y, 20);
    scrollBar.draw(g);
    g.enableBlend(false);

    codeLineRenderer.updateTextureOnScroll(renderCanvas, fontDesk, fontDesk[0].iSize, scrollPosH);

    codeLineRenderer.draw(200, 0, g, new V4f(), new V2i(), 1f,
        viewportSize.x, fontSize, scrollPosH, colors, null, null, usages);

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
  public void onResize(V2i size, float dpr) {
    viewportSize.set(size);
  }

  @Override
  public void dispose() {
    g.dispose();
    renderCanvas.dispose();
    codeLineRenderer.dispose();
  }

  boolean onMouseWheel(MouseEvent event, double dX, double dY) {
    int change = (Math.abs((int) dX) + 64) / 2;
    int changeX = (int) Math.signum(dX);
    int changeY = (int) Math.signum(dY);
    scrollPosH = clampScrollPos(scrollPosH + change * (changeX + changeY));

    return true;
  }


  private class HScrollInputListener implements MouseListener {
    Consumer<MouseEvent> dragLock;
    Consumer<IntUnaryOperator> vScrollHandler =
      move -> scrollPosH = move.applyAsInt(horizontalSize());


    @Override
    public boolean onMouseDown(MouseEvent event, int button) {
      if (button == MouseListener.MOUSE_BUTTON_LEFT) {
        dragLock = scrollBar.onMouseClick(event.position, vScrollHandler, false);
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

  int clampScrollPos(int pos) {
    return Math.min(Math.max(0, pos), horizontalSize());
  }

  int horizontalSize() {
    return horizontalSize - viewportSize.x;
  }
}
