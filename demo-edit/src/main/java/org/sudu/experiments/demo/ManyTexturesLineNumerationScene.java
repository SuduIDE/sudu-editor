package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.input.InputListener;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;

public class ManyTexturesLineNumerationScene extends Scene {

  final WglGraphics g;
  private final LineNumbersComponent lineNumeration;
  private ScrollBar scrollBar;
  private V2i viewPortSize;
  private int scrollPos = 0;
  private int editorBottom = 500;
  private V4f bgColor = Color.Cvt.gray(0);
  private int lineHeight = 20;
  private int fontSize = 20;

  public ManyTexturesLineNumerationScene(SceneApi api){
    //super(api);
    api.input.addListener(new LineNumerationInputListener());
    this.g = api.graphics;

    lineNumeration = new LineNumbersComponent(
      g, new V2i(0, 0), 50,
      new Color(0), new Color(255)
    );
    lineNumeration.setFont(g.fontDesk(Fonts.Consolas, fontSize), lineHeight);

    scrollBar = new ScrollBar();
  }

  @Override
  public boolean update(double timestamp) {
    return false;
  }

  @Override
  public void paint() {
    Debug.consoleInfo("scrollPos: " + scrollPos);
    g.clear(bgColor);

    scrollBar.layoutVertical(scrollPos, viewPortSize.x, editorBottom, 5000, 20);
    g.enableBlend(true);
    scrollBar.draw(g);
    g.enableBlend(false);

    Debug.consoleInfo("scrollPos: " + scrollPos);

    lineNumeration.update(scrollPos / 20);
    lineNumeration.draw(scrollPos);
  }

  @Override
  public void onResize(V2i size) {
    viewPortSize = size;
    lineNumeration.resize(size, 500);
    lineNumeration.initTextures();
  }

  @Override
  public void dispose() {
    lineNumeration.dispose();
  }

  private class LineNumerationInputListener implements InputListener {
    Consumer<V2i> dragLock;
    Consumer<IntUnaryOperator> vScrollHandler =
      move -> scrollPos = move.applyAsInt(verticalSize());

    @Override
    public boolean onMouseWheel(MouseEvent event, double dX, double dY) {
      int change = (Math.abs((int) dY) + 4) / 2;
      int change1 = dY < 0 ? -1 : 1;
      scrollPos = clampScrollPos(scrollPos + change * change1);

      return true;
    }

    @Override
    public boolean onMousePress(MouseEvent event, int button, boolean press, int clickCount) {
      if (!press && dragLock != null) {
        dragLock = null;
        return true;
      }

      if (button == mouseButtonLeft && clickCount == 1 && press) {
        dragLock = scrollBar.onMouseClick(event.position, vScrollHandler);
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
      return scrollBar.onMouseMove(event.position);
    }
  }

  int clampScrollPos(int pos) {
    return Math.min(Math.max(0, pos), verticalSize());
  }

  int verticalSize() {
    return 5000 - viewPortSize.y;
  }

}
