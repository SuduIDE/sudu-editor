package org.sudu.experiments.ui;

import org.sudu.experiments.DprUtil;
import org.sudu.experiments.Scene0;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.editor.ClrContext;
import org.sudu.experiments.editor.MergeButtons;
import org.sudu.experiments.editor.test.MergeButtonsTestModel;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.ui.window.TestColors;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.fonts.Codicons;

import java.util.function.Consumer;

public class MergeButtonsTest extends Scene0 implements MouseListener {

  final private SetCursor setCursor;

  MergeButtons buttons = new MergeButtons(true);
  EditorColorScheme colors = EditorColorScheme.darculaIdeaColorScheme();

  private final ScrollBar scrollBar = new ScrollBar();

  private int scrollPos = 0;
  private int lineHeight = 20;
  private int docLines = 100;
  private int virtualSize;

  public MergeButtonsTest(SceneApi api) {
    super(api);
    setCursor = SetCursor.wrap(api.window);
    TestColors.apply(scrollBar);

    api.input.onMouse.add(this);
    api.input.onScroll.add(this::onMouseWheel);

    int n = 10;
    MergeButtonsTestModel m = new MergeButtonsTestModel(n, docLines);
    buttons.setModel(m.actions, m.lines);
  }

  Consumer<ScrollBar.Event> vScrollHandler = event -> {
    scrollPos = event.getPosition(scrollMaxPos());
    System.out.println("scrollPos = " + scrollPos + " ... "
        + (scrollPos + buttons.size.y));
  };

  int scrollMaxPos() {
    return virtualSize - buttons.size.y;
  }

  @Override
  public void onResize(V2i newSize, float dpr) {
    super.onResize(newSize, dpr);
    int w = DprUtil.toPx(80, dpr);
    int top = DprUtil.toPx(20, dpr);
    int left = DprUtil.toPx(20, dpr);
    buttons.setPosition(left, top, w, size.y / 2, dpr);

    int _20 = DprUtil.toPx(20, dpr);
    lineHeight = _20;
    virtualSize = docLines * lineHeight;

    FontDesk fontDesk = api.graphics.fontDesk(Codicons.typeface, 10, dpr);
    buttons.setFont(lineHeight, true);
  }

  final ClrContext ctx = new ClrContext(true);

  public void paint() {
    super.paint();
    int controlHeight = buttons.size.y;

    buttons.setScrollPos(scrollPos);
    int firstLine = scrollPos / lineHeight;
    int lastLine = Numbers.iDivRoundUp(scrollPos + controlHeight, lineHeight);
    buttons.draw(
        firstLine, lastLine, (firstLine + lastLine) / 2,
        api.graphics, colors, ctx);

    drawScrollBar(api.graphics);
  }

  void drawScrollBar(WglGraphics g) {
    g.enableBlend(true);
    layoutScroll();
    scrollBar.draw(g);
    g.enableBlend(false);
  }

  private void layoutScroll() {
    int _20 = DprUtil.toPx(20, dpr);
    int _22 = DprUtil.toPx(22, dpr);
    int right = buttons.pos.x + buttons.size.x;
    scrollBar.layoutVertical(scrollPos,
        buttons.pos.y, buttons.size.y,
        virtualSize, right + _22, _20);
  }

  @Override
  public Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    if (button == MOUSE_BUTTON_LEFT) {
      var scr = scrollBar.onMouseDown(event.position, vScrollHandler, true);
      if (scr != null) return scr;
      return buttons.onMouseDown(event, button, setCursor);
    }

    return Static.emptyConsumer;
  }

  @Override
  public boolean onMouseUp(MouseEvent event, int button) {
    return buttons.onMouseUp(event, button);
  }

  @Override
  public boolean onMouseMove(MouseEvent event) {
    return scrollBar.onMouseMove(event.position, setCursor)
        || buttons.onMouseMove(event, setCursor);
  }

  boolean onMouseWheel(MouseEvent event, float dX, float dY) {
    int change = (Math.abs((int) dY) + 4) / 2;
    int change1 = dY < 0 ? -1 : 1;
    scrollPos = clampScrollPos(scrollPos + change * change1);

    return true;
  }

  int clampScrollPos(int pos) {
    return Math.min(Math.max(0, pos), scrollMaxPos());
  }
}
