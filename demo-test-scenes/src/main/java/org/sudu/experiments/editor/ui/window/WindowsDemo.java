package org.sudu.experiments.editor.ui.window;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.WindowScene;
import org.sudu.experiments.editor.ui.colors.Themes;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.XorShiftRandom;
import org.sudu.experiments.ui.*;
import org.sudu.experiments.ui.window.ScrollView;
import org.sudu.experiments.ui.window.Window;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.sudu.experiments.Const.emptyRunnable;

public class WindowsDemo extends WindowScene implements DprChangeListener {

  final int titleMargin = 3;
  final UiFont titleFont = new UiFont(Fonts.SegoeUI, 20);
  final XorShiftRandom r = new XorShiftRandom();
  int n = 1;

  public WindowsDemo(SceneApi api) {
    super(api);
    uiContext.dprListeners.add(this);
    clearColor.set(new Color(43));
    api.input.onKeyPress.add(this::onKey);
    api.input.onContextMenu.add(this::onContextMenu);
  }

  @Override
  public void onDprChanged(float oldDpr, float newDpr) {
    if (oldDpr == 0) openWindows();
  }

  private boolean onContextMenu(MouseEvent event) {
    var popupMenu = new PopupMenu(uiContext);
    popupMenu.setTheme(Themes.darculaColorScheme(),
        new UiFont("Consolas", 25));
    popupMenu.setItems(event.position, items(), emptyRunnable);
    windowManager.setPopupMenu(popupMenu);
    return true;
  }

  private Supplier<ToolbarItem[]> items() {
    return ArrayOp.supplier(
        new ToolbarItem(
            windowManager.hidePopupMenuThen(this::addWindow),
            "addWindow"));
  }

  private void addWindow() {
    windowManager.addWindow(newWindow());
  }

  private void openWindows() {
    windowManager.addWindow(newWindow());
    Window noScroll = newWindow(.5f, false, "Window 1");
    V2i screen = uiContext.windowSize;
    windowManager.addWindow(noScroll).setPosition(
        new V2i(screen.x / 10, screen.y / 10),
        new V2i(screen.x * 6 / 10, screen.y * 6 / 10)
    );
  }

  private Window newWindow() {
    Window window = newWindow(1, true, "Window " + ++n);
    V2i screen = uiContext.windowSize;
    int x = screen.x / 10  + r.nextInt(screen.x / 10);
    int y = screen.y / 20  + r.nextInt(screen.y / 20);
    int w = screen.x * 7 / 10  + r.nextInt(screen.x / 10);
    int h = screen.y * 7 / 10  + r.nextInt(screen.y / 10);
    window.setPosition(new V2i(x, y), new V2i(w, h));
    return window;
  }

  private Window newWindow(float v, boolean scroll, String title) {
    Window window = new Window(uiContext);
    Consumer<V2i> sizeListener = title != null
        ? s -> window.setTitle(title + ": " + s) : s -> {};
    ScrollContentDemo contentDemo = new ScrollContentDemo(v, sizeListener);
    window.setContent(scroll ? newScrollView(contentDemo) : contentDemo);
    window.setTheme(Themes.darculaColorScheme());
    window.setTitleFont(titleFont, titleMargin);
    window.setOnClose(() -> {
      windowManager.removeWindow(window);
      window.dispose();
    });
    return window;
  }

  private ScrollView newScrollView(ScrollContentDemo contentDemo) {
    ScrollView scrollView = new ScrollView(contentDemo);
    TestColors.apply(scrollView);
    return scrollView;
  }

  private boolean onKey(KeyEvent event) {
    if (event.isPressed && event.keyCode == KeyCode.ESC) {
      Window top = windowManager.topWindow();
      if (top != null) top.close();
    }
    return false;
  }
}
