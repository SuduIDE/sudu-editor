package org.sudu.experiments.demo.menu;

import org.sudu.experiments.Scene0;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.demo.Colors;
import org.sudu.experiments.demo.SetCursor;
import org.sudu.experiments.demo.ui.*;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.InputListener;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.*;

import java.util.function.Supplier;

public class ToolbarDemo extends Scene0 implements InputListener, DprChangeListener {

  private final FontDesk font;

  private final Toolbar tbH = new Toolbar();
  private final Toolbar tbV = new Toolbar();
  private final V2i hLine = new V2i();
  private final V2i vLine = new V2i();

  private final UiContext uiContext;
  private final PopupMenu popupMenu;

  public ToolbarDemo(SceneApi api) {
    super(api);
    uiContext = new UiContext(api);
    popupMenu = new PopupMenu(uiContext);
    uiContext.dprListeners.add(this);

    api.input.addListener(this);

    tbV.setLayoutVertical();
    font = api.graphics.fontDesk("Consolas", 25);

    popupMenu.setFont(font);
    clearColor.set(new Color(43));

    setToolbarStyle(tbH);
    setToolbarStyle(tbV);

    tbH.onClickOutside(() -> System.out.println("tbH onClickOutside"));
    tbV.onClickOutside(() -> System.out.println("tbV onClickOutside"));
    onEnterLeave(tbV);

    tbH.setItems(items(0).get());
    tbV.setItems(items(0).get());

  }

  @Override
  public void onDprChanged(float oldDpr, float newDpr) {
    tbH.measure(uiContext);
    tbV.measure(uiContext);
  }

  private void setToolbarStyle(Toolbar tb) {
    tb.setFont(font);
    tb.setTheme(DialogItemColors.darkColorScheme());
  }

  private void onEnterLeave(Toolbar tb) {
    tb.onEnter((mouse, index, item) ->
        System.out.println(
            "onEnter item " + index + ", item " + item));
    tb.onLeave((mouse, index, item) ->
        System.out.println(
            "onLeave item " + index + ", item " + item));
  }

  private static Supplier<ToolbarItem[]> items(int dph) {
    return items(dph, new XorShiftRandom());
  }

  private static Supplier<ToolbarItem[]> items(int dph, XorShiftRandom r) {
    ToolbarItemBuilder tbb = new ToolbarItemBuilder();
    addAction(tbb, pref(dph, RngHelper.rngString(r, 25)),
        dph == 0 ? null : items(dph - 1, r));
    addAction(tbb, pref(dph, RngHelper.rngString(r, 20)),
        dph == 0 ? null : items(dph - 1, r));
    addAction(tbb, pref(dph, RngHelper.rngString(r, 15)),
        dph == 0 ? null : items(dph - 1, r));
    addAction(tbb, pref(dph, RngHelper.rngString(r, 10)),
        dph == 0 ? null : items(dph - 1, r));
    return tbb.supplier();
  }

  private static String pref(int dph, String text) {
    return dph == 0 ? text : dph + ": " + text;
  }

  static ToolbarItemColors rngToolButton() {
    return new ToolbarItemColors(
            Color.Cvt.fromHSV(Math.random(), 1, 1),
            Colors.toolbarBg, Colors.toolbarSelectedBg);
  }

  private static void addAction(ToolbarItemBuilder tb, String action, Supplier<ToolbarItem[]> sub) {
    ToolbarItemColors colors = rngToolButton();
    if (sub != null) {
      tb.addItem(action, colors, sub);
    } else {
      tb.addItem(action, colors, () -> System.out.println(action));
    }
  }

  @Override
  public void dispose() {
    super.dispose();
    tbH.dispose();
    tbV.dispose();
    popupMenu.dispose();
  }

  @Override
  public void onResize(V2i newSize, float dpr) {
    uiContext.onResize(newSize, dpr);
    size.set(newSize);

    hLine.set(newSize.x, Numbers.iRnd(dpr) * 2);
    vLine.set(Numbers.iRnd(dpr) * 2, newSize.y);

    V2i tbhSize = tbH.size();
    V2i tbvSize = tbV.size();
    tbH.setPos((newSize.x - tbhSize.x) / 2, (newSize.y - 3 * tbhSize.y) / 2 - 5);
    tbV.setPos((newSize.x - tbvSize.x) / 2, (newSize.y + 3 * tbvSize.y) / 2 + 5);
  }

  @Override
  public void paint() {
    super.paint();
    WglGraphics graphics = api.graphics;
    graphics.enableBlend(true);
    V4f crossColors = Colors.scrollBarBody1;
    graphics.drawRect(0, size.y / 2 - hLine.y / 2, hLine, crossColors);
    graphics.drawRect(size.x / 2 - vLine.x / 2, 0, vLine, crossColors);
    graphics.enableBlend(true);
    tbH.render(uiContext);
    tbV.render(uiContext);
    popupMenu.paint();
    graphics.enableBlend(false);
  }

  @Override
  public boolean onMouseMove(MouseEvent event) {
    boolean r = popupMenu.onMouseMove(event.position, uiContext.windowCursor);
    boolean tbHResult = tbH.onMouseMove(event.position, uiContext.windowCursor);
    boolean tbVResult = tbV.onMouseMove(event.position, uiContext.windowCursor);
    return r || tbHResult || tbVResult;
  }

  @Override
  public boolean onMousePress(MouseEvent event, int button, boolean press, int clickCount) {
    boolean r = popupMenu.onMousePress(event.position, button, press, clickCount);
    boolean tbHResult = tbH.onMousePress(event.position, button, press, clickCount);
    boolean tbVResult = tbV.onMousePress(event.position, button, press, clickCount);
    return r || tbHResult || tbVResult;
  }

  @Override
  public boolean onContextMenu(MouseEvent event) {
    System.out.println("onContextMenu");
    if (!popupMenu.isVisible()) {
      popupMenu.display(event.position, items(4),
          this::onPopupClosed);
    }
    return true;
  }

  private void onPopupClosed() {
    System.out.println("onPopupClosed");
  }

  @Override
  public boolean onKey(KeyEvent event) {
    if (event.isPressed && event.keyCode == KeyCode.SPACE) {
      tbV.dispose();
      return true;
    }
    return false;
  }

  @Override
  public boolean update(double timestamp) {
    return false;
  }
}
