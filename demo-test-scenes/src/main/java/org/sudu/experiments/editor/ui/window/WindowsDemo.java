package org.sudu.experiments.editor.ui.window;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.diff.BinDataCache;
import org.sudu.experiments.diff.BinaryDiffView;
import org.sudu.experiments.diff.TestDiffContent;
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
import org.sudu.experiments.ui.window.ScrollContent;
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
    if (oldDpr == 0) initializeWindows();
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
            "addWindow"),
        new ToolbarItem(
            windowManager.hidePopupMenuThen(this::addBinDiff),
            "addBinDiff")
        );
  }

  private void addWindow() {
    Window window = newWindow(1, true, "Window " + ++n);
    setWindowRandomPos(window);
    windowManager.addWindow(window);
  }

  private void addBinDiff() {
    windowManager.addWindow(newBinWindow());
  }

  private Window newBinWindow() {
    String title = "BinView " + ++n;
    Window window = new Window(uiContext);
    var content = new BinaryDiffView(new UiContext(api));
    XorShiftRandom r = new XorShiftRandom();
    TestDiffContent sl = new TestDiffContent(
        1024 * 1024 + r.nextInt(1024 * 1024),
        r.nextInt(1024) + 1);
    TestDiffContent sr = new TestDiffContent(
        1024 * 1024 + r.nextInt(1024 * 1024),
        r.nextInt(1024) + 1);
    content.setData(sl, api.input.repaint, true);
    content.setData(sr, api.input.repaint, false);
    window.setContent(new ScrollView(content));
    window.setTheme(Themes.darculaColorScheme());
    window.setTitleFont(titleFont, titleMargin);
    window.setTitle(title);
    addColoseWindowHandler(window);

    setWindowRandomPos(window);
    return window;
  }

  private void initializeWindows() {
//    addWindow();
//    addNoScrollWindow();
    addBinDiff();
  }

  private void addNoScrollWindow() {
    Window noScroll = newWindow(.5f, false, "WindowNS " + ++n);
    V2i screen = uiContext.windowSize;
    windowManager.addWindow(noScroll).setPosition(
        new V2i(screen.x / 10, screen.y / 10),
        new V2i(screen.x * 6 / 10, screen.y * 6 / 10)
    );
  }

  private void setWindowRandomPos(Window window) {
    V2i screen = uiContext.windowSize;
    int x = screen.x / 10  + r.nextInt(screen.x / 10);
    int y = screen.y / 20  + r.nextInt(screen.y / 20);
    int w = screen.x * 7 / 10  + r.nextInt(screen.x / 10);
    int h = screen.y * 7 / 10  + r.nextInt(screen.y / 10);
    window.setPosition(new V2i(x, y), new V2i(w, h));
  }

  private Window newWindow(float v, boolean scroll, String title) {
    Window window = new Window(uiContext);
    Consumer<V2i> sizeListener = title != null
        ? s -> window.setTitle(title + ": " + s) : s -> {};
    ScrollContentDemo contentDemo = new ScrollContentDemo(v, sizeListener);
    window.setContent(scroll ? newScrollView(contentDemo) : contentDemo);
    window.setTheme(Themes.darculaColorScheme());
    window.setTitleFont(titleFont, titleMargin);
    addColoseWindowHandler(window);
    return window;
  }

  private ScrollView newScrollView(ScrollContent contentDemo) {
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

  private void addColoseWindowHandler(Window window) {
    window.setOnClose(() -> {
      windowManager.removeWindow(window);
      window.dispose();
    });
  }
}
