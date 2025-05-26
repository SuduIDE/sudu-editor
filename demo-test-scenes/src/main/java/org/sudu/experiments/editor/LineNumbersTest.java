package org.sudu.experiments.editor;

import org.sudu.experiments.*;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.ui.window.TestColors;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.ScrollBar;
import org.sudu.experiments.ui.SetCursor;

import java.util.function.Consumer;

public class LineNumbersTest extends Scene0 {

  public static final int yVirtualSize = 5000;
  final WglGraphics g;

  private final SetCursor setCursor;

  private final LineNumbersComponent lineNumbers = new LineNumbersComponent();
  private final ScrollBar scrollBar = new ScrollBar();
  private final Consumer<ScrollBar.Event> vScrollHandler = this::onScrollEvent;
  EditorColorScheme colors = EditorColorScheme.darkIdeaColorScheme();

  private int scrollPos = 0;
  private int lineHeight = 20;
  private int fontSize = 20;

  public LineNumbersTest(SceneApi api) {
    super(api);
    Color.Cvt.gray(0, clearColor);
    api.input.onMouse.add(new LineNumbersInputListener());
    api.input.onScroll.add(this::onMouseWheel);
    this.g = api.graphics;
    setCursor = SetCursor.wrap(api.window);

    lineNumbers.setFont(
        g.fontDesk(Fonts.Consolas, fontSize),
        lineHeight, g.cleartypeSupported);

    TestColors.apply(scrollBar);
  }

  @Override
  public void paint() {
    g.clear(clearColor);

    int controlHeight = lineNumbers.size.y;
    g.enableBlend(true);
    layoutScroll();
    scrollBar.draw(g);
    g.enableBlend(false);

    int yPos = -(scrollPos % lineHeight);
    lineNumbers.beginDraw(g, 0);
    int firstLine = scrollPos / lineHeight;
    int lastLine = 2 * controlHeight / lineHeight;

    lineNumbers.drawRange(yPos, firstLine, lastLine, g, colors);
    int dY = yPos + (lastLine - firstLine) * lineHeight;
    lineNumbers.drawEmptyLines(dY, g, colors);
    lineNumbers.drawCaretLine(-scrollPos, 10, 10, colors, g);
    lineNumbers.endDraw(g);
  }

  private void onScrollEvent(ScrollBar.Event event) {
    scrollPos = event.getPosition(scrollMaxValue());
  }

  private void layoutScroll() {
    int _20 = DprUtil.toPx(20, lineNumbers.dpr);
    int _22 = DprUtil.toPx(22, lineNumbers.dpr);
    scrollBar.layoutVertical(scrollPos,
        lineNumbers.pos.y,
        lineNumbers.size.y, yVirtualSize,
        lineNumbers.pos.x + lineNumbers.size.x + _22, _20);
  }

  @Override
  public void onResize(V2i size, float dpr) {
    super.onResize(size, dpr);
    int w = DprUtil.toPx(80, dpr);
    int top = DprUtil.toPx(20, dpr);
    int left = DprUtil.toPx(20, dpr);
    lineNumbers.setPosition(left, top,
        w, size.y / 2, dpr);
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
    return Math.min(Math.max(0, pos), scrollMaxValue());
  }

  int scrollMaxValue() {
    return yVirtualSize - lineNumbers.size.y;
  }
}
