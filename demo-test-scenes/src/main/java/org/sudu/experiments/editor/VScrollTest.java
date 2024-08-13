package org.sudu.experiments.editor;

import org.sudu.experiments.*;
import org.sudu.experiments.editor.ui.window.TestColors;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.ScrollBar;
import org.sudu.experiments.ui.SetCursor;

import java.util.function.Consumer;

public class VScrollTest extends Scene0 implements MouseListener {

  final WglGraphics g;

  private final SetCursor setCursor;
  private final ScrollBar scrollBar = new ScrollBar();

  DemoRect control = new DemoRect();

  int scrollPos = 0;
  int virtualSize = 5000;

  Consumer<ScrollBar.Event> vScrollHandler = event -> {
    scrollPos = event.getPosition(scrollMaxValue());
    System.out.println("scrollPos = " + scrollPos +
        " ... " + (scrollPos + control.size.y));
  };

  public VScrollTest(SceneApi api) {
    super(api);
    Color.Cvt.gray(0, clearColor);
    Color.Cvt.gray(100, control.color);
    api.input.onMouse.add(this);
    api.input.onScroll.add(this::onMouseWheel);
    this.g = api.graphics;
    setCursor = SetCursor.wrap(api.window);

    TestColors.apply(scrollBar);
  }

  @Override
  public void paint() {
    g.clear(clearColor);
    control.draw(g, 0, 0);
    g.enableBlend(true);
    layoutScroll();
    scrollBar.draw(g);
    g.enableBlend(false);
  }

  private void layoutScroll() {
    int _20 = DprUtil.toPx(20, dpr);
    int _22 = DprUtil.toPx(22, dpr);
    scrollBar.layoutVertical(scrollPos,
        control.pos.y,
        controlHeight(), virtualSize,
        control.pos.x + control.size.x + _22, _20);
  }

  @Override
  public void onResize(V2i size, float dpr) {
    super.onResize(size, dpr);
    int _20 = DprUtil.toPx(20, dpr);
    control.size.set(_20, size.y / 2);
  }

  boolean onMouseWheel(MouseEvent event, float dX, float dY) {
    int change = (Math.abs((int) dY) + 4) / 2;
    int change1 = dY < 0 ? -1 : 1;
    scrollPos = clampScrollPos(scrollPos + change * change1);

    return true;
  }

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

  int clampScrollPos(int pos) {
    return Math.min(Math.max(0, pos), scrollMaxValue());
  }

  int scrollMaxValue() { return virtualSize - controlHeight(); }

  int controlHeight() { return control.size.y; }

}
