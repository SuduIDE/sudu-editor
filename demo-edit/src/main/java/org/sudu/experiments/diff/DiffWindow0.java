package org.sudu.experiments.diff;

import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.ToolbarItem;
import org.sudu.experiments.ui.window.View;
import org.sudu.experiments.ui.window.Window;
import org.sudu.experiments.ui.window.WindowManager;

import java.util.function.Supplier;

abstract class DiffWindow0 {
  final WindowManager windowManager;
  final Supplier<String[]> fonts;
  EditorColorScheme theme;

  DiffWindow0(
      WindowManager windowManager,
      EditorColorScheme theme,
      Supplier<String[]> fonts
  ) {
    this.windowManager = windowManager;
    this.theme = theme;
    this.fonts = fonts;
  }

  Window createWindow(View view) {
    return createWindow(view, 0);
  }

  Window createWindow(View view, float dpOff) {
    Window window = new Window(windowManager.uiContext, view);
    window.setTheme(theme.dialogItem);
    window.setTitle(getClass().getSimpleName());
    window.setOnClose(() -> destroyWindow(window));
    window.setContextMenu(this::onContextMenu);
    WindowLayouts.largeWindowLayout(window, dpOff);
    return window;
  }

  abstract void dispose();

  private void destroyWindow(Window window) {
    windowManager.removeWindow(window);
    window.dispose();
    dispose();
  }

  protected Supplier<ToolbarItem[]> popupActions(V2i pos) {
    return null;
  }

  boolean onContextMenu(V2i pos) {
    var actions = popupActions(pos);
    if (actions != null)
      windowManager.showPopup(
          theme.dialogItem, theme.popupMenuFont,
          pos, actions);
    return actions != null;
  }
}
