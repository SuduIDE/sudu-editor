package org.sudu.experiments.ui;

import org.sudu.experiments.diff.WindowLayouts;
import org.sudu.experiments.editor.ThemeControl;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.window.View;
import org.sudu.experiments.ui.window.Window;
import org.sudu.experiments.ui.window.WindowManager;

import java.util.function.Supplier;

public abstract class ToolWindow0 implements ThemeControl {
  protected final WindowManager windowManager;
  protected final Supplier<String[]> fonts;
  protected EditorColorScheme theme;

  protected ToolWindow0(
      WindowManager windowManager,
      EditorColorScheme theme,
      Supplier<String[]> fonts
  ) {
    this.windowManager = windowManager;
    this.theme = theme;
    this.fonts = fonts;
  }

  public void applyTheme(EditorColorScheme theme) {
    this.theme = theme;
  }

  protected Window createWindow(View view) {
    return createWindow(view, 0);
  }

  protected Window createWindow(View view, float dpOff) {
    return createWindow(view, getClass().getSimpleName(), dpOff);
  }

  protected Window createWindow(View view, String title, float dpOff) {
    Window window = new Window(windowManager.uiContext, view);
    window.setTheme(theme.dialogItem);
    window.setTitle(title);
    window.setOnClose(() -> destroyWindow(window));
    window.setContextMenu(this::onContextMenu);
    WindowLayouts.largeWindowLayout(window, dpOff);
    return window;
  }

  protected abstract void dispose();

  private void destroyWindow(Window window) {
    windowManager.removeWindow(window);
    window.dispose();
    dispose();
  }

  protected Supplier<ToolbarItem[]> popupActions(V2i pos) {
    return null;
  }

  protected boolean onContextMenu(V2i pos) {
    var actions = popupActions(pos);
    if (actions != null)
      windowManager.showPopup(
          theme.dialogItem, theme.popupMenuFont,
          pos, actions);
    return actions != null;
  }
}
