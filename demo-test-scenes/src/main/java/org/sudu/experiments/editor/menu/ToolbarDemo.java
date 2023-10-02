package org.sudu.experiments.editor.menu;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.editor.Scene1;
import org.sudu.experiments.editor.TestHelper;
import org.sudu.experiments.editor.ui.colors.DialogColors;
import org.sudu.experiments.editor.ui.colors.DialogItemColors;
import org.sudu.experiments.editor.ui.colors.Themes;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.RngHelper;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.XorShiftRandom;
import org.sudu.experiments.ui.*;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ToolbarDemo extends Scene1 implements MouseListener, DprChangeListener {

  final TestHelper.Cross c = new TestHelper.Cross();

  private final Toolbar tbH = new Toolbar();
  private final Toolbar tbV = new Toolbar();

  private final PopupMenu popupMenu;
  private final UiFont consolas;

  public ToolbarDemo(SceneApi api) {
    super(api);
    popupMenu = new PopupMenu(uiContext);
    uiContext.dprListeners.add(this);

    api.input.onKeyPress.add(this::onKeyPress);
    api.input.onMouse.add(this);
    api.input.onContextMenu.add(this::onContextMenu);

    tbV.setLayoutVertical();
    consolas = new UiFont("Consolas", 25);
    popupMenu.setTheme(Themes.darculaColorScheme(), consolas);
    clearColor.set(new Color(43));

    DialogItemColors dark = Themes.darculaColorScheme();
    tbH.setTheme(dark);
    tbV.setTheme(dark);

    tbH.onClickOutside(() -> System.out.println("tbH onClickOutside"));
    tbV.onClickOutside(() -> System.out.println("tbV onClickOutside"));
    onEnterLeave(tbV);

    tbH.setItems(items(0).get());
    tbV.setItems(items(0).get());
  }

  @Override
  public void onDprChanged(float oldDpr, float newDpr) {
    FontDesk font = uiContext.fontDesk(consolas);
    tbH.setFont(font);
    tbV.setFont(font);
    tbH.measure(uiContext);
    tbV.measure(uiContext);
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
        DialogColors.Darcula.toolbarBg, DialogColors.Darcula.toolbarSelectedBg);
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
    tbH.dispose();
    tbV.dispose();
    popupMenu.dispose();
  }

  @Override
  public void onResize(V2i newSize, float dpr) {
    super.onResize(newSize, dpr);
    layout();
  }

  private void layout() {
    V2i windowSize = uiContext.windowSize;
    V2i tbhSize = tbH.size();
    V2i tbvSize = tbV.size();
    tbH.setPos((windowSize.x - tbhSize.x) / 2, (windowSize.y - 3 * tbhSize.y) / 2 - 5);
    tbV.setPos((windowSize.x - tbvSize.x) / 2, (windowSize.y) / 2 + 5);
  }

  @Override
  public void paint() {
    super.paint();
    WglGraphics graphics = api.graphics;
    graphics.enableBlend(true);
    c.draw(uiContext);
    tbH.render(uiContext);
    tbV.render(uiContext);
    popupMenu.paint();
    graphics.enableBlend(false);
  }

  @Override
  public boolean onMouseMove(MouseEvent event) {
    boolean r = popupMenu.onMouseMove(event);
    boolean tbHResult = tbH.onMouseMove(event.position, uiContext.windowCursor);
    boolean tbVResult = tbV.onMouseMove(event.position, uiContext.windowCursor);
    return r || tbHResult || tbVResult;
  }

  @Override
  public Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    return popupMenu.onMouseDown(event, button);
  }

  @Override
  public boolean onMouseUp(MouseEvent event, int button) {
    return popupMenu.onMouseUp(event, button);
  }

  @Override
  public boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    boolean r = popupMenu.onMouseClick(event, button, clickCount);
    boolean tbHResult = tbH.onMouseClick(event.position, button, clickCount);
    boolean tbVResult = tbV.onMouseClick(event.position, button, clickCount);
    return r || tbHResult || tbVResult;
  }

  boolean onContextMenu(MouseEvent event) {
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

  private boolean onKeyPress(KeyEvent event) {
    if (event.keyCode == KeyCode.SPACE) {
      tbV.dispose();
    }
    return false;
  }
}
