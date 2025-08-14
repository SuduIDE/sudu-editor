package org.sudu.experiments.ui;

import org.sudu.experiments.DprUtil;
import org.sudu.experiments.Scene0;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.editor.AcceptRejectModel;
import org.sudu.experiments.editor.ClrContext;
import org.sudu.experiments.editor.MergeButtons;
import org.sudu.experiments.editor.MergeButtonsModel;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.ui.colors.MergeButtonsColors;
import org.sudu.experiments.editor.ui.window.TestColors;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.XorShiftRandom;

import java.util.function.Consumer;

public class MergeButtonsTest extends Scene0 implements MouseListener {

  final private SetCursor setCursor;

  MergeButtons buttons1 = new MergeButtons(true);
  MergeButtons buttons2 = new MergeButtons(true);
  MergeButtons buttonsAR = new MergeButtons(true);
  EditorColorScheme colors = EditorColorScheme.darculaIdeaColorScheme();
  MergeButtonsColors mColors = colors.codeDiffMergeButtons();
  boolean toLeft;
  int buttonsHeight;

  private final ScrollBar scrollBar = new ScrollBar();

  private int scrollPos = 0;
  private int lineHeight = 0;
  private int docLines = 100;
  private int virtualSize;
  private FontDesk font;

  public MergeButtonsTest(SceneApi api) {
    super(api);
    setCursor = SetCursor.wrap(api.window);
    TestColors.apply(scrollBar);

    api.input.onMouse.add(this);
    api.input.onScroll.add(this::onMouseWheel);
    api.input.onKeyPress.add(this::onKey);

    XorShiftRandom rand = new XorShiftRandom();
    var m = new MergeButtonsModel.TestModel(docLines, rand);
    var arm = new AcceptRejectModel.TestModel(docLines, rand);
    buttons1.setModel(m.actions, m.lines);
    buttons2.setModel(m.actions, m.lines);
    buttonsAR.setModel(arm.actions, arm.lines);
    var map = diffMap(docLines, rand);
    buttons1.setColors(map);
    buttons2.setColors(map);
    buttonsAR.setColors(map);
  }

  static byte[] diffMap(int docLines, XorShiftRandom rand) {
    byte[] map = new byte[docLines];
    for (int i = 0; i < map.length; i++)
      map[i] = (byte) rand.nextInt(DiffTypes.EDITED + 1);
    return map;
  }

  @Override
  public void dispose() {
    buttons1.dispose();
    buttons2.dispose();
    buttonsAR.dispose();
    super.dispose();
  }

  private boolean onKey(KeyEvent keyEvent) {
    if (keyEvent.keyCode == KeyCode.SPACE) {
      toLeft = !toLeft;
      buttons1.setFont(lineHeight, toLeft, font);
      buttons2.setFont(lineHeight, !toLeft, font);
      buttonsAR.setFont(lineHeight, false, font);
      return true;
    }
    return false;
  }

  Consumer<ScrollBar.Event> vScrollHandler = event -> {
    scrollPos = event.getPosition(scrollMaxPos());
    System.out.println("scrollPos = " + scrollPos + " ... "
        + (scrollPos + buttonsHeight));
  };

  int scrollMaxPos() {
    return virtualSize - buttonsHeight;
  }

  @Override
  public void onResize(V2i newSize, float dpr) {
    super.onResize(newSize, dpr);
    int _20 = DprUtil.toPx(20, dpr);

    int top = DprUtil.toPx(20, dpr);
    int left = DprUtil.toPx(20, dpr);
    buttonsHeight = screen.y / 2;

    font = api.graphics.fontDesk(Fonts.Consolas, 20, dpr);
    lineHeight = font.lineHeight() * 125 / 100;

    buttons1.setFont(lineHeight, !toLeft, font);
    buttons2.setFont(lineHeight, toLeft, font);
    buttonsAR.setFont(lineHeight, false, font);

    int measured1 = buttons1.measure(font, api.graphics.mCanvas, dpr);
    int measured2 = buttons2.measure(font, api.graphics.mCanvas, dpr);
    int measuredAR = buttonsAR.measure(font, api.graphics.mCanvas, dpr);

    buttons1.setPosition(left, top, measured1, buttonsHeight, dpr);
    buttons2.setPosition(left + measured1 + 2,
        top, measured2, buttonsHeight, dpr);
    buttonsAR.setPosition(left + measured1 + 2 + measured2 + 2,
        top, measuredAR, buttonsHeight, dpr);

    System.out.println("measured1 = " + measured1 + ", measured2 = " + measured2);

    virtualSize = docLines * lineHeight;

  }

  final ClrContext ctx = new ClrContext(true);

  public void paint() {
    super.paint();

    buttons1.setScrollPos(scrollPos);
    buttons2.setScrollPos(scrollPos);
    buttonsAR.setScrollPos(scrollPos);
    int firstLine = scrollPos / lineHeight;
    int lastLine = Numbers.iDivRoundUp(scrollPos + buttonsHeight, lineHeight);
    buttons1.draw(
        firstLine, lastLine, (firstLine + lastLine) / 2,
        api.graphics, mColors, ctx, null);
    buttons2.draw(
        firstLine, lastLine, (firstLine + lastLine) / 2,
        api.graphics, mColors, ctx, null);
    buttonsAR.draw(
        firstLine, lastLine, (firstLine + lastLine) / 2,
        api.graphics, mColors, ctx, null);

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
    int right = buttonsAR.pos.x + buttonsAR.size.x;
    scrollBar.layoutVertical(scrollPos,
        buttons2.pos.y, buttons2.size.y,
        virtualSize, right + _22, _20);
  }

  @Override
  public Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    if (button == MOUSE_BUTTON_LEFT) {
      var scr = scrollBar.onMouseDown(event.position, vScrollHandler, true);
      if (scr != null) return scr;
      var b1 = buttons1.onMouseDown(event, button, setCursor);
      if (b1 != null) return b1;
      var b2 = buttons2.onMouseDown(event, button, setCursor);
      if (b2 != null) return b2;
      return buttonsAR.onMouseDown(event, button, setCursor);
    }

    return Static.emptyConsumer;
  }

  @Override
  public boolean onMouseUp(MouseEvent event, int button) {
    boolean b1 = buttons1.onMouseUp(event, button);
    boolean b2 = buttons2.onMouseUp(event, button);
    boolean b3 = buttonsAR.onMouseUp(event, button);
    return b1 || b2 || b3;
  }

  @Override
  public boolean onMouseMove(MouseEvent event) {
    boolean b1 = buttons1.onMouseMove(event, setCursor);
    boolean b2 = buttons2.onMouseMove(event, setCursor);
    boolean b3 = buttonsAR.onMouseMove(event, setCursor);
    return scrollBar.onMouseMove(event.position, setCursor) || b1 || b2 || b3;
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
