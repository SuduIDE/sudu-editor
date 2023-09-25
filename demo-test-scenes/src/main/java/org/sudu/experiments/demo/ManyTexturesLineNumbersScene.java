package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.demo.ui.UiContext;
import org.sudu.experiments.demo.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.function.Consumer;

public class ManyTexturesLineNumbersScene extends Scene {

  final WglGraphics g;
  private final LineNumbersComponent lineNumbers;
  private ScrollBar scrollBar;
  private V2i viewPortSize;
  private int scrollPos = 0;
  private int editorBottom = 500;
  private V4f bgColor = Color.Cvt.gray(0);
  private int lineHeight = 20;
  private int fontSize = 20;
  private SetCursor setCursor;

  public ManyTexturesLineNumbersScene(SceneApi api) {
    super(api);
    this.lineNumbers = new LineNumbersComponent(new UiContext(api));
    api.input.onMouse.add(new LineNumbersInputListener());
    api.input.onScroll.add(this::onMouseWheel);
    this.g = api.graphics;
    setCursor = SetCursor.wrap(api.window);

    lineNumbers.setFont(g.fontDesk(Fonts.Consolas, fontSize), lineHeight);

    scrollBar = new ScrollBar();
  }

  EditorColorScheme colors = EditorColorScheme.darculaIdeaColorScheme();

  @Override
  public void paint() {
    Debug.consoleInfo("scrollPos: " + scrollPos);
    g.clear(bgColor);

    scrollBar.layoutVertical(scrollPos, 0, editorHeight(), 5000, viewPortSize.x, 20);
    g.enableBlend(true);
    scrollBar.draw(g);
    g.enableBlend(false);

    Debug.consoleInfo("scrollPos: " + scrollPos);
    int eh = editorHeight();
    lineNumbers.draw(g, scrollPos, eh, 0, eh / lineHeight, -1, colors);
  }

  @Override
  public void onResize(V2i size, float dpr) {
    viewPortSize = size;
    lineNumbers.setPos(new V2i(0, 0), 50, editorHeight(), dpr);
  }

  @Override
  public void dispose() {
    lineNumbers.dispose();
  }

  boolean onMouseWheel(MouseEvent event, float dX, float dY) {
    int change = (Math.abs((int) dY) + 4) / 2;
    int change1 = dY < 0 ? -1 : 1;
    scrollPos = clampScrollPos(scrollPos + change * change1);

    return true;
  }

  private class LineNumbersInputListener implements MouseListener {
    Consumer<ScrollBar.Event> vScrollHandler =
      event -> scrollPos = event.getPosition(verticalSize());

    @Override
    public Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
      if (button == MOUSE_BUTTON_LEFT) {
        return scrollBar.onMouseDown(event.position, vScrollHandler, true);
      }

      return Static.emptyConsumer;
    }

    @Override
    public boolean onMouseUp(MouseEvent event, int button) {
      return true;
    }

    @Override
    public boolean onMouseMove(MouseEvent event) {
      return scrollBar.onMouseMove(event.position, setCursor);
    }
  }

  int clampScrollPos(int pos) {
    return Math.min(Math.max(0, pos), verticalSize());
  }

  int verticalSize() {
    return 5000 - editorHeight();
  }

  int editorHeight() {
    return Math.min(editorBottom, viewPortSize.y);
  }

}
