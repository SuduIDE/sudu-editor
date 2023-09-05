package org.sudu.experiments.demo.ui.window;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.demo.Scene1;
import org.sudu.experiments.demo.SetCursor;
import org.sudu.experiments.demo.TestHelper;
import org.sudu.experiments.demo.ui.*;
import org.sudu.experiments.demo.ui.colors.DialogItemColors;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.*;

import java.util.function.Supplier;

import static org.sudu.experiments.Const.emptyRunnable;

public class WindowDemo extends Scene1 implements DprChangeListener {

  static final int titleMargin = 3;

  private final PopupMenu popupMenu;

  private final WindowManager windowManager;
  private Window window1, window2;

  public WindowDemo(SceneApi api) {
    super(api);
    windowManager = new WindowManager();
    uiContext.dprListeners.add(windowManager);
    uiContext.dprListeners.add(this);
    clearColor.set(new Color(43));

    popupMenu = new PopupMenu(uiContext);
    popupMenu.setTheme(DialogItemColors.darculaColorScheme(),
        new UiFont("Consolas", 25));

    api.input.onKeyPress.add(this::onKey);
    api.input.onContextMenu.add(this::onContextMenu);
    api.input.onMouse.add(TestHelper.popupMouseListener(popupMenu));
    api.input.onMouse.add(windowManager);
    api.input.onMouse.add(desktopMouse(uiContext.windowCursor));
    api.input.onScroll.add(windowManager::onScroll);
  }

  static MouseListener desktopMouse(final SetCursor windowCursor) {
    return new MouseListener() {
      @Override
      public boolean onMouseMove(MouseEvent event) {
        return windowCursor.set(null);
      }
    };
  }

  @Override
  public void onDprChanged(float oldDpr, float newDpr) {
    if (oldDpr == 0) openWindows();
  }

  @Override
  public void dispose() {
    popupMenu.dispose();
    windowManager.dispose();
  }

  @Override
  public void paint() {
    super.paint();
    WglGraphics graphics = api.graphics;
    windowManager.draw(graphics);
    popupMenu.paint();
  }

  private boolean onContextMenu(MouseEvent event) {
    if (!popupMenu.isVisible()) {
      popupMenu.display(event.position, items(), emptyRunnable);
    }
    return true;
  }

  private Supplier<ToolbarItem[]> items() {
    return ArrayOp.supplier(
        new ToolbarItem(this::openWindows, "newWindow",
            popupMenu.theme().toolbarItemColors)
    );
  }

  private void openWindows() {
    disposeWindow(window1);
    disposeWindow(window2);

    window1 = newWindow(.5f, false, "Window 1");
    window2 = newWindow(1, true, "Window 2");
    windowManager.addWindow(window1);
    windowManager.addWindow(window2);
    layoutWindows();
  }

  private void disposeWindow(Window w) {
    if (w != null) {
      windowManager.removeWindow(w);
      w.dispose();
    }
  }
  UiFont titleFont = new UiFont(Fonts.SegoeUI, 20);

  private Window newWindow(float v, boolean scroll, String title) {
    Window window = new Window(uiContext);
    ScrollContentDemo contentDemo = new ScrollContentDemo(v,
        s -> window.setTitle(title + ": " + s, titleFont, titleMargin));
    window.setContent(scroll
        ? new ScrollView(contentDemo, uiContext)
        : contentDemo);
    window.setTheme(DialogItemColors.darculaColorScheme());
    return window;
  }

  @Override
  public void onResize(V2i newSize, float newDpr) {
    super.onResize(newSize, newDpr);
    windowManager.onResize(newSize, newDpr);
    layoutWindows();
  }

  private void layoutWindows() {
    V2i newSize = uiContext.windowSize;
    window2.setPosition(
        new V2i(newSize.x * 2 / 10, newSize.y * 2 / 10),
        new V2i(newSize.x * 7 / 10, newSize.y * 7 / 10)
    );
    window1.setPosition(
        new V2i(newSize.x / 10, newSize.y / 10),
        new V2i(newSize.x * 6 / 10, newSize.y * 6 / 10)
    );
  }


  private boolean onKey(KeyEvent event) {
    if (event.isPressed && event.keyCode == KeyCode.SPACE) {
      return true;
    }
    return false;
  }

  @Override
  public boolean update(double timestamp) {
    return windowManager.update(timestamp);
  }
}
